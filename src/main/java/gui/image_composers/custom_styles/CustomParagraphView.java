package gui.image_composers.custom_styles;

import javax.swing.text.*;

public class CustomParagraphView extends ParagraphView {

    public CustomParagraphView(Element elem) {
        super(elem);
    }

    /**
     * Returns the number of visual lines this paragraph occupies.
     * Each child view corresponds to one visual line after wrapping.
     */
    public int getVisualLineCount() {
        return getViewCount();
    }
}

