package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.type.model.field.ImageField;

@Component
public class ImageConverter implements NotImplementedElementConverter<ImageField> {

    @Override
    public Class<ImageField> fieldType() {
        return ImageField.class;
    }
}
