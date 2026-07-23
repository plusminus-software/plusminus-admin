package software.plusminus.admin;

import software.plusminus.admin.model.settings.AdminTypeConfig;

import java.util.Collection;

/**
 * SPI to register admin types programmatically. All the beans of this type are collected
 * on startup and their configs are merged with the configs discovered via the
 * {@code @Admin} annotation. A contributed config replaces a discovered one
 * with the same type name, so a contributor can be used both to register types
 * that can not be annotated and to override the settings of annotated types.
 */
public interface AdminTypesContributor {

    Collection<AdminTypeConfig> contribute();

}
