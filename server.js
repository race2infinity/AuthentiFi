// TODO(?): CHECK IF USER HAS THE PRODUCT IN STEP 1 OF SELL AND NOT IN LAST STEP

const express = require('express');
const app = express();
const mysql = require('mysql');
const session = require('express-session');
const bcrypt = require('bcrypt');
const bodyParser = require('body-parser');
const crypto = require('crypto');
const Web3 = require('web3');
const BigNumber = require('bignumber.js');
// const qr = require('qr-image');

// Secret ID for session
const secret_id = process.env.secret;

// Salt for hashing
const saltRounds = 10;

// IP and port
const IP = "localhost";
const port = process.env.PORT || 8080;

// // View engine
// app.set('views', path.join(__dirname, 'views'));
// app.set('view engine', 'ejs');

// Body-parser Middleware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

// Express session Middleware
app.use(session({
    secret: secret_id,
    saveUninitialized: true,
    resave: true
}));

// MySQL Connection
// var connection = mysql.createConnection({
//     host: IP,
//     user: process.env.database_user,
//     password: process.env.database_password,
//     database: 'authentifi'
// });
//
// connection.connect(function(err){
//     if(!err) {
//         console.log("Connected!");
//     } else {
//         console.log("Not connected.");
//     }
// });

const url = "http://" + IP + port.toString();

const web3 = new Web3(new Web3.providers.HttpProvider(url));
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
			}
		],
		"payable": false,
		"stateMutability": "view",
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
				"name": "_hashedEmail",
				"type": "string"
			},
			{
				"name": "_name",
				"type": "string"
			},
			{
				"name": "_phone",
				"type": "string"
			}
		],
		"name": "createCustomer",
		"outputs": [
			{
				"name": "",
				"type": "bool"
			}
		],
		"payable": true,
		"stateMutability": "payable",
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
		"name": "getretailerDetails",
		"outputs": [
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
		"constant": false,
		"inputs": [
			{
				"name": "_code",
				"type": "string"
			},
			{
				"name": "_customer",
				"type": "string"
			}
		],
		"name": "reportStolen",
		"outputs": [
			{
				"name": "",
				"type": "bool"
			}
		],
		"payable": true,
		"stateMutability": "payable",
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
	},
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
				"name": "_hashedEmail",
				"type": "string"
			},
			{
				"name": "_retailerName",
				"type": "string"
			},
			{
				"name": "_retailerLocation",
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
				"name": "_retailer",
				"type": "string"
			},
			{
				"name": "_customer",
				"type": "string"
			}
		],
		"name": "initialOwner",
		"outputs": [
			{
				"name": "",
				"type": "bool"
			}
		],
		"payable": true,
		"stateMutability": "payable",
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
			}
		],
		"payable": false,
		"stateMutability": "view",
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
				"name": "_oldCustomer",
				"type": "string"
			},
			{
				"name": "_newCustomer",
				"type": "string"
			}
		],
		"name": "changeOwner",
		"outputs": [
			{
				"name": "",
				"type": "bool"
			}
		],
		"payable": true,
		"stateMutability": "payable",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [
			{
				"name": "_customer",
				"type": "string"
			}
		],
		"name": "getCodes",
		"outputs": [
			{
				"name": "",
				"type": "string[]"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	}
];

const address = "";

var contract = web3.eth.contract(abiArray);

var contractInstance = contract.at(address);
web3.eth.defaultAccount = web3.eth.coinbase;

const QRCodes = [];

// This function generates a QR code
async function generateQRCode() {
    let QRCode = crypto.randomBytes(20).toString('hex');
}

// Hash function
function hash(email) {
    return bcrypt.hashSync(email, saltRounds);
}

// Verify hash function
function compareHash(email, hashedEmail) {
    return bcrypt.compareSync(email, hashedEmail);
}

/**
 * This function lists all the assets owned by the user
 * POST /myAssets
 * Send: JSON object which contains email
 * Receive: JSON array of objects which contain product brand, model, description, status, manufacturerName,
 * manufacturerLocation, manufacturerTimestamp, retailerName, retailerLocation, retailerTimestamp
 */
app.post('/myAssets', (req, res) => {
    var myAssetsArray = [];
    let email = req.body.email;
    let hashedEmail = hash(email);
    var arrayOfCodes = [];
    arrayOfCodes = contractInstance.getCodes(hashedEmail);
    for (code in arrayOfCodes) {
        ownedCodeDetails = contractInstance.getOwnedCodeDetails(code);
        notOwnedCodeDetails = contractInstance.getNotOwnedCodeDetails(code);
        myAssetsArray.push({brand: notOwnedCodeDetails[0], model: notOwnedCodeDetails[1], description: notOwnedCodeDetails[2],
                            status: notOwnedCodeDetails[3], manufacturerName: notOwnedCodeDetails[4], 
			    manufacturerLocation: notOwnedCodeDetails[5], manufacturerTimestamp: notOwnedCodeDetails[6], 
			    retailerName: ownedCodeDetails[0], retailerLocation: ownedCodeDetails[1],
			    retailerTimestamp: ownedCodeDetails[2]});
    }
    res.status(200).send(JSON.parse(JSON.stringify(myAssetsArray)));
});

/**
 * This function lists all the assets owned by the user
 * PUT /stolen
 * Send: JSON object which contains code, email
 * Receive: Status code 200 if product status was changed, and 400 otherwise.
 */
app.put('/stolen', (req, res) => {
    let code = req.body.code;
    let email = req.body.email;
    let hashedEmail = hash(email);
    let ok = contractInstance.reportStolen(code, hashedEmail);
    if (!ok) {
        return res.status(400).send('ERROR! Product status could not be changed.');
    }
    res.status(200).send('Product status successfully changed!');
});

// TODO: Convert to function
app.post('/createRetailer', (req, res) => {
    var retailerName = req.body.retailerName;
    var retailerEmail = req.body.retailerEmail;
    var retailerLocation = req.body.retailerLocation;
    var hashedEmailRetailer = hash(retailerEmail);
    console.log(hashedEmailRetailer);
    let ok = contractInstance.createRetailer(retailerEmail, retailerName, retailerLocation, 
					     {from: web3.eth.accounts[0], gas:3000000});
    if (!ok) {
        return res.status(400).send('ERROR! Could not add retailer.');
    }
    res.status(200).send('Retailer successfully added!');
});

// TODO: Convert to function
app.post('/createCustomer', (req, res) => {
    let email = req.body.email;
    let name = req.body.name;
    let phone = req.body.phone;
    hashedEmail = hash(email);
    console.log(hashedEmail);
    let ok = contractInstance.createCustomer(hashedEmail, name, phone, {from: web3.eth.accounts[0], gas:3000000});
    if (!ok) {
        return res.status(400).send('ERROR! Could not add customer.');
    }
    res.status(200).send('Customer successfully added!');
});

app.post('/sell', (req, res) => {
    let code = req.body.code;
    let sellerEmail = req.body.sellerEmail;
    hashedSellerEmail = hash(sellerEmail);
    let QRCode = generateQRCode();
    /**
     * TODO:
     * Create session that stays alive for 30 secs
     * Use events
     */
    res.status(200).send(QRCode);
});

app.post('/buy', (req, res) => {
    let QRCode = req.body.QRCode;
    let email = req.body.buyerEmail;
    hashedBuyerEmail = hash(buyerEmail);
    /**
     * TODO:
     * If buyer scans the code within 30 secs, then return 200
     * Figure out how to route the buyer if timer fails
     */
    if (ok) {

    }
});

/**
 * This function gives product details if the scannee is not the owner of the product
 * POST /scan
 * Send: JSON object which contains code
 * Receive: JSON object which has productDetails
 */
app.post('/scan', (req, res) => {
    console.log(req.body);
    let code = req.body.code;
    let productDetails = contractInstance.getNotOwnedCodeDetails(code);
    res.send(productDetails);
});

/**
 * This function generates QR codes for the manufacturers
 * POST /QRCodeForManufacturer
 * Send: JSON object which contains brand, model, status, description, manufacturerName, manufacturerLocation
 * Receive: Status code 200 if QR code was generated, and 400 otherwise.
 */
app.post('/QRCodeForManufacturer', (req, res) => {
    let brand = req.body.brand;
    let model = req.body.model;
    let status = 0;
    let description = req.body.description;
    let manufacturerName = req.body.manufacturerName;
    let manufacturerLocation = req.body.manufacturerLocation;
    var manufacturerTimestamp = new Date();
    manufacturerTimestamp = manufacturerTimestamp.toISOString().slice(0, 10);
    let salt = crypto.randomBytes(20).toString('hex');
    let code = hash(brand + model + status + description + manufacturerName + manufacturerLocation + salt);
    let ok = contractInstance.createCode(code, brand, model, status, description, manufacturerName, manufacturerLocation,
					 manufacturerTimestamp, {from: web3.eth.accounts[0], gas:3000000});
    console.log(code);
    if (!ok) {
        return res.status(400).send('ERROR! QR Code for manufacturer could not be generated.');
    }
    res.status(200).send('QR Code for manufacturer generated! ' + code);
});

/**
 * This function gives all the customer details
 * GET /getCustomerDetails
 * Send: JSON object which contains email
 * Receive: Name, phone
 */
app.get('/getCustomerDetails', (req, res) => {
    let email = req.body.email;
    let hashedEmail = hash(email);
    customerDetails = contractInstance.getCustomerDetails(hashedEmail);
    res.status(200).send(customerDetails);
});

// Server start
app.listen(port, (req, res) => {
    console.log(`Listening to port ${port}...`);
});

// Function which generates QR image (takes QR code as input)
// Uncomment require('qr-image') to use
// app.get('/qr', (req, res) => {
//     var code = qr.image('http://www.google.com', { type: 'png' });
//     res.setHeader('Content-type', 'image/png');  //sent qr image to client side
//     code.pipe(res);
// });
