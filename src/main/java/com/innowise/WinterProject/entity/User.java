package com.innowise.WinterProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    // вообще они ж должны совпадать с колонками в других таблиицах так что генерить надо в одном месте,
    // я предполагаю что тут а там просто присваивать,правильно?
    //так что в юзере и учителе я выкинула генерацию
    private UUID id;
    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "roles")
    private Role role; //создать таблицу ролей

}
