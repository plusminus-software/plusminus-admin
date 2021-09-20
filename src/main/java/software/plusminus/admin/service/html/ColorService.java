package software.plusminus.admin.service.html;

import org.springframework.stereotype.Component;
import software.plusminus.admin.exception.AdminException;
import software.plusminus.admin.model.DataAction;
import software.plusminus.admin.model.UiColor;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@Component
public class ColorService {

    private static final Map<DataAction, UiColor> COLORS;

    static {
        Map<DataAction, UiColor> colors = new EnumMap<>(DataAction.class);
        colors.put(DataAction.CREATE, UiColor.SUCCESS);
        colors.put(DataAction.UPDATE, UiColor.INFO);
        colors.put(DataAction.CLONE, UiColor.WARNING);
        colors.put(DataAction.DELETE, UiColor.DANGER);
        COLORS = Collections.unmodifiableMap(colors);
    }

    public UiColor getColor(DataAction action) {
        UiColor color = COLORS.get(action);
        if (color == null) {
            throw new AdminException("Unknown DataAction: " + action);
        }
        return color;
    }

}
