package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.application.Platform;
import org.greenrobot.eventbus.EventBus;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

public class SimpleClient extends AbstractClient {
	
	public static SimpleClient client = null;
	public static int port = 3000;
	public static String ip = "192.168.153.1";

	private SimpleClient(String host, int port) {
		super(host, port);
	}
// the first version - the built-in one
//	@Override
//	protected void handleMessageFromServer(Object msg) {
//		if (msg.getClass().equals(Warning.class)) {
//			EventBus.getDefault().post(new WarningEvent((Warning) msg));
//		}
//		else{
//			String message = msg.toString();
//			System.out.println(message);
//		}
//	}

	////////////////// I CHANGED THE FOLLOWING PART ////////////////////
	@Override
	protected void handleMessageFromServer(Object msg) {
		Platform.runLater(() -> {
			if (msg.toString().startsWith("move")) {
				EventBus.getDefault().post(new MoveEvent(msg.toString()));
			} else if (msg.toString().startsWith("Your turn")) {
				EventBus.getDefault().post(new TurnEvent(true));
			} else if (msg.toString().startsWith("Waiting")) {
				EventBus.getDefault().post(new TurnEvent(false));
			} else {
				System.out.println(msg);
			}
		});
	}
	/////////////////////////////////////////////////////////////////////////////


	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient(ip, port);
		}
		return client;
	}

}
