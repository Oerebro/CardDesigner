package gui.image_composers.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;

import abstractclasses.TextComponent;
import events.EventBus;
import events.FontUpdate;
import events.InfoFontUpdate;
import events.RepaintPanelEvent;
import events.TextAlignUpdate;
import events.ColorUpdate;
import events.TitleFontUpdate;
import events.VariableUpdate;
import events.TextUpdate;
import gui.GlobalVar;
import gui.controlpanel1.ColorPicker;
import gui.controlpanel1.FontLoader;


public class OneLineTextPane extends JLabel implements TextComponent {
    private int type, render;
    private String id, labelName;
    private int[] bounds;

    public static final int TITLE = 1;
    public static final int RANGE_NORMAL = 2;
    public static final int RANGE_MAX = 4;
    public static final int TYPE = 3;
    private Color fontColor;
    private boolean border = false;
    

    public OneLineTextPane(String id, String labelName, String alignement, int render, int[] bounds) {
        this.render = render;
        this.id=id;
        this.labelName = labelName;
        this.bounds = bounds;
        this.setBounds(bounds[0],bounds[1],bounds[2],bounds[3]);
        this.setSize(bounds[2],bounds[3]);
        this.setLayout(null);
        this.setOpaque(false);
        this.fontColor = Color.BLACK;
        this.setForeground(fontColor);

        this.setText("");

        switch(alignement){
            case "center": setHorizontalAlignment(SwingConstants.CENTER); break;
            case "right": setHorizontalAlignment(SwingConstants.RIGHT); break;
            default: setHorizontalAlignment(SwingConstants.LEFT); 
        }
        
        setVerticalAlignment(SwingConstants.CENTER);
        
        EventBus.subscribe(FontUpdate.class, this::onFontUpdate);
        EventBus.subscribe(TextUpdate.class, this::onTextUpdate); 
        EventBus.subscribe(ColorUpdate.class, this::onColorUpdate);
        EventBus.subscribe(TextAlignUpdate.class, this::onTextAlignUpdate);
        EventBus.subscribe(VariableUpdate.class, this::onVariableUpdate);
        
    }

    public boolean hasBorder(){
        return border;
    }

    private void onVariableUpdate(VariableUpdate e){
        if(e.id.equals(this.id)){
            switch (e.type) {
                case "borderBoolean":
                    this.border = (boolean) e.var;
                    EventBus.publish(new RepaintPanelEvent("text",this.render));
                    break;
            
                default:
                    break;
            }
        }
    }

    private void onColorUpdate(ColorUpdate e){
        if(e.id.equals(this.id)){
            fontColor = e.color;
            setColor(e.color);
        }
        EventBus.publish(new RepaintPanelEvent("text",this.render));;
    }

    private void setColor(Color color){
        fontColor = color;
        setForeground(color);    
    }

    private void onTextAlignUpdate(TextAlignUpdate e){
        if(e.id.equals(this.id)){
            this.setHorizontalAlignment(e.c);
            EventBus.publish( new RepaintPanelEvent("text",this.render));
        }
    }
    

    private void onTextUpdate(TextUpdate e){
        if(e.type == null && e.id.equals(this.id))
            {
                String str = e.text;
                //this.setFont(getScaledFontLabel(str, getFont(), getWidth(), getHeight(), this));
                this.setText(str.replaceAll("<->", "—").replaceAll("<\\.>", "•"));
                this.revalidate();
                this.repaint();
                EventBus.publish(new RepaintPanelEvent("text",render));
        }
    }


    private void onFontUpdate(FontUpdate e){
        if(e.id.equals(this.id)){
            Font font = FontLoader.loadFont(e.fontName, Font.PLAIN,100f);
            Font scaledFont = getScaledFontLabel(this.getText(), font, this.getWidth(), this.getHeight(), this);
            this.setFont(scaledFont);
            //(FontLoader.loadFont(e.fontName,Font.PLAIN, scaledFont.getSize())
            //publishRepaint();
            EventBus.publish(new RepaintPanelEvent("text",this.render));
        }
    }

    @Override
    public void setSize(int w, int h){
        super.setSize(w,h);
        Font scaledFont = getScaledFontLabel(this.getText(), this.getFont().deriveFont(200f), w, h, this);
        this.setFont(scaledFont);
    }

    public JPanel getInputComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(labelName));

        JPanel settings = new JPanel();
        settings.setLayout(new BoxLayout(settings, BoxLayout.X_AXIS));
        settings.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

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
        ColorPicker colorPicker = new ColorPicker(20,20,this.id);
        colorPicker.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        settings.add(colorPicker);

//text alignement buttons
        Dimension dim = new Dimension(20,20);
        JButton alignLeft = new IconButton("resources/glyphs/buttons/text_left.png",dim, this.id, e -> {EventBus.publish( new TextAlignUpdate(SwingConstants.LEFT,this.id));});
        JButton alignCenter = new IconButton("resources/glyphs/buttons/text_center.png",dim, this.id, e -> {EventBus.publish( new TextAlignUpdate(SwingConstants.CENTER,this.id));});
        JButton alignRight = new IconButton("resources/glyphs/buttons/text_right.png",dim, this.id, e -> {EventBus.publish( new TextAlignUpdate(SwingConstants.RIGHT,this.id));});
        
        JButton[] alignButtons = { alignLeft, alignCenter, alignRight };

        for (JButton btn : alignButtons) {
            settings.add(btn);
        }
        
        JCheckBox borderBox = new JCheckBox("Border");
        borderBox.addActionListener(e -> {
            EventBus.publish(new VariableUpdate("borderBoolean", this.id, borderBox.isSelected()));
        });

        settings.add(borderBox);

        FontSelection fontSelection = new FontSelection(this.id);
        fontSelection.setMaximumSize(new Dimension(800,30));
        settings.add(fontSelection);
        EventBus.publish(new FontUpdate(this.id,fontSelection.getSelectedItem().toString()));
        panel.add(settings);
        panel.add(input); 
        return panel;
    }

    

    private Font getScaledFontLabel(String text, Font baseFont, int maxWidth, int maxHeight, OneLineTextPane label) {
        int fontSize = baseFont.getSize();
        FontMetrics metrics;
        BufferedImage fontRenderImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
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
