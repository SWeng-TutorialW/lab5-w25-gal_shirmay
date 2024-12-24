package il.cshaifasweng.OCSFMediatorExample.client;

public class GameStatusEvent {
    private final String status;

    public GameStatusEvent(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
