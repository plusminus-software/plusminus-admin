package software.plusminus.admin.model.html;

import lombok.Data;
import software.plusminus.admin.model.DataAction;
import software.plusminus.admin.model.UiColor;

@Data
public class Modal {

    private String type;
    private String title;
    private DataAction action;
    private String url;
    private UiColor color;
    private Form form;

}
