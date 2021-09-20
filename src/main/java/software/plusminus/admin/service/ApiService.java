package software.plusminus.admin.service;

import com.google.common.base.CaseFormat;
import org.atteo.evo.inflector.English;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.plusminus.admin.exception.AdminException;
import software.plusminus.type.model.Annotation;
import software.plusminus.type.model.Type;

import java.util.Optional;

@Service
public class ApiService {

    @Value("${api.prefix:/api}")
    private String apiPrefix;

    public String getUrl(Type type) {
        Optional<Annotation> annotation = type.getAnnotations().stream()
                .filter(a -> a.getName().equals("Api"))
                .findFirst();
        if (annotation.isPresent()) {
            String url = annotation.get().getValue();
            if (url == null) {
                throw new AdminException("Api url must be defined in '"
                        + type.getName() + "' type");
            }
            return url;
        }
        String pluralName = English.plural(type.getName());
        String kebabCasePluralName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, pluralName);
        return apiPrefix + "/" + kebabCasePluralName;
    }
}
