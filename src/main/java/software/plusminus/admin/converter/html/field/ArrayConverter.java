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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class ArrayConverter implements ElementConverter<ArrayField, Taginput> {

    private static final Set<Class<?>> SUPPORTED_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            NumberField.class,
            TextField.class,
            BooleanField.class,
            ColorField.class,
            DateField.class,
            DatetimeField.class,
            DatetimeLocalField.class,
            TimeField.class,
            UuidField.class)));

    @Override
    public Class<ArrayField> fieldType() {
        return ArrayField.class;
    }

    @Override
    public boolean supports(ArrayField field) {
        return SUPPORTED_TYPES.contains(field.getArrayType().getClass());
    }

    @Override
    public Taginput convert(ArrayField field) {
        Taginput taginput = new Taginput();
        taginput.setName(field.getName());
        return taginput;
    }
}
