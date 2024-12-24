package il.cshaifasweng.OCSFMediatorExample.client;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SecondaryController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label cell00, cell01, cell02, cell10, cell11, cell12,cell20, cell21, cell22;

    @FXML
    private Label statusLabel;

    private boolean isXTurn = true; // Track turns: X or O
    private boolean isMyTurn = false;
    private String playerRole; // This will store "X" or "O"

    @FXML
    private void switchToPrimary() throws IOException {App.setRoot("primary");}

    @FXML
    void initialize() {
        assert cell00 != null : "fx:id=\"cell00\" was not injected: check your FXML file 'secondary.fxml'.";
        assert cell01 != null : "fx:id=\"cell01\" was not injected: check your FXML file 'secondary.fxml'.";
        assert cell02 != null : "fx:id=\"cell02\" was not injected: check your FXML file 'secondary.fxml'.";
        assert cell10 != null : "fx:id=\"cell10\" was not injected: check your FXML file 'secondary.fxml'.";
        assert cell11 != null : "fx:id=\"cell11\" was not injected: check your FXML file 'secondary.fxml'.";
        assert cell12 != null : "fx:id=\"cell12\" was not injected: check your FXML file 'secondary.fxml'.";
        assert cell20 != null : "fx:id=\"cell20\" was not injected: check your FXML file 'secondary.fxml'.";
        assert cell21 != null : "fx:id=\"cell21\" was not injected: check your FXML file 'secondary.fxml'.";
        assert cell22 != null : "fx:id=\"cell22\" was not injected: check your FXML file 'secondary.fxml'.";

        EventBus.getDefault().register(this);

        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow(); // Access the Stage using rootPane
            stage.setTitle("Tic-Tac-Toe Game"); // Set the window title
            stage.setOnCloseRequest(event -> EventBus.getDefault().unregister(this)); // Unregister EventBus on window close
        });

        setupCellClickHandler(cell00);
        setupCellClickHandler(cell01);
        setupCellClickHandler(cell02);
        setupCellClickHandler(cell10);
        setupCellClickHandler(cell11);
        setupCellClickHandler(cell12);
        setupCellClickHandler(cell20);
        setupCellClickHandler(cell21);
        setupCellClickHandler(cell22);
    }


    private void setupCellClickHandler(Label cell) {
        cell.setOnMouseClicked(event -> {
            if (isMyTurn && cell.getText().isEmpty()) {
                cell.setText(playerRole); // Use the player's role here
                try {
                    SimpleClient.client.sendToServer("move " + getCellId(cell) + " " + playerRole);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                isMyTurn = false;
            }
        });
    }

    @Subscribe
    public void onMoveEvent(MoveEvent event) {
        String[] parts = event.getMove().split(" ");
        String cellId = parts[1];
        String mark = parts[2];

        Label targetCell = getCellById(cellId);
        if (targetCell != null) {
            targetCell.setText(mark);
        }
    }

    @Subscribe
    public void onTurnEvent(TurnEvent event) {
        isMyTurn = event.isYourTurn();
        if (isMyTurn) {
            System.out.println("Your turn !");
            statusLabel.setText("Your turn !");
        } else {
            System.out.println("Waiting for the other player...");
            statusLabel.setText("Waiting for the other player...");
        }
    }

    private String getCellId(Label cell) {
        if (cell == cell00) return "cell00";
        if (cell == cell01) return "cell01";
        if (cell == cell02) return "cell02";
        if (cell == cell10) return "cell10";
        if (cell == cell11) return "cell11";
        if (cell == cell12) return "cell12";
        if (cell == cell20) return "cell20";
        if (cell == cell21) return "cell21";
        if (cell == cell22) return "cell22";
        return "";
    }

    private Label getCellById(String cellId) {
        switch (cellId) {
            case "cell00": return cell00;
            case "cell01": return cell01;
            case "cell02": return cell02;
            case "cell10": return cell10;
            case "cell11": return cell11;
            case "cell12": return cell12;
            case "cell20": return cell20;
            case "cell21": return cell21;
            case "cell22": return cell22;
            default: return null;
        }
    }

    @Subscribe
    public void onPlayerEvent(PlayerEvent event) {
        this.playerRole = event.getPlayerRole();
        System.out.println("Assigned player role: " + playerRole);
    }

    @Subscribe
    public void onGameStatusEvent(GameStatusEvent event) {
        Platform.runLater(() -> {
            String status = event.getStatus();
            System.out.println("Game status: " + status);
            statusLabel.setText(status);
            isMyTurn = false; // Disable further moves
        });
    }
}