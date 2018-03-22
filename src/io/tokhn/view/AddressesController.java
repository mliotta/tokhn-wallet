package io.tokhn.view;

import java.util.Optional;

import io.tokhn.model.Address;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AddressesController {
	@FXML
	private Button generate;
	@FXML
	private TableView<Address> addressTable;
	@FXML
	private TableColumn<Address, String> networkColumn;
	@FXML
	private TableColumn<Address, String> addressColumn;
	
	public void setAddressData(ObservableList<Address> addresses) {
		addressTable.setItems(addresses);
	}
	
	@FXML
	private void initialize() {
		networkColumn.setCellValueFactory(cellData -> cellData.getValue().getNetwork());
		addressColumn.setCellValueFactory(cellData -> cellData.getValue().getAddress());
	}
	
	@FXML
	private void handleGenerate() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Key Pair and Address Generation");
		alert.setHeaderText("You are about to generate a new key pair\n and assoicated addresses overwriting\n what is in your wallet file now.\n\nYou can cancel this and\n use File->Save As to store a copy\n of your existing wallet.");
		alert.setContentText("Are you ok with this?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			// ... user chose OK
		} else {
			// ... user chose CANCEL or closed the dialog
		}
	}
}