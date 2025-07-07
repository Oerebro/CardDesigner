package events;

public class ItemImageUpdateEvent {
    public final int type;
    public final String path;

    public ItemImageUpdateEvent(int type, String path) {
        this.type = type;
        this.path = path;
    }
}