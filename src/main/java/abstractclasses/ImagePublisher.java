package abstractclasses;

import events.EventBus;
import events.ImageUpdate;

public abstract class ImagePublisher {
    protected String path;
    protected int type;
    protected void publishImageUpdate(int type, String path) {
        EventBus.publish(new ImageUpdate(type,path));
    }
}
