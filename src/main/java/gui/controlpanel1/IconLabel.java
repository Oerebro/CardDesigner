package gui.controlpanel1;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Graphics;

public class IconLabel extends JLabel {
    public IconLabel(ImageIcon icon) {
        super(icon);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set the color for the outline
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, getWidth(), getHeight());
    }
}