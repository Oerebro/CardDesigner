package gui.previewpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.font.GlyphVector;

public class OutlinedLabel extends JLabel {
    private Color outlineColor = Color.BLACK;
    private float strokeWidth = 3f;

    public OutlinedLabel() {
        super();
        setOpaque(false);
    }

    public OutlinedLabel(String text) {
        super(text);
        setOpaque(false);
    }

    public OutlinedLabel(String text, int alignment) {
        super(text, alignment);
        setOpaque(false);
    }

    public void setOutlineColor(Color c) {
        this.outlineColor = c;
        repaint();
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        String text = getText();
        if (text == null || text.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setFont(getFont());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        int x = switch (getHorizontalAlignment()) {
            case CENTER -> (getWidth() - textWidth) / 2;
            case RIGHT -> getWidth() - textWidth;
            default -> 0;
        };

        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

        GlyphVector gv = getFont().createGlyphVector(g2.getFontRenderContext(), text);
        Shape shape = gv.getOutline(x, y);

        // Draw outline
        g2.setColor(outlineColor);
        g2.setStroke(new BasicStroke(strokeWidth));
        g2.draw(shape);

        // Draw fill
        g2.setColor(getForeground());
        g2.fill(shape);

        g2.dispose();
    }
}
