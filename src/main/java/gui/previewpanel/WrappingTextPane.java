package gui.previewpanel;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class WrappingTextPane extends JTextPane {
    public WrappingTextPane() {
        super();
        setEditorKit(new WrapEditorKit());
    }

    // Custom EditorKit that installs a ViewFactory that wraps words
    private static class WrapEditorKit extends StyledEditorKit {
        private final ViewFactory defaultFactory = new WrapColumnFactory();

        @Override
        public ViewFactory getViewFactory() {
            return defaultFactory;
        }
    }

    // ViewFactory that wraps at word boundaries
    private static class WrapColumnFactory implements ViewFactory {
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName:
                        return new WrapLabelView(elem);
                    case AbstractDocument.ParagraphElementName:
                        return new ParagraphView(elem);
                    case AbstractDocument.SectionElementName:
                        return new BoxView(elem, View.Y_AXIS);
                    case StyleConstants.ComponentElementName:
                        return new ComponentView(elem);
                    case StyleConstants.IconElementName:
                        return new IconView(elem);
                }
            }
            // fallback
            return new LabelView(elem);
        }
    }

    // Custom LabelView that allows word wrap
    private static class WrapLabelView extends LabelView {
        public WrapLabelView(Element elem) {
            super(elem);
        }

        @Override
        public float getMinimumSpan(int axis) {
            return axis == View.X_AXIS ? 0 : super.getMinimumSpan(axis);
        }
    }

    // Optional: make sure the text pane wraps visually
    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }
}

