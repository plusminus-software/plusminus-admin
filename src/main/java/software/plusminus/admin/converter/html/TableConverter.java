package software.plusminus.admin.converter.html;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.plusminus.admin.converter.AdminPanelConverter;
import software.plusminus.admin.model.html.Table;
import software.plusminus.admin.model.html.TableColumn;
import software.plusminus.admin.model.settings.AdminTypeConfig;
import software.plusminus.admin.service.ApiService;
import software.plusminus.type.model.Field;
import software.plusminus.type.model.TitleField;
import software.plusminus.type.model.Type;
import software.plusminus.util.FieldUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableConverter {

    @Autowired
    private ApiService apiService;
    @Autowired
    private Map<String, AdminTypeConfig> types;

    public Table convertToTable(Type type) {
        Table table = new Table();
        table.setId("table-" + type.getName());
        table.setUrl(apiService.getUrl(type));
        table.setColumns(getColumns(type));
        return table;
    }

    private List<TableColumn> getColumns(Type type) {
        return AdminPanelConverter.getFields(type, getFields(type)).stream()
                .filter(Objects::nonNull)
                .map(field -> {
                    TableColumn column = new TableColumn();
                    column.setTitle(StringUtils.capitalize(field.getName()));
                    column.setName(field.getName());
                    column.setTitleField(getTitleField(field));
                    return column;
                })
                .collect(Collectors.toList());
    }

    @Nullable
    private String getTitleField(Field field) {
        return FieldUtils.readFirstWithAnnotation(field, String.class, TitleField.class);
    }

    @Nullable
    private List<String> getFields(Type type) {
        AdminTypeConfig config = types.get(type.getName());
        if (config == null) {
            return Collections.emptyList();
        }
        return config.getTableSettings().getFields();
    }
}
