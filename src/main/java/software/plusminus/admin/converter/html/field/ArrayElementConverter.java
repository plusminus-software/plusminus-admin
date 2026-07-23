package software.plusminus.admin.converter.html.field;

import org.springframework.lang.Nullable;
import software.plusminus.admin.model.DataAction;
import software.plusminus.admin.model.html.Element;
import software.plusminus.type.model.Field;
import software.plusminus.type.model.field.ArrayField;

public interface ArrayElementConverter<T extends Field, E extends Element>
        extends ElementConverter<ArrayField, E> {

    @Override
    default Class<ArrayField> fieldType() {
        return ArrayField.class;
    }

    @Override
    default boolean supports(ArrayField field) {
        return field.getArrayType().getClass() == arrayType();
    }

    Class<T> arrayType();

    @Override
    default E convert(ArrayField field) {
        T typeField = (T) field.getArrayType();
        return convertArray(field, typeField);
    }

    @Override
    default E convert(ArrayField field, @Nullable DataAction action) {
        T typeField = (T) field.getArrayType();
        return convertArray(field, typeField, action);
    }

    E convertArray(ArrayField arrayField, T typeField);

    default E convertArray(ArrayField arrayField, T typeField, @Nullable DataAction action) {
        return convertArray(arrayField, typeField);
    }

}
