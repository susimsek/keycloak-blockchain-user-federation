package org.keycloak.fabric.validator;

import lombok.RequiredArgsConstructor;
import org.keycloak.fabric.model.User;
import org.keycloak.fabric.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        User user=userRepository.findByUsername(username);

        if(user!=null){
            return false;
        }
        return true;
    }
}
