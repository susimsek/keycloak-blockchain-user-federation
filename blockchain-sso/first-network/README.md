## Build Your First Network (BYFN)

The directions for using this are documented in the Hyperledger Fabric
["Build Your First Network"](http://hyperledger-fabric.readthedocs.io/en/latest/build_network.html) tutorial.

*NOTE:* After navigating to the documentation, choose the documentation version that matches your version of Fabric

### 运行

```
./byfn.sh up -a -s couchdb
```

### 修改

为了安装[通用链码](https://github.com/ecsoya/spring-fabric-gateway/raw/master/spring-fabric-gateway/src/chaincode/common/chaincode.go)，做了几处修改。

一、 由于将链码放到了`src/chaincode`目录下，所以在`scripts/script.sh`中对链码的位置做了相应的修改。

```
CC_SRC_PATH="github.com/chaincode/common/"
```

二、 同样的，将`docker-composite-cli.yaml`中关于链码的映射路径(83行)做了修改。

```
        - ./../src/chaincode/:/opt/gopath/src/github.com/chaincode 
```

三、修改了`utils.sh`中执行Chaincode的默认的`init`、`invoke`和`query`方法。

1. invoke -> create
2. query -> get
