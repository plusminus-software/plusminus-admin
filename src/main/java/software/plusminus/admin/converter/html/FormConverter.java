package software.plusminus.admin.converter.html;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import software.plusminus.admin.converter.AdminPanelConverter;
import software.plusminus.admin.model.DataAction;
import software.plusminus.admin.model.html.Element;
import software.plusminus.admin.model.html.Form;
import software.plusminus.admin.model.settings.AdminTypeConfig;
import software.plusminus.admin.service.AdminTypeRegistry;
import software.plusminus.type.model.Type;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Component
public class FormConverter {

    private FieldConverter fieldConverter;
    private AdminTypeRegistry typeRegistry;

    @Autowired
    void init(FieldConverter fieldConverter,
              AdminTypeRegistry typeRegistry) {
        this.fieldConverter = fieldConverter;
        this.typeRegistry = typeRegistry;
    }

    public Form toForm(Type type, DataAction action) {
        Form form = new Form();
        List<Element> elements = toElements(type, action);
        form.setElements(elements);
        return form;
    }

    public List<Element> toElements(Type type, DataAction action) {
        return AdminPanelConverter.getFields(type, getFields(type)).stream()
                .map(f -> fieldConverter.toField(f, action))
                .filter(Optional::isPresent)
                .<Element>map(Optional::get)
                .collect(Collectors.toList());
    }

    @Nullable
    private List<String> getFields(Type type) {
        AdminTypeConfig config = typeRegistry.find(type.getName());
        if (config == null) {
            return Collections.emptyList();
        }
        return config.getModalSettings().getFields();
    }
}
