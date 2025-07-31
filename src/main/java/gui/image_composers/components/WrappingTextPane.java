package gui.image_composers.components;

import java.awt.Dimension;

import javax.swing.JTextPane;


public class WrappingTextPane extends JTextPane {
    @Override
    public boolean getScrollableTracksViewportWidth() {
        System.out.println("Test Test");
        // Only track viewport width when the viewport is wider than the preferred width
        return getUI().getPreferredSize(this).width 
            <= getParent().getSize().width;
    };

    @Override
    public Dimension getPreferredSize() {
        // Avoid substituting the minimum width for the preferred width when the viewport is too narrow
        return getUI().getPreferredSize(this);
    };
}
