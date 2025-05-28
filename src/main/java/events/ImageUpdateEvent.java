package events;

public class ImageUpdateEvent {
    public final String type,path;

    public ImageUpdateEvent(String type, String path) {
        this.type = type;
        this.path = path;
    }
}