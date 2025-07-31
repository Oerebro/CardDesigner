package gui.image_composers.custom_styles;
import javax.swing.text.*;

public class CustomViewFactory implements ViewFactory {

    @Override
    public View create(Element elem) {
        String kind = elem.getName();
        if (kind != null) {
            switch (kind) {
                case AbstractDocument.ContentElementName:
                    return new CustomGlyphView(elem);
                case AbstractDocument.ParagraphElementName:;
                    return new CustomParagraphView(elem);
                case AbstractDocument.SectionElementName: 
                    return new BoxView(elem, View.Y_AXIS);
                case StyleConstants.ComponentElementName:
                    return new ComponentView(elem);
                case StyleConstants.IconElementName:
                    return new IconView(elem);
            }
        }

        return new LabelView(elem); // fallback
    }
}
