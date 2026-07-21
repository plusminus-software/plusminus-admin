package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.type.model.field.FileField;

@Component
public class FileConverter implements NotImplementedElementConverter<FileField> {

    @Override
    public Class<FileField> fieldType() {
        return FileField.class;
    }
}
