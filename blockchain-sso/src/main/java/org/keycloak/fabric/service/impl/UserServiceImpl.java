package org.keycloak.fabric.service.impl;

import lombok.RequiredArgsConstructor;
import org.keycloak.fabric.dto.*;
import org.keycloak.fabric.error.UserNotFoundException;
import org.keycloak.fabric.model.User;
import org.keycloak.fabric.repository.UserRepository;
import org.keycloak.fabric.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.Type;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @Cacheable(value= "userCache", key= "#id")
    @Override
    public UserDTO get(String id) {
        User user = userRepository.get(id);
        if (user==null){
            throw new UserNotFoundException();
        }
        return modelMapper.map(user,UserDTO.class);
    }


    @Cacheable(value= "allUsersCache", unless= "#result.size() == 0")
    @Override
    public List<UserDTO> findAll(int firstResult, int maxResults) {
        List<User> userList=userRepository.findAll(firstResult,maxResults);
        Type listType = new TypeToken<List<UserDTO>>(){}.getType();
        return modelMapper.map(userList,listType);

    }

    @Override
    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user==null){
            throw new UserNotFoundException();
        }
        return modelMapper.map(user,UserDTO.class);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user==null){
            throw new UserNotFoundException();
        }
        return modelMapper.map(user,UserDTO.class);
    }

    @Caching(
            put= { @CachePut(value= "userCache", key= "#user.id") },
            evict= { @CacheEvict(value= "allUsersCache", allEntries= true) }
    )
    @Override
    public UserDTO create(User user) {
        Assert.notNull(user, "user object must not be null!");
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.create(user);
        return modelMapper.map(user,UserDTO.class);
    }

    @Caching(
            put= { @CachePut(value= "userCache", key= "#id") },
            evict= { @CacheEvict(value= "allUsersCache", allEntries= true) }
    )
    @Override
    public UserDTO update(String id, UserDTO userDTO) {
        User user = userRepository.get(id);
        if (user==null){
            throw new UserNotFoundException();
        }

       user.setEmail(userDTO.getEmail());
       user.setName(userDTO.getName());
       user.setSurname(userDTO.getSurname());
       userRepository.update(user);

       return modelMapper.map(user,UserDTO.class);
    }

    @Caching(
            evict= {
                    @CacheEvict(value= "userCache", key= "#id"),
                    @CacheEvict(value= "allUsersCache", allEntries= true)
            }
    )
    @Override
    public void delete(String id) {
        User user = userRepository.get(id);
        if (user==null){
            throw new UserNotFoundException();
        }
        userRepository.delete(id);
    }

    @Override
    public CountDTO count() {
        int count=userRepository.count();
        CountDTO countDTO=new CountDTO();
        countDTO.setCount(count);
        return countDTO;
    }

    @Override
    public AuthStatusDTO isValid(CredentialDTO credentialDTO) {
        User user = userRepository.findByUsername(credentialDTO.getUsername());
        AuthStatusDTO authStatusDTO=new AuthStatusDTO();
        if (user==null){
            throw new UserNotFoundException();
        }
        authStatusDTO.setAuthenticated(passwordEncoder.matches(credentialDTO.getPassword(),user.getPassword()));
        return authStatusDTO;
    }

    @Override
    public void changePassword(String id, PasswordChangeDTO passwordChangeDTO) {
        User user = userRepository.get(id);
        if (user==null){
            throw new UserNotFoundException();
        }
        String encodedPassword = passwordEncoder.encode(passwordChangeDTO.getPassword());
        user.setPassword(encodedPassword);
        userRepository.update(user);

    }
}
