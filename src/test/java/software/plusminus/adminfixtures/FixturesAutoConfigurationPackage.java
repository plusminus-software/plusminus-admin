package software.plusminus.adminfixtures;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Configuration;

/* Registers software.plusminus.adminfixtures as an auto-configuration package,
   the same way the @SpringBootApplication annotation registers its own package */
@Configuration
@AutoConfigurationPackage
public class FixturesAutoConfigurationPackage {
}
