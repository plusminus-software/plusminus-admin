package software.plusminus.admin.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as an admin type. Annotated classes are discovered on startup by classpath
 * scanning of the application's auto-configuration packages (the package of the
 * {@code @SpringBootApplication} class) and the {@code @EntityScan} packages, and every
 * discovered class appears as a tab of the admin panel. Types that can not be annotated
 * can be registered via an {@code AdminTypesContributor} bean instead.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Admin {

    /**
     * Custom API URI of the type. When empty (the default), the URI is derived from
     * the type name: {@code ${api.prefix:/api}/} followed by the pluralized
     * kebab-case type name.
     *
     * @return the custom API URI of the type
     */
    String api() default "";

    /**
     * Order of the type's tab on the admin panel: lower values come first,
     * ties are broken by the type name.
     *
     * @return the tab order of the type
     */
    int order() default 0;

}
