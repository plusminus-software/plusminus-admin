package software.plusminus.admin.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import software.plusminus.admin.converter.html.ModalConverter;
import software.plusminus.admin.converter.html.TableConverter;
import software.plusminus.admin.model.AdminPanel;
import software.plusminus.admin.model.AdminType;
import software.plusminus.admin.model.DataAction;
import software.plusminus.admin.model.html.Tab;
import software.plusminus.admin.model.html.Tabs;
import software.plusminus.admin.model.settings.AdminTypeConfig;
import software.plusminus.type.model.Field;
import software.plusminus.type.model.Type;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AdminPanelConverter {

    @Autowired
    private TableConverter tableConverter;
    @Autowired
    private ModalConverter modalConverter;
    @Autowired
    private Map<String, AdminTypeConfig> typeConfigs;

    public AdminPanel toAdminPanel() {
        AdminPanel adminPanel = new AdminPanel();
        adminPanel.setTabs(toTabs(typeConfigs.values().stream()
                .map(AdminTypeConfig::getType)
                .collect(Collectors.toList())));
        return adminPanel;
    }

    private Tabs<AdminType> toTabs(Collection<Type> types) {
        Tabs<AdminType> tabs = new Tabs<>();

        List<Tab<AdminType>> tabList = types.stream()
                .map(type -> {
                    Tab<AdminType> tab = new Tab<>();
                    tab.setLabel(type.getName());
                    tab.setBody(toAdminType(type));
                    return tab;
                })
                .collect(Collectors.toList());

        tabs.setTabList(tabList);
        return tabs;
    }

    private AdminType toAdminType(Type type) {
        AdminType adminType = new AdminType();
        adminType.setParameters(type);
        adminType.setTable(tableConverter.convertToTable(type));
        adminType.setCreateModal(modalConverter.convert(type, DataAction.CREATE));
        adminType.setUpdateModal(modalConverter.convert(type, DataAction.UPDATE));
        adminType.setCloneModal(modalConverter.convert(type, DataAction.CLONE));
        adminType.setDeleteModal(modalConverter.convert(type, DataAction.DELETE));
        return adminType;
    }

    public static List<Field> getFields(Type type,
                                        @Nullable List<String> fields) {
        if (CollectionUtils.isEmpty(fields)) {
            return type.getAllFields();
        }

        Map<String, Field> fieldMap = type.getAllFields().stream()
                .collect(Collectors.toMap(Field::getName, Function.identity()));

        return fields.stream()
                .map(fieldMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
