package software.plusminus.admin.model.html;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Tab<B extends Element> extends Element {

    private String icon;
    private String label;
    private B body;

}
