package io.tokhn;

import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.tokhn.core.Wallet;
import io.tokhn.grpc.TokhnServiceGrpc;
import io.tokhn.grpc.TokhnServiceGrpc.TokhnServiceStub;
import io.tokhn.grpc.WelcomeRequest;
import io.tokhn.grpc.WelcomeRequest.PeerType;
import io.tokhn.grpc.WelcomeResponse;
import io.tokhn.model.Address;
import io.tokhn.model.TXI;
import io.tokhn.model.TXO;
import io.tokhn.model.Transaction;
import io.tokhn.store.MapDBWalletStore;
import io.tokhn.view.AddressesController;
import io.tokhn.view.BalancesController;
import io.tokhn.view.DigitalRain;
import io.tokhn.view.RootController;
import io.tokhn.view.TransactionController;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	private Wallet wallet = null;
	private ObservableList<Transaction> txData = FXCollections.observableArrayList();
	private Stage primaryStage;
	private BorderPane rootLayout;
	private BooleanProperty tokhnConnected;
	private TokhnServiceStub tokhnStub = null;

	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		launch(args);
	}

	public Main() {
		tokhnConnected = new SimpleBooleanProperty(false);
		
		// sample data
		txData.add(
				new Transaction("1ead691e832d54f2776c314281b3a9e07355bb8955df8111586241b48f4abc5b", 1520116601,
						"REGULAR",
						Arrays.asList(new TXI("1ead691e832d54f2776c314281b3a9e07355bb8955df8111586241b48f4abc5b", 0, "",
								new byte[0])),
						Arrays.asList(new TXO("1Nh7uHdvY6fNwtQtM1G5EZAFPLC33B59rB", 13035538, ""))));
		txData.add(
				new Transaction("2ead691e832d54f2776c314281b3a9e07355bb8955df8111586241b48f4abc5b", 1520226601,
						"REGULAR",
						Arrays.asList(new TXI("2ead691e832d54f2776c314281b3a9e07355bb8955df8111586241b48f4abc5b", 0, "",
								new byte[0])),
						Arrays.asList(new TXO("2Nh7uHdvY6fNwtQtM1G5EZAFPLC33B59rB", 23035538, ""))));
		txData.add(
				new Transaction("3ead691e832d54f2776c314281b3a9e07355bb8955df8111586241b48f4abc5b", 1520336601,
						"REGULAR",
						Arrays.asList(new TXI("3ead691e832d54f2776c314281b3a9e07355bb8955df8111586241b48f4abc5b", 0, "",
								new byte[0])),
						Arrays.asList(new TXO("3Nh7uHdvY6fNwtQtM1G5EZAFPLC33B59rB", 33035538, ""))));
		txData.add(
				new Transaction("4ead691e832d54f2776c314281b3a9e07355bb8955df8111586241b48f4abc5b", 1520446601,
						"REGULAR",
						Arrays.asList(new TXI("4ead691e832d54f2776c314281b3a9e07355bb8955df8111586241b48f4abc5b", 0, "",
								new byte[0])),
						Arrays.asList(new TXO("4Nh7uHdvY6fNwtQtM1G5EZAFPLC33B59rB", 43035538, ""))));
		txData.add(
				new Transaction("5ead691e832d54f2776c314281b3a9e07355bb8955df8111586241b48f4abc5b", 1520556601,
						"REGULAR",
						Arrays.asList(new TXI("5ead691e832d54f2776c314281b3a9e07355bb8955df8111586241b48f4abc5b", 0, "",
								new byte[0])),
						Arrays.asList(new TXO("5Nh7uHdvY6fNwtQtM1G5EZAFPLC33B59rB", 53035538, ""))));
		
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 1337).usePlaintext(true).build();
		tokhnStub = TokhnServiceGrpc.newStub(channel).withWaitForReady();
		
		tokhnStub.getWelcome(WelcomeRequest.newBuilder().setPeerType(PeerType.CLIENT).build(), new StreamObserver<WelcomeResponse>() {

			@Override
			public void onNext(WelcomeResponse welcomeResponse) {
				tokhnConnected.set(true);
			}

			@Override
			public void onError(Throwable t) {
				tokhnConnected.set(false);
			}

			@Override
			public void onCompleted() {
				//nop
			}
		});
	}

	public ObservableList<Transaction> getTxData() {
		return txData;
	}

	public TokhnServiceStub getTokhnStub() {
		return tokhnStub;
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Tokhn Wallet");
		this.primaryStage.getIcons().add(new Image("file:resources/images/wallet.png"));

		initRootLayout();
		
		showDigitalRain();
	}

	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Root.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			RootController controller = loader.getController();
			controller.setMain(this);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File file = getWalletFilePath();
		if (file != null) {
			loadWalletFile(file);
		}
	}
	
	public void showDigitalRain() {
		DigitalRain dr = new DigitalRain();
		Pane container = new Pane();
		container.getChildren().add(dr);
		rootLayout.setCenter(container);
		dr.widthProperty().bind(container.widthProperty());
		dr.heightProperty().bind(container.heightProperty());
	}

	public void showTransactionOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Transaction.fxml"));
			AnchorPane transactionOverview = (AnchorPane) loader.load();

			// Set transaction overview into the center of root layout.
			rootLayout.setCenter(transactionOverview);

			TransactionController controller = loader.getController();
			controller.setMain(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showAddresses() {
		try {
			// Load the fxml file and create a new stage for the popup.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Addresses.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Network Addresses");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the persons into the controller.
			AddressesController controller = loader.getController();
			List<Address> addresses = wallet.getAddresses().values().stream().map(a -> new Address(a.getNetwork().toString(), a.toString())).collect(Collectors.toList());
			controller.setAddressData(FXCollections.observableArrayList(addresses));

			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showBalances() {
		try {
			// Load the fxml file and create a new stage for the popup.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Balances.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Network Balances");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the persons into the controller.
			BalancesController controller = loader.getController();
			controller.setBalanceData(wallet.getBalances());

			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public BooleanProperty getTokhnConnected() {
		return tokhnConnected;
	}

	public File getWalletFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	public void setWalletFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());
		} else {
			prefs.remove("filePath");
		}
	}

	public void loadWalletFile(File file) {
		try {
			wallet = new Wallet(new MapDBWalletStore(file));
			setWalletFilePath(file);
		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	public void saveWalletFile(File file) {
		try {
			// TODO: actually do something here
			wallet = null;
		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());

			alert.showAndWait();
		}
	}
}
