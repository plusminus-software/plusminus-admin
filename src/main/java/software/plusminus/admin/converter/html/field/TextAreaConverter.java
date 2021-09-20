package software.plusminus.admin.converter.html.field;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Textarea;
import software.plusminus.type.model.field.TextField;

@Order(0)
@Component
public class TextAreaConverter implements ElementConverter<TextField, Textarea> {

    @Override
    public Class<TextField> fieldType() {
        return TextField.class;
    }

    @Override
    public boolean supports(TextField field) {
        return field.isClob();
    }

    @Override
    public Textarea convert(TextField field) {
        Textarea textarea = new Textarea();
        textarea.setName(field.getName());
        return textarea;
    }
}
