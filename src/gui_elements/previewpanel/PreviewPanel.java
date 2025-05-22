package gui_elements.previewpanel;
import javax.swing.*;

import gui_elements.CardDesignerGUI;
import gui_elements.controlpanel1.FontLoader;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class PreviewPanel {
    public JPanel panel;
    private JPanel object;
    private CardDesignerGUI parent;
    private JLabel titleTextDisplay;
    private JTextArea infoTextDisplay;
    private double panelRatio = 0.7;
    private int scaledWidth,scaledHeight;
    private int infoFontSize=20;

    public PreviewPanel(CardDesignerGUI parent){
        this.parent = parent;
        init();
    }

    public JLabel getTitleTextDisplay() {
        return titleTextDisplay;
    }

    public JTextArea getInfoTextDisplay() {
        return infoTextDisplay;
    }

    public JLabel copyTitleTextDisplay(double scale) {
        JLabel p = new JLabel("",SwingConstants.CENTER);
        
        p.setBounds((int)(80*scale), (int)(40*scale), (int)(590*scale), (int)(80*scale));
        p.setFont(titleTextDisplay.getFont());
        p.setText(titleTextDisplay.getText());

        String text = htmlToPlainText(titleTextDisplay.getText());
        Font baseFont =  titleTextDisplay.getFont();

        Font font = baseFont.deriveFont((float) (baseFont.getSize2D() * scale * (float)(80.0/titleTextDisplay.getHeight())));
        Font scaledFont = getScaledFontLabel(text, font, (int)(p.getWidth()), 80, p);
        
        p.setText("<html><div style='text-align:center;'>" + text + "</div></html>");
        p.setFont(scaledFont);
        
        p.setOpaque(false);
        p.setForeground(titleTextDisplay.getForeground());
        return p;
    }

    public JTextArea copyInfoTextDisplay(double scale) {
        JTextArea p = new JTextArea();
        
        p.setBounds((int) (65*scale), (int) (655*scale), (int) (620*scale), (int) (340*scale));

        Font f = infoTextDisplay.getFont();
        f = f.deriveFont((float) (infoTextDisplay.getFont().getSize2D() * scale * (620.0/infoTextDisplay.getWidth())));
        
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

    private void init() {
        scaledWidth = (int) (parent.getFrameScale() * (750*panelRatio));
        scaledHeight = (int) (parent.getFrameScale() * (1050*panelRatio));

        object = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                double scale = parent.getFrameScale();
                

                BufferedImage bg = parent.getCardBackground();
                BufferedImage tb = parent.getCardTextbox();
                BufferedImage ct = parent.getCardType();
                BufferedImage cf = parent.getCardFrame();
                BufferedImage ii = parent.getCardItemImage();
                BufferedImage ti = parent.getTitleImage();
                BufferedImage hi = parent.getCardHandedImage();

                if (bg != null) {
                    g.drawImage(bg, 0, 0, scaledWidth, scaledHeight, this);
                }
    
                if (cf != null) {
                    g.drawImage(cf, 0, 0, scaledWidth, scaledHeight, this);
                }

                /*try{
                    g.drawImage(ImageIO.read(new File("resources/misc/frontborder.png")), 0, 0, scaledWidth, scaledHeight, this);
                }catch(IOException e){};*/

                if (ii != null) {
                    g.drawImage(ii, (int)(40*scale*panelRatio), (int)(120*scale*panelRatio), (int)(660*scale*panelRatio), (int)(660*scale*panelRatio), this);
                }
    
                if (tb != null) {
                    g.drawImage(tb, (int) (0),(int) (440*scale*panelRatio), (int) (750*scale*panelRatio), (int) (610*scale*panelRatio), this);
                }
    
                if (ct != null) {
                    g.drawImage(ct, (int) (550*scale*panelRatio), (int)(500*scale*panelRatio), (int) (180*scale*panelRatio), (int) (180*scale*panelRatio), this);
                }

                if (ti != null) {
                    g.drawImage(ti, 0, (int) (10*scale*panelRatio), scaledWidth, scaledHeight, this);
                } 
                
                if (hi != null) {
                    g.drawImage(hi, 498, 650, (int)(60*scale*panelRatio), (int)(60*scale*panelRatio), this);
                } 
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

        infoTextDisplay.setOpaque(false);

        titleTextDisplay.setForeground(Color.WHITE);
        infoTextDisplay.setForeground(Color.WHITE);

        object.add(titleTextDisplay);
        object.add(infoTextDisplay);

        panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 1100));
        panel.add(object, BorderLayout.LINE_START);
        
        rescale(1.0);  
        System.out.println(infoTextDisplay.getBounds());
    }

    public void updateTitleTextDisplay(String str, Font font) {
        font = FontLoader.loadFont(font.getName(), 40f);
        Font scaledFont = getScaledFontLabel(str, font, (int)(titleTextDisplay.getWidth()), Integer.MAX_VALUE, titleTextDisplay);
        
        titleTextDisplay.setText("<html><div style='text-align:center;'>" + str + "</div></html>");
        titleTextDisplay.setFont(scaledFont);
        titleTextDisplay.repaint();
        
    }


    public void updateInfoTextDisplay(String str1, Font font) { 
        String str = htmlToPlainText(str1);
        int lineCount = getLineCount(str); 
        int lineHeight = infoTextDisplay.getFont().getSize();
        int availableLines = (infoTextDisplay.getHeight() / lineHeight)-1;
        System.out.println("lineCount: "+lineCount+" availableLines: "+availableLines+" lineHeight: "+lineHeight);

        if(availableLines < lineCount){
            double scaleFactor = (lineCount > 0) ? (9.0 / lineCount) : 1.0; 
            infoFontSize *= scaleFactor;
        }

        font = FontLoader.loadFont(font.getName(), (int)(infoFontSize));
        infoTextDisplay.setFont(font);
        infoTextDisplay.setText(htmlToPlainText(str));
        infoTextDisplay.repaint();
        
    }

    private int getLineCount(String str) {
        System.out.println(str);
        String[] lines = str.split("\n");
        System.out.println(Arrays.toString(lines));
        if (lines.length <= 10) {
            return 9;
        }
        return lines.length;
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

        text = text.replaceAll("<[^>]+>", "");

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

    public void loadDefault() {
        try{
            parent.setCardFrame(ImageIO.read(new File("resources/frame/default.png")));
            parent.setCardTitleImage(ImageIO.read(new File("resources/title/default.png")));
            parent.setCardTextbox(ImageIO.read(new File("resources/textbox/default.png")));
            parent.setCardBackground(ImageIO.read(new File("resources/background/default.png")));
        }catch(IOException e){
            JOptionPane.showMessageDialog(parent.getFrame(), "Error loading default images.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadImage(String type) {

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(parent.getFrame());
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    BufferedImage image = ImageIO.read(file);
                    switch (type) {
                        case "frame":
                            break;
                        case "background":
                            parent.setCardBackground(image);

                            break;
                        case "textbox":
                            parent.setCardTextbox(image);
                            break;
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(parent.getFrame(), "Error loading image.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
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

    //scaling for the previewPanel images works
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
        infoTextDisplay.setPreferredSize(new Dimension((int) (620*scale*panelRatio), (int) (340*scale*panelRatio)));

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

}


