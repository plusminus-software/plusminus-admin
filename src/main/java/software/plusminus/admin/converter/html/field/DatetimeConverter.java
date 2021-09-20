package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.DatetimePicker;
import software.plusminus.type.model.field.DatetimeField;

@Component
public class DatetimeConverter implements ElementConverter<DatetimeField, DatetimePicker> {

    @Override
    public Class<DatetimeField> fieldType() {
        return DatetimeField.class;
    }

    @Override
    public DatetimePicker convert(DatetimeField field) {
        DatetimePicker datetimePicker = new DatetimePicker();
        datetimePicker.setName(field.getName());
        return datetimePicker;
    }
}
