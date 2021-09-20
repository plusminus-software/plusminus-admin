package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Input;
import software.plusminus.type.model.field.UuidField;

@Component
public class UuidConverter implements ElementConverter<UuidField, Input> {

    @Override
    public Class<UuidField> fieldType() {
        return UuidField.class;
    }

    @Override
    public Input convert(UuidField field) {
        Input input = new Input();
        input.setName(field.getName());
        return input;
    }
}
