package gui.image_composers.cardTypes.itemTypes.equippableTypes.weaponTypes;

import java.awt.image.BufferedImage;

import events.CardLoadEvent;
import events.ItemImageUpdateEvent;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.cardTypes.itemTypes.equippableTypes.WeaponCardComposer;
import gui.previewpanel.OneLineTextPane;

import java.awt.*;

public class WeaponRangedCardComposer extends WeaponCardComposer{

    public WeaponRangedCardComposer(){
        super(GlobalVar.W_RANGED);
    }

    @Override
    protected void onImageUpdate(ItemImageUpdateEvent e){
        super.setField(e.type, e.path);
    }

    @Override
    protected void onLoadCard(CardLoadEvent e){
        super.onLoadCard(e);
    }

    public WeaponConfig writeToConfig(){
        return super.writeToConfig(new WeaponConfig());
    }

    public void loadFromConfig(CardConfig config) {
        super.loadFromConfig(config);
    }
    
}
