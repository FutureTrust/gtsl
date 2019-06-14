# GTSL - Project Setup Guidelines

This README provides the guidelines for setting up 
the Global Trust Service Status List (GTSL) project 
that is part of the Future Trust project.

## Prerequisites

### Dependencies

Tool | Version
--- | ---
Java | 1.8
Git | latest
Maven | latest
Docker | latest
Docker-compose | latest

### Ports

You have to ensure that the following ports are available:

Usage | Protocol | Port
--- | --- | ---
Ethereum | TCP | 30303
Ethereum | UDP | 30303
IPFS | TCP | 4001

## Warning

**On Windows and Mac OS**, you have to clone the repository 
wherever you want in your HOME. 
The docker feature which allows you mounting volumes only works in your HOME.

If you are using docker-machine (via Docker Toolbox):
- make sure to **always** use the *Docker Quickstart Terminal* 
(provided with the Docker Toolbox);
- make sure that your virtual machine has a minimum 2 Go of RAM. 

## Project Initialization

First of all, clone the repository of the project on your machine 
(preferably in your HOME).

## Choose a Network ID
In the **docker/docker-compose.yml** file, set a value for **NET_ID** (e.g. NET_ID=794613) 
and use this value for all nodes you will set up.

## Choose a Maximum Peers value
In the **docker/docker-compose.yml** file, set a value for **MAX_PEERS** (e.g. MAX_PEERS=50) 
to specify the maximum peers which can be connected to the node.

## Set up the environment

**If you are using docker-machine** 
(which should be the case on Windows (< 10) and Mac OS), 
pass in as arguments the machine name 
(if you are using Docker Toolbox the machine name should be default). 
The endpoints will be reached through the docker-machine.

**If you don't use docker-machine**, don't pass any arguments. 
The endpoints will be reached on localhost.

The following command will set up the project.

```sh
$ ./scripts/setup/setup.sh [docker-machine-name]
```

You have to wait for the containers to be ready. 
This can take up to 10 minutes.

**Note**: If you already did the configuration above and 
you run the starting script again, the script will use 
the keystore which was generated previously.

**Troubleshooting**: In order to check if everything is fine, 
you can type `docker logs -f ethereum-node`, and 
you should see *Generating DAG in progress*. 
If it is not the case, please check you followed all the previous steps.

## Connect to the node

Connect to the docker container with the following command.
```sh
$ docker exec -it ethereum-node sh
```

Copy the **genesis.json** file on your computer, you will need it later.
```sh
$ cat /opt/genesis.json
```

Then, connect to the Ethereum console with the following command.
```sh
$ geth attach
```

Copy the **enode** value of your node on your computer, you will need it later.
```sh
$ admin.nodeInfo
enode://723d96ad2fbc141cad0543e2a022648775effa963af12e05484436cbe8396248044177a9274a936fde08e873e81b729920319272791764c2b7548b0d9c5ad230@[::]:30303
```
In the **enode** value, replace **@[::]:30303** with the actual combination of 
**@ip:port** of your machine (e.g. **@10.0.0.0:30303**).

**Tips**: To exit a console, use the following command.
```sh
$ exit
```

## Deploy the smart-contracts

> Before deploying the smart-contracts, you should know that 
you can use existing smart-contracts. 
To do so, configure the file contract.properties in the folder properties.
If you use existing smart-contracts, skip the step.

You can deploy all the smart-contracts by using the command below.

**Warning**: the deploy script needs two arguments. 
<quorum> is the minimum of voters (**in percent**) 
required in order to accept a proposal in the voting system. 
<tlIdentifier> is the territory code (eg. EU) 
of the first member of the consortium.

```sh
$ ./scripts/deployment/deploy.sh <quorum> <tlIdentifier>
```

For instance, for development purpose you can use the following :

```sh
$ ./scripts/deployment/deploy.sh 20 EU
```

**FYI**: 3 smart-contracts are deployed, 
1 for the User Management, 
1 for the Tsl Store and 
1 for the Rules Properties.

Then, copy the properties/contract.properties file that have just been generated, 
you will need it later.

## Set up another environment

Copy the previously saved **genesis.json** 
(see Section [Connect to the node](#connect-to-the-node)
into the folder **docker/ethereum/src/**.

Then, follow the instructions 
in Section [Set up the environment](#set-up-the-environment).

## Connect to another node

Connect to the docker container with the following command.
```sh
$ docker exec -it ethereum-node sh
```

Then, connect to the Ethereum console with the following command.
```sh
$ geth attach
```

Check your connectivity.
```sh
$ net.listening 
true
```
Add the **enode** value of **all other nodes** you want to connect with.
```sh
$ admin.addPeer("enode://f4642fa65af50cfdea8fa7414a5def7bb7991478b768e296f5e4a54e8b995de102e0ceae2e826f293c481b5325f89be6d207b003382e18a8ecba66fbaf6416c0@33.4.2.1:30303")
```

Check the peer has been added.
```sh
$ admin.peers
```

The node should appear (it may take a few seconds to connect).

If it does not appear check the following problems https://github.com/ethereum/go-ethereum/wiki/Connecting-to-the-network#common-problems-with-connectivity.

Copy the **enode** value of your node on your computer, for potential future nodes.

```sh
$ admin.nodeInfo
enode://723d96ad2fbc141cad0543e2a022648775effa963af12e05484436cbe8396248044177a9274a936fde08e873e81b729920319272791764c2b7548b0d9c5ad230@[::]:30303
```
In the **enode** value, replace **@[::]:30303** with the actual combination of 
**@ip:port** of your machine (e.g. **@10.0.0.0:30303**).

**Troubleshooting**: if you want to ensure, at any time, that
your peers are well synchronized, check the last block number by using
the following command in the Ethereum console.
```sh
$ eth.blockNumber
46355
```
All your nodes MUST have the same value. If one node is not sync, 
restart the container to allow the peer to retry to sync.

**Troubleshooting**: you can check if your node is syncing, by using
the following command in the Ethereum console.
```sh
$ web3.eth.syncing
false
```
If the value is *false*, it means that your node is up-to-date, 
otherwise you should see values like currentBlock, highestBlock, etc.

## Properties configuration

Properties files has been generated in the folder properties 
by the _setup_ script.
You have to configure yourself the following files: 
lotl.properties, mail.properties, signature.properties.
See Section [Signature configuration](#signature-configuration).

## Properties configuration

Properties files have been generated in the folder **properties** by the _setup_ script.

You have to configure yourself the following files: 
**lotl.properties**, **mail.properties**, **signature.properties**, 
see Section [Signature configuration](#signature-configuration)..

In this folder, replace the **contract.properties** file by the one you have previously saved.

## Build the application

You have to build the application by using the following command.

```sh
$ ./scripts/app/build.sh
```

The application is now built.

## Start Nexu (eSignature solution)

You have to use Nexu as an interface to be able to sign the Trusted Lists.

```sh
$ ./scripts/app/nexu.sh
```

Nexu should be running.

## Start the application

If you want to start the **Administration** application, 
follow the instructions below for **gtsl-admin**.

If you want to start the **Browser** application, 
follow the instructions below for **gtsl-web**.

### gtsl-admin

```sh
$ ./scripts/app/start.sh admin
```

The Spring Boot application should be running 
on [http://localhost:8181/](http://localhost:8181/).

If the app asks you to configure the rules properties, 
see Section [Rules configuration](#rules-configuration).

To be able to sign a Trusted List, see Section [Signature configuration](#signature-configuration).

### gtsl-web

```sh
$ ./scripts/app/start.sh web
```

The Spring Boot application should be running 
on [http://localhost:8091/](http://localhost:8091/).

If the app asks you to configure the rules properties, 
see Section [Rules configuration](#rules-configuration).

## Rules configuration

When you opened the application, 
you should see a message indicating "Rules properties not found", 
so you have to import these rules in the **admin application**.

**On the Admin app, go to Properties, 
then click Import properties and browse 
the file _rules.json_ in the folder _rules_ of the gtsl project.**

You are done.

## Signature configuration

To be able to sign a Trusted List, you first have to make sure that 
you have configured the properties files: 
**signature.properties** and **lotl.properties**.

### signature.properties:

- digestAlgorithm: the digest algorithm to be used in the signing process (recommended: SHA-256)
- nexuPort: port in use by the Nexu API
- nexuScheme: protocol scheme used by Nexu (http or https)

### lotl.properties:

- keystorePath: absolute path to the keystore to be used as the List of the Lists keystore (make sure that you added your signing certificate in this keystore)
- keystorePassword: password of the keystore
- xmlUrl: URL pointing to the List of the Lists (should be https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml)
- cacheValidityWindow: validity window for the LoTL cache, after which the cache must be renewed
- fileCachePath (optional) : local file used as a cache for the LoTL

**Note**: Your certificate must contain the following extensions: SKI extension, Non repudiation.

# Sources

## Ethereum

https://github.com/ethereum/go-ethereum/wiki/Private-network

https://github.com/ethereum/go-ethereum/wiki/Connecting-to-the-network

http://www.ethdocs.org/en/latest/network/test-networks.html#id3

## IPFS

https://medium.com/@s_van_laar/deploy-a-private-ipfs-network-on-ubuntu-in-5-steps-5aad95f7261b

https://github.com/ipfs/go-ipfs/blob/master/docs/experimental-features.md#private-networks
