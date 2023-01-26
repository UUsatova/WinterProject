package com.innowise.WinterProject.validation;

import com.innowise.WinterProject.repository.UserRepository;
import com.innowise.WinterProject.validation.annotation.UniqueLogin;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class LoginExistInDataBase implements ConstraintValidator<UniqueLogin, String> {

    private final UserRepository repository;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        return repository.findByLogin(login).isEmpty();
    }
}
