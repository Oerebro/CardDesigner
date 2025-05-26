package gui;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SaveLoader {
    private class Card{
        String background;
        String frame;
        String textbox;
        String title_image;
        String cardType;
        String weaponType;
        String runeSlot;
        String attribute;

        String range;
        String title;
        String info;
    }
    
    private Card[] cardSet;
    
}
