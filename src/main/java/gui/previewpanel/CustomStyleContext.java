package gui.previewpanel;

import java.awt.Font;

import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class CustomStyleContext extends StyleContext {
    @Override
    public Font getFont(AttributeSet attr) {
        Object f = attr.getAttribute("MyFont");
        if (f instanceof Font) {
             System.out.println("getFont called!  MyFont=" + f + "  isItalicAttr?=" + StyleConstants.isItalic(attr));
            return (Font) f;
        }
        return super.getFont(attr);
    }
}
