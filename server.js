const express = require('express');
const app = express();
const mysql = require('mysql');
const session = require('express-session');
const bcrypt = require('bcrypt');
const bodyParser = require('body-parser');
const crypto = require('crypto');
const Web3 = require('web3');
const BigNumber = require('bignumber.js');
const fs = require('fs');

// Secret ID for session
const secret_id = process.env.secret;

// Salt for hashing
const saltRounds = 10;

// IP and port
const IP = "localhost";
const port = process.env.PORT || 8080;

// // View engine
//app.set('views', path.join(__dirname, 'views'));
//app.set('view engine', 'ejs');

// Body-parser Middleware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

// // Express session Middleware
// app.use(session({
//     secret: secret_id,
//     saveUninitialized: true,
//     resave: true
// }));

// MySQL Connection
const connection = mysql.createConnection({
    host: IP,
    user: process.env.database_user,
    password: process.env.database_password,
    database: 'authentifi'
});

connection.connect(function(err) {
    if (!err) {
        console.log('Connected!');
    } else {
        console.log('Not connected.');
    }
});

const web3 = new Web3(new Web3.providers.HttpProvider('http://localhost:7545'));
console.log('Talking with a geth server', web3.version.api);

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

const address = '';

const contract = web3.eth.contract(abiArray);

const contractInstance = contract.at(address);
web3.eth.defaultAccount = web3.eth.coinbase;

const QRCodes = [];

// This function generates a QR code
function generateQRCode() {
    let QRCode = crypto.randomBytes(20).toString('hex');
    return QRCode;
}

// Hash password using bcrypt
function hashPassword(password) {
    return bcrypt.hashSync(email, saltRounds);
}

// Hash email using md5
function hashEmail(email) {
    crypto.createHash('md5');
    return crypto.createHash('md5').update(email).digest('hex');
}

// Routes for webpages
app.use(express.static(__dirname + '/views'));
app.use(express.static(__dirname + '/views/davidshimjs-qrcodejs-04f46c6'));

// Code generated for the manufacturer
app.get("/createCodes", (req, res) => {
    res.sendFile('views/createCodes.html', { root: __dirname });
});

// Creating a new retailer
app.get("/createRetailer", (req, res) => {
    res.sendFile('views/createRetailer.html', { root: __dirname });
});


/**
 * Description: Adds a user to the database and to the blockchain
 * Request:     POST /signUp
 * Send:        JSON object which contains name, email, password, phone
 * Receive:     200 if successful, 400 otherwise
 */
app.post("/signUp", (req, res) => {
    let name = req.body.name;
    let email = req.body.email;
    let password = req.body.password;
    let phone = req.body.phone;
    let hashedPassword = hashPassword(password);
    // Adding the user in MySQL
    connection.query('SELECT * FROM USER WHERE Email = ? LIMIT 1', [email], (error, results) => {
        if (error) {
            callback(error);
            return res.status(400);
        }
        if (results.length) {
            return res.status(400).send('Email already exists!');
        } else {
            connection.query('INSERT INTO USER VALUES (?,?,?,?)', [name, email, hashedPassword, phone], (error, results) => {
                if (error) {
                    callback(error);
                    return res.status(400);
                }
                res.status(200).send('Signup successful!');
                // Adding user to the Blockchain
                let ok = createCustomer(email, name, phone);
                if (ok) {
                    console.log('User successfully added to Blockchain!');
                } else {
                    console.log('ERROR! User could not be added to Blockchain.');
                }
            });
        }
    });
});

// Add the user in Blockchain
function createCustomer(email, name, phone) {
    hashedEmail = hashEmail(email);
    console.log('The user created is:', hashedEmail);
    let ok = contractInstance.createCustomer(hashedEmail, name, phone, { from: web3.eth.accounts[0], gas: 3000000 });
    return ok;
};


/**
 * Description: Login the user to the app
 * Request:     POST /login
 * Send:        JSON object which contains email, password
 * Receive:     200 if successful, 400 otherwise
 */
app.post("/login", (req, res) => {
    let email = req.body.email;
    let password = req.body.password;
    connection.query('SELECT * FROM USER WHERE Email = ? LIMIT 1', [email], (error, results) => {
        if (error) {
            callback(error);
            return res.status(400);
        }
        if (results.length) {
            connection.query('SELECT Password FROM USER WHERE Email = (?)', [email], (error, results) => {
                if (error) {
                    callback(error);
                    return res.status(400);
                }
                let pass = results[0].Password;
                if (bcrypt.compareSync(password, pass))
                    return res.status(200).send('Login successful!');
                else
                    return res.status(400).send('Login failed.');
            });
        } else {
            return res.status(400).send('Email does not exist!');
        }
    });
});


/**
 * Description: Adds a retailer to the database and to the blockchain
 * Request:     POST /retailerSignUp
 * Send:        JSON object which contains name, email, password, location
 * Receive:     200 if successful, 400 otherwise
 */
app.post('/retailerSignup', (req, res) => {
    let retailerEmail = req.body.email;
    let retailerName = req.body.name;
    let retailerLocation = req.body.location;
    let retailerPassword = req.body.password;
    let retailerHashedPassword = hashPassword(retailerPassword);
    let retailerHashedEmail = hashEmail(retailerEmail);
    // Adding the retailer in MySQL
    connection.query('SELECT * FROM RETAILER WHERE Email = ? LIMIT 1', [retailerEmail], (error, results) => {
        if (error) {
            callback(error);
            return res.status(400);
        }
        if (results.length) {
            return res.status(400).send('Email already exists!');
        } else {
            connection.query('INSERT INTO RETAILER VALUES (?,?,?,?)', [retailerName, retailerEmail, retailerLocation, retailerHashedPassword],
                (error, results) => {
                    if (error) {
                        callback(error);
                        return res.status(400);
                    }
                    res.status(200).send('Signup successful!');
                    // Adding retailer to Blockchain
                    let ok = createRetailer(retailerHashedEmail, retailerName, retailerLocation);
                    if (ok) {
                        console.log('Retailer successfully added to Blockchain!');
                    } else {
                        console.log('ERROR! Retailer could not be added to Blockchain.');
                    }
                });
        }
    });
});

// Add retailer to Blockchain
function createRetailer(retailerHashedEmail, retailerName, retailerLocation) {
    let ok = contractInstance.createRetailer(retailerHashedEmail, retailerName, retailerLocation,
        { from: web3.eth.accounts[0], gas: 3000000 });
    return ok;
}


/**
 * Description: Login the retailer to the app
 * Request:     POST /retailerLogin
 * Send:        JSON object which contains email, password
 * Receive:     200 if successful, 400 otherwise
 */
app.get("/retailerLogin", (req, res) => {
    let retailerEmail = req.body.email;
    let retailerPassword = req.body.password;
    connection.query('SELECT Password FROM USER WHERE Email = (?)', [retailerEmail], (error, results) => {
        if (error) {
            callback(error);
            return res.status(400);
        }
        let pass = results[0].Password;
        if (bcrypt.compareSync(retailerPassword, pass))
            return res.status(200).send('Retailer login successful!');
        else
            return res.status(400).send('Retailer login failed.');
    })
});


/**
 * Description: Lists all the assets owned by the user
 * Request:     POST /myAssets
 * Send:        JSON object which contains email
 * Receive:     JSON array of objects which contain brand, model, description, status, manufacturerName,manufacturerLocation,
 *                                                  manufacturerTimestamp, retailerName, retailerLocation, retailerTimestamp
 */
app.post('/myAssets', (req, res) => {
    let myAssetsArray = [];
    let email = req.body.email;
    let hashedEmail = hash(email);
    let arrayOfCodes = contractInstance.getCodes(hashedEmail);
    for (code in arrayOfCodes) {
        console.log(arrayOfCodes[code]);
        let ownedCodeDetails = contractInstance.getOwnedCodeDetails(arrayOfCodes[code]);
        let notOwnedCodeDetails = contractInstance.getNotOwnedCodeDetails(arrayOfCodes[code]);
        console.log(notOwnedCodeDetails);
        myAssetsArray.push({
            'code': arrayOfCodes[code], 'brand': notOwnedCodeDetails[0],
            'model': notOwnedCodeDetails[1], 'description': notOwnedCodeDetails[2],
            'status': notOwnedCodeDetails[3], 'manufacturerName': notOwnedCodeDetails[4],
            'manufacturerLocation': notOwnedCodeDetails[5], 'manufacturerTimestamp': notOwnedCodeDetails[6],
            'retailerName': ownedCodeDetails[0], 'retailerLocation': ownedCodeDetails[1],
            'retailerTimestamp': ownedCodeDetails[2]
        });
    }
    console.log(myAssetsArray);
    res.status(200).send(JSON.parse(JSON.stringify(myAssetsArray)));
});


/**
 * Description: Lists all the assets owned by the user
 * Request:     PUT /stolen
 * Send:        JSON object which contains code, email
 * Receive:     200 if product status was changed, 400 otherwise.
 */
app.put('/stolen', (req, res) => {
    let code = req.body.code;
    let email = req.body.email;
    let hashedEmail = hashEmail(email);
    let ok = contractInstance.reportStolen(code, hashedEmail);
    if (!ok) {
        return res.status(400).send('ERROR! Product status could not be changed.');
    }
    res.status(200).send('Product status successfully changed!');
});


/**
 * Description: Sell a product from myAssets (aka your inventory)
 * Request:     POST /sell
 * Send:        JSON object which contains code, sellerEmail
 * Receive:     200 if successful, 400 otherwise
 */
app.post('/sell', (req, res) => {
    let code = req.body.code;
    let sellerEmail = req.body.sellerEmail;
    hashedSellerEmail = hash(sellerEmail);
    let currentTime = Date.now();         // Date.now() gets the current time in milliseconds
    let QRCode = generateQRCode();
    QRCodes.push({ 'QRCode': QRCode, 'currentTime': currentTime });
    /**
     * TODO:
     * Create session that stays alive for 30 secs
     * Use events
     */
    res.status(200).send(JSON.parse(QRCode));
});


/**
 * Description: Buy a product
 * Request:     POST /buy
 * Send:        JSON object which contains QRCode, buyerEmail
 * Receive:     200 if successful, 400 otherwise
 */
app.post('/buy', (req, res) => {
    let QRCode = req.body.QRCode;
    let buyerEmail = req.body.buyerEmail;
    let currentTime = Date.now();         // Date.now() gets the current time in milliseconds
    for (let i = 0; i < QRCodes.length; i++) {
        if (QRCode === QRCodes[i]['QRCode']) {
            let timeElapsed = Math.floor((QRCodes[i]['currentTime'] - currentTime) / 1000);
            // QR Codes are valid only for 30 secs
            if (timeElapsed <= 30) {
                QRCodes.splice(i, 1);
                return res.status(200).send('Validated!');
            }
            return res.status(400).send('Timed out!');
        }
    }
});


/**
 * Description: Get product details
 * Request:     POST /getProductDetails
 * Send:        JSON object which contains code
 * Receive:     brand, model, description, status, manufacturerName, manufacturerLocation, manufacturerTimestamp,
 *              retailerName, retailerLocation, retailerTimestamp
 */
app.post('/getProductDetails', (req, res) => {
    let code = req.body.code;
    let ownedCodeDetails = contractInstance.getOwnedCodeDetails(code);
    let notOwnedCodeDetails = contractInstance.getNotOwnedCodeDetails(code);
    if (!ownedCodeDetails || !notOwnedCodeDetails) {
        return res.status(400).send('Could not retrieve product details.');
    }
    let productDetails = {
        'brand': notOwnedCodeDetails[0], 'model': notOwnedCodeDetails[1], 'description': notOwnedCodeDetails[2],
        'status': notOwnedCodeDetails[3], 'manufacturerName': notOwnedCodeDetails[4],
        'manufacturerLocation': notOwnedCodeDetails[5], 'manufacturerTimestamp': notOwnedCodeDetails[6],
        'retailerName': ownedCodeDetails[0], 'retailerLocation': ownedCodeDetails[1],
        'retailerTimestamp': ownedCodeDetails[2]
    };
    res.status(200).send(productDetails);
});


/**
 * Description: Buyer confirms deal
 * Request:     POST /buyerConfirm
 * Send:        JSON object which contains buyerEmailId
 * Receive:     200 if successful, 400 otherwise
 */
app.post('/buyerConfirm', (req, res) => {
    let buyerEmail = req.body.buyerEmail;
    if (!buyerEmail) {
        return res.status(400);
    }
    res.status(200);
});


/**
 * Description: Seller confirms deal and gets registered as new owner on the Blockchain
 * Request:     POST /sellerConfirm
 * Send:        JSON object which contains sellerEmail
 * Receive:     200 if successful, 400 otherwise
 */
app.post('/sellerConfirm', (req, res) => {
    let confirm = req.body.confirm;
    let code = req.body.code;
    let sellerEmail = req.body.sellerEmail;
    let buyerEmail = req.body.buyerEmail;
    let sellerHashedEmail = hash(sellerEmail);
    let buyerHashedEmail = hash(buyerEmail);
    if (!confirm) {
        return res.status(400);
    }
    let ok = changeOwner(code, sellerHashedEmail, buyerHashedEmail);
    if (!ok) {
        return res.status(400);
    }
    res.status(200);
});

// Function that creates an initial owner for a product
function initialOwner(code, retailerHashedEmail, customerHashedEmail) {
    let ok = contractInstance.initialOwner(code, retailerHashedEmail, customerHashedEmail);
    return ok;
}

// Function that creates transfers ownership of a product
function changeOwner(code, oldOwnerHashedEmail, newOwnerHashedEmail) {
    let ok = contractInstance.changeOwner(code, oldOwnerHashedEmail, newOwnerHashedEmail);
    return ok;
}


/**
 * Description: Gives product details if the scannee is not the owner of the product
 * Request:     POST /scan
 * Send:        JSON object which contains code
 * Receive:     JSON object which has productDetails
 */
app.post('/scan', (req, res) => {
    console.log(req.body);
    let code = req.body.code;
    let productDetails = contractInstance.getNotOwnedCodeDetails(code);
    let productDetailsObj = {
        'name': productDetails[0], 'model': productDetails[1], 'status': productDetails[2],
        'description': productDetails[3], 'manufacturerName': productDetails[4],
        'manufacturerLocation': productDetails[5], 'manufacturerTimestamp': productDetails[6]
    };
    res.status(200).send(JSON.stringify(productDetailsObj));
});


/**
 * Description: Generates QR codes for the manufacturers
 * Request:     POST /QRCodeForManufacturer
 * Send:        JSON object which contains brand, model, status, description, manufacturerName, manufacturerLocation
 * Receive:     200 if QR code was generated, 400 otherwise.
 */
app.post('/QRCodeForManufacturer', (req, res) => {
    let brand = req.body.brand;
    let model = req.body.model;
    let status = 0;
    let description = req.body.description;
    let manufacturerName = req.body.manufacturerName;
    let manufacturerLocation = req.body.manufacturerLocation;
    let manufacturerTimestamp = new Date();
    manufacturerTimestamp = manufacturerTimestamp.toISOString().slice(0, 10);
    let salt = crypto.randomBytes(20).toString('hex');
    let code = hash(brand + model + status + description + manufacturerName + manufacturerLocation + salt);
    let ok = contractInstance.createCode(code, brand, model, status, description, manufacturerName, manufacturerLocation, manufacturerTimestamp, { from: web3.eth.accounts[0], gas: 3000000 });
    console.log('The QR Code generated is:', code);
    if (!ok) {
        return res.status(400).send('ERROR! QR Code for manufacturer could not be generated.');
    }
    fs.writeFile('views/davidshimjs-qrcodejs-04f46c6/code.txt', code, (err, code) => {
        if (err)
            console.log(err);
        console.log('Successfully written to file!');
    });
    res.sendFile('views/davidshimjs-qrcodejs-04f46c6/index.html', { root: __dirname });
});


/**
 * Description: Gives all the customer details
 * Request:     GET /getCustomerDetails
 * Send:        JSON object which contains email
 * Receive:     Name, phone, email
 */
app.get('/getCustomerDetails', (req, res) => {
    let email = req.body.email;
    let hashedEmail = hash(email);
    let customerDetails = contractInstance.getCustomerDetails(hashedEmail);
    let customerDetailsObj = {
        'name': customerDetails[0], 'phone': customerDetails[1], 'email': email
    };
    res.status(200).send(JSON.parse(JSON.stringify(customerDetailsObj)));
});

// Server start
app.listen(port, (req, res) => {
    console.log(`Listening to port ${port}...`);
});
