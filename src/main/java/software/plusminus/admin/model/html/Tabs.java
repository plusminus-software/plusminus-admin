package software.plusminus.admin.model.html;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Tabs<B extends Element> extends Element {

    private List<Tab<B>> tabList;

}
