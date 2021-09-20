package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Image;
import software.plusminus.type.model.field.ImageField;

@Component
public class ImageConverter implements ElementConverter<ImageField, Image> {

    @Override
    public Class<ImageField> fieldType() {
        return ImageField.class;
    }

    @Override
    public Image convert(ImageField field) {
        Image image = new Image(); // TODO implement
        return image;
    }
}
