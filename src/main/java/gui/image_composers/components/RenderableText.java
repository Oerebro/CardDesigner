package gui.image_composers.components;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import abstractclasses.TextComponent;
import events.EventBus;
import events.FontUpdate;

public class RenderableText {
        public String id, labelName, font;
        public int[] bounds;
        public int render;
        public JComponent component;

        public RenderableText(String id, String labelName,int render, int[] bounds, JComponent component) {
            this.id = id;
            this.labelName = labelName;
            this.bounds = bounds;
            this.render = render;
            this.component = component;
            EventBus.subscribe(FontUpdate.class, this::onFontUpdate);
        }

        public JPanel getInputComponent() {
            if (component instanceof TextComponent) {
                return ((TextComponent) component).getInputComponent();
            } else {
                throw new IllegalStateException("Component does not implement InputComponentProvider");
            }
        }

        public void onFontUpdate(FontUpdate e){
            if(this.id.equals(e.id)){
                this.font=e.fontName;
            }
        }

        public String getText(){
            if(component instanceof JTextComponent){
                return ((JTextComponent) component).getText();
            }
            return null;
        }
}