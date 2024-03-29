package com.innowise.WinterProject.repository;

import com.innowise.WinterProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    public Optional<User> findByLogin(String login);

    public Optional<User> findByStudentId(UUID id);

    public Optional<User> findByTeacherId(UUID id);

}
