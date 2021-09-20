package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.type.model.field.AnyRelationField;

@Component
public class AnyRelationConverter implements NotImplementedElementConverter<AnyRelationField> {
    @Override
    public Class<AnyRelationField> fieldType() {
        return AnyRelationField.class;
    }
}
