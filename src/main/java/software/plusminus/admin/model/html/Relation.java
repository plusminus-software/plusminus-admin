package software.plusminus.admin.model.html;

import lombok.Data;

@Data
public class Relation extends Element {

    private static final boolean RELATION = true;

    private String name;
    private Table table;

}
