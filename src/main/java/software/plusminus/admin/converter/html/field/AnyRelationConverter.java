package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Relation;
import software.plusminus.type.model.field.AnyRelationField;

@Component
public class AnyRelationConverter implements ElementConverter<AnyRelationField, Relation> {

    @Override
    public Class<AnyRelationField> fieldType() {
        return AnyRelationField.class;
    }

    @Override
    public Relation convert(AnyRelationField field) {
        Relation relation = new Relation();
        relation.setName(field.getName());
        return relation;
    }
}
