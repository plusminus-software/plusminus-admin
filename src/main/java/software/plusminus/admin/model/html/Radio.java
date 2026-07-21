package software.plusminus.admin.model.html;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Radio extends AbstractInput {
    private List<Option> options;
}
