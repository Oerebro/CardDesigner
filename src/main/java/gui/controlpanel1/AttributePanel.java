package gui.controlpanel1;
import javax.swing.*;
import javax.swing.event.DocumentListener;

import events.EventBus;
import events.ItemImageUpdateEvent;
import events.RuneChargesUpdate;
import events.TierUpdate;
import events.CardTypeUpdate;
import gui.*;
import gui.card_types.*;

import java.awt.*;
public class AttributePanel extends JPanel{
    private String type;
    JTextField armorClassInput;
    //private Font rangeAndACFont;

    //Rune Types
    JCheckBox melee,ranged,mixed,armor;

    //weapon type
    JCheckBox isMelee,isRanged, isThrowable;

    JCheckBox d4,d6,d8,d10,d12;

    //runeslots
    JCheckBox none_slot,one_slot,two_slot,three_slot;

    JCheckBox tier0,tier1,tier2,tier3,tier4;

    public AttributePanel(CardDesignerGUI parent, String type) {
        this.setLayout(new GridLayout(0, 1, 5, 5));
        this.type = type;
        
        switch(type){
            case "weapon": createWeaponPanel();break;
            case "armor": createArmorPanel();break;
            case "consumable": createConsumablePanel();break;
            case "rune": createRunePanel();break;
            case "other": createOtherPanel();break;
        }
    }

    public String getType(){
        return type;
    }

    public int getDice(){
        if(d6.isSelected())
            return 6;
        if(d8.isSelected())
            return 8;
        if(d10.isSelected())
            return 10;
        if(d12.isSelected())
            return 12;

        return 4;  
    }

    public int getTier(){
        if(tier1.isSelected())
            return 1;
        if(tier2.isSelected())
            return 2;
        if(tier3.isSelected())
            return 3;
        if(tier4.isSelected())
            return 4;

        return 0;  
    }

    public int getRuneSlots(){
        if(one_slot.isSelected())
            return 1;
        if(two_slot.isSelected())
            return 2;
        if(three_slot.isSelected())
            return 3;

        return 0;  
    }

    public int getArmorClass(){
        return Integer.parseInt(armorClassInput.getText());
    }

    private void createArmorPanel(){
        armorClassInput = new JTextField();
        armorClassInput.setBorder(BorderFactory.createTitledBorder("Armor Class"));

        armorClassInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                if(armorClassInput.getText().matches("")){
                    getArmorClassImage(0);
                    return;
                }
                getArmorClassImage(Integer.parseInt(armorClassInput.getText()));
            }
    
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                if(armorClassInput.getText().matches("")){
                    getArmorClassImage(0);
                    return;
                }
                getArmorClassImage(Integer.parseInt(armorClassInput.getText()));
            }
    
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                if(armorClassInput.getText().matches("")){
                    getArmorClassImage(0);
                    return;
                }
                getArmorClassImage(Integer.parseInt(armorClassInput.getText()));
            }
        });

        this.add(armorClassInput);
        createRuneSlotSelection();
        createTierSelection();

    }

    

    private void createConsumablePanel(){
        JTextField input = new JTextField();
        input.setBorder(BorderFactory.createTitledBorder("Uses"));
        this.add(input);
        createTierSelection();

         EventBus.publish(new CardTypeUpdate(Card.CONSUMABLE));

    }

    private void createOtherPanel(){
        createRuneSlotSelection();
        createTierSelection();
        return;
    }

    private void createWeaponPanel(){
        createWeaponTypeSelection();
        createDiceSelection();
        createRuneSlotSelection();
        createTierSelection();
        createStatSelection();

        EventBus.publish(new CardTypeUpdate(Card.W_MELEE));
    }

    private void createRunePanel(){
        createTierSelection();
        createRuneTypeSelection();
    }

    private void createWeaponTypeSelection() {
        
        JPanel weaponType = new JPanel();
        weaponType.setBorder(BorderFactory.createTitledBorder("Weapon Type"));
        isMelee = new JCheckBox("Melee", true);
        isRanged = new JCheckBox("Ranged", false);
        isThrowable = new JCheckBox("Throwable", false);
        
        JCheckBox[] weaponTypes = {isMelee,isRanged,isThrowable};
        for(JCheckBox x:weaponTypes){
            x.addActionListener(e->{uncheckOtherCheckboxes(x,weaponTypes);});
        }

        isMelee.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(Card.W_MELEE));});
        isRanged.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(Card.W_RANGED));});
        isThrowable.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(Card.W_THROWABLE));});
        weaponType.add(isMelee);
        weaponType.add(isRanged);
        weaponType.add(isThrowable);
        this.add(weaponType);
    }

    private void createRuneTypeSelection(){
        JPanel runeTypes = new JPanel();
        runeTypes.setBorder(BorderFactory.createTitledBorder("Type"));
        melee = new JCheckBox("Melee",true);
        ranged = new JCheckBox("Ranged",false);
        mixed = new JCheckBox("Either",false);
        armor = new JCheckBox("Armor",false);


        JCheckBox[] runeTypesArr = {melee,ranged,mixed,armor};

        for(JCheckBox x:runeTypesArr){
            x.addActionListener(e->{uncheckOtherCheckboxes(x,runeTypesArr);});
        }

        ranged.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(Card.W_RANGED));;});
        melee.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(Card.W_MELEE));});
        mixed.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(Card.W_THROWABLE));});
        armor.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(Card.ARMOR));});

        runeTypes.add(melee);
        runeTypes.add(ranged);
        runeTypes.add(mixed);
        runeTypes.add(armor);
        this.add(runeTypes);

        //parent.setImageComposer(Card.ATTRIBUTE,null);
        //parent.updateRuneType("melee");
    }


    private void createDiceSelection(){
        JPanel dice = new JPanel();
        dice.setBorder(BorderFactory.createTitledBorder("Weapon Dice"));

        d4 = new JCheckBox("D4",false);
        d6 = new JCheckBox("D6",false);
        d8 = new JCheckBox("D8",false);
        d10 = new JCheckBox("D10",false);
        d12 = new JCheckBox("D12",false);

        JCheckBox[] diceSelect = {d4,d6,d8,d10,d12};

        for(JCheckBox x:diceSelect){
            x.addActionListener(e->{uncheckOtherCheckboxes(x,diceSelect);});
        }

        d4.addActionListener(e -> {getDice(4);});
        d6.addActionListener(e -> {getDice(6);});
        d8.addActionListener(e -> {getDice(8);});
        d10.addActionListener(e -> {getDice(10);});
        d12.addActionListener(e -> {getDice(12);});

        dice.add(d4);
        dice.add(d6);
        dice.add(d8);
        dice.add(d10);
        dice.add(d12);

        this.add(dice);

    }


    private void createRuneSlotSelection(){
        JPanel runeSlots = new JPanel();
        runeSlots.setBorder(BorderFactory.createTitledBorder("Rune Charges"));

        none_slot = new JCheckBox("none", true);
        one_slot = new JCheckBox("1", false);
        two_slot = new JCheckBox("2", false);
        three_slot = new JCheckBox("3", false);

        JCheckBox[] runeSlotsArr = { none_slot,one_slot,two_slot,three_slot};
        for(JCheckBox x:runeSlotsArr){
            x.addActionListener(e->{uncheckOtherCheckboxes(x,runeSlotsArr);});
        }

        none_slot.addActionListener(e-> {EventBus.publish(new RuneChargesUpdate(0));});
        one_slot.addActionListener(e-> {EventBus.publish(new RuneChargesUpdate(1));});
        two_slot.addActionListener(e-> {EventBus.publish(new RuneChargesUpdate(2));});
        three_slot.addActionListener(e-> {EventBus.publish(new RuneChargesUpdate(3));});

        runeSlots.add(none_slot);
        runeSlots.add(one_slot);
        runeSlots.add(two_slot);
        runeSlots.add(three_slot);

       this.add(runeSlots);

    }

    private void createTierSelection(){
        JPanel tier = new JPanel();
        tier.setBorder(BorderFactory.createTitledBorder("Tier"));
        tier0 = new JCheckBox("Tier 0",true);
        tier1 = new JCheckBox("Tier 1",false);
        tier2 = new JCheckBox("Tier 2",false);
        tier3 = new JCheckBox("Tier 3",false);
        tier4 = new JCheckBox("Tier 4",false);

        JCheckBox[] tiers = {tier0,tier1,tier2,tier3,tier4};

        for(JCheckBox x:tiers){
            x.addActionListener(e->{uncheckOtherCheckboxes(x,tiers);});
        }

        tier0.addActionListener(e -> {EventBus.publish(new TierUpdate(0));});
        tier1.addActionListener(e -> {EventBus.publish(new TierUpdate(1));});
        tier2.addActionListener(e -> {EventBus.publish(new TierUpdate(2));});
        tier3.addActionListener(e -> {EventBus.publish(new TierUpdate(3));});
        tier4.addActionListener(e -> {EventBus.publish(new TierUpdate(4));});

        tier.add(tier0);
        tier.add(tier1);
        tier.add(tier2);
        tier.add(tier3);
        tier.add(tier4);

        this.add(tier);
    }

    

    private void createStatSelection(){
        JPanel attribute = new JPanel();
        attribute.setBorder(BorderFactory.createTitledBorder("Damage Attribute"));
        attribute.setLayout(new GridLayout(0, 3, 5, 5));
        JCheckBox str = new JCheckBox("Strength",true);
        JCheckBox con = new JCheckBox("Constitution",false);
        JCheckBox dex = new JCheckBox("Dexterity",false);
        JCheckBox intel = new JCheckBox("Intelligence",false);
        JCheckBox wis = new JCheckBox("Wisdom",false);
        JCheckBox rizz = new JCheckBox("Charisma",false);

        JCheckBox[] attributes = {str,con,dex,intel,wis,rizz};

        for(JCheckBox x:attributes){
            x.addActionListener(e->{uncheckOtherCheckboxes(x,attributes);});
        }

        publishImageUpdate(Card.ATTRIBUTE, "resources/glyphs/stat/strength.png");
        str.addActionListener(e -> {publishImageUpdate(Card.ATTRIBUTE, "resources/glyphs/stat/strength.png");});
        con.addActionListener(e -> {publishImageUpdate(Card.ATTRIBUTE, "resources/glyphs/stat/constitution.png");});
        dex.addActionListener(e -> {publishImageUpdate(Card.ATTRIBUTE, "resources/glyphs/stat/dexterity.png");});
        intel.addActionListener(e -> {publishImageUpdate(Card.ATTRIBUTE, "resources/glyphs/stat/intelligence.png");});
        wis.addActionListener(e -> {publishImageUpdate(Card.ATTRIBUTE, "resources/glyphs/stat/wisdom.png");});
        rizz.addActionListener(e -> {publishImageUpdate(Card.ATTRIBUTE, "resources/glyphs/stat/charisma.png");});

        attribute.add(str);
        attribute.add(con);
        attribute.add(dex);
        attribute.add(intel);
        attribute.add(wis);
        attribute.add(rizz);

        this.add(attribute);
    }

    private void getDice(int num){
        switch (num){
            case 0: publishImageUpdate(Card.DICE,null);break;
            case 4: publishImageUpdate(Card.DICE,"resources/dice/d4.png");break;
            case 6: publishImageUpdate(Card.DICE,"resources/dice/d6.png");break;
            case 8: publishImageUpdate(Card.DICE,"resources/dice/d8.png");break;
            case 10: publishImageUpdate(Card.DICE,"resources/dice/d10.png");break;
            case 12: publishImageUpdate(Card.DICE,"resources/dice/d12.png");
        }

    }

    private void getArmorClassImage(int ac) {
        
        if(ac == 0){
            publishImageUpdate(Card.ARMOR1, null);
            publishImageUpdate(Card.ARMOR2, null);
            return;
        }

        if(ac < 10){
            publishImageUpdate(Card.ARMOR1, "resources/glyphs/ac/"+ac+".png");
            publishImageUpdate(Card.ARMOR2, null);
            return;
        }else if(ac >=10 && ac < 20){
            publishImageUpdate(Card.ARMOR1, "resources/glyphs/ac/1_.png");
            ac %= 10;
            publishImageUpdate(Card.ARMOR2, "resources/glyphs/ac/_"+ac+".png");
            return;
        }else if(ac >=20 && ac < 30){
            publishImageUpdate(Card.ARMOR1, "resources/glyphsac//2_.png");
            ac %= 10;
            publishImageUpdate(Card.ARMOR2, "resources/glyphs/ac/_"+ac+".png");
            return;
        }else if(ac >=30){
            publishImageUpdate(Card.ARMOR1, "resources/glyphs/ac/3_.png");
            ac %= 10;
            publishImageUpdate(Card.ARMOR2, "resources/glyphs/ac/_"+ac+".png");
            return;
        }

    }

    /*public void getAttribute(String type){
        switch (type){
            case "str": publishImageUpdate(Card.ATTRIBUTE, "resources/glyphs/stat/strength.png");
            case "con": publishImageUpdate(Card.ATTRIBUTE, "resources/glyphs/stat/constitution.png");
            case "dex": publishImageUpdate(Card.ATTRIBUTE, "resources/glyphs/stat/dexterity.png");
            case "intel": publishImageUpdate(Card.ATTRIBUTE, "resources/glyphs/stat/intelligence.png");
            case "wis": publishImageUpdate(Card.ATTRIBUTE, "resources/glyphs/stat/wisdom.png");
            case "rizz": publishImageUpdate(Card.ATTRIBUTE, "resources/glyphs/stat/charisma.png");
        }
    }*/

    private void publishImageUpdate(int type, String path){
        EventBus.publish(new ItemImageUpdateEvent(type, path));
    }



    private void uncheckOtherCheckboxes(JCheckBox selectedCheckbox, JCheckBox[] allCheckboxes) {
        for (JCheckBox checkbox : allCheckboxes) {
            if (checkbox != selectedCheckbox) {
                checkbox.setSelected(false);
            }
        }
    }




}
