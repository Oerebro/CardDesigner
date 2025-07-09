package gui.image_composers.cardTypes;

import java.awt.image.BufferedImage;
import gui.GlobalVar;
import gui.image_composers.CardComposer;

public class CharacterCardComposer extends CardComposer{

    public CharacterCardComposer(){
        super(GlobalVar.CHARACTER);
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
