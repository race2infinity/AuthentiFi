![Rockets](https://imgur.com/1xXwOoZ.png) 
[![Hackathon](https://img.shields.io/badge/hackathon-SPIT-orange.svg)](http://csi.spit.ac.in/hackathon/) [![Status](https://img.shields.io/badge/status-active-green.svg)]() [![Github Issues](http://githubbadges.herokuapp.com/kylelobo/AuthentiFi/issues.svg?style=flat-square)](https://github.com/kylelobo/AuthentiFi/issues) [![Pending Pull-Requests](http://githubbadges.herokuapp.com/kylelobo/AuthentiFi/pulls.svg?style=flat-square)](https://github.com/kylelobo/AuthentiFi/pulls) [![License](https://img.shields.io/badge/license-GNU-blue.svg)](LICENSE.md)
---

A blockchain-based Product Ownership Management System for Anti-Counterfeits in the Post Supply Chain.

# Table of Content
+ [About](#description)
+ [Getting Started](#getting_started)
+ [Deployment](#deployment)
+ [Limitations](#limitations)
+ [Future Scope](#future_scope)
+ [Contributing](#contributing)
+ [Authors](#authors)

## About<a name="description"></a>
+ In today’s world, how do you know if you are buying a genuine product?
+ For more than a decade now, RFID (Radio Frequency IDentification) technology has been quite effective in providing anti-counterfeits measures in the supply chain.
+ We leverage the idea of Bitcoin’s blockchain that anyone can check the proof of possession of balance. Along with this, we plan to use QR codes.
+ We plan to implement a proof-of-concept system employing a blockchain-based decentralized application which gives a customer the entire history of a product (eg- brand info, owner, etc).

### Why blockchain?<a name="why_blockchain"></a>
+ Unlike a normal database, Blockchain has a non-destructive (immutable) way to track data changes over time. This means that data is not editable rather, whenever updates are made, a new block is added to the “block-chain”. This helps track historical data (authenticity and owner data) of a product.
+ Given the amount of data to be dealt with (large amount of products being developed), if you have to keep track of all of them, it is better to have a decentralized and distributed network of nodes so that no entity can tamper with the product data and we also obtain 100% up time.
+ Transparent nature of the Blockchain helps avoid [parallel trade](https://en.wikipedia.org/wiki/Parallel_import).
+ Using Blockchain, authenticity can be checked and ownership of a product can be transferred _decades_ from now; even if the product is discontinued.

## Getting Started<a name="getting_started"></a>

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them
- Installing NodeJs
```
$ sudo apt-get install nodejs
```
- Installing [Android Studio](https://developer.android.com/studio/)


### Installing

A step by step series of examples that tell you how to get a development env running

- Cloning the repo
```
$ git clone https://github.com/kylelobo/AuthentiFi.git
```
- Installing the dependencies
```
$ cd 
$ npm install
```
- Running the server
```
$ node server.js
```
End with an example of getting some data out of the system or using it for a little demo

## Deployment<a name="deployment"></a>
- Cloning the XDC repo
```
$ git clone https://github.com/XinFinorg/XDC01-docker-Nnodes.git    
```
- Install docker & docker-compose
```
$ sudo ./install_docker.sh
```
- Pull image from Docker Hub
```
$ sudo docker pull xinfinorg/quorum:v2.1.0
```
- Launch the setup script
```
$ cd static-nodes 
$ sudo ./setup.sh
```
- Enter number of nodes, private IP of host machine & unique docker subnet
```
$ sudo docker-compose -p <PROJECT_NAME_STATIC_NODE> up -d
```
- Accessing console
```
$ sudo docker exec -it PROJECT_NAME_STATIC_NODES_node_1_1 geth attach /qdata/dd/geth.ipc
```

## Built With<a name="built_with"></a>
Mobile App:
+ [Android Studio](https://developer.android.com/studio/) - The UI of the Android app
+ [NodeJs](https://nodejs.org/en/) - Server Environment
+ [MySQL](https://dev.mysql.com/downloads/os-linux.html) - Database

Blockchain:
+ [XDC Blockchain](https://www.xinfin.org/) - Blockchain Network
+ [Solidity](https://github.com/ethereum/solidity) - Smart Contracts
+ [Ganache](https://truffleframework.com/ganache) - Create private Ethereum blockchain to run tests

Website:
+ HTML - Markup language for creating web pages
+ CSS - Style Sheet Language
+ JavaScript - Scripting Language for web pages

## Limitations<a name="limitations"></a>
+ The user needs to have a QR code scanner in order to check the product information.
+ Products that have already been manufactured prior to today cannot be tracked.
+ We currently depend on the company to register with our services, without which, we cannot provide information about a brand to the user.

## Future Scope<a name="future_scope"></a>
+ To track every genuine product that is to be sold.
+ Implement this idea in other fields.
+ Implement our own tokens which can be sold to users so that they can purchase ownership of a product using tokens which helps in insurance processing. 

## Contributing<a name="contributing"></a>

1. Fork it (<https://github.com/kylelobo/AuthentiFi/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

## Authors<a name="authors"></a>

+ [Calden Rodrigues](https://github.com/caldenrodrigues) <br>
+ [JohnAnand Abraham](https://github.com/johnanand) <br>
+ [Kyle Lobo](https://github.com/kylelobo) <br>
+ [Pratik Nerurkar](https://github.com/PlayPratz) <br>

See also the list of [contributors](https://github.com/kylelobo/AuthentiFi/contributors) who participated in this project.
