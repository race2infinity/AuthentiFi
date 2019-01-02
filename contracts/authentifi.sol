pragma solidity ^0.4.22;

pragma experimental ABIEncoderV2;

// Data for testing the functions
// createCode:      "11", "Nike", "Jordan", 0, "It's costly", "Pratibha Co.", "Mumbai", "11:00"
// createCustomer:  "111", "Kyle", "9869245690"
// createRetailer:  "1111", "Lifestyle", "Andheri", "14:00"
//add retailer to code: "11","1111"


//Remove timestamp from createCustomer

contract Authentifi {
    address owner;
    //string temp;

    // A struct which helps create a new code
    // code is a combination of - brand name, model no, status, description,
    // manufacturer details, retailer details, owner details
    //function Authentifi(string _temp)public{
      //   temp=_temp;
     //}

    struct codeObj {
        uint status;
        string brand;
        string model;
        string description;
        string manufactuerName;
        string manufactuerLocation;
        string manufactuerTimestamp;
        string retailer;
        string[] customers;
    }

    // A struct which helps create a new customer
    struct customerObj {
        string name;
        string phone;
        string[] code;
        bool isValue;
    }

    struct retailerObj {
        string name;
        string location;
        string timestamp;
    }

    mapping (string => codeObj) codeArr;
    mapping (string => customerObj) customerArr;
    mapping (string => retailerObj) retailerArr;

    function createOwner() public {
        owner = msg.sender;
    }

    modifier onlyOwner {
        require(msg.sender == owner);
        _;
    }

    function whoIsOwner() public view returns (address) {
        return owner;
    }

    // Function to create a new code for the product
    function createCode (string _code, string _brand, string _model, uint _status, string _description, string _manufactuerName, string _manufactuerLocation, string _manufactuerTimestamp) public onlyOwner payable returns (uint) {
        codeObj newCode;
        newCode.brand = _brand;
        newCode.model = _model;
        newCode.status = _status;
        newCode.description = _description;
        newCode.manufactuerName = _manufactuerName;
        newCode.manufactuerLocation = _manufactuerLocation;
        newCode.manufactuerTimestamp = _manufactuerTimestamp;
        codeArr[_code] = newCode;
        owner = msg.sender; // Hacky fix for changing owner
        return 1;
    }

    // Function for showing product details if the person scanning the product is not the owner
    function getNotOwnedCodeDetails (string _code) public view returns (string, string, uint, string, string, string, string) {
        return (codeArr[_code].brand, codeArr[_code].model, codeArr[_code].status, codeArr[_code].description, codeArr[_code].manufactuerName, codeArr[_code].manufactuerLocation,codeArr[_code].manufactuerTimestamp);
    }

    // Function for showing product details if the person scanning the product is the owner
    function getOwnedCodeDetails (string _code) public view returns (string, string, string) {
        return (retailerArr[codeArr[_code].retailer].name, retailerArr[codeArr[_code].retailer].location,retailerArr[codeArr[_code].retailer].timestamp);
    }


    // Function for creating a new retailer
    function addRetailerToCode (string _code, string _hashedEmailRetailer) public onlyOwner payable returns (uint) {
        codeArr[_code].retailer = _hashedEmailRetailer;
        return 1;
    }

    // Function for creating a new customer
    function createCustomer (string _hashedEmail, string _name, string _phone) public onlyOwner payable returns (bool) {
        //if(customerArr[_hashedEmail].isValue){
        //    return false;
        //}
        customerObj memory newCustomer;
        newCustomer.name = _name;
        newCustomer.phone = _phone;
        newCustomer.isValue=true;
        customerArr[_hashedEmail] = newCustomer;
        return true;
    }

    function getCustomerDetails (string _code) public view returns (string, string) {
        return (customerArr[_code].name, customerArr[_code].phone);
    }

    function createRetailer (string _hashedEmail, string _retailerName, string _retailerLocation, string _retailerTimestamp) public onlyOwner payable returns (uint) {
        retailerObj newRetailer;
        newRetailer.name = _retailerName;
        newRetailer.location = _retailerLocation;
        newRetailer.timestamp = _retailerTimestamp;
        retailerArr[_hashedEmail] = newRetailer;
        owner = msg.sender;
        return 1;
    }


    function getretailerDetails (string _code) public view returns (string, string, string) {
        return (retailerArr[_code].name, retailerArr[_code].location, retailerArr[_code].timestamp);
    }

     //Function to report stolen
    function reportStolen(string _code, string _customer) public payable returns(bool){
        uint i;
        //checking if the customer exists
        if(customerArr[_customer].isValue){
            //checking if the customer owns the product
            for(i=0;i<customerArr[_customer].code.length;i++){
                if(compareStrings(customerArr[_customer].code[i],_code)){
                    codeArr[_code].status=2;        //changing the status to stolen.
                }
            }
        }
    }



    function changeOwner(string _code,string _oldCustomer, string _newCustomer)public payable returns(bool){
        uint i;
        uint index;
        bool flag=false;

        //creating objects for code,oldCustomer,newCustomer
        codeObj product=codeArr[_code];
        uint len_product_customer=product.customers.length;
        customerObj oldCustomer=customerArr[_oldCustomer];
        uint len_oldCustomer_code=oldCustomer.code.length;
        customerObj newCustomer=customerArr[_newCustomer];
        uint len_newCustomer_code=newCustomer.code.length;

        //Check if oldCustomer and newCustomer have an account
        if(oldCustomer.isValue && newCustomer.isValue){
            //Check if oldCustomer is owner
            for(i=0;i<len_oldCustomer_code;i++){
                if(compareStrings(oldCustomer.code[i],_code)){
                    flag=true;
                    break;
                }
            }
            if(flag==true){
                //Swaping oldCustomer with newCustomer in product
                for(i=0;i<len_product_customer;i++){
                    if(compareStrings(product.customers[i],_oldCustomer)){
                        product.customers[i]=_newCustomer;
                        break;
                    }
                }

                //Removing product from oldCustomer
                for(i=0;i<len_oldCustomer_code;i++){
                    if(compareStrings(oldCustomer.code[i],_code)){
                        remove(i,oldCustomer.code);
                    }
                }

                //Adding product to newCustomer
                newCustomer.code.push(_code);

            }
        }
    }




    function initialOwner(string _code,string _retailer, string _customer) public payable returns(bool){
        codeObj product=codeArr[_code];
        if(compareStrings(product.retailer,_retailer)){     //Check if retailer owns the prodct
            if(customerArr[_customer].isValue){             //Check if Customer has an account
                customerObj customer=customerArr[_customer];//Create customer object
                uint len_code=product.customers.length;     //length of customer array in code
                product.customers[len_code]=_customer;      //adding customer in code
                uint len_customer=customer.code.length;     //length of code array in customer
                customer.code[len_customer]=_code;          //adding code in customer
                return true;
            }
        }
        return false;
    }




        //Given a customer returns all the product codes he owwns
    function getCodes(string _customer) public payable returns(string[]){
        uint i;
        uint t=0;
        string[] memory temp=new string[](customerArr[_customer].code.length);
        for(i=0;i<customerArr[_customer].code.length;i++){
            temp[t]=customerArr[_customer].code[i];
            t++;
        }
        return temp;
    }





    // Cannot directly compare strings in Solidity
    // This function hashes the 2 strings and then compares the 2 hashes
    function compareStrings (string a, string b) view returns (bool) {
    	return keccak256(a) == keccak256(b);
    }

    //function to delete an element from an array
    function remove(uint index, string[] storage array) internal returns(bool) {
        if (index >= array.length) return false;

        for (uint i = index; i<array.length-1; i++){
            array[i] = array[i+1];
        }
        delete array[array.length-1];
        array.length--;
        return true;
    }
}
