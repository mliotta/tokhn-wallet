package io.tokhn.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Address {
	private final StringProperty network;
	private final StringProperty address;
	
	public Address(String network, String address) {
		this.network = new SimpleStringProperty(network);
		this.address = new SimpleStringProperty(address);
	}

	public StringProperty getNetwork() {
		return network;
	}

	public StringProperty getAddress() {
		return address;
	}
}