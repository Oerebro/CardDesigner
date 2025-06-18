package gui.card_types;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import events.CardLoadEvent;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardConfig {
    public CardLoadEvent e;
    public int type;
    public String titleText,infoText,cardFrame,cardBackground;
    public boolean hasTitleBorder,hasRuneCut;
}


