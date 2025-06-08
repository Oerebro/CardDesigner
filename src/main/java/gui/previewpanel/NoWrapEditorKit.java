package gui.previewpanel;

import javax.swing.text.*;

public class NoWrapEditorKit extends StyledEditorKit {
    private final ViewFactory defaultFactory = new NoWrapViewFactory();

    @Override
    public ViewFactory getViewFactory() {
        return defaultFactory;
    }

    private static class NoWrapViewFactory implements ViewFactory {
        @Override
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName:
                        return new LabelView(elem) {
                            @Override
                            public float getMinimumSpan(int axis) {
                                return super.getPreferredSpan(axis);
                            }
                        };
                    case AbstractDocument.ParagraphElementName:
                        return new ParagraphView(elem) {
                            @Override
                            public void layout(int width, int height) {
                                super.layout(Integer.MAX_VALUE, height);
                            }
                        };
                    case AbstractDocument.SectionElementName:
                        return new BoxView(elem, View.Y_AXIS);
                    case StyleConstants.ComponentElementName:
                        return new ComponentView(elem);
                    case StyleConstants.IconElementName:
                        return new IconView(elem);
                }
            }
            return new LabelView(elem);
        }
    }
}
