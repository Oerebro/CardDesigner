package events;

import java.awt.Font;
import java.awt.Color;

public class CardLoadEvent {
    public final String frameImage, textboxImage, backgroundImage, titleImage,titleText,rangeText,infoText;
    public Font titleFont,infoFont;
    public Color titleColor, infoColor;

    public CardLoadEvent(String frameImage,String textboxImage,String backgroundImage,String titleImage,String titleText,String rangeText,String infoText,Font titleFont, Font infoFont, Color titleColor, Color infoColor) {
        this.frameImage = frameImage;
        this.textboxImage = textboxImage;
        this.backgroundImage = backgroundImage;
        this.titleImage = titleImage;

        this.titleText = titleText;
        this.rangeText = rangeText;
        this.infoText = infoText;

        this.titleFont = titleFont;
        this.infoFont = infoFont;

        this.titleColor = titleColor;
        this.infoColor = infoColor;
    }
}