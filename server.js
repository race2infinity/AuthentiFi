/**
 * TODO:
 */

const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const Web3 = require('web3');
const BigNumber = require('bignumber.js');
//const Tx = require('ethereumjs-tx');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

// function connectToSolidity() {
const web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:7545"));
console.log("Talking with a geth server", web3.version.api);

const abiArray = [
	{
		"constant": false,
		"inputs": [
			{
				"name": "_code",
				"type": "string"
			},
			{
				"name": "_hashedEmailRetailer",
				"type": "string"
			}
		],
		"name": "addRetailerToCode",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"payable": true,
		"stateMutability": "payable",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"name": "_code",
				"type": "string"
			},
			{
				"name": "_brand",
				"type": "string"
			},
			{
				"name": "_model",
				"type": "string"
			},
			{
				"name": "_status",
				"type": "uint256"
			},
			{
				"name": "_description",
				"type": "string"
			},
			{
				"name": "_manufactuerName",
				"type": "string"
			},
			{
				"name": "_manufactuerLocation",
				"type": "string"
			},
			{
				"name": "_manufactuerTimestamp",
				"type": "string"
			}
		],
		"name": "createCode",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"payable": true,
		"stateMutability": "payable",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"name": "_hashedEmailCustomer",
				"type": "string"
			},
			{
				"name": "_name",
				"type": "string"
			},
			{
				"name": "_phone",
				"type": "string"
			},
			{
				"name": "_timestamp",
				"type": "string"
			}
		],
		"name": "createCustomer",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"payable": true,
		"stateMutability": "payable",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [],
		"name": "createOwner",
		"outputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"name": "_hashedEmailRetailer",
				"type": "string"
			},
			{
				"name": "_retailerName",
				"type": "string"
			},
			{
				"name": "_retailerLocation",
				"type": "string"
			},
			{
				"name": "_retailerTimestamp",
				"type": "string"
			}
		],
		"name": "createRetailer",
		"outputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"payable": true,
		"stateMutability": "payable",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"name": "_code",
				"type": "string"
			},
			{
				"name": "",
				"type": "string"
			}
		],
		"name": "initialOwner",
		"outputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [
			{
				"name": "_code",
				"type": "string"
			}
		],
		"name": "getCustomerDetails",
		"outputs": [
			{
				"name": "",
				"type": "string"
			},
			{
				"name": "",
				"type": "string"
			},
			{
				"name": "",
				"type": "string"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [
			{
				"name": "_code",
				"type": "string"
			}
		],
		"name": "getNotOwnedCodeDetails",
		"outputs": [
			{
				"name": "",
				"type": "string"
			},
			{
				"name": "",
				"type": "string"
			},
			{
				"name": "",
				"type": "string"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [
			{
				"name": "_code",
				"type": "string"
			}
		],
		"name": "getOwnedCodeDetails",
		"outputs": [
			{
				"name": "",
				"type": "string"
			},
			{
				"name": "",
				"type": "string"
			},
			{
				"name": "",
				"type": "uint256"
			},
			{
				"name": "",
				"type": "string"
			},
			{
				"name": "",
				"type": "string"
			},
			{
				"name": "",
				"type": "string"
			},
			{
				"name": "",
				"type": "string"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [
			{
				"name": "_code",
				"type": "string"
			}
		],
		"name": "getRetailerDetails",
		"outputs": [
			{
				"name": "",
				"type": "string"
			},
			{
				"name": "",
				"type": "string"
			},
			{
				"name": "",
				"type": "string"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [],
		"name": "whoIsOwner",
		"outputs": [
			{
				"name": "",
				"type": "address"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	}
];

const address = "0x92f05ffb5e894ad6a642830d509bb5c8d53cfa89";

var contract = web3.eth.contract(abiArray);

var contractInstance = contract.at(address);
web3.eth.defaultAccount = web3.eth.coinbase;

// console.log(contractInstance.createCode("11", "Nike", "Jordan", 0, "It's costly", "Pratibha Co.", "Mumbai", "11:00", {from: web3.eth.accounts[0], gas:3000000}));
// console.log(contractInstance.getNotOwnedCodeDetails("12"));

    // setTimeout(getsResponse,10000);
    // return res.send("It works");
// }

// connectToSolidity();

// Returns current owner
app.get('/whoIcodesOwner', (req, res) => {
    currentOwner = contractInstance.whoIsOwner();
    if (currentOwner === "0x0000000000000000000000000000000000000000") {
        res.status(404).send("No one is the owner");
        return;
    }
    res.send(contractInstance.whoIsOwner());
});

// Returns details of the customer
app.get('/getCustomerDetails/:id', (req, res) => {
    customerDetails = contractInstance.getCustomerDetails(req.params.id);
    if (customerDetails[0].length === 0) {
        res.status(404).send('No customer details!');
        return 0;
    }
    res.send(contractInstance.getCustomerDetails(req.params.id));
});

// Returns limited product details if the scannee is not the owner.
app.get('/getNotOwnedCodeDetails/:code', (req, res) => {
    notOwnedCodeDetails = contractInstance.getNotOwnedCodeDetails(req.params.code);
    if (notOwnedCodeDetails[0].length === 0) {
        res.status(404).send('Product does not exist!');
        return;
    }
    res.send(contractInstance.getNotOwnedCodeDetails(req.params.code));
});

// Returns all product details if the scannee is the owner.
app.get('/getOwnedCodeDetails/:code', (req, res) => {
    ownedCodeDetails = contractInstance.getOwnedCodeDetails(req.params.code);
    if (ownedCodeDetails[0].length === 0) {
        res.status(404).send('Product does not exist!');
        return;
    }
    res.send(contractInstance.getOwnedCodeDetails(req.params.code));
});

// Returns the details of the retailer
app.get('/getRetailerDetails/:id', (req, res) => {
    retailerDetails = contractInstance.getRetailerDetails(req.params.code);
    if (retailerDetails[0].length === 0) {
        res.status(404).send('Product does not exist!');
        return;
    }
    res.send(contractInstance.getRetailerDetails(req.params.id));
});

// Create new owner
app.post('/createOwner', (req, res) => {
    res.send(createOwner(req.params.id)); // createOwner() function has no ID yet. So, ID??
});

// Create new retailer
// Receiving a JSON objectD
app.post('/createRetailer', (req, res) => {
    res.send(createRetailer(req.params.body.hashedEmailRetailer, req.params.body.retailerName,
                            req.params.body.timestamp, {from: web3.eth.accounts[0], gas:3000000}));
});

// Add retailer to code
// Receiving a JSON object
app.post('/addRetailerToCode', (req, res) => {
    res.send(contractInstance.createRetailer(req.params.body.name, req.params.body.hashedEmailRetailer,
                                            {from: web3.eth.accounts[0], gas:3000000}));
});

// Create new code for a product
// JSON format:
app.post('/createCode', (req, res) => {
    res.send(contractInstance.createCode(req.params.body.code, req.params.body.brand, req.params.body.product,
                                        parseInt(req.params.body.status), req.params.body.description, req.params.body.manufacturer, req.params.body.location, req.params.body.timestamp,
                                        {from: web3.eth.accounts[0], gas:3000000}));
});

// Create new customer
// JSON format:
app.post('/createCustomer', (req, res) => {
    res.send(contractInstance.createCustomer(req.params.body.hashedEmailCustomer, req.params.body.name,
                                            req.params.body.phone, req.params.body.timestamp,
                                            {from: web3.eth.accounts[0], gas:3000000}));
});

const port = process.env.PORT || 8080

app.listen(8080, (req, res) => {
    console.log(`Listening to port ${port}...`);
});
