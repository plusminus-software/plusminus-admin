package software.plusminus.admin.converter.html.field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.plusminus.admin.converter.html.FormConverter;
import software.plusminus.admin.model.html.Accordion;
import software.plusminus.admin.model.html.Taglist;
import software.plusminus.admin.model.html.TaglistEmbedded;
import software.plusminus.type.model.field.ArrayField;
import software.plusminus.type.model.field.EmbeddedField;

@Component
public class ArrayEmbeddedConverter implements ArrayElementConverter<EmbeddedField, TaglistEmbedded> {

    @Autowired
    private FormConverter formConverter;

    @Override
    public Class<EmbeddedField> arrayType() {
        return EmbeddedField.class;
    }

    @Override
    public TaglistEmbedded convertArray(ArrayField arrayField, EmbeddedField typeField) {
        TaglistEmbedded taglistEmbedded = new TaglistEmbedded();
        taglistEmbedded.setName(arrayField.getName());

        Taglist taglist = new Taglist();
        taglist.setClosable(true);
        taglistEmbedded.setTaglist(taglist);

        Accordion accordion = new Accordion();
        accordion.setElements(formConverter.toElements(typeField.getType(), null)); // TODO null
        taglistEmbedded.setAccordion(accordion);

        return taglistEmbedded;
    }
}
