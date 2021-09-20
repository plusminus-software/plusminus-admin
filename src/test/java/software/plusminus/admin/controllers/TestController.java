package software.plusminus.admin.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.plusminus.admin.models.TestEmbedded;
import software.plusminus.admin.models.TestEntity;
import software.plusminus.admin.models.TestEnum;
import software.plusminus.admin.models.TestRelationEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api/test-entities")
public class TestController {

    private List<TestEntity> entities;

    @PostConstruct
    public void init() {
        refreshData();
    }

    @GetMapping
    public Page<TestEntity> getPage(Pageable pageable) {
        return new PageImpl<>(entities, pageable, 50);
    }

    @PostMapping
    public TestEntity create(@RequestBody TestEntity entity) {
        entity.setId((long) entities.size());
        entities.add(entity);
        return entity;
    }

    @PatchMapping
    public TestEntity patch(@RequestBody TestEntity entity) {
        entities.stream()
                .filter(p -> p.getId().equals(entity.getId()))
                .findFirst()
                .ifPresent(e -> BeanUtils.copyProperties(entity, e));
        return entity;
    }

    @DeleteMapping
    public void delete(@RequestBody TestEntity entity) {
        entities = entities.stream()
                .filter(p -> !p.getId().equals(entity.getId()))
                .collect(Collectors.toList());
    }

    public void refreshData() {
        entities = new ArrayList<>(Arrays.asList(
                new TestEntity(0L, true, 1, Collections.singletonList(1), "Entity 1",
                        Collections.singletonList("e1"), TestEnum.ONE, Collections.singletonList(TestEnum.TWO),
                        new TestEmbedded(10, "Embedded 10"),
                        Collections.singletonList(new TestEmbedded(11, "Embedded 11")),
                        new TestRelationEntity(), null),
                new TestEntity(1L, false, 2, Collections.singletonList(2), "Entity 2",
                        Collections.singletonList("e2"), TestEnum.TWO, Collections.singletonList(TestEnum.ONE),
                        new TestEmbedded(20, "Embedded 20"),
                        Collections.singletonList(new TestEmbedded(22, "Embedded 22")),
                        new TestRelationEntity(), null)));
    }
}
