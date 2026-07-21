package software.plusminus.admin.service.html;

import org.junit.Test;
import software.plusminus.admin.model.DataAction;
import software.plusminus.type.model.Field;
import software.plusminus.type.model.field.NumberField;

import static org.assertj.core.api.Assertions.assertThat;

public class InputServiceTest {

    private InputService service = new InputService();

    @Test
    public void isReadonlyOnDelete() {
        assertThat(service.isReadonly(field("number"), DataAction.DELETE)).isTrue();
    }

    @Test
    public void isReadonlyOnUpdateOfIdField() {
        assertThat(service.isReadonly(field("id"), DataAction.UPDATE)).isTrue();
    }

    @Test
    public void isNotReadonlyOnUpdateOfRegularField() {
        assertThat(service.isReadonly(field("number"), DataAction.UPDATE)).isFalse();
    }

    @Test
    public void isNotReadonlyOnCreate() {
        assertThat(service.isReadonly(field("id"), DataAction.CREATE)).isFalse();
    }

    private Field field(String name) {
        NumberField field = new NumberField();
        field.setName(name);
        return field;
    }
}
