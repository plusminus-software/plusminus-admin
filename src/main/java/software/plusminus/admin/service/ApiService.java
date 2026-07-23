package software.plusminus.admin.service;

import org.atteo.evo.inflector.English;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import software.plusminus.admin.annotation.Admin;
import software.plusminus.type.model.Type;

import java.util.Locale;

@Service
public class ApiService {

    @Value("${admin.api.prefix:/api}")
    private String apiPrefix;

    public String getUrl(Type type) {
        String uri = findUri(type);
        if (uri != null) {
            return uri;
        }
        String pluralName = English.plural(type.getName());
        String kebabCasePluralName = toLowerHyphen(pluralName);
        return apiPrefix + "/" + kebabCasePluralName;
    }

    @Nullable
    private String findUri(Type type) {
        if (type.getNamespace() == null || type.getName() == null) {
            return null;
        }
        Class<?> typeClass;
        try {
            typeClass = ClassUtils.forName(type.getNamespace() + "." + type.getName(),
                    getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        Admin admin = AnnotationUtils.findAnnotation(typeClass, Admin.class);
        if (admin == null || admin.api().isEmpty()) {
            return null;
        }
        return admin.api();
    }

    private String toLowerHyphen(String upperCamel) {
        return upperCamel.replaceAll("(?<=.)([A-Z])", "-$1")
                .toLowerCase(Locale.ROOT);
    }
}
