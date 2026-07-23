package software.plusminus.admin.service;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import software.plusminus.admin.AdminTypesContributor;
import software.plusminus.admin.annotation.Admin;
import software.plusminus.admin.exception.AdminException;
import software.plusminus.admin.model.settings.AdminTypeConfig;
import software.plusminus.type.ParseService;
import software.plusminus.type.model.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;

/**
 * Registry of the admin types. On startup it discovers the {@code @Admin} annotated classes
 * by scanning the auto-configuration packages (the package of the {@code @SpringBootApplication}
 * class) and the {@code @EntityScan} packages, then merges in the configs provided by the
 * {@link AdminTypesContributor} beans: a contributed config replaces a discovered one with
 * the same type name. The result is sorted by the {@link AdminTypeConfig} order ascending,
 * ties are broken by the type name.
 */
@Component
public class AdminTypeRegistry {

    private final BeanFactory beanFactory;
    private final ParseService parseService;
    private final ObjectProvider<AdminTypesContributor> contributors;
    private Map<String, AdminTypeConfig> configsByName = Collections.emptyMap();
    private List<AdminTypeConfig> sortedConfigs = Collections.emptyList();

    public AdminTypeRegistry(BeanFactory beanFactory,
                             ParseService parseService,
                             ObjectProvider<AdminTypesContributor> contributors) {
        this.beanFactory = beanFactory;
        this.parseService = parseService;
        this.contributors = contributors;
    }

    @PostConstruct
    public void init() {
        Map<String, AdminTypeConfig> merged = new LinkedHashMap<>();
        discover().forEach(config -> merged.put(config.getType().getName(), config));
        contributors.orderedStream()
                .map(AdminTypesContributor::contribute)
                .flatMap(Collection::stream)
                .forEach(config -> merged.put(config.getType().getName(), config));
        List<AdminTypeConfig> sorted = new ArrayList<>(merged.values());
        sorted.sort(Comparator.comparingInt(AdminTypeConfig::getOrder)
                .thenComparing(config -> config.getType().getName()));
        configsByName = merged;
        sortedConfigs = Collections.unmodifiableList(sorted);
    }

    public List<AdminTypeConfig> configs() {
        return sortedConfigs;
    }

    @Nullable
    public AdminTypeConfig find(String typeName) {
        return configsByName.get(typeName);
    }

    private List<AdminTypeConfig> discover() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Admin.class));
        List<AdminTypeConfig> discovered = new ArrayList<>();
        Set<String> classNames = new LinkedHashSet<>();
        for (String basePackage : packagesToScan()) {
            scanner.findCandidateComponents(basePackage)
                    .forEach(definition -> classNames.add(definition.getBeanClassName()));
        }
        classNames.forEach(className -> discovered.add(toConfig(loadClass(className))));
        return discovered;
    }

    private Set<String> packagesToScan() {
        Set<String> packages = new LinkedHashSet<>(autoConfigurationPackages());
        packages.addAll(EntityScanPackages.get(beanFactory).getPackageNames());
        return packages;
    }

    private List<String> autoConfigurationPackages() {
        try {
            return AutoConfigurationPackages.get(beanFactory);
        } catch (IllegalStateException e) {
            // AutoConfigurationPackages.get throws if none are registered
            // (e.g. a plain context without @SpringBootApplication) - treat as no packages
            return Collections.emptyList();
        }
    }

    private Class<?> loadClass(String className) {
        try {
            return ClassUtils.forName(className, getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new AdminException("Can't load the @Admin annotated class " + className, e);
        }
    }

    private AdminTypeConfig toConfig(Class<?> annotatedClass) {
        Admin admin = annotatedClass.getAnnotation(Admin.class);
        Type type = parseService.parse(annotatedClass);
        return AdminTypeConfig.builder(type)
                .order(admin == null ? 0 : admin.order())
                .build();
    }
}
