package org.keycloak.fabric.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.keycloak.fabric.model.constant.ModelConstant;
import org.keycloak.fabric.validator.UniqueUsername;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class User implements Serializable {

    @ApiModelProperty(notes = "The User ID")
    private String id = UUID.randomUUID().toString();

    @ApiModelProperty(notes = "The Created Time of User")
    private long createdAt = Instant.now().toEpochMilli();

    @ApiModelProperty(notes = "The Type of User")
    private String type= ModelConstant.USER_TYPE;

    @ApiModelProperty(notes = "The Username of User",required = true)
    @UniqueUsername
    @NotNull(message = "Username cannot be null")
    @Size(min = 3, max = 255)
    private String username;

    @ApiModelProperty(notes = "The Password of User",required = true)
    @NotNull(message = "{Password cannot be null")
    @Size(min = 3, max = 255)
    private String password;

    @ApiModelProperty(notes = "The Email of User",required = true)
    @NotNull(message = "Email cannot be null")
    @Size(min = 3, max = 255)
    private String email;

    @ApiModelProperty(notes = "The Firstname of User",required = true)
    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 255)
    private String name;

    @ApiModelProperty(notes = "The Lastname of User",required = true)
    @NotNull(message = "Surname cannot be null")
    @Size(min = 3, max = 255)
    private String surname;
}
