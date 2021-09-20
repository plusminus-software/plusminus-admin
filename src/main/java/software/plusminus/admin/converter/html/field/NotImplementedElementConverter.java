package software.plusminus.admin.converter.html.field;

import software.plusminus.admin.model.html.Span;
import software.plusminus.type.model.Field;

public interface NotImplementedElementConverter<F extends Field>
        extends ElementConverter<F, Span> {

    Class<F> fieldType();

    default Span convert(F field) {
        Span span = new Span();
        span.setName(field.getName());
        return span;
    }
}
