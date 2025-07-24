package gui.image_composers.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import events.EventBus;
import events.InfoFontUpdate;
import events.RepaintPanelEvent;
import events.ColorUpdate;
import events.TitleFontUpdate;
import events.TextUpdate;
import gui.GlobalVar;
import gui.controlpanel1.FontLoader;


public class OneLineTextPane extends JLabel {
    private int type;

    public static final int TITLE = 1;
    public static final int RANGE_NORMAL = 2;
    public static final int RANGE_MAX = 4;
    public static final int TYPE = 3;
    private BufferedImage fontRenderImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    

    public OneLineTextPane(int type,int[] bounds) {
        this.type=type;
        this.setBounds(bounds[0],bounds[1],bounds[2],bounds[3]);
        this.setSize(bounds[2],bounds[3]);
        this.setLayout(null);
        this.setOpaque(false);

        this.setText("");
        
        EventBus.subscribe(TextUpdate.class, this::onTextUpdate);
        switch(type){
            case GlobalVar.TITLE_TEXT_UPDATE:
                EventBus.subscribe(TitleFontUpdate.class, this::onTitleFontUpdate);     
                setVerticalAlignment(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.CENTER); 
                break;
            case GlobalVar.RANGE_NORMAL_TEXT_UPDATE:
                EventBus.subscribe(TitleFontUpdate.class, this::onTitleFontUpdate);
                setVerticalAlignment(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.LEFT); 
                break;
            case GlobalVar.RANGE_MAX_TEXT_UPDATE:
                EventBus.subscribe(TitleFontUpdate.class, this::onTitleFontUpdate);
                setVerticalAlignment(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.LEFT); 
                break;
            case GlobalVar.TYPE_TEXT_UPDATE:
                EventBus.subscribe(TitleFontUpdate.class, this::onTitleFontUpdate);
                setVerticalAlignment(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.LEFT);
                break;
            case GlobalVar.OTHER_TEXT_UPDATE_1:
                EventBus.subscribe(TitleFontUpdate.class, this::onTitleFontUpdate);
                setVerticalAlignment(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.LEFT);
                break;
            case GlobalVar.OTHER_TEXT_UPDATE_2:
                EventBus.subscribe(TitleFontUpdate.class, this::onTitleFontUpdate);
                setVerticalAlignment(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.LEFT);
                break;
        } 
           
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
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TITLE));  
                break;
            case GlobalVar.RANGE_NORMAL_TEXT_UPDATE:
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_ATTRIBUTE_LABEL));
                break;
            case GlobalVar.RANGE_MAX_TEXT_UPDATE:
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_ATTRIBUTE_LABEL));
                break;
            case GlobalVar.TYPE_TEXT_UPDATE:
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TYPE));
                break;
        }
    }

    private void publishFontRepaint(){
        EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TITLE));
        EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_ATTRIBUTE_LABEL));  
        EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_ATTRIBUTE_LABEL));
        EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TYPE));
    }


    

    private void onTextUpdate(TextUpdate e){
        if(e.type == this.type)
            {
                String str = e.text;
                //this.setFont(getScaledFontLabel(str, getFont(), getWidth(), getHeight(), this));
                this.setText(str.replaceAll("<->", "—").replaceAll("<\\.>", "•"));
                this.revalidate();
                this.repaint();
                publishRepaint();
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
