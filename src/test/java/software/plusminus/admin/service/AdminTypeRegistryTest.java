package software.plusminus.admin.service;

import org.junit.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import software.plusminus.admin.model.settings.AdminTypeConfig;
import software.plusminus.adminfixtures.ContributedTypeConfiguration;
import software.plusminus.adminfixtures.FixturesAutoConfigurationPackage;
import software.plusminus.adminfixtures.FixturesEntityScan;
import software.plusminus.adminfixtures.OverridingContributorConfiguration;
import software.plusminus.type.config.TypeAutoconfig;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminTypeRegistryTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(TypeAutoconfig.class, AdminTypeRegistry.class);

    @Test
    public void discoversViaEntityScanPackagesAndSortsByOrderAndName() {
        runner.withUserConfiguration(FixturesEntityScan.class).run(context -> {
            AdminTypeRegistry registry = context.getBean(AdminTypeRegistry.class);
            assertThat(registry.configs())
                    .extracting(config -> config.getType().getName())
                    .containsExactly("BetaEntity", "GammaEntity", "AlphaEntity", "CustomApiEntity");
        });
    }

    @Test
    public void discoversViaAutoConfigurationPackages() {
        runner.withUserConfiguration(FixturesAutoConfigurationPackage.class).run(context -> {
            AdminTypeRegistry registry = context.getBean(AdminTypeRegistry.class);
            assertThat(registry.configs())
                    .extracting(config -> config.getType().getName())
                    .containsExactly("BetaEntity", "GammaEntity", "AlphaEntity", "CustomApiEntity");
        });
    }

    @Test
    public void carriesAdminOrderIntoConfigs() {
        runner.withUserConfiguration(FixturesEntityScan.class).run(context -> {
            AdminTypeRegistry registry = context.getBean(AdminTypeRegistry.class);
            AdminTypeConfig config = registry.find("CustomApiEntity");
            assertThat(config).isNotNull();
            assertThat(config.getOrder()).isEqualTo(3);
        });
    }

    @Test
    public void contributorRegistersNonAnnotatedType() {
        runner.withUserConfiguration(FixturesEntityScan.class, ContributedTypeConfiguration.class)
                .run(context -> {
                    AdminTypeRegistry registry = context.getBean(AdminTypeRegistry.class);
                    assertThat(registry.configs())
                            .extracting(config -> config.getType().getName())
                            .containsExactly("ContributedEntity", "BetaEntity", "GammaEntity",
                                    "AlphaEntity", "CustomApiEntity");
                    assertThat(registry.find("ContributedEntity")).isNotNull();
                });
    }

    @Test
    public void contributorOverridesDiscoveredConfig() {
        runner.withUserConfiguration(FixturesEntityScan.class, OverridingContributorConfiguration.class)
                .run(context -> {
                    AdminTypeRegistry registry = context.getBean(AdminTypeRegistry.class);
                    assertThat(registry.configs())
                            .extracting(config -> config.getType().getName())
                            .containsExactly("BetaEntity", "GammaEntity", "CustomApiEntity", "AlphaEntity");
                    AdminTypeConfig config = registry.find("AlphaEntity");
                    assertThat(config).isNotNull();
                    assertThat(config.getOrder()).isEqualTo(9);
                    assertThat(config.getTableSettings().getFields()).containsExactly("myField");
                });
    }

    @Test
    public void emptyWithoutDiscoveredTypesAndContributors() {
        runner.run(context -> {
            AdminTypeRegistry registry = context.getBean(AdminTypeRegistry.class);
            assertThat(registry.configs()).isEmpty();
            assertThat(registry.find("TestEntity")).isNull();
        });
    }
}
