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
    private final int maxSizeFont;
    private JTextPane textPane;

    private static final Map<String, String> ICON_MAP = new HashMap<>();

    public String getFontName(){
        return textPane.getFont().getName();
    }

    public String getText(){
        return textPane.getText();
    }

    public JScalingTextPane(int maxNumLines, int maxSizeFont) {
        loadIconsFromDirectory();
        this.maxNumLines = maxNumLines;
        this.maxSizeFont = maxSizeFont;

        EventBus.subscribe(InfoTextUpdate.class, this::onTextUpdate);
        EventBus.subscribe(InfoFontUpdate.class, this::onFontUpdate);
        EventBus.subscribe(InfoColorUpdate.class, this::onColorUpdate);

        textPane = new JTextPane();
        Font baseFont = UIManager.getFont("Label.font");
        if (baseFont == null) {
            baseFont = new Font("SansSerif", Font.PLAIN, maxSizeFont);
        }
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
    }

    public float getMaxSizeFont() {
        return (float) maxSizeFont;
    }

    private static void loadIconsFromDirectory() {
        File iconDir = new File("resources/icons/");
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
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        Font font = textPane.getFont();
        FontMetrics metrics = textPane.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
        int iconSize = (int) (lineHeight * 0.8);

        Style defaultStyle = textPane.addStyle("default", null);
        StyleConstants.setFontFamily(defaultStyle, font.getFamily());
        StyleConstants.setFontSize(defaultStyle, font.getSize());

        Style iconStyle = textPane.addStyle("icon", null);

        try {
            for (String paragraph : rawText.split("\n")) {
                StringBuilder line = new StringBuilder();
                for (String word : paragraph.split(" ")) {
                    if (word.startsWith("<") && word.endsWith(">")) {
                        String key = word.substring(1, word.length() - 1);
                        if (ICON_MAP.containsKey(key)) {
                            if (line.length() > 0) {
                                doc.insertString(doc.getLength(), line.toString(), defaultStyle);
                                line.setLength(0);
                            }
                            ImageIcon icon = new ImageIcon(ICON_MAP.get(key));
                            Image scaled = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                            StyleConstants.setIcon(iconStyle, new ImageIcon(scaled));
                            doc.insertString(doc.getLength(), " ", defaultStyle); // optional space before icon
                            doc.insertString(doc.getLength(), word, iconStyle);
                            doc.insertString(doc.getLength(), " ", defaultStyle); // optional space after icon
                            continue;
                        }
                    }
                    line.append(word).append(" ");
                }

                // Always insert a linebreak, even for empty lines
                if (line.length() > 0) {
                    doc.insertString(doc.getLength(), line.toString(), defaultStyle);
                }
                doc.insertString(doc.getLength(), "\n", defaultStyle);
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }


    public void scaleFontToFit(String text) {
        Font font = textPane.getFont().deriveFont((float) maxSizeFont);

        int lineCount = getLineCount(text);
        if(lineCount<=11){
            lineCount=11;
        }
        int paneHeight = this.getHeight();
        int lineHeight = (int) (font.getSize2D()* 1.25f);
        System.out.println("font before scale: "+font.getSize2D());
        System.out.println((lineCount * lineHeight) +" "+ paneHeight);
        

        while ((lineCount * lineHeight) > paneHeight && font.getSize() > 1) {
            font = font.deriveFont((float) font.getSize() - 1);
            lineHeight = (int) (font.getSize2D()* 1.25f);
        }
        System.out.println((lineCount * lineHeight) +" "+ paneHeight);
        System.out.println("font after scale: "+font.getSize2D());
        textPane.setFont(font);
        text = unwrapText(text);
        textPane.setText(text);
    }

    private int getLineCount(String str){
        return str.split("\n").length;
    }

    private void onFontUpdate(InfoFontUpdate e){
        textPane.setFont(FontLoader.loadFont(e.font.getName(), maxSizeFont));
        EventBus.publish(new RepaintPanelEvent());
    }

    private void onColorUpdate(InfoColorUpdate e){
        textPane.setForeground(e.color);
        EventBus.publish(new RepaintPanelEvent());
    }

    private String wrapText(String text) {
        Font font = textPane.getFont();
        int maxWidth = this.getWidth();
        FontMetrics fm = this.getFontMetrics(font);
        int safeMaxWidth = maxWidth - 5;
        StringBuilder result = new StringBuilder();

        for (String paragraph : text.split("\n")) {
            for (String word : paragraph.split(" ")) {
                int wordWidth = fm.stringWidth(word);
                if (wordWidth > safeMaxWidth) {
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
                    }
                } else {
                    result.append(word).append(" ");
                }
            }
            result.append("\n");
        }
        return result.toString().replaceAll("\\s+$", "");
    }

    private String unwrapText(String text) {
        String[] lines = text.split("\\n");
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result.append(line.trim()).append(" ");
            }
        }
        return result.toString().replaceAll("\\s+$", "");
    }

    private void onTextUpdate(InfoTextUpdate event){
        String text = wrapText(event.str);
        Font font = textPane.getFont();
        if(text == null){
            text = "";
        }
        int lineCount = getLineCount(text);
        if(lineCount > maxNumLines || font.getSize2D() > (getHeight()/maxNumLines)){
             scaleFontToFit(text);
        }
        setFormattedText(text);
        this.repaint();
        textPane.repaint();
        this.revalidate();
        textPane.revalidate();; 
        EventBus.publish(new RepaintPanelEvent());
    }

    public void setBounds2(int x, int y, int width, int height){
        this.setBounds(x,y,width,height);
        this.setPreferredSize(new Dimension(width,height));
        textPane.setBounds(0,0,width,height);
        this.repaint();
        textPane.repaint();
        this.revalidate();
        textPane.revalidate();
    }
}
