package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Taginput;
import software.plusminus.type.model.field.ArrayField;
import software.plusminus.type.model.field.NumberField;
import software.plusminus.type.model.field.TextField;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class ArrayConverter implements ElementConverter<ArrayField, Taginput> {

    private static final Map<Class<?>, String> TYPES = createTypesMap();

    private static Map<Class<?>, String> createTypesMap() {
        Map<Class<?>, String> map = new HashMap<>();
        map.put(NumberField.class, "number");
        map.put(TextField.class, "text");
        return Collections.unmodifiableMap(map);
    }

    @Override
    public Class<ArrayField> fieldType() {
        return ArrayField.class;
    }

    @Override
    public boolean supports(ArrayField field) {
        return TYPES.keySet().contains(field.getArrayType().getClass());
    }

    @Override
    public Taginput convert(ArrayField field) {
        Taginput taginput = new Taginput();
        taginput.setName(field.getName());
        taginput.setType(getType(field));
        return taginput;
    }

    private String getType(ArrayField field) {
        return TYPES.get(field.getArrayType().getClass());
    }
}
