package software.plusminus.admin.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.plusminus.admin.models.TestRelationEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/test-relation-entities")
public class TestRelationController {

    private List<TestRelationEntity> entities = new ArrayList<>(Arrays.asList(
            new TestRelationEntity(901, "X-901"),
            new TestRelationEntity(902, "X-902")
    ));

    @GetMapping
    public Page<TestRelationEntity> getPage(Pageable pageable) {
        return new PageImpl<>(entities, pageable, 50);
    }
}
