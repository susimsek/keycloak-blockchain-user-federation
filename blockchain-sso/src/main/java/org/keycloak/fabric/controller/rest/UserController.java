package org.keycloak.fabric.controller.rest;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.keycloak.fabric.dto.*;
import org.keycloak.fabric.model.User;
import org.keycloak.fabric.model.elasticsearch.UserElasticModel;
import org.keycloak.fabric.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Api(value = "User Management System")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "Search user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully searching user"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/search")
    public List<UserElasticModel> search(@ApiParam(value = "Search Object", required = true) @RequestBody UserSearchDTO userSearchDTO) {
        return userService.searchUser(userSearchDTO);
    }

    @ApiOperation(value = "User Authentication")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/auth")
    public AuthStatusDTO isValid(@ApiParam(value = "User Credential", required = true) @Valid @RequestBody CredentialDTO credentialDTO) {
        return userService.isValid(credentialDTO);
    }

    @ApiOperation(value = "Change User Password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully changed password"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/password")
    public void changePassword(@ApiParam(value = "User Id", required = true) @PathVariable String id,@Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
        userService.changePassword(id,passwordChangeDTO);
    }

    @ApiOperation(value = "Get a user by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public UserDTO get(@ApiParam(value = "User Id", required = true) @PathVariable String id) {
        UserDTO userDTO=userService.get(id);
        return userDTO;
    }

    @ApiOperation(value = "Get a user by Username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/username/{username}")
    public UserDTO findByUsername(@ApiParam(value = "Username", required = true) @PathVariable String username) {
        UserDTO userDTO=userService.findByUsername(username);
        return userDTO;
    }

    @ApiOperation(value = "Get a user by Email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/email/{email}")
    public UserDTO findByEmail(@ApiParam(value = "Email", required = true) @PathVariable String email) {
        UserDTO userDTO=userService.findByEmail(email);
        return userDTO;
    }

    @ApiOperation(value = "Users Count")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user count"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/count")
    public CountDTO count() {
       return userService.count();
    }

    @ApiOperation(value = "View a list of available users", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user list"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserDTO> findAll(@RequestParam(defaultValue = "-1") int firstResult, @RequestParam(defaultValue = "-1") int maxResults) {
        List<UserDTO> userDTOList=userService.findAll(firstResult,maxResults);
        return userDTOList;
    }

    @ApiOperation(value = "Add a user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created user"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDTO create(@ApiParam(value = "User Object", required = true) @Valid @RequestBody User user) {
        UserDTO userDTO = userService.create(user);
        return userDTO;
    }

    @ApiOperation(value = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated user"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public UserDTO update(@ApiParam(value = "User Id", required = true) @PathVariable String id,@ApiParam(value = "Update User Object", required = true) @RequestBody UserDTO userDTO) {
       UserDTO updatedUserDTO = userService.update(id,userDTO);
       return updatedUserDTO;
    }


    @ApiOperation(value = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted user"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "User Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@ApiParam(value = "User Id", required = true) @PathVariable String id) {
        userService.delete(id);
    }
}
