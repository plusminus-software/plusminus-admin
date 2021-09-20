package software.plusminus.admin.model.html;

import lombok.Data;

@Data
public class Field extends Element {

    private static final boolean FIELD = true;

    private String label;
    private Element element;

}
