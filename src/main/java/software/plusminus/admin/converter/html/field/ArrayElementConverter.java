package software.plusminus.admin.converter.html.field;

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

    E convertArray(ArrayField arrayField, T typeField);

}
