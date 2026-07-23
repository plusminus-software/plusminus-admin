package software.plusminus.adminfixtures;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

/* Registers software.plusminus.adminfixtures as an entity-scan package.
   Lives in the fixtures package (and not as a nested class of a test) so that
   the software.plusminus.admin component scans never pick it up */
@Configuration
@EntityScan(basePackageClasses = AlphaEntity.class)
public class FixturesEntityScan {
}
