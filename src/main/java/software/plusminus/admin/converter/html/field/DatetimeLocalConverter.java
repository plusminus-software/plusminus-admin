package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.DatetimePicker;
import software.plusminus.type.model.field.DatetimeLocalField;

@Component
public class DatetimeLocalConverter implements ElementConverter<DatetimeLocalField, DatetimePicker> {

    @Override
    public Class<DatetimeLocalField> fieldType() {
        return DatetimeLocalField.class;
    }

    @Override
    public DatetimePicker convert(DatetimeLocalField field) {
        DatetimePicker datetimePicker = new DatetimePicker();
        datetimePicker.setLocal(true);
        datetimePicker.setName(field.getName());
        return datetimePicker;
    }
}
