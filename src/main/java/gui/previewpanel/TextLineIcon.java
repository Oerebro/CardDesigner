package gui.previewpanel;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class TextLineIcon implements Icon {
    private final Icon baseIcon;
    private final int yOffset;

    public TextLineIcon(Icon baseIcon, int yOffset) {
        this.baseIcon = baseIcon;
        this.yOffset = yOffset;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        baseIcon.paintIcon(c, g, x, y + yOffset);
    }

    @Override
    public int getIconWidth() {
        return baseIcon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return baseIcon.getIconHeight();
    }
}
