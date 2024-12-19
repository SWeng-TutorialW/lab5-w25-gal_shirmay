package il.cshaifasweng.OCSFMediatorExample.client;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class PrimaryController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField IP_TextFiled;

	@FXML
	private TextField Port_TextFiled;

	@FXML
	private Button btnConAndPlay;

	@FXML
	void ConnectAndPlay(ActionEvent event) throws IOException {
		String ip = IP_TextFiled.getText();
		String port = Port_TextFiled.getText();

		// Initialize the InitController and attempt a connection
		InitController initController = new InitController(ip, port);
		initController.connect();

		// If connection is successful, open the secondary window
		Stage connnectStage = new Stage();
		Parent parent = App.loadFXML("secondary");
		if (parent == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "FXML loading error.");
			alert.show();
		}
		Scene scene = new Scene(parent);
		connnectStage.setScene(scene);
		connnectStage.show();
		}

	@FXML
	void enterIP(ActionEvent event) {
	}

	@FXML
	void enterPORT(ActionEvent event) {
	}

	@FXML
	void initialize() {
		IP_TextFiled.setText("localhost");
		Port_TextFiled.setText("3000");
		assert IP_TextFiled != null : "fx:id=\"IP_TexrFiled\" was not injected: check your FXML file 'primary.fxml'.";
		assert Port_TextFiled != null : "fx:id=\"Port_TextFiled\" was not injected: check your FXML file 'primary.fxml'.";
		assert btnConAndPlay != null : "fx:id=\"btnConAndPlay\" was not injected: check your FXML file 'primary.fxml'.";
	}

}
