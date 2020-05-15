package org.keycloak.fabric.storage.user.service;

import org.keycloak.fabric.storage.user.dto.CountDTO;
import org.keycloak.fabric.storage.user.dto.CredentialDTO;
import org.keycloak.fabric.storage.user.dto.PasswordChangeDTO;
import org.keycloak.fabric.storage.user.dto.UserDTO;
import org.keycloak.fabric.storage.user.model.User;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers(int firstResult, int maxResults);

    UserDTO getUserById(String id);

    UserDTO getUserByUsername(String username);

    UserDTO getUserByEmail(String email);

    UserDTO addUser(User user);

    UserDTO updateUser(String id,UserDTO userDTO);

    boolean deleteUser(String id);

    boolean authUser(CredentialDTO credentialDTO);

    boolean changePassword(String id, PasswordChangeDTO passwordChangeDTO);

    int countUsers();
}
