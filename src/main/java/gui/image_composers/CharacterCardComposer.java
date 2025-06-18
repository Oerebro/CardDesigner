package gui.image_composers;

import java.awt.image.BufferedImage;
import events.EventBus;
import events.RepaintPanelEvent;
import gui.card_types.*;

public class CharacterCardComposer extends CardComposer{

    public CharacterCardComposer(){
        super(Card.CHARACTER);
    }

    @Override
    public BufferedImage composeCard(double scale){
        return super.composeCard(scale);
    }


    @Override
    protected void setField(int field, String path){
        EventBus.publish(new RepaintPanelEvent());
    }


    
}
