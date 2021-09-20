package software.plusminus.admin.service.html;

import org.springframework.stereotype.Service;
import software.plusminus.admin.model.DataAction;
import software.plusminus.type.model.Field;

@Service
public class InputService {

    private boolean isId(Field field) {
        return field.getName().equals("id");
    }

    @SuppressWarnings("squid:S1126")
    public boolean isReadonly(Field field, DataAction action) {
        if (action == DataAction.DELETE) {
            return true;
        }
        if (action == DataAction.UPDATE && isId(field)) {
            return true;
        }
        return false;
    }
}
