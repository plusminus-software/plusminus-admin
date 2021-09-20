package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Taginput;
import software.plusminus.type.model.field.ArrayField;
import software.plusminus.type.model.field.UrlField;

@Component
public class ArrayUrlConverter implements ArrayElementConverter<UrlField, Taginput> {

    @Override
    public Class<UrlField> arrayType() {
        return UrlField.class;
    }

    @Override
    public Taginput convertArray(ArrayField arrayField, UrlField urlField) {
        Taginput taginput = new Taginput();
        taginput.setName(arrayField.getName());
        return taginput;
    }
}
