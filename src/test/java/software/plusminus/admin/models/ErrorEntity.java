package software.plusminus.admin.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.plusminus.admin.annotation.Admin;

@Admin(order = 2)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEntity {

    private String error;

}
