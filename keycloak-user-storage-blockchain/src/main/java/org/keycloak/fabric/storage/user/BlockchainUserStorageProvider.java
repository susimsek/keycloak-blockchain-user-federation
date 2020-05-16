/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.keycloak.fabric.storage.user;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.fabric.storage.user.dto.*;
import org.keycloak.fabric.storage.user.model.User;
import org.keycloak.fabric.storage.user.service.UserService;
import org.keycloak.fabric.storage.user.service.impl.UserServiceImpl;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.cache.OnUserCache;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import java.io.IOException;
import java.util.*;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Stateful
@Local(BlockchainUserStorageProvider.class)
public class BlockchainUserStorageProvider implements UserStorageProvider,
        UserLookupProvider,
        UserRegistrationProvider,
        UserQueryProvider,
        CredentialInputUpdater,
        CredentialInputValidator,
        OnUserCache
{
    private static final Logger logger = Logger.getLogger(BlockchainUserStorageProvider.class);
    public static final String PASSWORD_CACHE_KEY = UserAdapter.class.getName() + ".password";

    private UserService userService;


    protected ComponentModel model;
    protected KeycloakSession session;

    public void setModel(ComponentModel model) throws IOException {
        this.model = model;
        this.userService=new UserServiceImpl();
    }

    public void setSession(KeycloakSession session) {
        this.session = session;
        this.session.userCache().clear();
    }

    @Override
    public void preRemove(RealmModel realm) {

    }

    @Override
    public void preRemove(RealmModel realm, GroupModel group) {

    }

    @Override
    public void preRemove(RealmModel realm, RoleModel role) {

    }

    @Remove
    @Override
    public void close() {
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        logger.info("getUserById: " + id);
        String persistenceId = StorageId.externalId(id);

        UserDTO userDTO=userService.getUserById(persistenceId);

        if (userDTO == null) {
            logger.info("could not find user by id: " + id);
            return null;
        }
        return new UserAdapter(session, realm, model,userDTO,this.userService);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        logger.info("getUserByUsername: " + username);
        UserDTO userDTO=userService.getUserByUsername(username);
        if (userDTO == null) {
            logger.info("could not find username: " + username);
            return null;
        }
        return new UserAdapter(session, realm, model,userDTO,this.userService);
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        logger.info("getUserByEmail: " + email);

        UserDTO userDTO=userService.getUserByEmail(email);
        if (userDTO == null) {
            logger.info("could not find email: " + email);
            return null;
        }
        return new UserAdapter(session, realm, model,userDTO,this.userService);
    }

    @Override
    public UserModel addUser(RealmModel realm, String username) {
        logger.info("added user: " + username);
        User user=new User();
        user.setUsername(username);
        user.setName("keycloak");
        user.setSurname("keycloak");
        user.setEmail("keycloak@test.com");
        user.setPassword("keycloak");

        UserDTO userDTO=userService.addUser(user);

        logger.info("added user: " + username);

        return new UserAdapter(session, realm, model, userDTO,this.userService);
    }

    @Override
    public boolean removeUser(RealmModel realm, UserModel user) {
        String persistenceId = StorageId.externalId(user.getId());
        return userService.deleteUser(persistenceId);
    }

    @Override
    public void onCache(RealmModel realm, CachedUserModel user, UserModel delegate) {
        String password = ((UserAdapter)delegate).getPassword();
        if (password != null) {
            user.getCachedWith().put(PASSWORD_CACHE_KEY, password);
        }
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return CredentialModel.PASSWORD.equals(credentialType);
    }

    @Override
    public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;
        UserCredentialModel cred = (UserCredentialModel)input;
        UserAdapter adapter = getUserAdapter(user);

        String persistenceId = StorageId.externalId(user.getId());
        String password=cred.getValue();

        PasswordChangeDTO passwordChangeDTO=new PasswordChangeDTO();
        passwordChangeDTO.setPassword(password);

        return userService.changePassword(persistenceId,passwordChangeDTO);
    }

    public UserAdapter getUserAdapter(UserModel user) {
        UserAdapter adapter = null;
        if (user instanceof CachedUserModel) {
            adapter = (UserAdapter)((CachedUserModel)user).getDelegateForUpdate();
        } else {
            adapter = (UserAdapter)user;
        }
        return adapter;
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
        if (!supportsCredentialType(credentialType)) return;

        getUserAdapter(user).setPassword(null);

    }

    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {
        if (getUserAdapter(user).getPassword() != null) {
            Set<String> set = new HashSet<>();
            set.add(CredentialModel.PASSWORD);
            return set;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        return supportsCredentialType(credentialType) && getPassword(user) != null;
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;
        UserCredentialModel cred = (UserCredentialModel)input;

        String username=user.getUsername();

        String password= (cred.getValue());

        CredentialDTO credentialDTO=new CredentialDTO();
        credentialDTO.setUsername(username);
        credentialDTO.setPassword(password);

        return userService.authUser(credentialDTO);

    }

    public String getPassword(UserModel user) {
        String password = null;
        if (user instanceof CachedUserModel) {
            password = (String)((CachedUserModel)user).getCachedWith().get(PASSWORD_CACHE_KEY);
        } else if (user instanceof UserAdapter) {
            password = ((UserAdapter)user).getPassword();
        }
        return password;
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        return userService.countUsers();
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm) {
        return getUsers(realm, -1, -1);
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
        List<UserDTO> userDTOList = userService.getAllUsers(firstResult,maxResults);
        List<UserModel> users = new LinkedList<>();
        for (UserDTO userDTO : userDTOList) users.add(new UserAdapter(session, realm, model, userDTO,this.userService));
        return users;
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm) {
        return searchForUser(search, realm, -1, -1);
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
        UserSearchDTO userSearchDTO = new UserSearchDTO();
        userSearchDTO.setSearch(search);
        userSearchDTO.setFirstResult(firstResult);
        userSearchDTO.setMaxResults(maxResults);

        List<UserDTO> userDTOList = userService.searchUser(userSearchDTO);

        List<UserModel> users = new LinkedList<>();
        for (UserDTO userDTO : userDTOList)
            users.add(new UserAdapter(session, realm, model, userDTO, this.userService));
        return users;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults) {
        List<UserDTO> userDTOList = userService.getAllUsers(firstResult,maxResults);
        List<UserModel> users = new LinkedList<>();
        for (UserDTO userDTO : userDTOList) users.add(new UserAdapter(session, realm, model, userDTO,this.userService));
        return users;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
        return Collections.EMPTY_LIST;
    }
}
