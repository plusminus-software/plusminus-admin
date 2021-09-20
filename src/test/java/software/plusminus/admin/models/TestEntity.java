package software.plusminus.admin.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import software.plusminus.type.model.TitleField;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TestEntity extends ParentClass {

    private Long id;
    private Boolean checkbox;
    private int number;
    private List<Integer> numbers;
    private String string;
    private List<String> strings;
    private TestEnum testEnum;
    private List<TestEnum> enums;
    @TitleField(name = "embeddedInt")
    private TestEmbedded embedded;
    private List<TestEmbedded> embeddeds;
    private TestRelationEntity relation;
    private List<TestRelationEntity> relations;

}
