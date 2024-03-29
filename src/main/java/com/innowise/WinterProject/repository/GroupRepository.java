package com.innowise.WinterProject.repository;

import com.innowise.WinterProject.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {
    boolean existsByIdAndNumberOfStudents(UUID id, int numberOfStudents);
}
