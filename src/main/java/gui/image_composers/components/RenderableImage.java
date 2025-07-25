package gui.image_composers.components;
import java.awt.image.BufferedImage;

public class RenderableImage {
        public String id, sourcePath;
        public BufferedImage image;
        public int[] bounds;
        public int render;

        public RenderableImage(String id, String sourcePath,BufferedImage image, int[] bounds, int render) {
            this.id = id;
            this.sourcePath = sourcePath;
            this.image = image;
            this.bounds = bounds;
            this.render = render;
        }
}