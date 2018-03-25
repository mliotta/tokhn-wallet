package io.tokhn.view;

import io.tokhn.Main;
import io.tokhn.core.Address;
import io.tokhn.core.Token;
import io.tokhn.core.Transaction;
import io.tokhn.node.InvalidNetworkException;
import io.tokhn.node.Network;
import io.tokhn.util.GRPC;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NewTransactionController {
	
	@FXML
	private ChoiceBox<Network> netChoice;
	@FXML
	private Label balance;
	@FXML
	private TextField toAddress;
	@FXML
	private TextField sendAmount;
	
	private Main main;
	private ObservableList<Network> networks = null;
	
	@FXML
	private void handleSend() {
		boolean failed = false;
		String reason = null;
		
		try {
			Address to = new Address(toAddress.getText());
			Token amount = Token.parseToken(sendAmount.getText());
			Transaction tx = main.getWallet().newTx(netChoice.getValue(), to, amount);
			main.getTxObserver().onNext(GRPC.transform(netChoice.getValue(), tx));
		} catch(InvalidNetworkException e) {
			failed = true;
			reason = "Malformed address";
		} catch(Exception e) {
			failed = true;
			reason = e.getMessage();
		}
		
		if(failed) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Unable to submit transaction");
			alert.setContentText(reason);

			alert.showAndWait();
		}
	}
	
	@FXML
	private void initialize() {
		networks = FXCollections.observableArrayList(Network.values());
		netChoice.setItems(networks);
		netChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Network selected = networks.get(newValue.intValue());
				balance.setText(main.getWallet().getBalance(selected).toString());
			}
		});
	}
	
	public void setMain(Main main) {
		this.main = main;
		
		netChoice.getSelectionModel().selectFirst();
	}
}