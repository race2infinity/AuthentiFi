package com.pratz.authentifi.Assets;

public class Asset {

	String productCode, productBrand, productModel, productDescription, manufacturerName, manufacturerLocation, manufacturerTimestamp;
	int status;


	public Asset(String productCode, String productBrand, String productModel) {
		this.productCode = productCode;
		this.productBrand = productBrand;
		this.productModel = productModel;
	}

	public Asset(String productCode, String productBrand, String productModel, String productDescription,
	             String manufacturerName, String manufacturerLocation, String manufacturerTimestamp, int status) {
		this.productCode = productCode;
		this.productBrand = productBrand;
		this.productModel = productModel;
		this.productDescription = productDescription;
		this.manufacturerName = manufacturerName;
		this.manufacturerLocation = manufacturerLocation;
		this.manufacturerTimestamp = manufacturerTimestamp;
		this.status = status;
	}

	public String getProductCode() {
		return productCode;
	}

	public String getProductBrand() {
		return productBrand;
	}

	public String getProductModel() {
		return productModel;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public String getManufacturerLocation() {
		return manufacturerLocation;
	}

	public String getManufacturerTimestamp() {
		return manufacturerTimestamp;
	}

	public int getStatus() {
		return status;
	}
}
