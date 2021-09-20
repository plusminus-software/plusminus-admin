package software.plusminus.admin.model.html;

import lombok.Data;

import java.util.List;

@Data
public class Table extends Element {

    private String url;
    private List<TableColumn> columns;

}
