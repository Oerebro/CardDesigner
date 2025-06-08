package abstractclasses;

import events.EventBus;
import events.ImageUpdateEvent;

public abstract class ImagePublisher {
    protected String type,path;
    protected void publishImageUpdate(String type, String path) {
        EventBus.publish(new ImageUpdateEvent(type,path));
    }
}
