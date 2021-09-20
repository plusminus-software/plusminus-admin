package software.plusminus.admin.model.html;

import lombok.Data;

@Data
public class TableColumn extends Element {

    private static final String OPEN = "{{";
    private static final String CLOSE = "}}";

    private String name;
    private String title;
    private String type = "";
    private boolean sortable;
    private boolean centered;
    private boolean tag;
    private String filter;
    private String titleField;
    private Integer width;

}
