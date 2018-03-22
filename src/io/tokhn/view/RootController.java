package io.tokhn.view;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.tokhn.Main;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class RootController {
	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@FXML
	private MenuItem save;
	@FXML
	private MenuItem saveAs;
	@FXML
	private Label localLabel;
	@FXML
	private Label utcLabel;
	@FXML
	private FontAwesomeIconView tokhnConnected;
	
	private Main main;
	
	public RootController() {
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.seconds(0), event -> localLabel.setText(LocalDateTime.now().format(formatter))),
				new KeyFrame(Duration.seconds(0), event -> utcLabel.setText(ZonedDateTime.now(ZoneOffset.UTC).format(formatter))),
				new KeyFrame(Duration.seconds(1)));

		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	@FXML
	private void handleOpen() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB files (*.db)", "*.db");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(main.getPrimaryStage());

		if (file != null) {
			main.loadWalletFile(file);
			save.disableProperty().set(false);
			saveAs.disableProperty().set(false);
		}
	}

	@FXML
	private void handleSave() {
		File walletFile = main.getWalletFilePath();
		if (walletFile != null) {
			main.saveWalletFile(walletFile);
		} else {
			handleSaveAs();
		}
	}
	
	@FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB files (*.db)", "*.db");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(main.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".db")) {
                file = new File(file.getPath() + ".db");
            }
            main.saveWalletFile(file);
        }
    }
	
	@FXML
	private void handleAddresses() {
		main.showAddresses();
	}
	
	@FXML
	private void handleBalances() {
		main.showBalances();
	}
	
	@FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Tokhn Wallet");
        alert.setHeaderText("About");
        alert.setContentText("Author: Matt Liotta\nWebsite: http://tokhn.io");

        alert.showAndWait();
    }
	
	@FXML
    private void handleExit() {
        System.exit(0);
    }

	public void setMain(Main main) {
		this.main = main;
		
		updateTokhnConnected(main.getTokhnConnected().getValue());
		main.getTokhnConnected().addListener((observable, oldValue, newValue) -> updateTokhnConnected(newValue.booleanValue()));
		
		if(main.getWalletFilePath() == null) {
			save.disableProperty().set(true);
			saveAs.disableProperty().set(true);
		}
	}
	
	private void updateTokhnConnected(boolean connected) {
		if(connected) {
			tokhnConnected.setFill(Color.GREEN);
		} else {
			tokhnConnected.setFill(Color.RED);
		}
	}
}