package software.plusminus.admin.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import software.plusminus.adminfixtures.BetaEntity;
import software.plusminus.adminfixtures.CustomApiEntity;
import software.plusminus.type.ParseService;
import software.plusminus.type.model.Type;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiServiceTest {

    private ApiService apiService = new ApiService();
    private ParseService parseService = new ParseService(Collections.emptyList());

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(apiService, "apiPrefix", "/api");
    }

    @Test
    public void returnsUrlFromAdminApiAttribute() {
        Type type = parseService.parse(CustomApiEntity.class);

        String url = apiService.getUrl(type);

        assertThat(url).isEqualTo("/custom/custom-api-url");
    }

    @Test
    public void generatesPluralKebabCaseUrlIfAdminApiAttributeIsEmpty() {
        Type type = parseService.parse(BetaEntity.class);

        String url = apiService.getUrl(type);

        assertThat(url).isEqualTo("/api/beta-entities");
    }

    @Test
    public void generatesPluralKebabCaseUrlWithoutAdminAnnotation() {
        Type type = parseService.parse(PlainEntity.class);

        String url = apiService.getUrl(type);

        assertThat(url).isEqualTo("/api/plain-entities");
    }

    /* Deliberately a nested class: its name is not resolvable from the parsed
       namespace + name pair, which also covers the url of a type
       that is not backed by a loadable top-level class */
    private static class PlainEntity {
    }
}
