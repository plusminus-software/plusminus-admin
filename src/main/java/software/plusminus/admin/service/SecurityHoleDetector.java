package software.plusminus.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import software.plusminus.admin.controller.AdminController;
import software.plusminus.admin.exception.AdminException;
import software.plusminus.security.Security;
import software.plusminus.security.service.TokenProcessor;

import java.util.Collections;
import java.util.Objects;
import javax.servlet.ServletContext;

@AllArgsConstructor
@Component
public class SecurityHoleDetector {

    /* Referenced by name: the security stack is an optional dependency,
       so its classes may be absent from the consumer's classpath */
    private static final String TOKEN_PROCESSOR_CLASS = "software.plusminus.security.service.TokenProcessor";

    private ApplicationContext context;
    private RestOperations restOperations;

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
        String url = buildAdminUrl(event.getWebServer().getPort(), adminUri);
        checkPageIsNotServed(url, null, "without a user");
        String token = generateToken();
        if (token != null) {
            checkPageIsNotServed(url, token, "by a user without roles");
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
    private String buildAdminUrl(int port, String adminUri) {
        Environment environment = context.getEnvironment();
        boolean ssl = environment.getProperty("server.ssl.key-store") != null
                && !"false".equalsIgnoreCase(environment.getProperty("server.ssl.enabled"));
        String scheme = ssl ? "https" : "http";
        String host = environment.getProperty("server.address", "localhost");
        String contextPath = context.getBeansOfType(ServletContext.class).values().stream()
                .map(ServletContext::getContextPath)
                .findFirst()
                .orElse("");
        String servletPath = environment.getProperty("spring.mvc.servlet.path", "");
        if ("/".equals(servletPath)) {
            servletPath = "";
        }
        return scheme + "://" + host + ":" + port + contextPath + servletPath + adminUri;
    }

    private void checkPageIsNotServed(String url, @Nullable String token, String description) {
        HttpHeaders headers = new HttpHeaders();
        if (token != null) {
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
        HttpStatus status;
        try {
            status = restOperations.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class)
                    .getStatusCode();
        } catch (HttpStatusCodeException e) {
            status = e.getStatusCode();
        }
        if (status.is2xxSuccessful()) {
            throw new AdminException("Security hole detected: the admin page " + url
                    + " responded with " + status.value() + " when called " + description + ". "
                    + "Secure the application (e.g. with plusminus-security).");
        }
    }

    @Nullable
    private String generateToken() {
        if (!ClassUtils.isPresent(TOKEN_PROCESSOR_CLASS, context.getClassLoader())) {
            return null;
        }
        return TokenGenerator.generate(context);
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
