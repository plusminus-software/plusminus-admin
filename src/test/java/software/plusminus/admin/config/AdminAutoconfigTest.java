package software.plusminus.admin.config;

import org.junit.Test;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import software.plusminus.admin.TestApplication;
import software.plusminus.admin.converter.AdminPanelConverter;
import software.plusminus.admin.model.AdminPanel;
import software.plusminus.admin.service.AdminTypeRegistry;
import software.plusminus.type.config.TypeAutoconfig;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminAutoconfigTest {

    /* Mirrors the AdminAutoconfig component scan, but excludes TestApplication
       (it lives in the scanned package and would pull all the autoconfigurations
       of the test classpath in and register software.plusminus.admin as
       an auto-configuration package, making the @Admin discovery find the test models)
       and AdminAutoconfig itself (its unfiltered re-scan would register
       TestApplication back). TypeAutoconfig supplies the ParseService
       the AdminTypeRegistry needs */
    private final WebApplicationContextRunner runner = new WebApplicationContextRunner()
            .withUserConfiguration(AdminScanWithoutTestApplication.class, TypeAutoconfig.class);

    @Test
    public void contextBootsWithoutAdminTypes() {
        runner.run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).hasSingleBean(AdminPanelConverter.class);
            assertThat(context.getBean(AdminTypeRegistry.class).configs()).isEmpty();
        });
    }

    @Test
    public void adminPanelIsEmptyWithoutAdminTypes() {
        runner.run(context -> {
            AdminPanel panel = context.getBean(AdminPanelConverter.class).toAdminPanel();
            assertThat(panel.getTabs().getTabList()).isEmpty();
        });
    }

    @Configuration
    @ComponentScan(basePackages = "software.plusminus.admin",
            excludeFilters = @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    classes = {TestApplication.class, AdminAutoconfig.class}))
    static class AdminScanWithoutTestApplication {
    }
}
