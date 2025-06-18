package gui.controlpanel1;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FontLoader {
    public static Font loadFont(String family, int style, float size) {
        String basePath = "resources/misc/fonts/";
        String styleSuffix;

        // Map the style int to suffix string in filename
        switch (style) {
            case Font.BOLD:
                styleSuffix = "-bold";
                break;
            case Font.ITALIC:
                styleSuffix = "-italic";
                break;
            case Font.BOLD | Font.ITALIC:
                styleSuffix = "-bolditalic";
                break;
            default:
                styleSuffix = "-regular";
        }

        String[] extensions = {".ttf", ".otf"};

        for (String ext : extensions) {
            File file = new File(basePath + family + styleSuffix + ext);
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, fis);
                    return font.deriveFont(size);
                } catch (FontFormatException | IOException e) {
                    // optionally log error, continue to next extension
                }
            }
        }

        // Fallback to regular if style font not found
        for (String ext : extensions) {
            File file = new File(basePath + family + "-regular" + ext);
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, fis);
                    return font.deriveFont(size);
                } catch (FontFormatException | IOException e) {
                    // optionally log error
                }
            }
        }

        // Final fallback to system font
        return new Font(Font.SANS_SERIF, Font.PLAIN, Math.round(size));
    }
}
