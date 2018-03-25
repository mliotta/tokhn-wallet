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