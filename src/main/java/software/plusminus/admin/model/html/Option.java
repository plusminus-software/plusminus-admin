package software.plusminus.admin.model.html;

import lombok.Data;
import software.plusminus.admin.model.UiColor;

@Data
public class Option {

    private String value;
    private UiColor color;
    private String icon;
    private String text;
}
