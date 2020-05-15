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
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.fabric.storage.user.dto.UserDTO;
import org.keycloak.fabric.storage.user.model.User;
import org.keycloak.fabric.storage.user.service.UserService;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class UserAdapter extends AbstractUserAdapterFederatedStorage {
    private static final Logger logger = Logger.getLogger(UserAdapter.class);
    protected String keycloakId;

    private UserService userService;

    protected UserDTO userDTO;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, UserDTO userDTO,UserService userService) {
        super(session, realm, model);
        this.userDTO=userDTO;
        keycloakId = StorageId.keycloakId(model, userDTO.getId());
        this.userService=userService;
    }

    public String getPassword() {
        return null;
    }

    public void setPassword(String password) {
        //entity.setPassword(password);
    }

    @Override
    public String getUsername() {

        return userDTO.getUsername();
    }

    @Override
    public void setUsername(String username) {

        userDTO.setUsername(username);
        userService.updateUser(keycloakId,userDTO);

    }

    @Override
    public void setEmail(String email) {

        userDTO.setEmail(email);
        userService.updateUser(keycloakId,userDTO);
    }

    @Override
    public String getEmail() {

        return userDTO.getEmail();
    }

    @Override
    public String getId() {
        return keycloakId;
    }

    @Override
    public void setSingleAttribute(String name, String value) {

        if (name.equals("FIRST_NAME")) {
            userDTO.setName(value);
            userDTO=userService.updateUser(userDTO.getId(),userDTO);
        }
       if (name.equals("LAST_NAME")) {
            userDTO.setSurname(value);
            userDTO=userService.updateUser(userDTO.getId(),userDTO);
        }
        else {
            super.setSingleAttribute(name, value);
        }
    }

    @Override
    public void removeAttribute(String name) {
        if (name.equals("FIRST_NAME")) {
            userDTO.setName(null);
            userDTO=userService.updateUser(userDTO.getId(),userDTO);
        }
        if (name.equals("LAST_NAME")) {
            userDTO.setSurname(null);
            userDTO=userService.updateUser(userDTO.getId(),userDTO);
        }
        else {
            super.removeAttribute(name);
        }

    }

    @Override
    public void setAttribute(String name, List<String> values)
    {
        if (name.equals("FIRST_NAME")) {
            userDTO.setName(values.get(0));
            userDTO=userService.updateUser(userDTO.getId(),userDTO);
        }
       if (name.equals("LAST_NAME")) {
            userDTO.setSurname(values.get(0));
            userDTO=userService.updateUser(userDTO.getId(),userDTO);
        }
        else {
            super.setAttribute(name, values);
        }
    }

    @Override
    public String getFirstAttribute(String name) {
        if (name.equals("FIRST_NAME")) {
            return userDTO.getName();
        }
        if (name.equals("LAST_NAME")) {
            return userDTO.getSurname();
        }
        else {
            return super.getFirstAttribute(name);
        }
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        Map<String, List<String>> attrs = super.getAttributes();
        MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();
        all.putAll(attrs);
        all.add("FIRST_NAME", userDTO.getName());
        all.add("LAST_NAME", userDTO.getSurname());
        return all;
    }

    @Override
    public List<String> getAttribute(String name) {
        if (name.equals("FIRST_NAME")) {
            List<String> firstName = new LinkedList<>();
            firstName.add(userDTO.getName());
            return firstName;
        }
        if (name.equals("LAST_NAME")) {
            List<String> firstName = new LinkedList<>();
            firstName.add(userDTO.getSurname());
            return firstName;
        }

        else {
            return super.getAttribute(name);
        }
    }
}
