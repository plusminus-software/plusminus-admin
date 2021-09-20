package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.File;
import software.plusminus.type.model.field.VideoField;

@Component
public class VideoConverter implements ElementConverter<VideoField, File> {

    @Override
    public Class<VideoField> fieldType() {
        return VideoField.class;
    }

    @Override
    public File convert(VideoField field) {
        File file = new File(); // TODO implement
        return file;
    }
}
