package events;

import gui.ImageComposerConfig;

public class LoadConfigEvent {
    public ImageComposerConfig config;

    public LoadConfigEvent(ImageComposerConfig config){
        this.config = config;
    }
}
