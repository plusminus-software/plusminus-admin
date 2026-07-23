package software.plusminus.admin.converter.html.field;

import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import software.plusminus.admin.converter.html.FormConverter;
import software.plusminus.admin.model.DataAction;
import software.plusminus.admin.model.html.Accordion;
import software.plusminus.admin.model.html.Taglist;
import software.plusminus.admin.model.html.TaglistEmbedded;
import software.plusminus.type.model.field.ArrayField;
import software.plusminus.type.model.field.EmbeddedField;

@AllArgsConstructor
@Component
public class ArrayEmbeddedConverter implements ArrayElementConverter<EmbeddedField, TaglistEmbedded> {

    private FormConverter formConverter;

    @Override
    public Class<EmbeddedField> arrayType() {
        return EmbeddedField.class;
    }

    @Override
    public TaglistEmbedded convertArray(ArrayField arrayField, EmbeddedField typeField) {
        return convertArray(arrayField, typeField, null);
    }

    @Override
    public TaglistEmbedded convertArray(ArrayField arrayField, EmbeddedField typeField,
                                        @Nullable DataAction action) {
        TaglistEmbedded taglistEmbedded = new TaglistEmbedded();
        taglistEmbedded.setName(arrayField.getName());

        Taglist taglist = new Taglist();
        taglist.setClosable(true);
        taglistEmbedded.setTaglist(taglist);

        Accordion accordion = new Accordion();
        accordion.setElements(formConverter.toElements(typeField.getType(), action));
        taglistEmbedded.setAccordion(accordion);

        return taglistEmbedded;
    }
}
