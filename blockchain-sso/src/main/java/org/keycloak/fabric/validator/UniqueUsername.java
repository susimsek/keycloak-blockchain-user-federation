package org.keycloak.fabric.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = { UniqueUsernameValidator.class }
)
public @interface UniqueUsername {

    String message() default "This username in use";//hata sonucu dönülen mesaj

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
