package software.plusminus.admin.converter.html.field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.plusminus.admin.converter.html.FormConverter;
import software.plusminus.admin.model.html.Embedded;
import software.plusminus.type.model.field.EmbeddedField;

@Component
public class EmbeddedConverter implements ElementConverter<EmbeddedField, Embedded> {

    @Autowired
    private FormConverter formConverter;

    @Override
    public Class<EmbeddedField> fieldType() {
        return EmbeddedField.class;
    }

    @Override
    public Embedded convert(EmbeddedField field) {
        Embedded embedded = new Embedded();
        embedded.setName(field.getName());
        embedded.setElements(formConverter.toElements(field.getType(), null)); // TODO null
        return embedded;
    }
}
