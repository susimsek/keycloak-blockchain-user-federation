package org.keycloak.fabric.repository;


import org.keycloak.fabric.model.User;

import java.util.List;

public interface UserRepository {

    User get(String id);

    List<User> findAll(int firstResult, int maxResults);

    User findByUsername(String username);

    User findByEmail(String email);

    User create(User user);

    User update(User user);

    void delete(String id);

    int count();

}
