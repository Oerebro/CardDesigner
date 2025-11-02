package gui.image_composers.components;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import abstractclasses.TextComponent;
import events.ColorUpdate;
import events.EventBus;
import events.FontUpdate;
import events.TextAlignUpdate;
import events.VariableUpdate;

public class RenderableText {
        public String id, labelName, font;
        public int[] bounds;
        public int render, alignement;
        public JComponent component;
        public boolean hasBorder = true;
        public Color color;

        public RenderableText(String id, String labelName,int render, int[] bounds, JComponent component) {
            this.id = id;
            this.labelName = labelName;
            this.bounds = bounds;
            this.render = render;
            this.component = component;
            EventBus.subscribe(FontUpdate.class, this::onFontUpdate);
            EventBus.subscribe(TextAlignUpdate.class, this::onAlignementUpdate);
            EventBus.subscribe(ColorUpdate.class, this::onColorUpdate);
            EventBus.subscribe(VariableUpdate.class, this::onBorderUpdate);
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

        public void onAlignementUpdate(TextAlignUpdate e){
            if(this.id.equals(e.id)){
                this.alignement=e.c;
            }
        }

        public void onBorderUpdate(VariableUpdate e){
            if(this.id.equals(e.id) && e.type.equals("borderBoolean")){
                this.hasBorder= (boolean) e.var;
            }
        }

        public void onColorUpdate(ColorUpdate e){
            if(this.id.equals(e.id)){
                this.color= e.color;
            }
        }

        public String getText(){
            if(component instanceof JTextComponent){
                return ((JTextComponent) component).getText();
            }
            return null;
        }
}