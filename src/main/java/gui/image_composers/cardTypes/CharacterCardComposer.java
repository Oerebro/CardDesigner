package gui.image_composers.cardTypes;

import java.awt.image.BufferedImage;
import events.EventBus;
import events.RepaintPanelEvent;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.CardComposer;

public class CharacterCardComposer extends CardComposer{

    public CharacterCardComposer(){
        super(GlobalVar.CHARACTER);
    }

    @Override
    public BufferedImage composeCard(double scale){
        return super.composeCard(scale);
    }


    @Override
    protected void setField(int field, String path){
        super.setField(field, path);
        //EventBus.publish(new RepaintPanelEvent());
    }


    
}
