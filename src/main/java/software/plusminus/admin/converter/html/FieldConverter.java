package software.plusminus.admin.converter.html;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import software.plusminus.admin.converter.html.field.ElementConverter;
import software.plusminus.admin.model.DataAction;
import software.plusminus.admin.model.html.AbstractInput;
import software.plusminus.admin.model.html.Element;
import software.plusminus.admin.model.html.Field;
import software.plusminus.admin.model.html.Span;
import software.plusminus.admin.service.html.FieldNameService;
import software.plusminus.admin.service.html.InputService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class FieldConverter {

    private static final List<DataAction> LABEL_ACTIONS = Arrays.asList(DataAction.READ, DataAction.DELETE);
    private static final List<String> ANNOTATIONS_TO_IGNORE =
            Arrays.asList("JsonIgnore", "Ignore", "ReadOnly", "Readonly");
    private static final String ANNOTATION_TO_OVERRIDE_IGNORING = "NotIgnoredForAdmin";

    private FieldNameService fieldNameService;
    private InputService inputService;
    private List<ElementConverter<? extends software.plusminus.type.model.Field, ? extends Element>> elementConverters;

    public Optional<Field> toField(software.plusminus.type.model.Field field, DataAction action) {
        return toElement(field, action)
                .map(element -> {
                    Field f = new Field();
                    f.setLabel(fieldNameService.getName(field, action));
                    f.setElement(element);
                    return f;
                });
    }

    public Optional<Element> toElement(software.plusminus.type.model.Field field, DataAction action) {
        if (isIgnored(field)) {
            return Optional.empty();
        }
        if (LABEL_ACTIONS.contains(action)) {
            return Optional.of(toSpan(field));
        }
        /* A field with no matching converter falls back to a display-only span */
        Element element = elementConverters.stream()
                .filter(c -> c.fieldType() == field.getClass())
                .filter(c -> supports(field, c))
                .findFirst()
                .<Element>map(c -> convert(field, action, c))
                .orElseGet(() -> toSpan(field));
        if (element instanceof AbstractInput) {
            ((AbstractInput) element).setReadonly(inputService.isReadonly(field, action));
        }
        return Optional.of(element);
    }

    private Span toSpan(software.plusminus.type.model.Field field) {
        Span span = new Span();
        span.setName(field.getName());
        return span;
    }

    private boolean isIgnored(software.plusminus.type.model.Field field) {
        boolean notIgnore = field.getAnnotations().stream()
                .anyMatch(a -> ANNOTATION_TO_OVERRIDE_IGNORING.equals(a.getName()));
        if (notIgnore) {
            return false;
        }
        return field.getAnnotations().stream()
                .anyMatch(a -> ANNOTATIONS_TO_IGNORE.contains(a.getName()));
    }

    private <F extends software.plusminus.type.model.Field, T extends Element> T convert(
            software.plusminus.type.model.Field field,
            DataAction action,
            ElementConverter<F, T> converter) {
        F castedField = converter.fieldType().cast(field);
        return converter.convert(castedField, action);
    }

    private <F extends software.plusminus.type.model.Field> boolean supports(
            software.plusminus.type.model.Field field,
            ElementConverter<F, ?> converter) {
        return converter.supports(converter.fieldType().cast(field));
    }
}
