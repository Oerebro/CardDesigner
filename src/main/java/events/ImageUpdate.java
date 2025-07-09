package events;

public class ImageUpdate {
    public final int type;
    public final String path;

    public ImageUpdate(int type, String path) {
        this.type = type;
        this.path = path;
    }
}