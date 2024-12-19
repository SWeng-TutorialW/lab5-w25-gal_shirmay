package il.cshaifasweng.OCSFMediatorExample.client;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.client;

public class InitController {

    private String ip;    // Hold the IP value
    private String port;  // Hold the port value

    // Constructor to initialize IP and Port
    public InitController(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public void connect(){
        SimpleClient.ip = ip;
        SimpleClient.port = Integer.parseInt(port);
        client = SimpleClient.getClient();
        try {
            //client.openConnection();
            System.out.println("Connection Successful!");
        } catch (Exception e){
            throw new RuntimeException("Failed to connect", e);
        }
    }
}
