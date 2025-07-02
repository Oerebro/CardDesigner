package gui.image_composers.cardTypes.itemTypes.equippableTypes.weaponTypes;

import events.CardLoadEvent;
import events.ItemImageUpdateEvent;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.cardTypes.itemTypes.equippableTypes.WeaponCardComposer;

public class WeaponMeleeCardComposer extends WeaponCardComposer{

    public WeaponMeleeCardComposer(){
        
        super(GlobalVar.W_MELEE);
    }

    @Override
    protected void onLoadCard(CardLoadEvent e){
        super.onLoadCard(e);
    }

    @Override
    protected void onImageUpdate(ItemImageUpdateEvent e){
        super.setField(e.type, e.path);
    }
 
    public WeaponConfig saveConfig(){
        return super.writeToConfig(new WeaponConfig());
    }

    public void loadFromConfig(CardConfig config) {
        super.loadFromConfig(config);
    }

}
