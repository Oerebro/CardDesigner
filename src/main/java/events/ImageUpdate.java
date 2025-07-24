package events;

public class ImageUpdate {
    public final int type;
    public final String path;
    public final String id;

    public ImageUpdate(int type, String path) {
        this.type = type;
        this.path = path;
        this.id = "";
    }

    public ImageUpdate(String id, String path) {
        this.id = id;
        this.path = path;
        this.type = 999;
    }
}