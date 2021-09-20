package software.plusminus.admin.model.html;

import lombok.Data;

import java.util.List;

@Data
public class Tabs<B extends Element> extends Element {

    private List<Tab<B>> tabList;

}
