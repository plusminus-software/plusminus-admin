package software.plusminus.admin.converter.html;

import org.junit.Before;
import org.junit.Test;
import software.plusminus.admin.converter.html.field.NumberConverter;
import software.plusminus.admin.converter.html.field.TextConverter;
import software.plusminus.admin.model.DataAction;
import software.plusminus.admin.model.html.Element;
import software.plusminus.admin.model.html.Input;
import software.plusminus.admin.model.html.Span;
import software.plusminus.admin.service.html.FieldNameService;
import software.plusminus.admin.service.html.InputService;
import software.plusminus.type.model.field.NumberField;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldConverterTest {

    private FieldConverter fieldConverter;

    @Before
    public void setUp() {
        fieldConverter = new FieldConverter(new FieldNameService(), new InputService(),
                Arrays.asList(new NumberConverter(), new TextConverter()));
    }

    @Test
    public void idIsReadonlyOnUpdate() {
        Optional<Element> element = fieldConverter.toElement(idField(), DataAction.UPDATE);

        assertThat(element).isPresent();
        assertThat(((Input) element.get()).isReadonly()).isTrue();
    }

    @Test
    public void idIsEditableOnCreateAndClone() {
        Optional<Element> onCreate = fieldConverter.toElement(idField(), DataAction.CREATE);
        Optional<Element> onClone = fieldConverter.toElement(idField(), DataAction.CLONE);

        assertThat(((Input) onCreate.get()).isReadonly()).isFalse();
        assertThat(((Input) onClone.get()).isReadonly()).isFalse();
    }

    @Test
    public void fieldWithoutConverterFallsBackToSpan() {
        UnsupportedField field = new UnsupportedField();
        field.setName("visibleAnyway");
        field.setAnnotations(Collections.emptyList());

        Optional<Element> element = fieldConverter.toElement(field, DataAction.UPDATE);

        assertThat(element).isPresent();
        assertThat(element.get()).isInstanceOf(Span.class);
        assertThat(((Span) element.get()).getName()).isEqualTo("visibleAnyway");
    }

    private NumberField idField() {
        NumberField field = new NumberField();
        field.setName("id");
        field.setAnnotations(Collections.emptyList());
        return field;
    }

    /* Every shipped field type has a converter now; the fallback stays as a safety net
       for field types introduced in the future */
    private static class UnsupportedField extends software.plusminus.type.model.Field {

        @Override
        public software.plusminus.type.model.Validation getValidation() {
            return null;
        }
    }
}
