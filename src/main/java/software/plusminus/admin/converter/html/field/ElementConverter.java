package software.plusminus.admin.converter.html.field;

import software.plusminus.admin.model.html.Element;
import software.plusminus.type.model.Field;

public interface ElementConverter<F extends Field, T extends Element> {

    Class<F> fieldType();

    default boolean supports(F field) {
        return true;
    }

    T convert(F field);

}
