package software.plusminus.admin.model.html;

import lombok.Data;

@Data
public class TaglistRelation extends AbstractInput {

    private static final boolean TAGLIST_RELATION = true;

    private Taglist taglist;
    private Table table;

}
