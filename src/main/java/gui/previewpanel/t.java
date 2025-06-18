package gui.previewpanel;

import events.EventBus;
import events.InfoTextUpdate;
import events.RepaintPanelEvent;

public class t {
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



     public void scaleFontToFit(String text) {
       System.out.println("font rescale");

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
}
