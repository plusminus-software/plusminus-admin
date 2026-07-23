package software.plusminus.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import software.plusminus.admin.controller.AdminController;
import software.plusminus.admin.exception.AdminException;
import software.plusminus.security.Security;
import software.plusminus.security.service.TokenProcessor;

import java.util.Collections;
import java.util.Objects;
import javax.servlet.ServletContext;

@Slf4j
@Component
public class SecurityHoleDetector {

    /* Referenced by name: the security stack is an optional dependency,
       so its classes may be absent from the consumer's classpath */
    private static final String TOKEN_PROCESSOR_CLASS = "software.plusminus.security.service.TokenProcessor";

    private static final int PROBE_TIMEOUT_MILLIS = 5000;

    private ApplicationContext context;
    /* The probe deliberately uses its own plain RestTemplate: a consumer-defined
       RestOperations bean may carry interceptors that attach credentials
       and would make the probe see the page as publicly available */
    private RestOperations restOperations = createProbeRestTemplate();

    public SecurityHoleDetector(ApplicationContext context) {
        this.context = context;
    }

    /* Probes the admin page over real HTTP once the server is up: calls it without
       a user and with a user that has no roles; a successful (2xx) response means
       the page was served to the probe and fails the start */
    @EventListener
    public void detectSecurityHole(WebServerInitializedEvent event) {
        if (event.getApplicationContext() != context) {
            /* a web server of a child context, e.g. the management/actuator port */
            return;
        }
        String adminUri = findAdminControllerUri();
        if (adminUri == null) {
            return;
        }
        String adminUrl = buildBaseUrl(event.getWebServer().getPort()) + adminUri;
        checkPageIsNotServed(adminUrl, null, "without a user");
        String token = generateToken();
        if (token == null) {
            log.warn("The role-escalation scenario is NOT verified: no TokenProcessor bean is present, "
                    + "so the admin page {} was probed without a user only. If the application "
                    + "authenticates users by other means (e.g. sessions or an auth gateway), verify "
                    + "yourself that authenticated users without the 'admin' role cannot open the page.",
                    adminUrl);
        } else {
            checkPageIsNotServed(adminUrl, token, "by a user without roles");
        }
    }

    /* The real uri AdminController is mapped to; null if the controller is not enabled */
    @Nullable
    private String findAdminControllerUri() {
        return context.getBeansOfType(RequestMappingHandlerMapping.class).values().stream()
                .flatMap(mapping -> mapping.getHandlerMethods().entrySet().stream())
                .filter(entry -> entry.getValue().getBeanType() == AdminController.class)
                .flatMap(entry -> entry.getKey().getPatternsCondition().getPatterns().stream())
                .findFirst()
                .orElse(null);
    }

    /* Handler mapping patterns exclude the context path and the servlet path,
       and the server may listen with TLS or on a non-localhost address only */
    private String buildBaseUrl(int port) {
        Environment environment = context.getEnvironment();
        boolean ssl = environment.getProperty("server.ssl.key-store") != null
                && !"false".equalsIgnoreCase(environment.getProperty("server.ssl.enabled"));
        String scheme = ssl ? "https" : "http";
        String host = resolveHost(environment);
        String contextPath = context.getBeansOfType(ServletContext.class).values().stream()
                .map(ServletContext::getContextPath)
                .findFirst()
                .orElse("");
        String servletPath = environment.getProperty("spring.mvc.servlet.path", "");
        if ("/".equals(servletPath)) {
            servletPath = "";
        }
        return scheme + "://" + host + ":" + port + contextPath + servletPath;
    }

    /* Wildcard bind addresses are not connectable - probe via loopback instead.
       The IP literals are intentional here (this method exists to special-case them),
       so the AvoidUsingHardCodedIP rule does not apply */
    @SuppressWarnings("PMD.AvoidUsingHardCodedIP")
    private String resolveHost(Environment environment) {
        String host = environment.getProperty("server.address", "localhost");
        if ("0.0.0.0".equals(host) || "::".equals(host) || "0:0:0:0:0:0:0:0".equals(host)) {
            return "127.0.0.1";
        }
        return host;
    }

    private void checkPageIsNotServed(String url, @Nullable String token, String description) {
        HttpStatus status = probe(url, token);
        if (status != null && status.is2xxSuccessful()) {
            throw new AdminException("Security hole detected: the admin page " + url
                    + " responded with " + status.value() + " when called " + description + ". "
                    + "Secure the application (e.g. with plusminus-security).");
        }
    }

    /* Returns the response status, or null if the probe request could not be performed at all */
    @Nullable
    private HttpStatus probe(String url, @Nullable String token) {
        HttpHeaders headers = new HttpHeaders();
        if (token != null) {
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
        try {
            return restOperations.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class)
                    .getStatusCode();
        } catch (HttpStatusCodeException e) {
            return e.getStatusCode();
        } catch (RestClientException e) {
            log.warn("Could not verify {}: the security probe request failed ({}). "
                    + "The security hole detection was skipped for that url.", url, e.getMessage());
            return null;
        }
    }

    @Nullable
    private String generateToken() {
        if (!ClassUtils.isPresent(TOKEN_PROCESSOR_CLASS, context.getClassLoader())) {
            return null;
        }
        return TokenGenerator.generate(context);
    }

    private static RestOperations createProbeRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(PROBE_TIMEOUT_MILLIS);
        requestFactory.setReadTimeout(PROBE_TIMEOUT_MILLIS);
        return new RestTemplate(requestFactory);
    }

    /* Visible for tests */
    void setRestOperations(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    /* Separate holder so the security-core classes are only linked
       after the classpath check in generateToken */
    private static class TokenGenerator {

        private static String generate(ApplicationContext context) {
            Security security = Security.builder()
                    .username("security-hole-detector")
                    .roles(Collections.emptySet())
                    .build();
            return context.getBeansOfType(TokenProcessor.class)
                    .values().stream()
                    .map(processor -> processor.getToken(security))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
    }
}
