package gui.image_composers.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;

import abstractclasses.TextComponent;
import events.EventBus;
import events.InfoFontUpdate;
import events.RepaintPanelEvent;
import events.ColorUpdate;
import events.TitleFontUpdate;
import events.TextUpdate;
import gui.GlobalVar;
import gui.controlpanel1.FontLoader;


public class OneLineTextPane extends JLabel implements TextComponent {
    private int type, render;
    private String id, labelName;
    private int[] bounds;

    public static final int TITLE = 1;
    public static final int RANGE_NORMAL = 2;
    public static final int RANGE_MAX = 4;
    public static final int TYPE = 3;
    private BufferedImage fontRenderImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    

    public OneLineTextPane(String id, String labelName, int render, int[] bounds) {
        this.render = render;
        this.id=id;
        this.labelName = labelName;
        this.bounds = bounds;
        this.setBounds(bounds[0],bounds[1],bounds[2],bounds[3]);
        this.setSize(bounds[2],bounds[3]);
        this.setLayout(null);
        this.setOpaque(false);

        this.setText("");

        
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.LEFT); 
        EventBus.subscribe(TitleFontUpdate.class, this::onTitleFontUpdate);
        EventBus.subscribe(TextUpdate.class, this::onTextUpdate); 
        EventBus.subscribe(ColorUpdate.class, this::onColorUpdate);
        
    }


    private void onColorUpdate(ColorUpdate e){
        if(e.type == this.type){
            setColor(e.color);
        }
        publishRepaint();
    }

    private void setColor(Color color){
        setForeground(color);    
    }

    private void publishRepaint(){
        switch(type){
            case GlobalVar.TITLE_TEXT_UPDATE:
                EventBus.publish(new RepaintPanelEvent("text",-1));  
                break;
            case GlobalVar.RANGE_NORMAL_TEXT_UPDATE:
                EventBus.publish(new RepaintPanelEvent());
                break;
            case GlobalVar.RANGE_MAX_TEXT_UPDATE:
                EventBus.publish(new RepaintPanelEvent("text",-1)); 
                break;
            case GlobalVar.TYPE_TEXT_UPDATE:
                EventBus.publish(new RepaintPanelEvent("text",-1)); 
                break;
        }
    }

    private void publishFontRepaint(){
        EventBus.publish(new RepaintPanelEvent("text",-1)); 
    }


    

    private void onTextUpdate(TextUpdate e){
        if(e.id.equals(this.id))
            {
                String str = e.text;
                //this.setFont(getScaledFontLabel(str, getFont(), getWidth(), getHeight(), this));
                this.setText(str.replaceAll("<->", "—").replaceAll("<\\.>", "•"));
                this.revalidate();
                this.repaint();
                EventBus.publish(new RepaintPanelEvent("text",render));
        }
    }

    private void onTitleFontUpdate(TitleFontUpdate e){
        Font font = FontLoader.loadFont(e.fontFamily, Font.BOLD,100f);
        Font scaledFont = getScaledFontLabel(this.getText(), font, this.getWidth(), this.getHeight(), this);
        this.setFont(scaledFont);
        //(FontLoader.loadFont(e.fontName,Font.PLAIN, scaledFont.getSize())
        publishRepaint();
    }

    @Override
    public void setSize(int w, int h){
        super.setSize(w,h);
        Font scaledFont = getScaledFontLabel(this.getText(), this.getFont().deriveFont(100f), w, h, this);
        this.setFont(scaledFont);
    }

    private void onInfoFontUpdate(InfoFontUpdate e){
        
    }

    public JPanel getInputComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(labelName));

        JTextField input = new JTextField();
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        input.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(id, input.getText()));
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(id, input.getText()));
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(id, input.getText()));
            }
        });

        panel.add(input); 
        return panel;
    }

    private Font getScaledFontLabel(String text, Font baseFont, int maxWidth, int maxHeight, OneLineTextPane label) {
        int fontSize = baseFont.getSize();
        FontMetrics metrics;
        
        Graphics2D g2 = fontRenderImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        do {
            fontSize--;
            Font tempFont = baseFont.deriveFont((float) fontSize);
            metrics = g2.getFontMetrics(tempFont);
        } while (metrics.stringWidth(text) > maxWidth - 15 || metrics.getHeight() > maxHeight - 1);

        g2.dispose();


        int lineHeight = metrics.getHeight();
        float descentRatio = .2f;
        if(type== GlobalVar.TITLE_TEXT_UPDATE){
            descentRatio = .3f;
        }

        int descentOffset = (int) (lineHeight * descentRatio);

        setBorder(new EmptyBorder(descentOffset, 0, 0, 0));
        return baseFont.deriveFont((float) fontSize);
    }
}
