package software.plusminus.admin.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestEmbedded {

    private int embeddedInt;
    private String embeddedString;

}
