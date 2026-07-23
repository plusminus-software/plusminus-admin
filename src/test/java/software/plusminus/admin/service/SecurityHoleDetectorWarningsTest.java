package software.plusminus.admin.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SecurityHoleDetectorWarningsTest extends SecurityHoleDetectorTestBase {

    @Rule
    public OutputCaptureRule output = new OutputCaptureRule();

    @Test
    public void warnsIfRoleProbeIsSkipped() {
        when(restOperations.exchange(eq(ADMIN_URL), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        Throwable thrown = catchThrowable(() -> detector.detectSecurityHole(event));

        assertThat(thrown).isNull();
        assertThat(output).contains("role-escalation scenario is NOT verified");
    }

    @Test
    public void warnsAndContinuesIfProbeCannotConnect() {
        when(restOperations.exchange(eq(ADMIN_URL), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenThrow(new ResourceAccessException("Connection refused"));

        Throwable thrown = catchThrowable(() -> detector.detectSecurityHole(event));

        assertThat(thrown).isNull();
        assertThat(output).contains("Could not verify");
    }

}
