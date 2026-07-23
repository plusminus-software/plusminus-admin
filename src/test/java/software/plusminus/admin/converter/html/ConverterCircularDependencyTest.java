package software.plusminus.admin.converter.html;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import software.plusminus.admin.converter.html.field.ArrayEmbeddedConverter;
import software.plusminus.admin.converter.html.field.EmbeddedConverter;
import software.plusminus.admin.service.AdminTypeRegistry;
import software.plusminus.admin.service.html.FieldNameService;
import software.plusminus.admin.service.html.InputService;

import static org.mockito.Mockito.mock;

public class ConverterCircularDependencyTest {

    @Test
    public void contextStartsWhenLeafConvertersAreCreatedFirst() {
        refreshContext(ArrayEmbeddedConverter.class,
                EmbeddedConverter.class,
                FieldConverter.class,
                FormConverter.class);
    }

    @Test
    public void contextStartsWhenFieldConverterIsCreatedFirst() {
        refreshContext(FieldConverter.class,
                ArrayEmbeddedConverter.class,
                EmbeddedConverter.class,
                FormConverter.class);
    }

    @Test
    public void contextStartsWhenFormConverterIsCreatedFirst() {
        refreshContext(FormConverter.class,
                FieldConverter.class,
                EmbeddedConverter.class,
                ArrayEmbeddedConverter.class);
    }

    private void refreshContext(Class<?>... converterClasses) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(converterClasses);
            context.register(FieldNameService.class, InputService.class);
            context.registerBean(AdminTypeRegistry.class, () -> mock(AdminTypeRegistry.class));
            context.refresh();
        }
    }
}
