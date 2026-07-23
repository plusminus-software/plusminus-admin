package software.plusminus.adminfixtures;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.plusminus.admin.AdminTypesContributor;
import software.plusminus.admin.model.settings.AdminTypeConfig;
import software.plusminus.type.ParseService;
import software.plusminus.type.model.Type;

import java.util.Collections;

/* Contributes the non-annotated ContributedEntity type.
   Lives in the fixtures package (and not as a nested class of a test) so that
   the software.plusminus.admin component scans never pick it up */
@Configuration
public class ContributedTypeConfiguration {

    @Bean
    public AdminTypesContributor contributedTypeContributor(ParseService parseService) {
        Type type = parseService.parse(ContributedEntity.class);
        return () -> Collections.singletonList(AdminTypeConfig.builder(type).build());
    }
}
