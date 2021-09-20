package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.ColorPicker;
import software.plusminus.type.model.field.ColorField;

@Component
public class ColorConverter implements ElementConverter<ColorField, ColorPicker> {

    @Override
    public Class<ColorField> fieldType() {
        return ColorField.class;
    }

    @Override
    public ColorPicker convert(ColorField field) {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setName(field.getName());
        return colorPicker;
    }
}
