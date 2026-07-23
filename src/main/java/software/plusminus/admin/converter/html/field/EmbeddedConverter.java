package software.plusminus.admin.converter.html.field;

import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import software.plusminus.admin.converter.html.FormConverter;
import software.plusminus.admin.model.DataAction;
import software.plusminus.admin.model.html.Embedded;
import software.plusminus.type.model.field.EmbeddedField;

@AllArgsConstructor
@Component
public class EmbeddedConverter implements ElementConverter<EmbeddedField, Embedded> {

    private FormConverter formConverter;

    @Override
    public Class<EmbeddedField> fieldType() {
        return EmbeddedField.class;
    }

    @Override
    public Embedded convert(EmbeddedField field) {
        return convert(field, null);
    }

    @Override
    public Embedded convert(EmbeddedField field, @Nullable DataAction action) {
        Embedded embedded = new Embedded();
        embedded.setName(field.getName());
        embedded.setElements(formConverter.toElements(field.getType(), action));
        return embedded;
    }
}
