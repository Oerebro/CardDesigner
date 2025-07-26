package gui.image_composers.custom_styles;

import javax.swing.text.*;
import java.awt.*;

public class CustomGlyphView extends GlyphView {
    public CustomGlyphView(Element elem) {
        super(elem);
    }

    @Override
    public Font getFont() {
        AttributeSet attr = getAttributes();
        Font font = (Font) attr.getAttribute("fontInstance");
        if (font != null) {
            return font;
        }
        return super.getFont();
    }
}
