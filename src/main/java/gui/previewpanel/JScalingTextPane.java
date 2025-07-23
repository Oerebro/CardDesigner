package gui.previewpanel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;
import javax.swing.text.Style;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import gui.GlobalVar;
import gui.controlpanel1.FontLoader;
import events.ColorUpdate;
import events.EventBus;
import events.FontSizeUpdate;
import events.InfoColorUpdate;
import events.TextUpdate;
import events.RepaintPanelEvent;
import events.InfoFontUpdate;

public class JScalingTextPane extends JScrollPane {
    private final int maxNumLines;
    private int maxSizeFont, currentFontSize;
    private Font currentFont;
    public JTextPane textPane;
    private Boolean wasEmpty = true;
    String fontName;
    private int iconSize, StyleConstantsAlignement;


    private static final Map<String, String> ICON_MAP = new HashMap<>();

    public String getFontName() {
        return textPane.getFont().getName();
    }

    public String getText() {
        return textPane.getText();
    }

    public JScalingTextPane(int maxNumLines, int maxSizeFont) {
        fontName = "";
        loadIconsFromDirectory();
        this.maxNumLines = maxNumLines;
        //this.maxSizeFont = maxSizeFont;
        this.maxSizeFont = 200;
        this.currentFontSize = 19;
        

        

        textPane = new JTextPane();

        Font baseFont = UIManager.getFont("Label.font");
        if (baseFont == null) {
            baseFont = new Font("SansSerif", Font.PLAIN, maxSizeFont);
        }

        currentFont = baseFont.deriveFont((float) maxSizeFont);
        StyleConstantsAlignement = StyleConstants.ALIGN_LEFT;

        textPane.setFont(baseFont.deriveFont((float) maxSizeFont));
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
        EventBus.subscribe(InfoFontUpdate.class, this::onFontUpdate);
        EventBus.subscribe(ColorUpdate.class, this::onColorUpdate);
        
    }

    public float getMaxSizeFont() {
        return (float) maxSizeFont;
    }

    public void setColor(Color color) {
            textPane.setForeground(color);
            setFormattedText(textPane.getText());
            //EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_INFO));
    }

    private void stylesInit(){
        Font font = textPane.getFont();
        Color color = textPane.getForeground();
        Style defaultStyle = textPane.addStyle("default", null);
        StyleConstants.setFontFamily(defaultStyle, font.getFamily());
        StyleConstants.setFontSize(defaultStyle, font.getSize());
        StyleConstants.setForeground(defaultStyle, color);

        Style boldStyle = textPane.addStyle("bold", null);
        StyleConstants.setBold(boldStyle, true);
        StyleConstants.setFontFamily(boldStyle, font.getFamily());
        StyleConstants.setFontSize(boldStyle, font.getSize());
        StyleConstants.setForeground(boldStyle, color);

        Style italicStyle = textPane.addStyle("italic", null);
        StyleConstants.setItalic(italicStyle, true);
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

    public void setStyleConstantAlignement(int i){
        StyleConstantsAlignement = i;
        Style paragraphStyle = textPane.getStyle("paragraph");
        StyleConstants.setAlignment(paragraphStyle, StyleConstantsAlignement);
        EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_INFO));
    }

    private void setFormattedText(String rawText) {
        rawText = rawText.replaceAll("<->", "—").replaceAll("<\\.>", "•");
        StyledDocument doc = textPane.getStyledDocument();
         try {
            doc.remove(0, doc.getLength());

            Style paragraphStyle = textPane.getStyle("paragraph");
            doc.setParagraphAttributes(0, 1, paragraphStyle, false);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        Font font = textPane.getFont();

        FontMetrics metrics = textPane.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
        iconSize = (int) (lineHeight * 0.8);

        Style defaultStyle = textPane.getStyle("default");
        /*StyleConstants.setFontFamily(defaultStyle, font.getFamily());
        StyleConstants.setFontSize(defaultStyle, font.getSize());*/

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
                                Icon offsetIcon = new TextLineIcon(scaledIcon, (int)(lineHeight*0.15));
                                StyleConstants.setIcon(iconStyle, offsetIcon);
                                doc.insertString(doc.getLength(), "", defaultStyle);
                                doc.insertString(doc.getLength(), "<"+key+">", iconStyle);
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


    private void onFontUpdate(InfoFontUpdate e){
    
        fontName = e.fontName;
        textPane.setFont(FontLoader.loadFont(e.fontName,Font.PLAIN, currentFontSize));
        updateStylesToCurrentFont(fontName);
        EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_INFO));
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

    private void updateStylesToCurrentFont(String fontName) {
        Color color = textPane.getForeground();

        float fontSize = textPane.getFont().getSize();
        Font font = textPane.getFont();
        

        // Apply to styles
        Style defaultStyle = textPane.getStyle("default");
        StyleConstants.setFontFamily(defaultStyle, font.getFamily());
        StyleConstants.setFontSize(defaultStyle, font.getSize());
        StyleConstants.setForeground(defaultStyle, color);

        Style boldStyle = textPane.getStyle("bold");
        StyleConstants.setFontFamily(boldStyle, font.getFamily());
        StyleConstants.setFontSize(boldStyle, font.getSize());
        StyleConstants.setBold(boldStyle, true);
        StyleConstants.setForeground(boldStyle, color);

        Style italicStyle = textPane.getStyle("italic");
        StyleConstants.setFontFamily(italicStyle, font.getFamily());
        StyleConstants.setFontSize(italicStyle, font.getSize());
        StyleConstants.setItalic(italicStyle, true);
        StyleConstants.setForeground(italicStyle, color);

        Style boldItalicStyle = textPane.getStyle("bolditalic");
        StyleConstants.setFontFamily(boldItalicStyle, font.getFamily());
        StyleConstants.setFontSize(boldItalicStyle, font.getSize());
        StyleConstants.setBold(boldItalicStyle, true);
        StyleConstants.setItalic(boldItalicStyle, true);
        StyleConstants.setForeground(boldItalicStyle, color);
    }



    private void onColorUpdate(ColorUpdate e) {
        if(e.type == GlobalVar.INFO_TEXT_UPDATE){
            textPane.setForeground(e.color);
            setFormattedText(textPane.getText());
            EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_INFO));
        }
    }

    private void onTextUpdate(TextUpdate event) {
        
        if(event.type == GlobalVar.FONTSIZE_TEXT_UPDATE){
            

            if(event.text.matches("")) return;
            if(event.text.equals("+")||event.text.equals("-")){
                onFontSizeUpdate(event.text.charAt(0));
                return;
            }
            this.currentFontSize = Integer.parseInt(event.text);
            updateStylesFontSize((float) this.currentFontSize);
            setFormattedText(textPane.getText());
            EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_INFO));
            return;
        }

        if(event.type != GlobalVar.INFO_TEXT_UPDATE) return;

        boolean isNowEmpty = event.text.trim().isEmpty();
        String text = event.text;
        Font font = textPane.getFont();
        if (text == null) {
            text = "";
        }  
        setFormattedText(text);
        EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_INFO));
        if (wasEmpty != isNowEmpty) {
            EventBus.publish(new RepaintPanelEvent());
        }
        wasEmpty = isNowEmpty;
    }


    public void setBounds(int x, int y, int width, int height, double scale) {
        currentFont = textPane.getFont();
        maxSizeFont = (int) (100 * scale);
        super.setBounds(x, y, width, height);
        this.setPreferredSize(new Dimension(width, height));
        textPane.setBounds(0,0, width, height);
        textPane.setSize(width, height);
    }

    private void onFontSizeUpdate(char scaler){
        switch(scaler){
            case '+': this.currentFontSize+=1; break;
            case '-': if(this.currentFontSize>0) this.currentFontSize-=1; break;
        }

        
        updateStylesFontSize((float) this.currentFontSize);
        setFormattedText(textPane.getText());
        EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_INFO));
        EventBus.publish(new TextUpdate(GlobalVar.FONTSIZE_FIELD_UPDATE, String.valueOf(currentFontSize)));
    }

    public void scaleFont(double scale){
        float newSize = currentFontSize * ((float) scale / 0.7f);
        updateStylesFontSize(newSize);
        setFormattedText(textPane.getText());
    }


}
