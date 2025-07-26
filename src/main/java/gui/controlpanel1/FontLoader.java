package gui.controlpanel1;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
public class FontLoader {
    public static Font loadFont(String family, int style, float size) {
        String basePath = "resources/misc/fonts/";
        File dir = new File(basePath);

        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Font directory not found.");
            return fallbackFont(size);
        }

        String familyLower = family.toLowerCase();
        String styleSuffix;

        switch (style) {
            case Font.BOLD:
                styleSuffix = "bold";
                break;
            case Font.ITALIC:
                styleSuffix = "italic";
                break;
            case Font.PLAIN:
            default:
                styleSuffix = "regular";
        }

        File[] files = dir.listFiles();
        if (files == null) return fallbackFont(size);

        for (File file : files) {
            String name = file.getName().toLowerCase();
            if (name.contains(familyLower) && name.contains(styleSuffix) && (name.endsWith(".ttf") || name.endsWith(".otf"))) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, fis);
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                    return font.deriveFont(size);
                } catch (FontFormatException | IOException e) {
                    System.err.println("Failed to load font: " + file.getName());
                    e.printStackTrace();
                }
            }
        }

        System.out.println("No matching font file found for: " + family + styleSuffix);
        return fallbackFont(size);
    }

    private static Font fallbackFont(float size) {
        return new Font(Font.SANS_SERIF, Font.PLAIN, Math.round(size));
    }
}

