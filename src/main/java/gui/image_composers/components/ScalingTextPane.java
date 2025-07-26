package gui.image_composers.components;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import abstractclasses.TextComponent;

import javax.swing.text.Style;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import gui.GlobalVar;
import gui.controlpanel1.ColorPicker;
import gui.controlpanel1.FontLoader;
import gui.previewpanel.TextLineIcon;
import events.ColorUpdate;
import events.EventBus;
import events.FontUpdate;
import events.TextUpdate;
import events.RepaintPanelEvent;
import events.TextAlignUpdate;
import events.InfoFontUpdate;

public class ScalingTextPane extends JScrollPane implements TextComponent{
    private final int maxNumLines;
    private int maxSizeFont, currentFontSize;
    private Font currentFont, fontRegular, fontItalic, fontBold;
    private Boolean wasEmpty = true;
    private JTextPane textPane = new JTextPane();
    String fontName;
    private int iconSize, StyleConstantsAlignement, render;
    private String id, labelName;


    private static final Map<String, String> ICON_MAP = new HashMap<>();

    public ScalingTextPane(String id, String labelName, int render, int[] bounds) {
        this.render = render;
        this.id = id;
        this.labelName = labelName;
        fontName = "";
        loadIconsFromDirectory();
        this.maxNumLines = 8;
        //this.maxSizeFont = maxSizeFont;
        this.maxSizeFont = 200;
        this.currentFontSize = 24;
        Font baseFont = UIManager.getFont("Label.font");
        if (baseFont == null) {
            baseFont = new Font("SansSerif", Font.PLAIN, currentFontSize);
        }

        currentFont = baseFont.deriveFont((float) currentFontSize);
        StyleConstantsAlignement = StyleConstants.ALIGN_LEFT;
//
        textPane.setFont(baseFont.deriveFont((float) currentFontSize));
        textPane.setForeground(Color.WHITE);
        textPane.setBorder(null);
        DefaultCaret caret = (DefaultCaret) textPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        this.setBorder(null);
        this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        this.setViewportView(textPane);

        this.setOpaque(false);
        this.getViewport().setOpaque(false);
        textPane.setOpaque(false);

        stylesInit();

        EventBus.subscribe(TextUpdate.class, this::onTextUpdate);
        EventBus.subscribe(FontUpdate.class, this::onFontUpdate);
        EventBus.subscribe(ColorUpdate.class, this::onColorUpdate);
        EventBus.subscribe(TextAlignUpdate.class, this::onTextAlignUpdate);
        
    }

    public String getFontName() {
        return textPane.getFont().getName();
    }

    public String getText(){
        return textPane.getText();
    }

    public void printAll(Graphics2D g){
        textPane.printAll(g);
    }

    private void onTextAlignUpdate(TextAlignUpdate e){
        if(e.id.equals(this.id)){
            setStyleConstantAlignement(e.c);
            //EventBus.publish( new RepaintPanelEvent("text",this.render));
        }
    }

    public void setStyleConstantAlignement(int i){
        StyleConstantsAlignement = i;
        Style paragraphStyle = textPane.getStyle("paragraph");
        StyleConstants.setAlignment(paragraphStyle, StyleConstantsAlignement);
        setFormattedText(textPane.getText());
        EventBus.publish(new RepaintPanelEvent("text", this.render)); 
    }

     public JPanel getInputComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(labelName));
        JPanel settings = new JPanel();
        settings.setLayout(new BoxLayout(settings, BoxLayout.X_AXIS));
        settings.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JTextArea input = new JTextArea();
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        input.setPreferredSize(new Dimension(200,500));

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
        JButton alignLeft = new IconButton("resources/glyphs/buttons/text_left.png",dim, this.id, e -> {EventBus.publish( new TextAlignUpdate(StyleConstants.ALIGN_LEFT,this.id));});
        JButton alignCenter = new IconButton("resources/glyphs/buttons/text_center.png",dim, this.id, e -> {EventBus.publish( new TextAlignUpdate(StyleConstants.ALIGN_CENTER,this.id));});
        JButton alignRight = new IconButton("resources/glyphs/buttons/text_right.png",dim, this.id, e -> {EventBus.publish( new TextAlignUpdate(StyleConstants.ALIGN_RIGHT,this.id));});
        
        JButton[] alignButtons = { alignLeft, alignCenter, alignRight };

        for (JButton btn : alignButtons) {
            settings.add(btn);
        }

        FontSelection fontSelection = new FontSelection(this.id);
        settings.add(fontSelection);
        panel.add(settings);
        panel.add(input); 
        return panel;
    }

    @Override
    public void setBounds(int x, int y, int width, int height){
        super.setBounds(x, y, width, height);
        this.setPreferredSize(new Dimension(width, height));
        textPane.setBounds(0,0, width, height);
        textPane.setSize(width, height);
    }

    public float getMaxSizeFont() {
        return (float) maxSizeFont;
    }

    public void setColor(Color color) {
            textPane.setForeground(color);
            setFormattedText(getText());
            //EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_INFO));
    }

    private void stylesInit(){
        Font font =  textPane.getFont();
        Color color =  textPane.getForeground();
        Style defaultStyle = textPane.addStyle("default", null);
        StyleConstants.setFontFamily(defaultStyle, font.getFamily());
        StyleConstants.setFontSize(defaultStyle, font.getSize());
        StyleConstants.setForeground(defaultStyle, color);

        Style boldStyle = textPane.addStyle("bold", null);
        //StyleConstants.setBold(boldStyle, false);
        StyleConstants.setFontFamily(boldStyle, font.getFamily());
        StyleConstants.setFontSize(boldStyle, font.getSize());
        StyleConstants.setForeground(boldStyle, color);

        Style italicStyle = textPane.addStyle("italic", null);
        //StyleConstants.setItalic(italicStyle, false);
        StyleConstants.setFontFamily(italicStyle, font.getFamily());
        StyleConstants.setFontSize(italicStyle, font.getSize());
        StyleConstants.setForeground(italicStyle, color);

        Style boldItalicStyle = textPane.addStyle("bolditalic", null);
        StyleConstants.setBold(boldItalicStyle, true);
        StyleConstants.setItalic(boldItalicStyle, true);
        StyleConstants.setFontFamily(boldItalicStyle, font.getFamily());
        StyleConstants.setFontSize(boldItalicStyle, font.getSize());
        StyleConstants.setForeground(boldItalicStyle, color);

        Style paragraphStyle = textPane.addStyle("paragraph", null);
        StyleConstants.setAlignment(paragraphStyle, StyleConstantsAlignement);
    }

    private static void loadIconsFromDirectory() {
        File iconDir = new File("resources/glyphs/icons/");
        if (!iconDir.exists() || !iconDir.isDirectory()) return;

        File[] files = iconDir.listFiles((dir, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".gif");
        });

        if (files != null) {
            for (File file : files) {
                String filename = file.getName();
                String key = filename.substring(0, filename.lastIndexOf('.'));
                ICON_MAP.put(key, file.getPath());
            }
        }
    }

    

    private void setFormattedText(String rawText) {
        rawText = rawText.replaceAll("<->", "—").replaceAll("<\\.>", "•");
        StyledDocument doc = textPane.getStyledDocument();

        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        Font font = textPane.getFont();
        FontMetrics metrics = getFontMetrics(font);
        int lineHeight = metrics.getHeight();
        iconSize = (int) (lineHeight * 0.8);

        Style defaultStyle = textPane.getStyle("default");
        Style paragraphStyle = textPane.getStyle("paragraph");
        Style iconStyle = textPane.addStyle("icon", null);

        try {
            for (String paragraph : rawText.split("\n")) {
                StringBuilder wordBuffer = new StringBuilder();
                boolean bold = false;
                boolean italic = false;

                int i = 0;
                while (i < paragraph.length()) {
                    char c = paragraph.charAt(i);

                    if (c == '*' && i + 1 < paragraph.length() && paragraph.charAt(i + 1) == '*') {
                        insertStyledText(doc, wordBuffer.toString(), bold, italic);
                        wordBuffer.setLength(0);
                        bold = !bold;
                        i += 2;
                    } else if (c == '*') {
                        insertStyledText(doc, wordBuffer.toString(), bold, italic);
                        wordBuffer.setLength(0);
                        italic = !italic;
                        i++;
                    } else if (c == '<') {
                        int close = paragraph.indexOf('>', i);
                        if (close > i + 1) {
                            insertStyledText(doc, wordBuffer.toString(), bold, italic);
                            wordBuffer.setLength(0);
                            String key = paragraph.substring(i + 1, close);
                            if (ICON_MAP.containsKey(key)) {
                                ImageIcon icon = new ImageIcon(ICON_MAP.get(key));
                                Image scaled = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                                ImageIcon scaledIcon = new ImageIcon(scaled);
                                Icon offsetIcon = new TextLineIcon(scaledIcon, (int) (lineHeight * 0.15));
                                StyleConstants.setIcon(iconStyle, offsetIcon);
                                doc.insertString(doc.getLength(), "", defaultStyle);
                                doc.insertString(doc.getLength(), "<" + key + ">", iconStyle);
                                doc.insertString(doc.getLength(), "", defaultStyle);
                            } else {
                                doc.insertString(doc.getLength(), "<" + key + ">", defaultStyle);
                            }
                            i = close + 1;
                        } else {
                            wordBuffer.append(c);
                            i++;
                        }
                    } else {
                        wordBuffer.append(c);
                        i++;
                    }
                }

                insertStyledText(doc, wordBuffer.toString(), bold, italic);
                doc.insertString(doc.getLength(), "\n", defaultStyle);

                // 🟡 Apply paragraph style to the just-inserted paragraph
                int paraStart = doc.getParagraphElement(doc.getLength() - 1).getStartOffset();
                doc.setParagraphAttributes(paraStart, 1, paragraphStyle, true);
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }


    private void insertStyledText(StyledDocument doc, String text, boolean bold, boolean italic) throws BadLocationException {
        if (text.isEmpty()) return;
        String styleName = bold && italic ? "bolditalic" : bold ? "bold" : italic ? "italic" : "default";
        doc.insertString(doc.getLength(), text, textPane.getStyle(styleName));
    }


    private void onFontUpdate(FontUpdate e){
        if(e.id.equals(this.id)){
            textPane.setFont(FontLoader.loadFont(e.fontName,Font.PLAIN, currentFontSize));
            fontRegular = FontLoader.loadFont(e.fontName,Font.PLAIN, currentFontSize);
            fontItalic = FontLoader.loadFont(e.fontName,Font.ITALIC, currentFontSize);
            fontBold = FontLoader.loadFont(e.fontName,Font.BOLD, currentFontSize);
            updateStylesToCurrentFont();
            setFormattedText(textPane.getText());
            EventBus.publish(new RepaintPanelEvent("text",this.render));
         }
    }

    private void updateStylesFontSize(float size){
        textPane.setFont(textPane.getFont().deriveFont(size)); 

        Style defaultStyle = textPane.getStyle("default");
        StyleConstants.setFontSize(defaultStyle, (int) size);

        Style boldStyle = textPane.getStyle("bold");
        StyleConstants.setFontSize(boldStyle, (int) size);

        Style italicStyle = textPane.getStyle("italic");
        StyleConstants.setFontSize(italicStyle, (int) size);

        Style boldItalicStyle = textPane.getStyle("bolditalic");
        StyleConstants.setFontSize(boldItalicStyle, (int) size);
    }

    private void updateStylesToCurrentFont() {
        Color color = textPane.getForeground();

        float fontSize = textPane.getFont().getSize();
        Font font = textPane.getFont();

        // Apply to styles
        Style defaultStyle = textPane.getStyle("default");
        StyleConstants.setFontFamily(defaultStyle, fontRegular.getFamily());
        StyleConstants.setFontSize(defaultStyle, font.getSize());
        StyleConstants.setForeground(defaultStyle, color);
        System.out.println("Fontname: "+fontRegular.getName());
        System.out.println("Fontfamily regular (font): "+fontRegular.getFamily());
        System.out.println("Fontfamily regular (style): "+StyleConstants.getFontFamily(defaultStyle));

        Style boldStyle = textPane.getStyle("bold");
        StyleConstants.setFontFamily(boldStyle, fontBold.getFamily());
        StyleConstants.setFontSize(boldStyle, font.getSize());
        StyleConstants.setForeground(boldStyle, color);
        System.out.println("Fontname: "+fontBold.getName());
        System.out.println("Fontfamily bold (font): "+fontBold.getFamily());
        System.out.println("Fontfamily bold (style): "+StyleConstants.getFontFamily(boldStyle));

        Style italicStyle = textPane.getStyle("italic");
        StyleConstants.setFontFamily(italicStyle, fontItalic.getFamily());
        StyleConstants.setFontSize(italicStyle, font.getSize());
        StyleConstants.setForeground(italicStyle, color);
        System.out.println("Fontname: "+fontItalic.getName());
        System.out.println("Fontfamily regular (font): "+fontItalic.getFamily());
        System.out.println("Fontfamily italic(style): "+StyleConstants.getFontFamily(italicStyle));

        Style boldItalicStyle = textPane.getStyle("bolditalic");
        StyleConstants.setFontFamily(boldItalicStyle, font.getFamily());
        StyleConstants.setFontSize(boldItalicStyle, font.getSize());
        StyleConstants.setBold(boldItalicStyle, true);
        StyleConstants.setItalic(boldItalicStyle, true);
        StyleConstants.setForeground(boldItalicStyle, color);
    }



    private void onColorUpdate(ColorUpdate e) {
        if(e.id.equals(this.id)){
            textPane.setForeground(e.color);
            setFormattedText(getText());
            EventBus.publish(new RepaintPanelEvent("text",-1)); 
        }
    }

    private void onTextUpdate(TextUpdate e) {
        
        if(e.type == GlobalVar.FONTSIZE_TEXT_UPDATE){
            if(e.text.matches("")) return;
            if(e.text.equals("+")||e.text.equals("-")){
                onFontSizeUpdate(e.text.charAt(0));
                return;
            }
            this.currentFontSize = Integer.parseInt(e.text);
            updateStylesFontSize((float) this.currentFontSize);
            setFormattedText(getText());
            EventBus.publish(new RepaintPanelEvent("text",-1)); 
            return;
        }

        if(e.id.equals(this.id)) {
            boolean isNowEmpty = e.text.trim().isEmpty();
            String text = e.text;
            Font font = textPane.getFont();
            if (text == null) {
                text = "";
            }  
            setFormattedText(text);
            EventBus.publish(new RepaintPanelEvent("text",render)); 
            wasEmpty = isNowEmpty;
        }
    }

    private void onFontSizeUpdate(char scaler){
        switch(scaler){
            case '+': this.currentFontSize+=1; break;
            case '-': if(this.currentFontSize>0) this.currentFontSize-=1; break;
        }

        
        updateStylesFontSize((float) this.currentFontSize);
        setFormattedText(getText());
        EventBus.publish(new RepaintPanelEvent("text",-1)); 
        EventBus.publish(new TextUpdate(GlobalVar.FONTSIZE_FIELD_UPDATE, String.valueOf(currentFontSize)));
    }

    public void scaleFont(double scale){
        float newSize = currentFontSize * ((float) scale / 0.7f);
        updateStylesFontSize(newSize);
        setFormattedText(getText());
    }


}