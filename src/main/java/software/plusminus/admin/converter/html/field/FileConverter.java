package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Input;
import software.plusminus.type.model.field.FileField;

@Component
public class FileConverter implements ElementConverter<FileField, Input> {

    @Override
    public Class<FileField> fieldType() {
        return FileField.class;
    }

    @Override
    public Input convert(FileField field) {
        Input input = new Input();
        input.setName(field.getName());
        return input;
    }
}
