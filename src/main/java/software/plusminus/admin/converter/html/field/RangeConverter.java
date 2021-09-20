package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Range;
import software.plusminus.type.model.field.RangeField;

@Component
public class RangeConverter implements ElementConverter<RangeField, Range> {

    @Override
    public Class<RangeField> fieldType() {
        return RangeField.class;
    }

    @Override
    public Range convert(RangeField field) {
        Range range = new Range(); // TODO implement
        return range;
    }
}
