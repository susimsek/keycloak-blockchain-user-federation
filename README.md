# Keycloak Blockchain User Federation
Decentralized Distributed Single Sign On Authentication System with Keycloak  


Supported Keycloak Version : 10.0.1  


## Keycloak 
<img src="https://github.com/susimsek/keycloak-blockchain-user-federation/blob/master/images/keycloak-logo.png" alt="Keycloak" width="75%" height="75%"/>  

## Redis 
<img src="https://github.com/susimsek/keycloak-blockchain-user-federation/blob/master/images/redis-logo.png" alt="Redis" width="75%" height="75%"/>  

## Hyperledger Fabric
<img src="https://github.com/susimsek/keycloak-blockchain-user-federation/blob/master/images/hyperledger-fabric-logo.png" alt="Hyperledger Fabric" width="75%" height="75%"/>  


## Hyperledger Explorer
<img src="https://github.com/susimsek/keycloak-blockchain-user-federation/blob/master/images/hyperledger-explorer-logo.png" alt="Hyperledger Explorer" width="75%" height="75%"/>  

## Spring Boot
<img src="https://github.com/susimsek/keycloak-blockchain-user-federation/blob/master/images/spring-boot-logo.png" alt="Spring Boot" width="75%" height="75%"/>  

## Prerequisites

* Jdk 1.8
* Maven 3.x
* Docker 19.03.x
* Docker Compose 1.25.x

## Installation

```sh
cd keycloak-user-storage-blockchain
```

```sh
mvn clean install
```

```sh
sudo cp target/user-storage-blockchain.jar ../blockchain-sso/keycloak/jars
```

```sh
cd ..
```

```sh
cd blockchain-sso/first-network
```

```sh
./byfn.sh up -a -s couchdb
```

```sh
cd ..
```

```sh
sudo chmod +x build.sh
```

```sh
./build.sh
```

```sh
docker-compose up -d
```


## Getting Started

### Keycloak

Keycloak Admin Username : admin  
Keycloak Admin Password : keycloak  
Keycloak Admin Url : http://localhost:9080/auth/  

<img src="https://github.com/susimsek/keycloak-blockchain-user-federation/blob/master/images/keycloak-login.png" alt="Keycloak Login" width="75%" height="75%"/>  


Keycloak blockchain federation is enabled by default on blochain realm.  

<img src="https://github.com/susimsek/keycloak-blockchain-user-federation/blob/master/images/user-federation.png" alt="Keycloak User Federation" width="75%" height="75%"/>  


The following operations are currently active in the keycloak admin panel.  

* View All Users on Blockchain  
* Create User on Blockchain  
* Update User on Blockchain  
* Delete User on Blockchain  
* Change User Password on Blockchain  
* Authenticate User on Blockchain  

<img src="https://github.com/susimsek/keycloak-blockchain-user-federation/blob/master/images/keycloak-users-view.png" alt="Keycloak User View" width="75%" height="75%"/>  

### Blockchain User Rest Api

Admin Username : admin      
Admin Password : root  
Swagger Url : http://localhost:8081  

<img src="https://github.com/susimsek/keycloak-blockchain-user-federation/blob/master/images/swagger.png" alt="Blockchain Rest Api Swagger" width="75%" height="75%"/>  


Basic authentication is enabled in all rest apis.  
Only admin user can access these APIs.        
All rest apis can be tested on the swagger.    
<img src="https://github.com/susimsek/keycloak-blockchain-user-federation/blob/master/images/apis.png" alt="Blockchain Rest Apis" width="75%" height="75%"/>  

### Hyperledger Explorer

Admin Username : admin    
Admin Password : adminpw  
Hyperledger Explorer Url : http://localhost:8090  

All transactions on the blockchain network can be viewed on hyperledger explorer.  
<img src="https://github.com/susimsek/keycloak-blockchain-user-federation/blob/master/images/hyperledger-explorer-view.png" alt="Hyperledger Explorer View" width="75%" height="75%"/>

### Fabric Explorer

Fabric Explorer Url : http://localhost:8081/explorer  

Block info,block hash,blockchain network info information  on the blockchain network can be viewed on fabric explorer.  
<img src="https://github.com/susimsek/keycloak-blockchain-user-federation/blob/master/images/fabric-explorer-view.png" alt="Fabric Explorer View" width="75%" height="75%"/>  


## Used Technologies

* Spring Boot 2.2.6  
* Keycloak  
* Swagger  
* Redis  
* Hyperledger Fabric  
* Hyperledger Explorer  
* Fabric Explorer  

## Todo

* Keycloak Blockchain Federation Elasticsearch Integration for User Searching  
* Keycloak Blockchain Federation Helm Deployment  