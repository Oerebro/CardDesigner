package gui_elements.controlpanel1;
import javax.swing.*;

import gui_elements.CardDesignerGUI;

import java.awt.*;
import java.awt.event.ActionListener;

public class AttributePanel extends JPanel{
    private final CardDesignerGUI parent;

    public AttributePanel(CardDesignerGUI parent, String type) {
        this.parent = parent; 
        
        switch(type){
            case "weapon": createWeaponPanel();break;
            case "armor": createArmorPanel();break;
            case "consumable": createConsumablePanel();break;
        }
    }



    private void createWeaponPanel(){
        createWeaponTypeSelection();
        createDiceSelection();
        createRuneSlotSelection();
        createTierSelection();
    }

    private void createWeaponTypeSelection() {
        JPanel weaponType = new JPanel();
        weaponType.setBorder(BorderFactory.createTitledBorder("Weapon Type"));
        JCheckBox isMelee = new JCheckBox("Melee", true);
        JCheckBox isRanged = new JCheckBox("Ranged", false);
        weaponType.add(isMelee);
        weaponType.add(isRanged);
        this.add(weaponType);
    }

    private void createDiceSelection(){
        JPanel dice = new JPanel();
        dice.setBorder(BorderFactory.createTitledBorder("Weapon Dice"));

        JCheckBox d4 = new JCheckBox("D4",false);
        JCheckBox d6 = new JCheckBox("D6",false);
        JCheckBox d8 = new JCheckBox("D8",false);
        JCheckBox d10 = new JCheckBox("D10",false);
        JCheckBox d12 = new JCheckBox("D12",false);

        dice.add(d4);
        dice.add(d6);
        dice.add(d8);
        dice.add(d10);
        dice.add(d12);

        this.add(dice);

    }

    private void createRuneSlotSelection(){
        JPanel runeSlots = new JPanel();
        runeSlots.setBorder(BorderFactory.createTitledBorder("Rune Slots"));

        JCheckBox none_slot = new JCheckBox("none", true);
        JCheckBox one_slot = new JCheckBox("1", false);
        JCheckBox two_slot = new JCheckBox("2", false);
        JCheckBox three_slot = new JCheckBox("3", false);
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

        tier.add(tier0);
        tier.add(tier1);
        tier.add(tier2);
        tier.add(tier3);
        tier.add(tier4);

        this.add(tier);
    }

    private void createArmorPanel(){
        JTextField input = new JTextField();
        input.setBorder(BorderFactory.createTitledBorder("Armor Class"));
        this.add(input);
        createRuneSlotSelection();
        createTierSelection();

    }

    private void createConsumablePanel(){
        JTextField input = new JTextField();
        input.setBorder(BorderFactory.createTitledBorder("Uses"));
        this.add(input);
        createTierSelection();

    }

    private ActionListener createCheckboxListener(JCheckBox selectedCheckbox) {
        return e -> {
            //uncheckOtherCheckboxes(selectedCheckbox, allCheckboxes);
        };
    }
    private void uncheckOtherCheckboxes(JCheckBox selectedCheckbox, JCheckBox[] allCheckboxes) {
        for (JCheckBox checkbox : allCheckboxes) {
            if (checkbox != selectedCheckbox) {
                checkbox.setSelected(false);
            }
        }
    }




}
