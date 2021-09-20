package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.plusminus.admin.model.html.Switch;
import software.plusminus.type.model.field.BooleanField;

@Component
public class BooleanConverter implements ElementConverter<BooleanField, Switch> {

    @Override
    public Class<BooleanField> fieldType() {
        return BooleanField.class;
    }

    @Override
    public Switch convert(BooleanField field) {
        Switch element = new Switch();
        element.setName(field.getName());
        element.setText(StringUtils.capitalize(field.getName()));
        return element;
    }
}
