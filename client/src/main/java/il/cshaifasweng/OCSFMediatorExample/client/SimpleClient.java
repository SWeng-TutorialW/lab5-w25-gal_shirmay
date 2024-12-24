package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.application.Platform;
import org.greenrobot.eventbus.EventBus;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

public class SimpleClient extends AbstractClient {
	
	public static SimpleClient client = null;
	public static int port = 3000;
	public static String ip = "localhost";

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		Platform.runLater(() -> {
			System.out.println("Received message from server: " + msg);
			if (msg.toString().startsWith("You are Player 1")) {
				EventBus.getDefault().post(new PlayerEvent("X"));
				EventBus.getDefault().post(new GameStatusEvent("You are Player 1 (X). Waiting for Player 2..."));
			} else if (msg.toString().startsWith("You are Player 2")) {
				EventBus.getDefault().post(new PlayerEvent("O"));
				EventBus.getDefault().post(new GameStatusEvent("You are Player 2 (O). Player 1 starts."));
			} else if (msg.toString().startsWith("move")) {
				EventBus.getDefault().post(new MoveEvent(msg.toString()));
			} else if (msg.toString().startsWith("Your turn")) {
				EventBus.getDefault().post(new TurnEvent(true));
			} else if (msg.toString().startsWith("Waiting")) {
				EventBus.getDefault().post(new TurnEvent(false));
			} else if (msg.toString().startsWith("You won !")) {
				EventBus.getDefault().post(new GameStatusEvent("You won !"));
			} else if (msg.toString().startsWith("You lost :(")) {
				EventBus.getDefault().post(new GameStatusEvent("You lost :("));
			} else if (msg.toString().startsWith("It's a tie !")) {
				EventBus.getDefault().post(new GameStatusEvent("It's a tie !"));
			} else {
				System.out.println(msg);
			}
		});
	}

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient(ip, port);
		}
		return client;
	}

}
