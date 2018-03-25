/*
 * Copyright 2018 Matt Liotta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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