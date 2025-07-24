package events;

public class ImageUpdate {
    public final int type;
    public final String path;
    public final String imageName;

    public ImageUpdate(int type, String path) {
        this.type = type;
        this.path = path;
        this.imageName = "";
    }

    public ImageUpdate(int type, String path, String imgName) {
        this.type = type;
        this.path = path;
        this.imageName = imgName;
    }
}