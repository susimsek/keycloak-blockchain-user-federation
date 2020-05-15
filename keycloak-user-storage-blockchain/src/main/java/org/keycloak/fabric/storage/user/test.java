package org.keycloak.fabric.storage.user;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.fabric.storage.user.dto.CredentialDTO;
import org.keycloak.fabric.storage.user.dto.UserDTO;
import org.keycloak.fabric.storage.user.model.User;
import org.keycloak.fabric.storage.user.service.UserService;
import org.keycloak.fabric.storage.user.service.impl.UserServiceImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class test {

    public static void main(String[] args) throws IOException {
        UserService userService=new UserServiceImpl();

        CredentialDTO credentialDTO=new CredentialDTO();
        credentialDTO.setUsername("testcv8");
        credentialDTO.setPassword("1234");

        Boolean k=userService.authUser(credentialDTO);
        System.out.println(k);




    }
}
