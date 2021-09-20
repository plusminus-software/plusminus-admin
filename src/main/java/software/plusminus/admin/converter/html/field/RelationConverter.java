package software.plusminus.admin.converter.html.field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.plusminus.admin.converter.html.TableConverter;
import software.plusminus.admin.model.html.Relation;
import software.plusminus.admin.model.html.Table;
import software.plusminus.type.model.field.RelationField;

@Component
public class RelationConverter implements ElementConverter<RelationField, Relation> {

    @Autowired
    private TableConverter tableConverter;

    @Override
    public Class<RelationField> fieldType() {
        return RelationField.class;
    }

    @Override
    public Relation convert(RelationField field) {
        Relation relation = new Relation();
        relation.setName(field.getName());

        Table table = tableConverter.convertToTable(field.getRelationType());
        relation.setTable(table);

        return relation;
    }
}
