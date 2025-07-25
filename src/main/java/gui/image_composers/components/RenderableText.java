package gui.image_composers.components;

import javax.swing.JComponent;
import javax.swing.JPanel;

import abstractclasses.InputComponentProvider;

public class RenderableText {
        public String id, labelName;
        public int[] bounds;
        public int render;
        public JComponent component;

        public RenderableText(String id, String labelName,int render, int[] bounds, JComponent component) {
            this.id = id;
            this.labelName = labelName;
            this.bounds = bounds;
            this.render = render;
            this.component = component;
        }

        public JPanel getInputComponent() {
            if (component instanceof InputComponentProvider) {
                return ((InputComponentProvider) component).getInputComponent();
            } else {
                throw new IllegalStateException("Component does not implement InputComponentProvider");
            }
        }
}