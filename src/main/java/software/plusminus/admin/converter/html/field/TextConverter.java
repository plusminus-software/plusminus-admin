package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Input;
import software.plusminus.type.model.field.TextField;

@Component
public class TextConverter implements ElementConverter<TextField, Input> {

    @Override
    public Class<TextField> fieldType() {
        return TextField.class;
    }

    @Override
    public Input convert(TextField field) {
        Input input = new Input();
        input.setName(field.getName());
        return input;
    }
}
