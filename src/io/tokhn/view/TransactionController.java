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

import io.tokhn.Main;
import io.tokhn.model.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TransactionController {
	@FXML
	private TableView<Transaction> txTable;
	@FXML
	private TableColumn<Transaction, String> idColumn;

	@FXML
	private Label idLabel;
	@FXML
	private Label timestampLabel;
	@FXML
	private Label typeLabel;
	
	private Main main;
	
	private void showTxDetails(Transaction tx) {
		if(tx != null) {
			idLabel.setText(tx.getId().get());
			timestampLabel.setText(tx.getTimestamp().get().toString());
			typeLabel.setText(tx.getType().get());
		} else {
			idLabel.setText("");
			timestampLabel.setText("");
			typeLabel.setText("");
		}
	}
	
	@FXML
    private void initialize() {
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getId());
		
		showTxDetails(null);
		
		txTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showTxDetails(newValue));
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
}