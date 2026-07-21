package software.plusminus.admin.model.html;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractInput extends Element {

    private boolean readonly;
    private String name;

}
