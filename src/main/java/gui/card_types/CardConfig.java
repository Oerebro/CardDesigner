package gui.card_types;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardConfig {
    public int type;
    public String titleText,infoText,typeText,cardFramePath, cardBackgroundPath,cardTypePath,overlayPath,runeCutPath, cardTextBoxPath, cardTitlePath, cardCrownPath;
    public boolean hasTitleBorder,hasRuneCut;
}


