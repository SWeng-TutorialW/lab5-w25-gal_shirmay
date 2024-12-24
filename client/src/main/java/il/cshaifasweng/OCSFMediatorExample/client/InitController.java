package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.scene.control.Alert;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.client;

public class InitController {
    private String ip;
    private String port;

    public InitController(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public void connect() {
        SimpleClient.ip = ip;
        SimpleClient.port = Integer.parseInt(port);
        client = SimpleClient.getClient();
        try {
            client.openConnection();
            System.out.println("Connection Successful! Connected to server at IP: " + ip + " on Port: " + port);
            System.out.println("Sending 'add client' message to the server...");
            client.sendToServer("add client");
        } catch (Exception e) {
            System.out.println("Failed to connect to server at IP: " + ip + " on Port: " + port);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to the server. Please check the IP and port.");
            alert.show();
            throw new RuntimeException("Failed to connect", e);
        }
    }


}
