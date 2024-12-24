package il.cshaifasweng.OCSFMediatorExample.client;

public class MoveEvent {
    private final String move;

    public MoveEvent(String move) {
        this.move = move;
    }

    public String getMove() {
        return move;
    }
}
