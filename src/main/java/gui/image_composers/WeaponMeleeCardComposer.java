package gui.image_composers;

import events.CardLoadEvent;
import events.ItemImageUpdateEvent;
import gui.card_types.*;

public class WeaponMeleeCardComposer extends EquippableCardComposer{

    public WeaponMeleeCardComposer(){
        super(Card.W_MELEE);


    }

    @Override
    protected void onLoadCard(CardLoadEvent e){
        super.onLoadCard(e);
    }

    @Override
    protected void onImageUpdate(ItemImageUpdateEvent e){
        super.setField(e.type, e.path);
    }
 

}
