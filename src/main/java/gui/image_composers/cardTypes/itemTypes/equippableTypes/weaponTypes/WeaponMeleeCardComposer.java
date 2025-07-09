package gui.image_composers.cardTypes.itemTypes.equippableTypes.weaponTypes;

import events.CardLoadEvent;
import events.EventBus;
import events.ImageUpdate;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.cardTypes.itemTypes.equippableTypes.WeaponCardComposer;

public class WeaponMeleeCardComposer extends WeaponCardComposer{

    public WeaponMeleeCardComposer(){
        super(GlobalVar.W_MELEE);
        EventBus.subscribe(CardLoadEvent.class, this::onLoadCard);
        EventBus.subscribe(ImageUpdate.class, this::onImageUpdate);
        
    }

    protected void onLoadCard(CardLoadEvent e){
        super.loadCard(e);
    }

    protected void onImageUpdate(ImageUpdate e){
        super.setField(e.type, e.path);
    }
 
    public WeaponConfig saveConfig(){
        return super.writeToConfig(new WeaponConfig());
    }

    public void loadFromConfig(CardConfig config) {
        super.loadFromConfig(config);
    }

}
