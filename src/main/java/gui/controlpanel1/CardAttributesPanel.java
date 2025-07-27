package gui.controlpanel1;
import javax.swing.*;
import javax.swing.event.DocumentListener;

import events.EventBus;
import events.ImageUpdate;
import events.RuneChargesUpdate;
import events.TextUpdate;
import events.TierUpdate;
import events.AttributeUpdate;
import events.CardTypeUpdate;
import events.DiceUpdateEvent;
import gui.*;
import gui.image_composers.components.DigitOnlyTextField;

import java.awt.*;
public class CardAttributesPanel extends JPanel{
    private int type;
    int[] buttonSize = {40,40};
    JTextField armorClassInput, successesInput, damageInput;

    public CardAttributesPanel(int type) {
        //this.setLayout(new GridLayout(0, 1, 5, 5));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.type = type;
        
        switch(type){
            case GlobalVar.WEAPON: createWeaponPanel();break;
            case GlobalVar.ARMOR: createArmorPanel();break;
            case GlobalVar.CONSUMABLE: createConsumablePanel();break;
            case GlobalVar.RUNE: createRunePanel();break;
            case GlobalVar.ACCESSOIRE: createArmorPanel();break;
            case GlobalVar.ARKHAM: createArkhamPanel();break;
            default: createOtherPanel();break;
        }
    }

    private void createArkhamPanel(){
        successesInput = new DigitOnlyTextField();
        successesInput.setPreferredSize(new Dimension(200,50) );
        successesInput.setBorder(BorderFactory.createTitledBorder("Diffculty Class"));
        successesInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(GlobalVar.OTHER_TEXT_UPDATE_1, successesInput.getText()));
            }
    
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(GlobalVar.OTHER_TEXT_UPDATE_1, successesInput.getText()));;
            }
    
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(GlobalVar.OTHER_TEXT_UPDATE_1, successesInput.getText()));
            }
        });

        damageInput = new DigitOnlyTextField();
        damageInput.setBorder(BorderFactory.createTitledBorder("Damage"));
        damageInput.setPreferredSize(new Dimension(200,50) );
        damageInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(GlobalVar.OTHER_TEXT_UPDATE_2, damageInput.getText()));
            }
    
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(GlobalVar.OTHER_TEXT_UPDATE_2, damageInput.getText()));;
            }
    
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(GlobalVar.OTHER_TEXT_UPDATE_2, damageInput.getText()));
            }
        });

        add(damageInput);
        add(successesInput);

        JPanel weaponType = new JPanel();
        weaponType.setBorder(BorderFactory.createTitledBorder("Weapon Type"));
        JButton isMelee = new JButton("",loadIcon("resources/glyphs/arkham/melee.png", buttonSize[0], buttonSize[1]));
        JButton isRanged = new JButton("",loadIcon("resources/glyphs/arkham/ranged.png", buttonSize[0], buttonSize[1]));
        JButton none = new JButton("",loadIcon("resources/glyphs/arkham/none.png", buttonSize[0], buttonSize[1]));

        isMelee.addActionListener(e -> {EventBus.publish(new ImageUpdate(GlobalVar.ARKHAM_RANGE_IMAGE_UPDATE,"resources/glyphs/arkham/melee.png"));});
        isRanged.addActionListener(e -> {EventBus.publish(new ImageUpdate(GlobalVar.ARKHAM_RANGE_IMAGE_UPDATE,"resources/glyphs/arkham/ranged.png"));});
        none.addActionListener(e -> {EventBus.publish(new ImageUpdate(GlobalVar.ARKHAM_RANGE_IMAGE_UPDATE_NONE,null));});

        weaponType.add(isMelee);
        weaponType.add(isRanged);
        weaponType.add(none);
        this.add(weaponType);

        JPanel cardSide = new JPanel();
        JButton front = new JButton("",loadIcon("resources/glyphs/arkham/frontside.png", buttonSize[0], buttonSize[1]));
        JButton back = new JButton("",loadIcon("resources/glyphs/arkham/backside.png", buttonSize[0], buttonSize[1]));

        front.addActionListener(e -> {EventBus.publish(new ImageUpdate(GlobalVar.ARKHAM_CARDSIDE_IMAGE_UPDATE,"resources/glyphs/arkham/frontside.png"));});
        back.addActionListener(e -> {EventBus.publish(new ImageUpdate(GlobalVar.ARKHAM_CARDSIDE_IMAGE_UPDATE,"resources/glyphs/arkham/backside.png"));});
        cardSide.add(front);
        cardSide.add(back);
        this.add(cardSide);
        
        
    }

    private void createArmorPanel(){
        armorClassInput = new DigitOnlyTextField();
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

         EventBus.publish(new CardTypeUpdate(GlobalVar.CONSUMABLE));

    }

    private void createOtherPanel(){
        createRuneSlotSelection();
        createTierSelection();
        return;
    }

    private void createWeaponPanel(){
        createWeaponTypeSelection();
        createRangeInput();
        createDiceSelection();
        createRuneSlotSelection();
        createTierSelection();
        createAttributeSelection();

        //EventBus.publish(new CardTypeUpdate(GlobalVar.W_MELEE));
    }

    public void createRangeInput(){
        JPanel panel = new JPanel();

        JTextField range1 = new JTextField();
        JTextField range2 = new JTextField();
        range1.setBorder(BorderFactory.createTitledBorder("Range Min"));
        range2.setBorder(BorderFactory.createTitledBorder("Range Max"));

        range1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(GlobalVar.RANGE_NORMAL_TEXT_UPDATE, range1.getText()));
            }
    
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(GlobalVar.RANGE_NORMAL_TEXT_UPDATE, range1.getText()));
            }
    
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(GlobalVar.RANGE_NORMAL_TEXT_UPDATE, range1.getText()));
            }
        });

        range2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(GlobalVar.RANGE_MAX_TEXT_UPDATE, range2.getText()));
            }
    
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(GlobalVar.RANGE_MAX_TEXT_UPDATE, range2.getText()));
            }
    
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                EventBus.publish(new TextUpdate(GlobalVar.RANGE_MAX_TEXT_UPDATE, range2.getText()));
            }
        });

        panel.add(range1);
        panel.add(range2);
        this.add(panel);
    }

    private void createRunePanel(){
        createTierSelection();
        createRuneTypeSelection();
    }

    private void createWeaponTypeSelection() {
        
        JPanel weaponType = new JPanel();
        //weaponType.setLayout(new GridLayout(0, 3, 5, 5));
        weaponType.setBorder(BorderFactory.createTitledBorder("Weapon Type"));
        JButton isMelee = new JButton("",loadIcon("resources/glyphs/AttributeLabel/damage_melee.png", buttonSize[0], buttonSize[1]));
        JButton isRanged = new JButton("",loadIcon("resources/glyphs/AttributeLabel/damage_ranged.png", buttonSize[0], buttonSize[1]));
        JButton isMagic = new JButton("",loadIcon("resources/glyphs/AttributeLabel/damage_magic.png", buttonSize[0], buttonSize[1]));

        isMelee.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(GlobalVar.W_MELEE));});
        isRanged.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(GlobalVar.W_RANGED));});
        isMagic.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(GlobalVar.W_MAGIC));});
        weaponType.add(isMelee);
        weaponType.add(isRanged);
        weaponType.add(isMagic);
        this.add(weaponType);
    }

    private void createRuneTypeSelection(){
        JPanel runeTypes = new JPanel();
        //runeTypes.setLayout(new GridLayout(2, 2, 5, 5));
        runeTypes.setBorder(BorderFactory.createTitledBorder("Type"));

        JButton melee = new JButton("",loadIcon("resources/glyphs/AttributeLabel/damage_melee.png", buttonSize[0], buttonSize[1]));
        JButton ranged = new JButton("",loadIcon("resources/glyphs/AttributeLabel/damage_ranged.png", buttonSize[0], buttonSize[1]));
        JButton mixed = new JButton("",loadIcon("resources/glyphs/AttributeLabel/damage_magic.png", buttonSize[0], buttonSize[1]));
        JButton armor = new JButton("",loadIcon("resources/glyphs/buttons/damage_mixed.png", buttonSize[0], buttonSize[1]));

        ranged.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(GlobalVar.W_RANGED));;});
        melee.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(GlobalVar.W_MELEE));});
        mixed.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(GlobalVar.W_THROWABLE));});
        armor.addActionListener(e -> {EventBus.publish(new CardTypeUpdate(GlobalVar.ARMOR));});

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
        //dice.setLayout(new GridLayout(0, 5, 5, 5));
        dice.setBorder(BorderFactory.createTitledBorder("Weapon Dice"));
        JButton d4 = new JButton("",loadIcon("resources/glyphs/dice/d4.png", buttonSize[0], buttonSize[1]));
        JButton d6 = new JButton("",loadIcon("resources/glyphs/dice/d6.png", buttonSize[0], buttonSize[1]));
        JButton d8 = new JButton("",loadIcon("resources/glyphs/dice/d8.png", buttonSize[0], buttonSize[1]));
        JButton d10 = new JButton("",loadIcon("resources/glyphs/dice/d10.png", buttonSize[0], buttonSize[1]));
        JButton d12 = new JButton("",loadIcon("resources/glyphs/dice/d12.png", buttonSize[0], buttonSize[1]));

        d4.addActionListener(e -> {EventBus.publish(new DiceUpdateEvent(4));});
        d6.addActionListener(e -> {EventBus.publish(new DiceUpdateEvent(6));});
        d8.addActionListener(e -> {EventBus.publish(new DiceUpdateEvent(8));});
        d10.addActionListener(e -> {EventBus.publish(new DiceUpdateEvent(10));});
        d12.addActionListener(e -> {EventBus.publish(new DiceUpdateEvent(12));});

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
        //runeSlots.setLayout(new GridLayout(2, 2, 5, 5));

        JButton none_slot = new JButton("None");
        JButton one_slot = new JButton("",loadIcon("resources/glyphs/buttons/charge1.png", buttonSize[0], buttonSize[1]));
        JButton two_slot = new JButton("",loadIcon("resources/glyphs/buttons/charge2.png", buttonSize[0], buttonSize[1]));
        JButton three_slot = new JButton("",loadIcon("resources/glyphs/buttons/charge3.png", buttonSize[0], buttonSize[1]));


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
        //tier.setLayout(new GridLayout(0, 5, 5, 5));

        JButton tier0 = new JButton("Common");
        JButton tier1 = new JButton("",loadIcon("resources/glyphs/tier/1.png", buttonSize[0], buttonSize[1]));
        JButton tier2 = new JButton("",loadIcon("resources/glyphs/tier/2.png", buttonSize[0], buttonSize[1]));
        JButton tier3 = new JButton("",loadIcon("resources/glyphs/tier/3.png", buttonSize[0], buttonSize[1]));
        JButton tier4 = new JButton("",loadIcon("resources/glyphs/tier/4.png", buttonSize[0], buttonSize[1]));


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

    

    private void createAttributeSelection(){
        JPanel attribute = new JPanel();
        attribute.setBorder(BorderFactory.createTitledBorder("Damage Attribute"));
        attribute.setLayout(new GridLayout(0, 3, 5, 5));

        JButton str = new JButton("",loadIcon("resources/glyphs/attributes/strength.png", buttonSize[0], buttonSize[1]));
        JButton con = new JButton("",loadIcon("resources/glyphs/attributes/constitution.png", buttonSize[0], buttonSize[1]));
        JButton dex = new JButton("",loadIcon("resources/glyphs/attributes/dexterity.png", buttonSize[0], buttonSize[1]));
        JButton rizz = new JButton("",loadIcon("resources/glyphs/attributes/charisma.png", buttonSize[0], buttonSize[1]));
        JButton intel = new JButton("",loadIcon("resources/glyphs/attributes/intelligence.png", buttonSize[0], buttonSize[1]));
        JButton wis = new JButton("",loadIcon("resources/glyphs/attributes/wisdom.png", buttonSize[0], buttonSize[1]));

        publishImageUpdate(GlobalVar.ATTRIBUTE, GlobalVar.ATTRIBUTE_IMAGE_PATH+"strength.png");
        str.addActionListener(e -> {EventBus.publish(new AttributeUpdate(GlobalVar.STRENGTH));});
        con.addActionListener(e -> {EventBus.publish(new AttributeUpdate(GlobalVar.CONSTITUTION));});
        dex.addActionListener(e -> {EventBus.publish(new AttributeUpdate(GlobalVar.DEXTERITY));});
        intel.addActionListener(e -> {EventBus.publish(new AttributeUpdate(GlobalVar.INTELLIGENCE));});
        wis.addActionListener(e -> {EventBus.publish(new AttributeUpdate(GlobalVar.WISDOM));});
        rizz.addActionListener(e -> {EventBus.publish(new AttributeUpdate(GlobalVar.CHARISMA));});

        attribute.add(str);
        attribute.add(con);
        attribute.add(dex);
        attribute.add(intel);
        attribute.add(wis);
        attribute.add(rizz);

        this.add(attribute);
    }

    private void getArmorClassImage(int ac) {
        
        if(ac == 0){
            publishImageUpdate(GlobalVar.ARMOR1, null);
            publishImageUpdate(GlobalVar.ARMOR2, null);
            return;
        }

        if(ac < 10){
            publishImageUpdate(GlobalVar.ARMOR1, GlobalVar.AC_IMAGE_PATH+ac+".png");
            publishImageUpdate(GlobalVar.ARMOR2, null);
            return;
        }else if(ac >=10 && ac < 20){
            publishImageUpdate(GlobalVar.ARMOR1, GlobalVar.AC_IMAGE_PATH+"1_.png");
            ac %= 10;
            publishImageUpdate(GlobalVar.ARMOR2, GlobalVar.AC_IMAGE_PATH+"_"+ac+".png");
            return;
        }else if(ac >=20 && ac < 30){
            publishImageUpdate(GlobalVar.ARMOR1, GlobalVar.AC_IMAGE_PATH+"2_.png");
            ac %= 10;
            publishImageUpdate(GlobalVar.ARMOR2, GlobalVar.AC_IMAGE_PATH+"_"+ac+".png");
            return;
        }else if(ac >=30){
            publishImageUpdate(GlobalVar.ARMOR1, GlobalVar.AC_IMAGE_PATH+"3_.png");
            ac %= 10;
            publishImageUpdate(GlobalVar.ARMOR2, GlobalVar.AC_IMAGE_PATH+"_"+ac+".png");
            return;
        }

    }

    private void publishImageUpdate(int type, String path){
        EventBus.publish(new ImageUpdate(type, path));
    }



    private ImageIcon loadIcon(String path, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(path);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

}