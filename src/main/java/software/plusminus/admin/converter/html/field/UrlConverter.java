package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Input;
import software.plusminus.type.model.field.UrlField;

@Component
public class UrlConverter implements ElementConverter<UrlField, Input> {

    @Override
    public Class<UrlField> fieldType() {
        return UrlField.class;
    }

    @Override
    public Input convert(UrlField field) {
        Input input = new Input();
        input.setName(field.getName());
        return input;
    }
}
