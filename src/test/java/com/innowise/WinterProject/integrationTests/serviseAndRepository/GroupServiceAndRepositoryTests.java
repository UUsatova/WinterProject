package com.innowise.WinterProject.integrationTests.serviseAndRepository;

import com.innowise.WinterProject.entity.Group;
import com.innowise.WinterProject.exeption.ItemNotFoundException;
import com.innowise.WinterProject.repository.GroupRepository;
import com.innowise.WinterProject.service.GroupService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GroupServiceAndRepositoryTests {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    GroupService groupService;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>()
            .withUsername("DeadushkaMoroz")
            .withPassword("3belihKonia")
            .withDatabaseName("NoviGod");


    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @Order(1)
    void getAllGroupsTest() {
        Group group1 = new Group(UUID.fromString("100e0000-e00b-00d0-a000-000000000000"), 1, 0, 1);
        Group group2 = new Group(UUID.fromString("200e0000-e00b-00d0-a000-000000000000"), 2, 0, 2);
        Group group3 = new Group(UUID.fromString("300e0000-e00b-00d0-a000-000000000000"), 3, 0, 3);
        List<Group> expectedGroups = Arrays.asList(group1, group2, group3);

        List<Group> actualGroups = groupService.getAllGroups();
        assertEquals(expectedGroups, actualGroups);

    }

    @Test
    @Order(2)
    void getGroupByIdWithCorrectId() {
        Group expectedGroup = new Group(UUID.fromString("100e0000-e00b-00d0-a000-000000000000"), 1, 0, 1);
        Group actualGroup = groupService.getGroupById(UUID.fromString("100e0000-e00b-00d0-a000-000000000000"));
        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    @Order(3)
    void getGroupByIdWithWrongID() {
        assertThrows(ItemNotFoundException.class, () -> groupService.getGroupById(UUID.fromString("100e0000-e00b-00d0-a000-000000900000")));
    }

    @Test
    @Order(4)
    void addGroupReturnAddedGroup() {
        Group expectedGroup = Group.builder().number(4).year(0).numberOfStudents(4).build();
        Group actualGroup = groupService.addGroup(expectedGroup);
        expectedGroup.setId(actualGroup.getId());
        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    @Order(5)
    void removeGroupRemoveGroupWithSuchId() {
        Group group = groupService.addGroup(Group.builder().number(5).year(5).numberOfStudents(0).build());
        groupService.removeGroup(group.getId());
        assertThrows(ItemNotFoundException.class, () -> groupService.getGroupById(group.getId()));

    }

    @Test
    @Order(6)
    void updateGroupUpdateGroupWithSuchId() {
        Group groupBeforeModifications = groupService.addGroup(Group.builder().number(6).year(6).numberOfStudents(0).build());
        Group groupModification = Group.builder().number(6).year(7).numberOfStudents(0).build();
        groupModification.setId(groupBeforeModifications.getId());
        Group groupAfterModifications = groupService.updateGroup(groupModification);

        assertEquals(groupBeforeModifications.getId(), groupAfterModifications.getId());
        assertEquals(groupModification, groupAfterModifications);

    }

    @Test
    @Order(7)
    public void increaseNumberOfStudentsInGroupTest() {
        Group group = groupService.addGroup(Group.builder().number(5).year(5).numberOfStudents(0).build());
        groupService.increaseNumberOfStudentsInGroup(group);
        assertEquals(group.getNumberOfStudents(),groupService.getGroupById(group.getId()).getNumberOfStudents());
    }

    @Test
    @Order(8)
    public void decreaseNumberOfStudentsInGroupTest() {
        Group group = groupService.addGroup(Group.builder().number(5).year(5).numberOfStudents(1).build());
        groupService.decreaseNumberOfStudentsInGroup(group);
        assertEquals(group.getNumberOfStudents(),groupService.getGroupById(group.getId()).getNumberOfStudents());
    }


}
