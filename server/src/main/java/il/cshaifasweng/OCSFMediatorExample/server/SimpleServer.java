package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import java.io.IOException;
import java.util.ArrayList;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private String[][] board = {
			{"", "", ""},
			{"", "", ""},
			{"", "", ""}
	};

	public SimpleServer(int port) {
		super(port);
	}

	private ConnectionToClient player1 = null;
	private ConnectionToClient player2 = null;
	private boolean isXTurn = true; // Track turns: X or O

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();
		System.out.println("Received message from client: " + msgString);

		if (msgString.startsWith("add client")) {
			if (player1 == null) {
				player1 = client;
				sendMessage(client, "You are Player 1 (X). Waiting for Player 2...");
				System.out.println("Player 1 connected: " + client);
			} else if (player2 == null) {
				player2 = client;
				sendMessage(client, "You are Player 2 (O). Player 1 starts.");
				sendMessage(player1, "Player 2 connected. Game starts!");
				System.out.println("Player 2 connected: " + client);
				broadcastTurn();
			} else {
				sendMessage(client, "Server is full. Two players already connected.");
				System.out.println("A third player tried to join. Connection rejected.");
			}
		} else if (msgString.startsWith("move")) {
			System.out.println("Processing move from client: " + msgString);
			handlePlayerMove(msgString, client);
		}
	}


	private void handlePlayerMove(String msg, ConnectionToClient client) {
		String[] parts = msg.split(" "); // Example format: "move cellId X"
		String cellId = parts[1];        // e.g., "cell00"
		String mark = parts[2];          // e.g., "X" or "O"

		// Map cellId to board indices
		int row = Character.getNumericValue(cellId.charAt(4)); // e.g., '0' from "cell00"
		int col = Character.getNumericValue(cellId.charAt(5)); // e.g., '0' from "cell00"

		if (board[row][col].isEmpty()) {
			board[row][col] = mark; // Update board state

			String moveMessage = "move " + cellId + " " + mark; // Ensure the correct mark (X or O)
			broadcastMove(msg); // Notify clients of the move

			// Check for win or draw
			if (checkWinCondition()) {
				sendMessage(player1, isXTurn ? "You won !" : "You lost :(");
				sendMessage(player2, isXTurn ? "You lost :(" : "You won !");
				resetGame();
			} else if (checkDrawCondition()) {
				sendMessage(player1, "It's a tie !");
				sendMessage(player2, "It's a tie !");
				resetGame();
			} else {
				isXTurn = !isXTurn; // Switch turn
				broadcastTurn();
			}
		} else {
			sendMessage(client, "Invalid move: Cell is already occupied.");
		}
	}

	private boolean checkWinCondition() {
		// Check rows and columns
		for (int i = 0; i < 3; i++) {
			if (!board[i][0].isEmpty() && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
				return true; // Row match
			}
			if (!board[0][i].isEmpty() && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
				return true; // Column match
			}
		}
		// Check diagonals
		if (!board[0][0].isEmpty() && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
			return true; // Top-left to bottom-right diagonal
		}
		if (!board[0][2].isEmpty() && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
			return true; // Top-right to bottom-left diagonal
		}
		return false; // No win
	}

	private boolean checkDrawCondition() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j].isEmpty()) {
					return false; // Found an empty cell
				}
			}
		}
		return true; // All cells filled with no winner
	}
	private void resetGame() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = ""; // Clear board state
			}
		}
		isXTurn = true; // Reset turn
		sendMessage(player1, "New game started. You are Player 1 (X). It's your turn!");
		sendMessage(player2, "New game started. You are Player 2 (O). Waiting for Player 1's move.");
	}

	private void broadcastMove(String msg) {
		System.out.println("Broadcasting move to both players: " + msg);
		sendMessage(player1, msg);
		sendMessage(player2, msg);
	}

	private void broadcastTurn() {
		if (isXTurn) {
			sendMessage(player1, "Your turn");
			sendMessage(player2, "Waiting for Player 1...");
			System.out.println("Broadcasting turn: Player 1's turn");
		} else {
			sendMessage(player2, "Your turn");
			sendMessage(player1, "Waiting for Player 2...");
			System.out.println("Broadcasting turn: Player 2's turn");
		}
	}

	private void sendMessage(ConnectionToClient client, String message) {
		try {
			client.sendToClient(message);
			System.out.println("Message sent to client: " + message);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to send message to client.");
		}
	}

	public void sendToAllClients(String message) {
		try {
			System.out.println("Broadcasting message to all clients: " + message);
			for (SubscribedClient subscribedClient : SubscribersList) {
				subscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Failed to broadcast message to all clients.");
		}
	}
}
