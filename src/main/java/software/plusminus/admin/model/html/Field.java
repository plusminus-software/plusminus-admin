package software.plusminus.admin.model.html;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Field extends Element {

    private String label;
    private Element element;

}
