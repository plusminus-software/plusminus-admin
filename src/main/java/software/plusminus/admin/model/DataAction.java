package software.plusminus.admin.model;

import org.springframework.util.StringUtils;

public enum DataAction {

    READ,
    CREATE,
    UPDATE,
    CLONE,
    DELETE;

    @Override
    public String toString() {
        return StringUtils.capitalize(name().toLowerCase());
    }
}
