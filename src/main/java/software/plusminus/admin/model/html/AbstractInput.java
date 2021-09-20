package software.plusminus.admin.model.html;

import lombok.Data;

@Data
public abstract class AbstractInput extends Element {

    private boolean readonly;
    private String name;

}
