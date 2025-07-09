package gui.image_composers.cardTypes;

import java.awt.image.BufferedImage;
import events.EventBus;
import events.RepaintPanelEvent;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.CardComposer;

public class EffectCardComposer extends CardComposer{

    public EffectCardComposer(){
        super(GlobalVar.EFFECT);
    }

    @Override
    public BufferedImage composeCard(double scale, int type){
        return super.composeCard(scale,type);
    }


    @Override
    protected void setField(int field, String path){
        super.setField(field, path);
    }


    
}
