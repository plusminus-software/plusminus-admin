package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.plusminus.admin.model.html.Option;
import software.plusminus.admin.model.html.Select;
import software.plusminus.type.model.field.EnumField;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EnumConverter implements ElementConverter<EnumField, Select> {

    @Override
    public Class<EnumField> fieldType() {
        return EnumField.class;
    }

    @Override
    public Select convert(EnumField field) {
        Select select = new Select();
        select.setName(field.getName());
        select.setOptions(getOptions(field));
        return select;
    }

    public static List<Option> getOptions(EnumField field) {
        return field.getEnumValues().stream()
                .map(value -> {
                    Option option = new Option();
                    option.setValue(value);
                    option.setText(StringUtils.capitalize(value.toLowerCase()));
                    return option;
                })
                .collect(Collectors.toList());
    }
}
