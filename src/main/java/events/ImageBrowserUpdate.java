package events;

import abstractclasses.ImageBrowser;

public class ImageBrowserUpdate {
    public final ImageBrowser browser;
        public ImageBrowserUpdate(ImageBrowser browser){
            this.browser = browser;
        }
}
