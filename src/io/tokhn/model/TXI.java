package io.tokhn.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TXI {
	private final StringProperty sourceTxId;
	private final IntegerProperty sourceTxoIndex;
	private final StringProperty script;
	private final ObjectProperty<byte[]> signature;
	
	public TXI(String sourceTxId, int sourceTxoIndex, String script, byte[] signature) {
		this.sourceTxId = new SimpleStringProperty(sourceTxId);
		this.sourceTxoIndex = new SimpleIntegerProperty(sourceTxoIndex);
		this.script = new SimpleStringProperty(script);
		this.signature = new SimpleObjectProperty<byte[]>(signature);
	}

	public StringProperty getSourceTxId() {
		return sourceTxId;
	}

	public IntegerProperty getSourceTxoIndex() {
		return sourceTxoIndex;
	}

	public StringProperty getScript() {
		return script;
	}

	public ObjectProperty<byte[]> getSignature() {
		return signature;
	}
}