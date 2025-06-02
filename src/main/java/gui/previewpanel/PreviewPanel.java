package gui.previewpanel;
import javax.swing.*;
/*import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;*/
import javax.swing.text.View;

import events.EventBus;
import events.InfoTextUpdate;
import events.RepaintPanelEvent;
import events.CardLoadEvent;
import gui.*;
import gui.controlpanel1.FontLoader;

import java.awt.*;


public class PreviewPanel {
    public JPanel panel;
    private JPanel object;
    private CardDesignerGUI parent;
    private JLabel titleTextDisplay,rangeTextDisplay,armorClassDisplay;
    private JTextArea infoTextDisplay;
    private double panelRatio = 0.7;
    private int scaledWidth,scaledHeight;
    //private int infoFontSize=20;

    public PreviewPanel(CardDesignerGUI parent){
        this.parent = parent;
        init();
    }

    
      private void init() {
        EventBus.subscribe(RepaintPanelEvent.class, this::onRepaintEvent);
        EventBus.subscribe(CardLoadEvent.class, this::onCardLoad);
        scaledWidth = (int) (parent.getFrameScale() * (750*panelRatio));
        scaledHeight = (int) (parent.getFrameScale() * (1050*panelRatio));

        object = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                double scale = parent.getFrameScale();
                

                g.drawImage(parent.getComposedCard(scale*panelRatio), 0, 0,scaledWidth,scaledHeight, this);

                
            }
        };
    
        object.setLayout(null);
        object.setPreferredSize(new Dimension(scaledWidth, scaledHeight));



        titleTextDisplay = new JLabel("", SwingConstants.CENTER);
        titleTextDisplay.setOpaque(false);
        titleTextDisplay.setForeground(Color.GRAY);

        infoTextDisplay = new JTextArea();
        infoTextDisplay.setLineWrap(true);
        infoTextDisplay.setWrapStyleWord(true);
        infoTextDisplay.setEditable(false);
        infoTextDisplay.setFocusable(false);
        infoTextDisplay.setBorder(null);

        armorClassDisplay = new JLabel("", SwingConstants.CENTER);

        rangeTextDisplay = new JLabel("", SwingConstants.CENTER);

        armorClassDisplay.setForeground(Color.BLACK);

        infoTextDisplay.setOpaque(false);

        titleTextDisplay.setForeground(Color.WHITE);
        infoTextDisplay.setForeground(Color.WHITE);

        /*testDisplay = new JScalingTextPane(10, 20);
        testDisplay.setBounds2(65,100,300,200);
        object.add(testDisplay);*/


        object.add(titleTextDisplay);
        object.add(infoTextDisplay);
        object.add(armorClassDisplay);

        panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 1100));
        panel.add(object, BorderLayout.LINE_START);
        
        rescale(1.0);  
    }

    private void onRepaintEvent(RepaintPanelEvent e){
        this.repaint();
    }

    private void onCardLoad(CardLoadEvent e){
        titleTextDisplay.setText(e.titleText);
        titleTextDisplay.setFont(e.titleFont);
        titleTextDisplay.setForeground(e.titleColor);

        infoTextDisplay.setText(e.infoText);
        infoTextDisplay.setFont(e.infoFont);
        infoTextDisplay.setForeground(e.infoColor);
    }

    public JLabel getTitleTextDisplay() {
        return titleTextDisplay;
    }

    public JTextArea getInfoTextDisplay() {
        return infoTextDisplay;
    }

    public JLabel copyTitleTextDisplay(double scale) {
        return copyJLabel(scale,titleTextDisplay,80,40,590,80);
    }

    public JLabel copyRangeTextDisplay(double scale) {
        return copyJLabel(scale,rangeTextDisplay,120,564,110,40);
    }

    public JLabel copyArmorClassDisplay(double scale) {
        return copyJLabel(scale,armorClassDisplay,120,564,110,40);
    }

    private JLabel copyJLabel(double scale,JLabel label,int x, int y, int width, int height) {
        JLabel p = new JLabel("",SwingConstants.CENTER);
        
        p.setBounds((int)(x*scale), (int)(y*scale), (int)(width*scale), (int)(height*scale));
        p.setFont(label.getFont());
        p.setText(label.getText());

        String text = htmlToPlainText(label.getText());
        Font baseFont =  label.getFont();

        Font font = baseFont.deriveFont((float) (baseFont.getSize2D() * scale * (float)((float)height/label.getHeight())));
        Font scaledFont = getScaledFontLabel(text, font, (int)(p.getWidth()), height, p);
        
        p.setText("<html><div style='text-align:center;'>" + text + "</div></html>");
        p.setFont(scaledFont);
        
        p.setOpaque(false);
        p.setForeground(label.getForeground());
        return p;
    }

    public JTextArea copyInfoTextDisplay(double scale) {
        JTextArea p = new JTextArea();
        
        p.setBounds((int) (65*scale), (int) (655*scale), (int) (620*scale), (int) (340*scale));

        

        Font f = infoTextDisplay.getFont();
        f = f.deriveFont((float) Math.floor((float) (infoTextDisplay.getFont().getSize2D() * ((620.0* scale)/infoTextDisplay.getWidth()))));
        
        p.setFont(f);
        p.setLineWrap(true);
        p.setWrapStyleWord(true);
        p.setText(infoTextDisplay.getText());
        p.setOpaque(false);
        p.setForeground(infoTextDisplay.getForeground());
        return p;
    }

    public void updateInfoColor(Color color){
        infoTextDisplay.setForeground(color);
    }

    public void updateTitleColor(Color color){
        titleTextDisplay.setForeground(color);
    }

  

    public void updateTitleTextDisplay(String str, Font font) {
        font = FontLoader.loadFont(font.getName(), 40f);
        Font scaledFont = getScaledFontLabel(str, font, (int)(titleTextDisplay.getWidth()), Integer.MAX_VALUE, titleTextDisplay);
        
        
        titleTextDisplay.setText("<html><div style='text-align:center;'>" + str + "</div></html>");
        titleTextDisplay.setFont(scaledFont);
        titleTextDisplay.repaint();
        
    }

    public void updateRangeText(String str, Font font) {
        font = FontLoader.loadFont(font.getName(), 40f);
        Font scaledFont = getScaledFontLabel(str, font, (int)(rangeTextDisplay.getWidth()), Integer.MAX_VALUE, rangeTextDisplay);
        
        rangeTextDisplay.setText("<html><div style='text-align:center;'>" + str + "</div></html>");
        rangeTextDisplay.setFont(scaledFont);
        rangeTextDisplay.repaint();
        
    }


    public void setRangeAndACFont(String font){

        
        Font fontrange = FontLoader.loadFont(font, rangeTextDisplay.getFont().getSize());
        Font fontac = FontLoader.loadFont(font, armorClassDisplay.getFont().getSize());
        rangeTextDisplay.setFont(fontrange);
        armorClassDisplay.setFont(fontac);
    }


    public void updateInfoTextDisplay(String str1, Font font) { 
        String str = htmlToPlainText(str1);
        //int lineCount = infoTextDisplay.getLineCount()+1; 
        int lineCount = lineCounter(str1);
        
        int boxHeight = infoTextDisplay.getHeight();

        //FontMetrics fm = infoTextDisplay.getFontMetrics(font);
        //int lineHeight = fm.getHeight();
        int lineHeight = (int) font.getSize2D();
        int availableLines = (boxHeight / lineHeight)-lineCount-1;

        //System.out.println("height: "+boxHeight+" lineheight: "+lineHeight);
        //System.out.println("total lines: "+(boxHeight/lineHeight)+" used lines: "+lineCount+" available lines: "+availableLines);
        


        if(availableLines <= 1){
            while (lineHeight * lineCount > boxHeight && font.getSize() > 1) {
                lineHeight = (int) font.getSize2D();
                font = font.deriveFont((float) font.getSize() - 1);
                
            }
        }

        font = FontLoader.loadFont(font.getName(), font.getSize());
        //infoTextDisplay.setText(htmlToPlainText(str));
        //infoTextDisplay.setFont(font);
        
        EventBus.publish(new InfoTextUpdate(str));
        //testDisplay.setFont(font);
        //testDisplay.repaint();
        
        
    }

    private int lineCounter(String str){
        return str.split("<br>").length;
    }

    


    private static String htmlToPlainText(String html) {
        if (html == null) return "";
        String text = html
            .replaceAll("(?i)<br\\s*/?>", "\n")
            .replaceAll("(?i)</p>", "\n")
            .replaceAll("(?i)<li>", "• ")
            .replaceAll("(?i)</li>", "\n")
            .replaceAll("(?i)<div.*?>", "")
            .replaceAll("(?i)</div>", "\n");

        //text = text.replaceAll("<[^>]+>", "");
        text = text.replaceAll("<html>","");
        text = text.replaceAll("</html>","");

        text = text.replace("&nbsp;", " ")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&");

        return text.trim();
    }

    private Font getScaledFontLabel(String text, Font baseFont, int maxWidth, int maxHeight,JLabel label) {
        int fontSize = baseFont.getSize();
        FontMetrics metrics;
        do {
            fontSize--;
            Font tempFont = baseFont.deriveFont((float) fontSize);
            metrics = label.getFontMetrics(tempFont);
        } while (metrics.stringWidth(text) > maxWidth || metrics.getHeight() > maxHeight);

        return baseFont.deriveFont((float) fontSize);
    }

    
    
    public int[] getScaledDimensions(int imageWidth, int imageHeight, int maxWidth, int maxHeight) {
        // Compute scale factors for width and height
        double scaleX = (double) maxWidth / imageWidth;
        double scaleY = (double) maxHeight / imageHeight;
    
        // Choose the smaller scale factor to maintain aspect ratio
        double scaleFactor = Math.min(scaleX, scaleY);
    
        // Apply scaling, making sure neither dimension exceeds maxWidth or maxHeight
        int newWidth = (int) (imageWidth * scaleFactor*parent.getFrameScale());
        int newHeight = (int) (imageHeight * scaleFactor*parent.getFrameScale());
    
        // Return the scaled dimensions
        return new int[]{newWidth, newHeight};
    }

    public void repaint(){
        panel.repaint();
    }

    public void rescale(double scale){
        scaledWidth = (int) (parent.getFrameScale() * (750*panelRatio));
        scaledHeight = (int) (parent.getFrameScale() * (1050*panelRatio));

        panel.setBounds((int) (10*scale), (int) (10*scale), (int) (scaledWidth), (int) (scaledHeight));
        object.setPreferredSize(new Dimension((int) (scaledWidth), (int) (scaledHeight)));

        rescaleComponents(scale);
        panel.repaint();
    }

    public void rescaleComponents(double scale){
        titleTextDisplay.setBounds((int) (80*scale*panelRatio), (int) (20*scale*panelRatio), (int) (590*scale*panelRatio), (int) (80*scale*panelRatio));
        infoTextDisplay.setBounds((int) (65*scale*panelRatio), (int) (655*scale*panelRatio), (int) (620*scale*panelRatio), (int) (340*scale*panelRatio));
        armorClassDisplay.setBounds((int) (565*scale*panelRatio), (int) (485*scale*panelRatio), (int) (120*scale*panelRatio), (int) (120*scale*panelRatio));

    }

    public JTextArea cloneTextArea(JTextArea area) {
        JTextArea newArea = new JTextArea();
        newArea.setText(area.getText());
        newArea.setFont(area.getFont());
        newArea.setCaretPosition(area.getCaretPosition());
        newArea.setEditable(area.isEditable());
        newArea.setLineWrap(area.getLineWrap());
        newArea.setWrapStyleWord(area.getWrapStyleWord());
        if (area.getSelectedText() != null) {
            newArea.select(area.getSelectionStart(), area.getSelectionEnd());
        }
        newArea.setOpaque(false);
        return newArea;
    }


    public void loadDefault() {
        EventBus.publish(new CardLoadEvent(
            "resources/card_components/frame/default.png",
            "resources/card_components/textbox/default.png",
            "resources/card_components/background/default.png",
            "resources/card_components/title/default.png",
            "",
            "",
            "",
            null,
            null,
            Color.WHITE,
            Color.WHITE));
    }

}


