package gui.previewpanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import events.EventBus;
import events.InfoColorUpdate;
import events.InfoFontUpdate;
import events.RepaintPanelEvent;
import events.TitleColorUpdate;
import events.TitleFontUpdate;
import events.TextUpdate;
import gui.GlobalVar;
import gui.Loggable;
import gui.controlpanel1.FontLoader;


public class OneLineTextPane extends JLabel {
    private int type, id;

    public static final int TITLE = 1;
    public static final int RANGE_NORMAL = 2;
    public static final int RANGE_MAX = 4;
    public static final int TYPE = 3;
    

    public OneLineTextPane(int type, int maxSizeFont,int x, int y, int width,int height) {
        this.type=type;
        this.setBounds(x,y,width,height);
        this.setSize(width,height);
        this.setLayout(null);
        //this.setOpaque(true);

        this.setText("");
        
        EventBus.subscribe(TextUpdate.class, this::onTextUpdate);
        switch(type){
            case TITLE:
                EventBus.subscribe(TitleFontUpdate.class, this::onTitleFontUpdate);
                EventBus.subscribe(TitleColorUpdate.class, this::onTitleColorUpdate);
                setVerticalAlignment(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.CENTER);
                break;
            case GlobalVar.RANGE_NORMAL_TEXT_UPDATE:
                EventBus.subscribe(TitleFontUpdate.class, this::onTitleFontUpdate);
                EventBus.subscribe(InfoColorUpdate.class, this::onInfoColorUpdate);
                setVerticalAlignment(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.LEFT);
                break;
            case GlobalVar.RANGE_MAX_TEXT_UPDATE:
                EventBus.subscribe(TitleFontUpdate.class, this::onTitleFontUpdate);
                EventBus.subscribe(InfoColorUpdate.class, this::onInfoColorUpdate);
                setVerticalAlignment(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.LEFT);
                break;
            case TYPE:
                EventBus.subscribe(TitleFontUpdate.class, this::onTitleFontUpdate);
                EventBus.subscribe(InfoColorUpdate.class, this::onInfoColorUpdate);
                setVerticalAlignment(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.LEFT);
                break;
        }     
        
    }


    private void onTitleColorUpdate(TitleColorUpdate e){
        setColor(e.color);
    }

    private void onInfoColorUpdate(InfoColorUpdate e){
        setColor(e.color);
    }

    private void setColor(Color color){
        setForeground(color);
        EventBus.publish(new RepaintPanelEvent());
    }


    

    private void onTextUpdate(TextUpdate e){
        if(e.type == this.type)
            {
                String str = e.text;
                //this.setFont(getScaledFontLabel(str, getFont(), getWidth(), getHeight(), this));
                this.setText(str.replaceAll("<->", "—").replaceAll("<\\.>", "•"));
                this.revalidate();
                this.repaint();
                EventBus.publish(new RepaintPanelEvent());
        }
    }

    private void onTitleFontUpdate(TitleFontUpdate e){
        Font font = FontLoader.loadFont(e.fontFamily, Font.BOLD,100f);
        Font scaledFont = getScaledFontLabel(this.getText(), font, this.getWidth(), this.getHeight(), this);
        setFont(scaledFont);
    }

    @Override
    public void setSize(int w, int h){
        super.setSize(w,h);
        Font scaledFont = getScaledFontLabel(this.getText(), this.getFont().deriveFont(100f), w, h, this);
        setFont(scaledFont);
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
        do {
            fontSize--;
            Font tempFont = baseFont.deriveFont((float) fontSize);
            metrics = label.getFontMetrics(tempFont);
        } while (metrics.stringWidth(text) > maxWidth-15 || metrics.getHeight() > maxHeight-1);

        int lineHeight = metrics.getHeight();
        float descentRatio = .2f;
        if(type== TITLE){
            descentRatio = .3f;
        }

        int descentOffset = (int) (lineHeight * descentRatio);

        setBorder(new EmptyBorder(descentOffset, 0, 0, 0));

        
        return baseFont.deriveFont((float) fontSize);
    }
}
