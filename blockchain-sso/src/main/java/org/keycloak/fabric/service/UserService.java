package org.keycloak.fabric.service;


import org.keycloak.fabric.dto.*;
import org.keycloak.fabric.model.User;
import org.keycloak.fabric.model.elasticsearch.UserElasticModel;

import java.util.List;

public interface UserService {

    UserDTO get(String id);

    List<UserDTO> findAll(int firstResult, int maxResults);

    UserDTO findByUsername(String username);

    UserDTO findByEmail(String email);

    UserDTO create(User user);

    UserDTO update(String id, UserDTO userDTO);

    void delete(String id);

    CountDTO count();

    AuthStatusDTO isValid(CredentialDTO credentialDTO);

    void changePassword(String id, PasswordChangeDTO passwordChangeDTO);

    List<UserElasticModel> searchUser(UserSearchDTO userSearchDTO);

}
