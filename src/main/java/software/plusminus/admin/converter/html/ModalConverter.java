package software.plusminus.admin.converter.html;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.plusminus.admin.model.DataAction;
import software.plusminus.admin.model.html.Modal;
import software.plusminus.admin.service.ApiService;
import software.plusminus.admin.service.html.ColorService;
import software.plusminus.type.model.Type;

@Component
public class ModalConverter {

    @Autowired
    private FormConverter formConverter;
    @Autowired
    private ColorService colorService;
    @Autowired
    private ApiService apiService;

    public Modal convert(Type type, DataAction action) {
        Modal modal = new Modal();

        modal.setForm(formConverter.toForm(type, action));
        modal.setAction(action);
        modal.setColor(colorService.getColor(action));
        modal.setTitle(generateTitle(type.getName(), action));
        modal.setType(type.getName());
        modal.setUrl(apiService.getUrl(type));

        return modal;
    }

    private String generateTitle(String typeName, DataAction action) {
        return getCapitalizedName(action) + " " + typeName;
    }

    private String getCapitalizedName(DataAction action) {
        return StringUtils.capitalize(action.toString().toLowerCase());
    }
}
