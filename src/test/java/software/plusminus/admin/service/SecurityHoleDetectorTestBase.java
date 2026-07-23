package software.plusminus.admin.service;

import org.junit.Before;
import org.mockito.Mock;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.ui.Model;
import org.springframework.web.client.RestOperations;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import software.plusminus.admin.controller.AdminController;
import software.plusminus.security.service.TokenProcessor;

import java.util.Collections;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;

public abstract class SecurityHoleDetectorTestBase {

    protected static final String ADMIN_URL = "http://localhost:8080/admin";

    @Mock
    protected WebServerApplicationContext context;
    @Mock
    protected RestOperations restOperations;
    @Mock
    protected WebServerInitializedEvent event;
    @Mock
    protected WebServer webServer;
    @Mock
    protected RequestMappingHandlerMapping handlerMapping;
    @Mock
    protected TokenProcessor tokenProcessor;
    @Mock
    protected ServletContext servletContext;

    protected SecurityHoleDetector detector;

    protected MockEnvironment environment = new MockEnvironment();

    @Before
    public void setUp() throws NoSuchMethodException {
        detector = new SecurityHoleDetector(context);
        detector.setRestOperations(restOperations);
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

    protected RequestMappingInfo adminMapping() {
        return RequestMappingInfo.paths("/admin").build();
    }

    protected HandlerMethod adminHandlerMethod() throws NoSuchMethodException {
        return new HandlerMethod(new AdminController(null),
                AdminController.class.getDeclaredMethod("adminPage", Model.class, HttpServletRequest.class));
    }
}
