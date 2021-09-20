package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.TimePicker;
import software.plusminus.type.model.field.TimeField;

@Component
public class TimeConverter implements ElementConverter<TimeField, TimePicker> {

    @Override
    public Class<TimeField> fieldType() {
        return TimeField.class;
    }

    @Override
    public TimePicker convert(TimeField field) {
        TimePicker timePicker = new TimePicker(); // TODO implement
        return timePicker;
    }
}
