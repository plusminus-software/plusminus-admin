package software.plusminus.admin.model.html;

import lombok.Data;

import java.util.List;

@Data
public class Radio extends AbstractInput {

    private static final boolean RADIO = true;

    private List<Option> options;
}
