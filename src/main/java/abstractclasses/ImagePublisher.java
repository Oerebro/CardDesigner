package abstractclasses;

import events.EventBus;
import events.ImageUpdate;

public abstract class ImagePublisher{
    protected String path;
    protected String type;
    protected String id;
    protected void publishImageUpdate(String id, String path) {
        EventBus.publish(new ImageUpdate(id,path));
    }
}
