package com.innowise.WinterProject.validation;

import com.innowise.WinterProject.repository.UserRepository;
import com.innowise.WinterProject.validation.annotation.UniqueLogin;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginExistInDataBase implements ConstraintValidator<UniqueLogin, String> {

    private final UserRepository repository;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        return repository.findByLogin(login).isPresent();
    }
}
