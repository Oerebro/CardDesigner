package gui_elements.controlpanel1;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontLoader {
    public static Font loadFont(String path, float size) {
        try {
            File fontFile = new File("resources/misc/fonts/"+path);
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (IOException | FontFormatException e) {
            System.out.println("Error loading font: " + e.getMessage());
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }
}