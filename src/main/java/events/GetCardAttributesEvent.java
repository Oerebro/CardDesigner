package events;

import gui.ImageComposerConfig;

public class GetCardAttributesEvent {
    public ImageComposerConfig config;

    public GetCardAttributesEvent(ImageComposerConfig config){
        this.config = config;
    }
}
