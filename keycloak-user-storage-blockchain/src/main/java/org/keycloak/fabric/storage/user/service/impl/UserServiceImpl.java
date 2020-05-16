package org.keycloak.fabric.storage.user.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.keycloak.fabric.storage.user.dto.*;
import org.keycloak.fabric.storage.user.model.User;
import org.keycloak.fabric.storage.user.service.UserService;
import org.keycloak.fabric.storage.user.util.AbstractHttpClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserServiceImpl extends AbstractHttpClient implements UserService {

    private ObjectMapper objectMapper;

    public UserServiceImpl() throws MalformedURLException {
        super();
        objectMapper = new ObjectMapper();
    }

    @Override
    public List<UserDTO> searchUser(UserSearchDTO userSearchDTO) {

        String uri = getBaseUrl() + "/api/v1/users/search";
        HttpResponse response = null;
        try {
            String userString = objectMapper.writeValueAsString(userSearchDTO);
            response = post(uri, userString);
            if (response.getStatusLine().getStatusCode() == 200) {
                List<UserDTO> userDTOList = objectMapper.readValue(response.getEntity().getContent(), new TypeReference<List<UserDTO>>() {
                });
                return userDTOList;
            }
        } catch (IOException e) {
            return Collections.EMPTY_LIST;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserDTO> getAllUsers(int firstResult, int maxResults) {
        String uri = getBaseUrl() + "/api/v1/users";
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        NameValuePair firstResultNv = new BasicNameValuePair("firstResult", Integer.toString(firstResult));
        NameValuePair maxResultsNv = new BasicNameValuePair("maxResults", Integer.toString(maxResults));
        nameValuePairList.add(firstResultNv);
        nameValuePairList.add(maxResultsNv);
        HttpResponse response = null;
        try {
            response = get(uri,nameValuePairList);
            if(response.getStatusLine().getStatusCode()==200){
                List<UserDTO> userDTOList = objectMapper.readValue(response.getEntity().getContent(), new TypeReference<List<UserDTO>>(){});
                return userDTOList;
            }
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
        return Collections.EMPTY_LIST;

    }

    @Override
    public UserDTO getUserById(String id){
        String uri=getBaseUrl()+"/api/v1/users/"+id;
        HttpResponse response= null;
        try {
            response = get(uri);
            if(response.getStatusLine().getStatusCode()==200){
                UserDTO userDTO = objectMapper.readValue(response.getEntity().getContent(), UserDTO.class);
                return userDTO;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        String uri=getBaseUrl()+"/api/v1/users/username/"+username;
        HttpResponse response= null;
        try {
            response = get(uri);
            if(response.getStatusLine().getStatusCode()==200){
                UserDTO userDTO = objectMapper.readValue(response.getEntity().getContent(), UserDTO.class);
                return userDTO;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        String uri=getBaseUrl()+"/api/v1/users/email/"+email;
        HttpResponse response= null;
        try {
            response = get(uri);
            if(response.getStatusLine().getStatusCode()==200){
                UserDTO userDTO = objectMapper.readValue(response.getEntity().getContent(), UserDTO.class);
                return userDTO;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    @Override
    public UserDTO addUser(User user) {

        String uri=getBaseUrl()+"/api/v1/users";
        HttpResponse response= null;
        try {
            String userString = objectMapper.writeValueAsString(user);
            response = post(uri,userString);
            if(response.getStatusLine().getStatusCode()==201){
                UserDTO userDTO = objectMapper.readValue(response.getEntity().getContent(), UserDTO.class);
                return userDTO;
            }
        } catch (IOException e) {
            return null;
        }
        return null;



    }

    @Override
    public UserDTO updateUser(String id, UserDTO userDTO) {
        String uri=getBaseUrl()+"/api/v1/users/"+id;
        HttpResponse response= null;
        try {
            String userString = objectMapper.writeValueAsString(userDTO);
            response = put(uri,userString);
            if(response.getStatusLine().getStatusCode()==200){
                UserDTO updatedUserDTO = objectMapper.readValue(response.getEntity().getContent(), UserDTO.class);
                return userDTO;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    @Override
    public boolean deleteUser(String id) {

        String uri=getBaseUrl()+"/api/v1/users/"+id;
        HttpResponse response= null;
        try {
            response = delete(uri);
            if(response.getStatusLine().getStatusCode()==204){
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;

    }

    @Override
    public boolean authUser(CredentialDTO credentialDTO) {
        String uri=getBaseUrl()+"/api/v1/users/auth";
        HttpResponse response= null;
        try {
            String userString = objectMapper.writeValueAsString(credentialDTO);
            response = post(uri,userString);
            if(response.getStatusLine().getStatusCode()==200){
                AuthStatusDTO authStatusDTO = objectMapper.readValue(response.getEntity().getContent(), AuthStatusDTO.class);
                boolean authenticated=authStatusDTO.getAuthenticated();
                return authenticated;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }


    @Override
    public boolean changePassword(String id, PasswordChangeDTO passwordChangeDTO) {
        String uri=getBaseUrl()+"/api/v1/users/"+id+"/password";
        HttpResponse response= null;
        try {
            String userString = objectMapper.writeValueAsString(passwordChangeDTO);
            response = put(uri,userString);
            if(response.getStatusLine().getStatusCode()==200){
               return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;

    }

    @Override
    public int countUsers() {
        String uri=getBaseUrl()+"/api/v1/users/count";
        HttpResponse response= null;
        try {
            response = get(uri);
            if(response.getStatusLine().getStatusCode()==200){
                CountDTO countDTO = objectMapper.readValue(response.getEntity().getContent(), CountDTO.class);
                return countDTO.getCount();
            }
        } catch (IOException e) {
            return 0;
        }
        return 0;
    }
}
