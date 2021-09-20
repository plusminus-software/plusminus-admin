package software.plusminus.admin.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestRelationEntity {

    private int relationInt;
    private String relationString;

}
