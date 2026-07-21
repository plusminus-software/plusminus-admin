package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.type.model.field.RangeField;

@Component
public class RangeConverter implements NotImplementedElementConverter<RangeField> {

    @Override
    public Class<RangeField> fieldType() {
        return RangeField.class;
    }
}
