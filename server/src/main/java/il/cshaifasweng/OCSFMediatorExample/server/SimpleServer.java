package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import java.io.IOException;
import java.util.ArrayList;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

	public SimpleServer(int port) {
		super(port);
	}
// the first version - the built-in one
//	@Override
//	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
//		String msgString = msg.toString();
//		if (msgString.startsWith("#warning")) {
//			Warning warning = new Warning("Warning from server!");
//			try {
//				client.sendToClient(warning);
//				System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		else if(msgString.startsWith("add client")){
//			SubscribedClient connection = new SubscribedClient(client);
//			SubscribersList.add(connection);
//			try {
//				client.sendToClient("client added successfully");
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			}
//		}
//		else if(msgString.startsWith("remove client")){
//			if(!SubscribersList.isEmpty()){
//				for(SubscribedClient subscribedClient: SubscribersList){
//					if(subscribedClient.getClient().equals(client)){
//						SubscribersList.remove(subscribedClient);
//						break;
//					}
//				}
//			}
//		}
//	}
	////////////////// I CHANGED THE FOLLOWING PART ////////////////////

	private ConnectionToClient player1 = null;
	private ConnectionToClient player2 = null;
	private boolean isXTurn = true; // Track turns: X or O

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();

		if (msgString.startsWith("add client")) {
			if (player1 == null) {
				player1 = client;
				sendMessage(client, "You are Player 1 (X). Waiting for Player 2...");
			} else if (player2 == null) {
				player2 = client;
				sendMessage(client, "You are Player 2 (O). Player 1 starts.");
				sendMessage(player1, "Player 2 connected. Game starts!");
				broadcastTurn();
			} else {
				sendMessage(client, "Server is full. Two players already connected.");
			}
		} else if (msgString.startsWith("move")) {
			handlePlayerMove(msgString, client);
		}
	}

	private void handlePlayerMove(String msg, ConnectionToClient client) {
		if ((client == player1 && isXTurn) || (client == player2 && !isXTurn)) {
			broadcastMove(msg);
			isXTurn = !isXTurn; // Switch turn
			broadcastTurn();
		} else {
			sendMessage(client, "Not your turn!");
		}
	}

	private void broadcastMove(String msg) {
		sendMessage(player1, msg);
		sendMessage(player2, msg);
	}

	private void broadcastTurn() {
		if (isXTurn) {
			sendMessage(player1, "Your turn");
			sendMessage(player2, "Waiting for Player 1...");
		} else {
			sendMessage(player2, "Your turn");
			sendMessage(player1, "Waiting for Player 2...");
		}
	}

	private void sendMessage(ConnectionToClient client, String message) {
		try {
			client.sendToClient(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/////////////////////////////////////////////////////////////////////////////

	public void sendToAllClients(String message) {
		try {
			for (SubscribedClient subscribedClient : SubscribersList) {
				subscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
