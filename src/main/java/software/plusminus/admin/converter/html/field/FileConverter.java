package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.File;
import software.plusminus.type.model.field.FileField;

@Component
public class FileConverter implements ElementConverter<FileField, File> {

    @Override
    public Class<FileField> fieldType() {
        return FileField.class;
    }

    @Override
    public File convert(FileField field) {
        File file = new File(); // TODO implement
        return file;
    }
}
