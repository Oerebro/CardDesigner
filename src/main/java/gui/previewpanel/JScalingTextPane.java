package gui.previewpanel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.Style;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import gui.controlpanel1.FontLoader;
import events.EventBus;
import events.InfoColorUpdate;
import events.InfoTextUpdate;
import events.RepaintPanelEvent;
import events.InfoFontUpdate;

public class JScalingTextPane extends JScrollPane {
    private final int maxNumLines;
    private int maxSizeFont;
    private Font currentFont;
    private JTextPane textPane;
    String fontFamily;
    private int iconSize;

    private static final Map<String, String> ICON_MAP = new HashMap<>();

    public String getFontName() {
        return textPane.getFont().getName();
    }

    public String getText() {
        return textPane.getText();
    }

    public JScalingTextPane(int maxNumLines, int maxSizeFont) {
        fontFamily = "";
        loadIconsFromDirectory();
        this.maxNumLines = maxNumLines;
        //this.maxSizeFont = maxSizeFont;
        this.maxSizeFont = 200;

        

        textPane = new JTextPane();
        Font baseFont = UIManager.getFont("Label.font");
        if (baseFont == null) {
            baseFont = new Font("SansSerif", Font.PLAIN, maxSizeFont);
        }

        currentFont = baseFont.deriveFont((float) maxSizeFont);

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

        EventBus.subscribe(InfoTextUpdate.class, this::onTextUpdate);
        EventBus.subscribe(InfoFontUpdate.class, this::onFontUpdate);
        EventBus.subscribe(InfoColorUpdate.class, this::onColorUpdate);
        
    }

    public float getMaxSizeFont() {
        return (float) maxSizeFont;
    }

    public void setColor(Color color) {
        StyleConstants.setForeground(textPane.getStyle("default"), color);
        EventBus.publish(new RepaintPanelEvent());
    }

    private void stylesInit(){
        Font font = textPane.getFont();
        Color color = textPane.getForeground();
        Style defaultStyle = textPane.addStyle("default", null);
        StyleConstants.setFontFamily(defaultStyle, font.getFamily());
        StyleConstants.setFontSize(defaultStyle, font.getSize());
        StyleConstants.setForeground(defaultStyle, color);

        Style boldStyle = textPane.addStyle("bold", null);
        StyleConstants.setBold(boldStyle, false);
        StyleConstants.setFontFamily(boldStyle, font.getFamily());
        StyleConstants.setFontSize(boldStyle, font.getSize());
        StyleConstants.setForeground(boldStyle, color);

        Style italicStyle = textPane.addStyle("italic", null);
        StyleConstants.setItalic(italicStyle, false);
        StyleConstants.setFontFamily(italicStyle, font.getFamily());
        StyleConstants.setFontSize(italicStyle, font.getSize());
        StyleConstants.setForeground(italicStyle, color);

        Style boldItalicStyle = textPane.addStyle("bolditalic", null);
        StyleConstants.setBold(boldItalicStyle, false);
        StyleConstants.setItalic(boldItalicStyle, true);
        StyleConstants.setFontFamily(boldItalicStyle, font.getFamily());
        StyleConstants.setFontSize(boldItalicStyle, font.getSize());
        StyleConstants.setForeground(boldItalicStyle, color);
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
        System.out.println("format text");
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
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
                                doc.insertString(doc.getLength(), key, iconStyle);
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

    public void scaleFontToFit(String text) {
        int lineCount = getLineCount(text);
        if (lineCount <= 8) {
            lineCount = 8;
        }
        int paneHeight = this.getHeight();
        Font font = textPane.getFont().deriveFont((float) maxSizeFont);
        int lineHeight = (int) (font.getSize2D() * 1.25f);

        while ((lineCount * lineHeight) > paneHeight && font.getSize() > 1) {
            font = font.deriveFont((float) font.getSize() - 1);
            lineHeight = (int) (font.getSize2D() * 1.25f);
        }

        currentFont = font.deriveFont((float) font.getSize());
        textPane.setFont(font);
        text = unwrapText(text);
        
        textPane.setText(text);
        updateStylesFontSize(font.getSize());;
    }

    public void scaleFont(){
        scaleFontToFit(textPane.getText());
        //setFormattedText(textPane.getText());
    }

    private int getLineCount(String str) {

        return str.split("\n").length;
    }

    private void onFontUpdate(InfoFontUpdate e){
        
        fontFamily = e.fontFamily;
        textPane.setFont(FontLoader.loadFont(e.fontFamily,Font.PLAIN, maxSizeFont));
        updateStylesToCurrentFont(fontFamily);
        EventBus.publish(new RepaintPanelEvent());
    }

    private void updateStylesFontSize(float size){
        Style defaultStyle = textPane.getStyle("default");
        StyleConstants.setFontSize(defaultStyle, (int) size);

        Style boldStyle = textPane.getStyle("bold");
        StyleConstants.setFontSize(boldStyle, (int) size);

        Style italicStyle = textPane.getStyle("italic");
        StyleConstants.setFontSize(italicStyle, (int) size);

        Style boldItalicStyle = textPane.getStyle("bolditalic");
        StyleConstants.setFontSize(boldItalicStyle, (int) size);
    }

    private void updateStylesToCurrentFont(String fontFamily) {
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



    private void onColorUpdate(InfoColorUpdate e) {
        textPane.setForeground(e.color);
        EventBus.publish(new RepaintPanelEvent());
    }

    private String wrapText(String text) {
        Font font = currentFont;
        //int maxWidth = this.getWidth();
        FontMetrics fm = this.getFontMetrics(font);
        int safeMaxWidth = textPane.getWidth();
        StringBuilder result = new StringBuilder();

        for (String paragraph : text.split("\n")) {
            int lineWidth = 0;

            for (String word : paragraph.split(" ")) {

                int wordWidth;
                //check if word is an icon
                if (word.startsWith("<") && word.matches(".*>[\\.,!?:]*$")) {
                    int iconCount = word.split(",", -1).length + word.split("><", -1).length-1;
                    wordWidth = iconSize * iconCount;
                } else {
                    wordWidth = fm.stringWidth(word + " ");
                }

                if (wordWidth > safeMaxWidth) {
                    // Break long word into parts
                    StringBuilder wordBuilder = new StringBuilder();
                    for (char c : word.toCharArray()) {
                        if (fm.stringWidth(wordBuilder.toString() + c) > safeMaxWidth) {
                            result.append(wordBuilder).append("\n");
                            wordBuilder = new StringBuilder();
                        }
                        wordBuilder.append(c);
                    }
                    if (wordBuilder.length() > 0) {
                        result.append(wordBuilder).append(" ");
                        lineWidth = fm.stringWidth(wordBuilder + " ");
                    } else {
                        lineWidth = 0;
                    }
                } else {
                    //check if line is too long
                    if (lineWidth + wordWidth > safeMaxWidth) {
                        result.append("\n");
                        lineWidth = 0;
                    }
                    result.append(word).append(" ");
                    lineWidth += wordWidth;
                }
            }
            result.append("\n");
        }
        return result.toString().replaceAll("\\s+$", "");
    }

    private String unwrapText(String text) {
        String[] lines = text.split("\n");
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result.append(line.trim()).append(" ");
            }
        }
        return result.toString().replaceAll("\\s+$", "");
    }

     private void onTextUpdate(InfoTextUpdate event) {
       
        String text = event.str;
        text = wrapText(text);


        
        Font font = textPane.getFont();
        if (text == null) {
            text = "";
        }
        
        int lineCount = getLineCount(text);
        if (lineCount > maxNumLines || font.getSize2D() > (getHeight() / maxNumLines)) {
            scaleFontToFit(text);
        }
        setFormattedText(text);
        /*this.repaint();
        textPane.repaint();
        this.revalidate();
        textPane.revalidate();*/
        EventBus.publish(new RepaintPanelEvent());
    }


    public void setBounds(int x, int y, int width, int height, double scale) {
        //maxSizeFont = (int) (100 * scale);
        super.setBounds(x, y, width, height);
        this.setPreferredSize(new Dimension(width, height));
        textPane.setBounds(0,0, width, height);
        textPane.setSize(width, height);
    }
}
