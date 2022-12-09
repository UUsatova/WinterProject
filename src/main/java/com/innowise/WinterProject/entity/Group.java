package com.innowise.WinterProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups")
public class Group {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "number")
    private int number;

    @Column(name = "number_of_students")
    private int numberOfStudents;

    @Column(name = "year")
    private int year;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return number == group.number && numberOfStudents == group.numberOfStudents && year == group.year && Objects.equals(id, group.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, numberOfStudents, year);
    }
}
