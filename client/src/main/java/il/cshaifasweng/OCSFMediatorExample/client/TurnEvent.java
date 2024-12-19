package il.cshaifasweng.OCSFMediatorExample.client;

public class TurnEvent {
    private final boolean isYourTurn;

    public TurnEvent(boolean isYourTurn) {
        this.isYourTurn = isYourTurn;
    }

    public boolean isYourTurn() {
        return isYourTurn;
    }
}
