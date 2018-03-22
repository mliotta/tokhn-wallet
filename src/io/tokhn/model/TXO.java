package io.tokhn.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TXO {
	private final StringProperty address;
	private final LongProperty amount;
	private final StringProperty script;
	
	public TXO(String address, long amount, String script) {
		this.address = new SimpleStringProperty(address);
		this.amount = new SimpleLongProperty(amount);
		this.script = new SimpleStringProperty(script);
	}

	public StringProperty getAddress() {
		return address;
	}

	public LongProperty getAmount() {
		return amount;
	}

	public StringProperty getScript() {
		return script;
	}
}