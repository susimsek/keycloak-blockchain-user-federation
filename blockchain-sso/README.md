# Keycloak Blockchain User Federation
> Decentralized Distributed Single Sign On Authentication System With Keycloak

## Keycloak 
<img src="https://github.com/susimsek/spring-boot-blockchain-app/blob/master/images/keycloak-logo.png" alt="Keycloak" width="75%" height="75%"/>

## Hyperledger Fabric
<img src="https://github.com/susimsek/spring-boot-blockchain-app/blob/master/images/hyperledger-fabric-logo.png" alt="Hyperledger Fabric" width="75%" height="75%"/>


## Hyperledger Explorer
<img src="https://github.com/susimsek/spring-boot-blockchain-app/blob/master/images/hyperledger-explorer-logo.png" alt="Hyperledger Explorer" width="75%" height="75%"/>

## Spring Boot
<img src="https://github.com/susimsek/spring-boot-blockchain-app/blob/master/images/spring-boot-logo.png" alt="Spring Boot" width="75%" height="75%"/>

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


## App Images

> Url : http://localhost:8081/explorer

<img src="https://github.com/susimsek/spring-boot-blockchain-app/blob/master/images/fabric-explorer.png" alt="Fabric Explorer Home Page" width="75%" height="75%"/>

> Url : http://localhost:8090
> Username : admin
> Password : adminpw

<img src="https://github.com/susimsek/spring-boot-blockchain-app/blob/master/images/hyperledger-explorer.png" alt="Hyperledger Explorer Home Page" width="75%" height="75%"/>

## Used Technologies

* Spring Boot 2.2.6
* Hyperledger Fabric
* Hyperledger Explorer
* Fabric Explorer

