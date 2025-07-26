package gui.image_composers.custom_styles;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

public class CustomEditorKit extends StyledEditorKit {
    private final ViewFactory factory = new CustomViewFactory();

    @Override
    public ViewFactory getViewFactory() {
        return factory;
    }
}
