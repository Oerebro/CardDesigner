package gui.controlpanel1;
import javax.swing.*;
import javax.swing.event.DocumentListener;

import events.EventBus;
import events.ImageUpdateEvent;
import events.ClearUnrelatedImagesEvent;
import gui.*;

import java.awt.*;
public class AttributePanel extends JPanel{
    private final CardDesignerGUI parent;
    //private Font rangeAndACFont;

    public AttributePanel(CardDesignerGUI parent, String type) {
        this.parent = parent; 
        this.setLayout(new GridLayout(0, 1, 5, 5));

        EventBus.publish(new ClearUnrelatedImagesEvent());
        
        switch(type){
            case "weapon": createWeaponPanel();break;
            case "armor": createArmorPanel();break;
            case "consumable": createConsumablePanel();break;
            case "rune": createRunePanel();break;
            case "other": createOtherPanel();break;
        }
    }

    public void setRangeAndACFont(String font){
        //rangeAndACFont = FontLoader.loadFont(font, 20f);
    }

    private void createArmorPanel(){
        JTextField input = new JTextField();
        input.setBorder(BorderFactory.createTitledBorder("Armor Class"));

        input.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                if(input.getText().matches("")){
                    getArmorClass(0);
                    return;
                }
                getArmorClass(Integer.parseInt(input.getText()));
            }
    
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                if(input.getText().matches("")){
                    getArmorClass(0);
                    return;
                }
                getArmorClass(Integer.parseInt(input.getText()));
            }
    
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                if(input.getText().matches("")){
                    getArmorClass(0);
                    return;
                }
                getArmorClass(Integer.parseInt(input.getText()));
            }
        });

        this.add(input);
        createRuneSlotSelection();
        createTierSelection();
        //createStatSelection();

    }

    

    private void createConsumablePanel(){
        JTextField input = new JTextField();
        input.setBorder(BorderFactory.createTitledBorder("Uses"));
        this.add(input);
        createTierSelection();

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

         publishImageUpdate("weaponType", "resources/glyphs/weapon_type/melee.png");
    }

    private void createRunePanel(){
        createTierSelection();
        createRuneTypeSelection();
    }

    private void createWeaponTypeSelection() {
        
        JPanel weaponType = new JPanel();
        weaponType.setBorder(BorderFactory.createTitledBorder("Weapon Type"));
        JCheckBox isMelee = new JCheckBox("Melee", true);
        JCheckBox isRanged = new JCheckBox("Ranged", false);
        JCheckBox isThrowable = new JCheckBox("Throwable", false);
        
        JCheckBox[] weaponTypes = {isMelee,isRanged,isThrowable};
        for(JCheckBox x:weaponTypes){
            x.addActionListener(e->{uncheckOtherCheckboxes(x,weaponTypes);});
        }

        isMelee.addActionListener(e -> {publishImageUpdate("weaponType", "resources/glyphs/weapon_type/melee.png");});
        isRanged.addActionListener(e -> {publishImageUpdate("weaponType", "resources/glyphs/weapon_type/ranged.png");});
        isThrowable.addActionListener(e -> {publishImageUpdate("weaponType", "resources/glyphs/weapon_type/throwable.png");});
        weaponType.add(isMelee);
        weaponType.add(isRanged);
        weaponType.add(isThrowable);
        this.add(weaponType);
    }

    private void createRuneTypeSelection(){
        JPanel runeTypes = new JPanel();
        runeTypes.setBorder(BorderFactory.createTitledBorder("Type"));
        JCheckBox melee = new JCheckBox("Melee",true);
        JCheckBox ranged = new JCheckBox("Ranged",false);
        JCheckBox mixed = new JCheckBox("Either",false);
        JCheckBox armor = new JCheckBox("Armor",false);


        JCheckBox[] runeTypesArr = {melee,ranged,mixed,armor};

        for(JCheckBox x:runeTypesArr){
            x.addActionListener(e->{uncheckOtherCheckboxes(x,runeTypesArr);});
        }

        ranged.addActionListener(e -> {parent.updateRuneType("ranged");});
        melee.addActionListener(e -> {parent.updateRuneType("melee");});
        mixed.addActionListener(e -> {parent.updateRuneType("mixed");});
        armor.addActionListener(e -> {parent.updateRuneType("armor");});

        runeTypes.add(melee);
        runeTypes.add(ranged);
        runeTypes.add(mixed);
        runeTypes.add(armor);
        this.add(runeTypes);

        parent.setImageComposer("attributeImage",null);
        parent.updateRuneType("melee");
    }


    private void createDiceSelection(){
        JPanel dice = new JPanel();
        dice.setBorder(BorderFactory.createTitledBorder("Weapon Dice"));

        JCheckBox d4 = new JCheckBox("D4",false);
        JCheckBox d6 = new JCheckBox("D6",false);
        JCheckBox d8 = new JCheckBox("D8",false);
        JCheckBox d10 = new JCheckBox("D10",false);
        JCheckBox d12 = new JCheckBox("D12",false);

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

        JCheckBox none_slot = new JCheckBox("none", true);
        JCheckBox one_slot = new JCheckBox("1", false);
        JCheckBox two_slot = new JCheckBox("2", false);
        JCheckBox three_slot = new JCheckBox("3", false);

        JCheckBox[] runeSlotsArr = { none_slot,one_slot,two_slot,three_slot};
        for(JCheckBox x:runeSlotsArr){
            x.addActionListener(e->{uncheckOtherCheckboxes(x,runeSlotsArr);});
        }

        none_slot.addActionListener(e-> {parent.updateRuneSlots(0);});
        one_slot.addActionListener(e-> {parent.updateRuneSlots(1);});
        two_slot.addActionListener(e-> {parent.updateRuneSlots(2);});
        three_slot.addActionListener(e-> {parent.updateRuneSlots(3);});

        runeSlots.add(none_slot);
        runeSlots.add(one_slot);
        runeSlots.add(two_slot);
        runeSlots.add(three_slot);

       this.add(runeSlots);

    }

    private void createTierSelection(){
        JPanel tier = new JPanel();
        tier.setBorder(BorderFactory.createTitledBorder("Tier"));
        JCheckBox tier0 = new JCheckBox("Tier 0",true);
        JCheckBox tier1 = new JCheckBox("Tier 1",false);
        JCheckBox tier2 = new JCheckBox("Tier 2",false);
        JCheckBox tier3 = new JCheckBox("Tier 3",false);
        JCheckBox tier4 = new JCheckBox("Tier 4",false);

        JCheckBox[] tiers = {tier0,tier1,tier2,tier3,tier4};

        for(JCheckBox x:tiers){
            x.addActionListener(e->{uncheckOtherCheckboxes(x,tiers);});
        }

        tier0.addActionListener(e -> {EventBus.publish(new ImageUpdateEvent("tierGlyph",null));});
        tier1.addActionListener(e -> {EventBus.publish(new ImageUpdateEvent("tierGlyph","resources/glyphs/tier/1.png"));});
        tier2.addActionListener(e -> {EventBus.publish(new ImageUpdateEvent("tierGlyph","resources/glyphs/tier/2.png"));});
        tier3.addActionListener(e -> {EventBus.publish(new ImageUpdateEvent("tierGlyph","resources/glyphs/tier/3.png"));});
        tier4.addActionListener(e -> {EventBus.publish(new ImageUpdateEvent("tierGlyph","resources/glyphs/tier/4.png"));});

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

        publishImageUpdate("attributeImage", "resources/glyphs/stat/strength.png");
        str.addActionListener(e -> {publishImageUpdate("attributeImage", "resources/glyphs/stat/strength.png");});
        con.addActionListener(e -> {publishImageUpdate("attributeImage", "resources/glyphs/stat/constitution.png");});
        dex.addActionListener(e -> {publishImageUpdate("attributeImage", "resources/glyphs/stat/dexterity.png");});
        intel.addActionListener(e -> {publishImageUpdate("attributeImage", "resources/glyphs/stat/intelligence.png");});
        wis.addActionListener(e -> {publishImageUpdate("attributeImage", "resources/glyphs/stat/wisdom.png");});
        rizz.addActionListener(e -> {publishImageUpdate("attributeImage", "resources/glyphs/stat/charisma.png");});

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
            case 0: publishImageUpdate("cardType",null);break;
            case 4: publishImageUpdate("cardType","resources/dice/d4.png");break;
            case 6: publishImageUpdate("cardType","resources/dice/d6.png");break;
            case 8: publishImageUpdate("cardType","resources/dice/d8.png");break;
            case 10: publishImageUpdate("cardType","resources/dice/d10.png");break;
            case 12: publishImageUpdate("cardType","resources/dice/d12.png");
        }

    }

    private void getArmorClass(int ac) {
        
        if(ac == 0){
            publishImageUpdate("ac1", null);
            publishImageUpdate("ac2", null);
            return;
        }

        if(ac < 10){
            publishImageUpdate("ac1", "resources/glyphs/ac/"+ac+".png");
            publishImageUpdate("ac2", null);
            return;
        }else if(ac >=10 && ac < 20){
            publishImageUpdate("ac1", "resources/glyphs/ac/1_.png");
            ac %= 10;
            publishImageUpdate("ac2", "resources/glyphs/ac/_"+ac+".png");
            return;
        }else if(ac >=20 && ac < 30){
            publishImageUpdate("ac1", "resources/glyphsac//2_.png");
            ac %= 10;
            publishImageUpdate("ac2", "resources/glyphs/ac/_"+ac+".png");
            return;
        }else if(ac >=30){
            publishImageUpdate("ac1", "resources/glyphs/ac/3_.png");
            ac %= 10;
            publishImageUpdate("ac2", "resources/glyphs/ac/_"+ac+".png");
            return;
        }

    }

    /*public void getAttribute(String type){
        switch (type){
            case "str": publishImageUpdate("attributeImage", "resources/glyphs/stat/strength.png");
            case "con": publishImageUpdate("attributeImage", "resources/glyphs/stat/constitution.png");
            case "dex": publishImageUpdate("attributeImage", "resources/glyphs/stat/dexterity.png");
            case "intel": publishImageUpdate("attributeImage", "resources/glyphs/stat/intelligence.png");
            case "wis": publishImageUpdate("attributeImage", "resources/glyphs/stat/wisdom.png");
            case "rizz": publishImageUpdate("attributeImage", "resources/glyphs/stat/charisma.png");
        }
    }*/

    private void publishImageUpdate(String type, String path){
        EventBus.publish(new ImageUpdateEvent(type, path));
    }



    private void uncheckOtherCheckboxes(JCheckBox selectedCheckbox, JCheckBox[] allCheckboxes) {
        for (JCheckBox checkbox : allCheckboxes) {
            if (checkbox != selectedCheckbox) {
                checkbox.setSelected(false);
            }
        }
    }




}
