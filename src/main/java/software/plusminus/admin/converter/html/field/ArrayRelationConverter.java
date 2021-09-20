package software.plusminus.admin.converter.html.field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.plusminus.admin.converter.html.TableConverter;
import software.plusminus.admin.model.html.Table;
import software.plusminus.admin.model.html.Taglist;
import software.plusminus.admin.model.html.TaglistRelation;
import software.plusminus.type.model.field.ArrayField;
import software.plusminus.type.model.field.RelationField;

@Component
public class ArrayRelationConverter implements ArrayElementConverter<RelationField, TaglistRelation> {

    @Autowired
    private TableConverter tableConverter;

    @Override
    public Class<RelationField> arrayType() {
        return RelationField.class;
    }

    @Override
    public TaglistRelation convertArray(ArrayField arrayField, RelationField relationField) {
        TaglistRelation taglistRelation = new TaglistRelation();
        taglistRelation.setName(arrayField.getName());

        Taglist taglist = new Taglist();
        taglist.setClosable(true);
        taglistRelation.setTaglist(taglist);

        Table table = tableConverter.convertToTable(relationField.getRelationType());
        taglistRelation.setTable(table);

        return taglistRelation;
    }
}
