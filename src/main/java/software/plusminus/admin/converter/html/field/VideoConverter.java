package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.type.model.field.VideoField;

@Component
public class VideoConverter implements NotImplementedElementConverter<VideoField> {

    @Override
    public Class<VideoField> fieldType() {
        return VideoField.class;
    }
}
