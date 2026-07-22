package software.plusminus.admin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import software.plusminus.admin.controller.AdminController;
import software.plusminus.admin.exception.AdminException;
import software.plusminus.security.Security;
import software.plusminus.security.service.TokenProcessor;

import java.util.Collections;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SecurityHoleDetectorTest {

    private static final String ADMIN_URL = "http://localhost:8080/admin";

    @Mock
    private WebServerApplicationContext context;
    @Mock
    private RestOperations restOperations;
    @Mock
    private WebServerInitializedEvent event;
    @Mock
    private WebServer webServer;
    @Mock
    private RequestMappingHandlerMapping handlerMapping;
    @Mock
    private TokenProcessor tokenProcessor;
    @Mock
    private ServletContext servletContext;

    @InjectMocks
    private SecurityHoleDetector detector;

    private MockEnvironment environment = new MockEnvironment();

    @Before
    public void setUp() throws NoSuchMethodException {
        when(context.getEnvironment()).thenReturn(environment);
        when(context.getClassLoader()).thenReturn(getClass().getClassLoader());
        when(context.getBeansOfType(TokenProcessor.class)).thenReturn(Collections.emptyMap());
        when(context.getBeansOfType(ServletContext.class)).thenReturn(Collections.emptyMap());
        when(context.getBeansOfType(RequestMappingHandlerMapping.class))
                .thenReturn(Collections.singletonMap("requestMappingHandlerMapping", handlerMapping));
        when(handlerMapping.getHandlerMethods())
                .thenReturn(Collections.singletonMap(adminMapping(), adminHandlerMethod()));
        when(event.getApplicationContext()).thenReturn(context);
        when(event.getWebServer()).thenReturn(webServer);
        when(webServer.getPort()).thenReturn(8080);
    }

    @Test
    public void failsIfAdminPageRespondsWithSuccessWithoutUser() {
        when(restOperations.exchange(eq(ADMIN_URL), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>("<html>admin</html>", HttpStatus.OK));

        Throwable thrown = catchThrowable(() -> detector.detectSecurityHole(event));

        assertThat(thrown).isInstanceOf(AdminException.class)
                .hasMessageContaining("without a user");
    }

    @Test
    public void failsIfAdminPageRespondsWithSuccessToUserWithoutRoles() {
        when(context.getBeansOfType(TokenProcessor.class))
                .thenReturn(Collections.singletonMap("tokenProcessor", tokenProcessor));
        when(tokenProcessor.getToken(any(Security.class))).thenReturn("token-without-roles");
        when(restOperations.exchange(eq(ADMIN_URL), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenAnswer(invocation -> {
                    HttpEntity<?> entity = invocation.getArgument(2);
                    if (entity.getHeaders().containsKey("Authorization")) {
                        return new ResponseEntity<>("<html>admin</html>", HttpStatus.OK);
                    }
                    throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
                });

        Throwable thrown = catchThrowable(() -> detector.detectSecurityHole(event));

        assertThat(thrown).isInstanceOf(AdminException.class)
                .hasMessageContaining("by a user without roles");
    }

    @Test
    public void probesUrlWithSchemeAddressAndContextPath() {
        environment.setProperty("server.ssl.key-store", "classpath:keystore.p12");
        environment.setProperty("server.address", "192.168.1.5");
        when(servletContext.getContextPath()).thenReturn("/app");
        when(context.getBeansOfType(ServletContext.class))
                .thenReturn(Collections.singletonMap("servletContext", servletContext));
        when(restOperations.exchange(eq("https://192.168.1.5:8080/app/admin"),
                eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>("<html>admin</html>", HttpStatus.OK));

        Throwable thrown = catchThrowable(() -> detector.detectSecurityHole(event));

        assertThat(thrown).isInstanceOf(AdminException.class);
    }

    @Test
    public void passesIfAdminPageRedirects() {
        when(restOperations.exchange(eq(ADMIN_URL), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.FOUND));

        Throwable thrown = catchThrowable(() -> detector.detectSecurityHole(event));

        assertThat(thrown).isNull();
    }

    @Test
    public void passesIfAdminPageRespondsWith4xxToAllProbes() {
        when(context.getBeansOfType(TokenProcessor.class))
                .thenReturn(Collections.singletonMap("tokenProcessor", tokenProcessor));
        when(tokenProcessor.getToken(any(Security.class))).thenReturn("token-without-roles");
        when(restOperations.exchange(eq(ADMIN_URL), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));

        Throwable thrown = catchThrowable(() -> detector.detectSecurityHole(event));

        assertThat(thrown).isNull();
    }

    @Test
    public void skipsProbingIfAdminControllerIsNotMapped() {
        when(handlerMapping.getHandlerMethods()).thenReturn(Collections.emptyMap());

        Throwable thrown = catchThrowable(() -> detector.detectSecurityHole(event));

        assertThat(thrown).isNull();
        verifyZeroInteractions(restOperations);
    }

    @Test
    public void skipsWebServersOfChildContexts() {
        WebServerApplicationContext managementContext =
                mock(WebServerApplicationContext.class);
        when(event.getApplicationContext()).thenReturn(managementContext);

        Throwable thrown = catchThrowable(() -> detector.detectSecurityHole(event));

        assertThat(thrown).isNull();
        verifyZeroInteractions(restOperations);
    }

    private RequestMappingInfo adminMapping() {
        return RequestMappingInfo.paths("/admin").build();
    }

    private HandlerMethod adminHandlerMethod() throws NoSuchMethodException {
        return new HandlerMethod(new AdminController(null),
                AdminController.class.getDeclaredMethod("adminPage", Model.class, HttpServletRequest.class));
    }
}
