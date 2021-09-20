package software.plusminus.admin.converter.html;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import software.plusminus.admin.converter.AdminPanelConverter;
import software.plusminus.admin.model.DataAction;
import software.plusminus.admin.model.html.Accordion;
import software.plusminus.admin.model.html.Element;
import software.plusminus.admin.model.html.Field;
import software.plusminus.admin.model.html.Form;
import software.plusminus.admin.model.settings.AdminTypeConfig;
import software.plusminus.type.model.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FormConverter {

    @Autowired
    private FieldConverter fieldConverter;
    @Autowired
    private Map<String, AdminTypeConfig> types;

    public Form toForm(Type type, DataAction action) {
        Form form = new Form();
        List<Element> elements = toElements(type, action);
        form.setElements(elements);
        return form;
    }

    public List<Element> toElements(Type type, DataAction action) {
        Stream<Optional<Field>> formRows = AdminPanelConverter.getFields(type, getFields(type)).stream()
                .map(f -> fieldConverter.toField(f, action));
        Stream<Optional<Element>> parents = getTypeHierarchy(type).stream()
                .map(t -> parentTypeToElement(t, action));

        return Stream.concat(formRows, parents)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private List<Type> getTypeHierarchy(Type type) {
        List<Type> hierarchy = new ArrayList<>();
        Type current = type.getParent();
        while (current != null) {
            hierarchy.add(current);
            current = current.getParent();
        }
        return hierarchy;
    }

    private Optional<Element> parentTypeToElement(Type type, DataAction action) {
        Accordion accordion = new Accordion();
        accordion.setId(type.getName());
        accordion.setTitle(type.getName());
        List<Element> rows = AdminPanelConverter.getFields(type, getFields(type)).stream()
                .map(f -> fieldConverter.toField(f, action))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        accordion.setElements(rows);

        if (rows.isEmpty()) {
            return Optional.empty();
        }
        //return Optional.of(accordion);
        return Optional.empty(); // TODO remove if unnecessary for parent
    }

    @Nullable
    private List<String> getFields(Type type) {
        AdminTypeConfig config = types.get(type.getName());
        if (config == null) {
            return Collections.emptyList();
        }
        return config.getModalSettings().getFields();
    }
}
