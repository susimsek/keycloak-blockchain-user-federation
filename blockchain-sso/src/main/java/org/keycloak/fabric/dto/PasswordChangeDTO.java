package org.keycloak.fabric.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDTO {

    @NotNull(message = "Password cannot be null")
    private String password;
}
