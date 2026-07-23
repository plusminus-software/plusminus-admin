package software.plusminus.adminfixtures;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.plusminus.admin.AdminTypesContributor;
import software.plusminus.admin.model.settings.AdminTypeConfig;
import software.plusminus.admin.model.settings.TableSettings;
import software.plusminus.type.ParseService;
import software.plusminus.type.model.Type;

import java.util.Collections;

/* Contributes a config for the discovered AlphaEntity type: the contributed config
   must replace the discovered one. Lives in the fixtures package (and not as a nested
   class of a test) so that the software.plusminus.admin component scans never pick it up */
@Configuration
public class OverridingContributorConfiguration {

    @Bean
    public AdminTypesContributor overridingContributor(ParseService parseService) {
        Type type = parseService.parse(AlphaEntity.class);
        return () -> Collections.singletonList(AdminTypeConfig.builder(type)
                .order(9)
                .tableSettings(TableSettings.builder()
                        .fields(Collections.singletonList("myField"))
                        .build())
                .build());
    }
}
