package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.DatePicker;
import software.plusminus.type.model.field.DateField;

@Component
public class DateConverter implements ElementConverter<DateField, DatePicker> {

    @Override
    public Class<DateField> fieldType() {
        return DateField.class;
    }

    @Override
    public DatePicker convert(DateField field) {
        DatePicker datePicker = new DatePicker();
        datePicker.setName(field.getName());
        return datePicker;
    }
}
