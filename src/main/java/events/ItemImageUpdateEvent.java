package events;

public class ItemImageUpdateEvent {
    public final int type;
    public final String path;

    public ItemImageUpdateEvent(int type, String path) {
        //System.out.println("ImageUpdate: "+path+" type: "+type);
        this.type = type;
        this.path = path;
    }
}