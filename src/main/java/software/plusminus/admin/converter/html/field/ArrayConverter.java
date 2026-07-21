package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Taginput;
import software.plusminus.type.model.field.ArrayField;
import software.plusminus.type.model.field.BooleanField;
import software.plusminus.type.model.field.ColorField;
import software.plusminus.type.model.field.DateField;
import software.plusminus.type.model.field.DatetimeField;
import software.plusminus.type.model.field.DatetimeLocalField;
import software.plusminus.type.model.field.NumberField;
import software.plusminus.type.model.field.TextField;
import software.plusminus.type.model.field.TimeField;
import software.plusminus.type.model.field.UuidField;

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
        map.put(BooleanField.class, "text");
        map.put(ColorField.class, "text");
        map.put(DateField.class, "date");
        map.put(DatetimeField.class, "text");
        map.put(DatetimeLocalField.class, "datetime-local");
        map.put(TimeField.class, "time");
        map.put(UuidField.class, "text");
        return Collections.unmodifiableMap(map);
    }

    @Override
    public Class<ArrayField> fieldType() {
        return ArrayField.class;
    }

    @Override
    public boolean supports(ArrayField field) {
        return TYPES.containsKey(field.getArrayType().getClass());
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
