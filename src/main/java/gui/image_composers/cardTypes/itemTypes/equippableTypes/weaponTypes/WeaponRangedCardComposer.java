package gui.image_composers.cardTypes.itemTypes.equippableTypes.weaponTypes;

import events.CardLoadEvent;
import events.ImageUpdate;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.cardTypes.itemTypes.equippableTypes.WeaponCardComposer;


public class WeaponRangedCardComposer extends WeaponCardComposer{

    public WeaponRangedCardComposer(){
        super(GlobalVar.W_RANGED);
    }

    protected void onImageUpdate(ImageUpdate e){
        super.setField(e.type, e.path);
    }

    protected void loadCard(CardLoadEvent e){
        super.loadCard(e);
    }

    public WeaponConfig writeToConfig(){
        return super.writeToConfig(new WeaponConfig());
    }

    public void loadFromConfig(CardConfig config) {
        super.loadFromConfig(config);
    }
    
}
