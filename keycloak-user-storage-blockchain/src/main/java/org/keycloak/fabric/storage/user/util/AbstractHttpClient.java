package org.keycloak.fabric.storage.user.util;

import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public abstract class AbstractHttpClient {

    protected CloseableHttpClient httpClient;
    protected HttpClientContext context;

    private String baseUrl;
    private String adminUsername;
    private String adminPassword;



    public AbstractHttpClient() throws MalformedURLException {

        URL url = new URL(getBaseUrl());
        PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();

        poolingConnManager.setMaxTotal(5);
        poolingConnManager.setDefaultMaxPerRoute(4);

        HttpHost targetHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
        poolingConnManager.setMaxPerRoute(new HttpRoute(targetHost), 5);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(getAdminUsername(), getAdminPassword()));

        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());


        this.context = HttpClientContext.create();
        this.context.setCredentialsProvider(credsProvider);
        this.context.setAuthCache(authCache);

        this.httpClient = HttpClients.custom().setConnectionManager(poolingConnManager).build();
    }

    protected HttpResponse get(String uri) throws IOException {
        HttpGet request = new HttpGet(uri);
        request.setHeader("Content-Type", "application/json; charset=utf-8");
        HttpResponse response = httpClient.execute(
                request, context);
        return response;
    }

    protected HttpResponse get(String uri, List<NameValuePair> nameValuePairList) throws IOException, URISyntaxException {
        HttpGet request = new HttpGet(uri);
        URI uriWithParamater = new URIBuilder(request.getURI()).addParameters(nameValuePairList).build();
        request.setURI(uriWithParamater);
        request.setHeader("Content-Type", "application/json; charset=utf-8");
        HttpResponse response = httpClient.execute(
                request, context);
        return response;
    }


    protected HttpResponse post(String uri,String object) throws IOException {
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-Type", "application/json; charset=utf-8");
        HttpEntity stringEntity = new StringEntity(object, ContentType.APPLICATION_JSON);
        request.setEntity(stringEntity);
        HttpResponse response = httpClient.execute(
                request, context);
        return response;
    }

    protected HttpResponse put(String uri,String object) throws IOException {
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-Type", "application/json; charset=utf-8");
        HttpEntity stringEntity = new StringEntity(object, ContentType.APPLICATION_JSON);
        request.setEntity(stringEntity);
        HttpResponse response = httpClient.execute(
                request, context);
        return response;
    }

    protected HttpResponse delete(String uri) throws IOException {
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-Type", "application/json; charset=utf-8");
        HttpResponse response = httpClient.execute(
                request, context);
        return response;
    }

    public String getBaseUrl() {
        baseUrl=System.getenv("BLOCKCHAIN_BASE_URL");
        if(baseUrl==null){
            baseUrl="http://localhost:8081";
        }
        return baseUrl;
    }

    public String getAdminUsername() {
        adminUsername=System.getenv("BLOCKCHAIN_ADMIN_USER");
        if(adminUsername==null){
            adminUsername="admin";
        }
        return adminUsername;
    }

    public String getAdminPassword() {
        adminPassword=System.getenv("BLOCKCHAIN_ADMIN_PASS");
        if(adminPassword==null){
            adminPassword="root";
        }
        return adminPassword;
    }


}
