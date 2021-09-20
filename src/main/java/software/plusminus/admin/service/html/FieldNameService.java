package software.plusminus.admin.service.html;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.plusminus.admin.model.DataAction;
import software.plusminus.type.model.Field;
import software.plusminus.type.model.field.BooleanField;

@Component
public class FieldNameService {

    public String getName(Field field, DataAction action) {
        if (field.getClass() == BooleanField.class && action != DataAction.DELETE) {
            return "";
        }
        return StringUtils.capitalize(field.getName());
    }

}
