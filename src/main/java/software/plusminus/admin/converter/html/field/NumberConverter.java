package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Input;
import software.plusminus.type.model.field.NumberField;

@Component
public class NumberConverter implements ElementConverter<NumberField, Input> {

    @Override
    public Class<NumberField> fieldType() {
        return NumberField.class;
    }

    @Override
    public Input convert(NumberField field) {
        Input input = new Input();
        input.setName(field.getName());
        input.setNumber(true);
        input.setType("number");
        return input;
    }
}
