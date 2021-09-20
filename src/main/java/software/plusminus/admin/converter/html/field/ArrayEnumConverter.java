package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Dropdown;
import software.plusminus.type.model.field.ArrayField;
import software.plusminus.type.model.field.EnumField;

@Component
public class ArrayEnumConverter implements ArrayElementConverter<EnumField, Dropdown> {

    @Override
    public Class<EnumField> arrayType() {
        return EnumField.class;
    }

    @Override
    public Dropdown convertArray(ArrayField arrayField, EnumField enumField) {
        Dropdown dropdown = new Dropdown();
        dropdown.setName(arrayField.getName());
        dropdown.setMultiple(true);
        dropdown.setOptions(EnumConverter.getOptions(enumField));
        return dropdown;
    }
}
