package gui.image_composers.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;
import javax.swing.text.View;

import abstractclasses.TextComponent;

import javax.swing.text.Style;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import gui.GlobalVar;
import gui.controlpanel1.ColorPicker;
import gui.controlpanel1.FontLoader;
import gui.image_composers.custom_styles.CustomEditorKit;
import gui.image_composers.custom_styles.CustomParagraphView;
import gui.previewpanel.TextLineIcon;
import events.ColorUpdate;
import events.EventBus;
import events.FontUpdate;
import events.TextUpdate;
import events.VariableUpdate;
import events.RepaintPanelEvent;
import events.TextAlignUpdate;
import events.InfoFontUpdate;

public class ScalingTextPane extends JScrollPane implements TextComponent{
    private final int maxNumLines;
    private int maxSizeFont, currentFontSize,minLineCount;
    private Font currentFont, fontRegular, fontItalic, fontBold;
    private Boolean border = false;
    //private JTextPane textPane = new WrappingTextPane();
    private JTextPane textPane;
    String fontName;
    private int iconSize, StyleConstantsAlignement, render;
    private String id, labelName, text;
    private SimpleAttributeSet defaultStyle,boldStyle,italicStyle;
    private Color fontColor;

    private static final Map<String, String> ICON_MAP = new HashMap<>();

    public ScalingTextPane(String id, String labelName, String alignement, int render, int[] bounds, int minLineCount) {
        this.render = render;
        
        this.id = id;
        this.labelName = labelName;
        fontName = "";
        loadIconsFromDirectory();
        this.maxNumLines = 8;
        text = "";

        //settings for minimum line count, dynamically scales font
        this.maxSizeFont = 100;
        this.currentFontSize = 24;
        this.minLineCount = minLineCount;

        fontColor = Color.BLACK;


        Font baseFont = UIManager.getFont("Label.font");
        if (baseFont == null) {
            baseFont = new Font("SansSerif", Font.PLAIN, currentFontSize);
        }

        defaultStyle = new SimpleAttributeSet();
        boldStyle = new SimpleAttributeSet();
        italicStyle = new SimpleAttributeSet();

        currentFont = baseFont.deriveFont((float) currentFontSize);

        switch(alignement){
            case "center": StyleConstantsAlignement = StyleConstants.ALIGN_CENTER; break;
            case "right": StyleConstantsAlignement = StyleConstants.ALIGN_RIGHT; break;
            default: StyleConstantsAlignement = StyleConstants.ALIGN_LEFT; break;
        }
        

        fontRegular = currentFont;
        fontBold = currentFont;
        fontItalic = currentFont;

        //experimental custom editor
        textPane = new WrappingTextPane();
        textPane.setEditorKit(new CustomEditorKit());
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
        EventBus.subscribe(VariableUpdate.class, this::onVariableUpdate);
        
    }

    public String getFontName() {
        return textPane.getFont().getName();
    }

    public boolean hasBorder(){
        return border;
    }

    public String getText(){
        return this.text;
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
        setFormattedText(this.text);
        SwingUtilities.invokeLater(() -> EventBus.publish(new RepaintPanelEvent("text", this.render)));
        //EventBus.publish(new RepaintPanelEvent("text", this.render)); 
    }

     public JPanel getInputComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(labelName));
        JPanel settings = new JPanel();
        settings.setLayout(new BoxLayout(settings, BoxLayout.X_AXIS));
        settings.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        //settings.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JTextArea input = new JTextArea();
        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
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
        ColorPicker colorPicker = new ColorPicker(Color.WHITE,20,20,this.id);
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

        JCheckBox borderBox = new JCheckBox("Border");
        borderBox.addActionListener(e -> {
            EventBus.publish(new VariableUpdate("borderBoolean", this.id, borderBox.isSelected()));
        });

        settings.add(borderBox);

        FontSelection fontSelection = new FontSelection(this.id);
        fontSelection.setSelectedItem("PlantinMTPro");
        fontSelection.setMaximumSize(new Dimension(800,30));
        settings.add(fontSelection);
        panel.add(settings);
        panel.add(input); 
        EventBus.publish(new FontUpdate(this.id,fontSelection.getSelectedItem().toString()));
        return panel;
    }

    public void printAll(Graphics g){

        textPane.printAll(g);
    }

    @Override
    public void setSize(int w, int h){
        textPane.setSize(w,h);
        
        scaleFont();
        setFormattedText(text);
        textPane.revalidate();
        textPane.repaint(); 
        
    }


    public void scaleFont(){
        float newSize = getScaledFont(textPane.getFont());
        currentFontSize = (int) newSize;
        updateStylesFontSize(newSize);
        
        
    }

    private float getScaledFont(Font font) {
        int height = textPane.getHeight();
        int size = currentFontSize;
        int lineCount = Math.max(minLineCount, getVisualLineCount(textPane));
        Font testFont = font.deriveFont((float)size);

        FontMetrics fm = textPane.getFontMetrics(testFont);
        int lineHeight = fm.getHeight();


        if(lineHeight * lineCount < height){
                size = maxSizeFont;
                testFont = font.deriveFont((float)size);
                fm = textPane.getFontMetrics(testFont);
                lineHeight = fm.getHeight();
        }

        while(lineHeight * lineCount > height && size > 1){
            size--;
            testFont = testFont.deriveFont((float)size);
            fm = textPane.getFontMetrics(testFont);
            lineHeight = fm.getHeight();
        
        }
        textPane.setFont(currentFont);
        return (float)size;
    }



    public int getVisualLineCount(JTextPane textPane) {
        View root = textPane.getUI().getRootView(textPane);
       // View section = root.getView(0);
        int totalLines = 0 ;
        int test1 = 0;

        for (int i = 0; i < root.getViewCount(); i++) {
            View paragraph = root.getView(i);
            if (paragraph instanceof CustomParagraphView) {
                totalLines += ((CustomParagraphView) paragraph).getVisualLineCount();
                test1++;
            } else {
                totalLines += paragraph.getViewCount();
                test1++;
            }
        }

        return totalLines-1;
    }



    public float getMaxSizeFont() {
        return (float) maxSizeFont;
    }

    public void setColor(Color color) {
            fontColor = color;
            textPane.setForeground(color);
            setFormattedText(getText());
            //EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_INFO));
    }

    private void stylesInit(){
        defaultStyle.addAttribute("fontInstance", textPane.getFont());
        StyleConstants.setForeground(defaultStyle, fontColor);
        StyleConstants.setFontSize(defaultStyle, currentFontSize);

        boldStyle.addAttribute("fontInstance", textPane.getFont());
        StyleConstants.setForeground(boldStyle, fontColor);
        StyleConstants.setFontSize(boldStyle, currentFontSize);

        italicStyle.addAttribute("fontInstance", textPane.getFont());
        StyleConstants.setForeground(italicStyle, fontColor);
        StyleConstants.setFontSize(italicStyle, currentFontSize);

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
            textPane.revalidate();
            textPane.repaint();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        
    }


    private void insertStyledText(StyledDocument doc, String text, boolean bold, boolean italic) throws BadLocationException {
        if (text.isEmpty()) return;
        String styleName = bold && italic ? "bolditalic" : bold ? "bold" : italic ? "italic" : "default";
        doc.insertString(doc.getLength(), text, getStyle(styleName));
    }
    
    private SimpleAttributeSet getStyle(String name){
        switch(name){
            case "bold":
                return boldStyle;
            case "italic":
                return italicStyle;
            default:
                return defaultStyle;
        }
    }


    private void onFontUpdate(FontUpdate e){
        if(e.id.equals(this.id)){
            //textPane.setFont(FontLoader.loadFont(e.fontName,Font.PLAIN, currentFontSize)); 
            fontRegular = FontLoader.loadFont(e.fontName,Font.PLAIN, currentFontSize);
            fontItalic = FontLoader.loadFont(e.fontName,Font.ITALIC, currentFontSize);
            fontBold = FontLoader.loadFont(e.fontName,Font.BOLD, currentFontSize);
            updateStylesToCurrentFont();
            //scaleFont();
            setFormattedText(this.text);
            
            SwingUtilities.invokeLater(() -> EventBus.publish(new RepaintPanelEvent("text", this.render)));
         }
    }

    private void updateStylesToCurrentFont() {


        float fontSize = textPane.getFont().getSize();
        Font font = textPane.getFont();

        // Apply to styles
        defaultStyle.addAttribute("fontInstance", fontRegular);
        StyleConstants.setFontSize(defaultStyle, fontRegular.getSize());
        StyleConstants.setForeground(defaultStyle, fontColor);

        boldStyle.addAttribute("fontInstance", fontBold);
        StyleConstants.setFontSize(boldStyle, font.getSize());
        StyleConstants.setForeground(boldStyle, fontColor);;

        italicStyle.addAttribute("fontInstance", fontItalic);
        StyleConstants.setFontSize(italicStyle, font.getSize());
        StyleConstants.setForeground(italicStyle, fontColor);
    }


    private void updateStylesFontSize(float size){
        if(fontRegular == null || fontBold == null || fontItalic == null) return;
        textPane.setFont(textPane.getFont().deriveFont(size)); 
        fontRegular = fontRegular.deriveFont((float)size);
        fontBold = fontBold.deriveFont((float)size);
        fontItalic = fontItalic.deriveFont((float)size);

        defaultStyle.addAttribute("fontInstance", fontRegular);
        boldStyle.addAttribute("fontInstance", fontBold);
        italicStyle.addAttribute("fontInstance", fontItalic);

        StyleConstants.setFontSize(defaultStyle, (int) size);
        StyleConstants.setFontSize(boldStyle, (int) size);
        StyleConstants.setFontSize(italicStyle, (int) size);
    }

    private void onColorUpdate(ColorUpdate e) {

        if(e.id.equals(this.id)){
            textPane.setForeground(e.color);
            fontColor = e.color;
            updateStylesToCurrentFont();
            setFormattedText(getText());
            EventBus.publish(new RepaintPanelEvent("text",this.render)); 
        }
    }

    private void onTextUpdate(TextUpdate e) {
        
        /*if(e.type == GlobalVar.FONTSIZE_TEXT_UPDATE){
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
        }*/

        if(e.id.equals(this.id)) {
            String txt = e.text;
            if (txt == null) {
                txt = "";
            }  

            this.text = txt;
            scaleFont();
            setFormattedText(text);
            
            EventBus.publish(new RepaintPanelEvent("text",render)); 

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


}