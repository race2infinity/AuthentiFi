package com.pratz.authentifi.User;

public class User {
	String name, phone, email;
	String password;

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}

// getCustomerDetails email -> name, phone
