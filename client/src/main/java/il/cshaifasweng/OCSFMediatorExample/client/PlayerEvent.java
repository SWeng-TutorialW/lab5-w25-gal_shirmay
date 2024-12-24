package il.cshaifasweng.OCSFMediatorExample.client;

public class PlayerEvent {
    private final String playerRole;

    public PlayerEvent(String playerRole) {
        this.playerRole = playerRole;
    }

    public String getPlayerRole() {
        return playerRole;
    }
}
