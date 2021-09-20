package software.plusminus.admin.model;

import lombok.Data;
import software.plusminus.admin.model.html.Element;
import software.plusminus.admin.model.html.Modal;
import software.plusminus.admin.model.html.Table;
import software.plusminus.type.model.Type;

@Data
public class AdminType extends Element {

    private static final boolean ADMIN_TYPE = true;

    private Type parameters;
    private Table table;
    private Modal createModal;
    private Modal updateModal;
    private Modal cloneModal;
    private Modal deleteModal;

}
