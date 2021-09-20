package software.plusminus.admin.model;

import lombok.Data;
import software.plusminus.admin.model.html.Tabs;

@Data
public class AdminPanel {

    private Tabs<AdminType> tabs;

}
