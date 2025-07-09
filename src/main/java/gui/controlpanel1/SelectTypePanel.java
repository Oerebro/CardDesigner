package gui.controlpanel1;
import javax.swing.*;

import events.EventBus;
import events.ItemImageUpdateEvent;
import events.CardTypeUpdate;
//import events.ImageUpdateEvent;
import gui.CardDesignerGUI;
import gui.GlobalVar;

import java.awt.*;
import java.awt.event.ActionListener;

public class SelectTypePanel extends JPanel{
    private final CardDesignerGUI parent;
    private CardAttributesPanel attributePanel;
    private JCheckBox isWeapon, isArmor, isClothing, isAccessoire, isConsumable, isRune,isEffect, hasRuneCut;
    JCheckBox[] checkboxes;

    public SelectTypePanel(CardDesignerGUI parent) {
        this.parent = parent; 
        createCheckBoxPanel();
    }


    public void createCheckBoxPanel() {
        JPanel typePanel = new JPanel();
        typePanel.setBorder(BorderFactory.createTitledBorder("Item Type"));
        typePanel.setLayout(new GridLayout(0, 2, 5, 5));
        isWeapon = new JCheckBox("Weapon", true);       
        isArmor = new JCheckBox("Armor", false);
        isClothing = new JCheckBox("Clothing", false);
        isAccessoire = new JCheckBox("Accessoire", false);
        isConsumable = new JCheckBox("Consumable", false);
        isRune = new JCheckBox("Rune", false);
        isEffect = new JCheckBox("Effect", false);
        hasRuneCut = new JCheckBox("Show Rune Cut Line", false);
        hasRuneCut.addActionListener(e->{updateRuneCut(hasRuneCut.isSelected());});
        // Add the checkboxes to the panel
        typePanel.add(isWeapon);
        typePanel.add(isArmor);
        typePanel.add(isClothing);
        typePanel.add(isAccessoire);
        typePanel.add(isConsumable);
        typePanel.add(isRune);
        typePanel.add(isEffect);
        typePanel.add(hasRuneCut);

        this.add(typePanel);

        
        this.add(attributePanel = new CardAttributesPanel(GlobalVar.WEAPON));


        // Collect all checkboxes in this panel in an array
        JCheckBox[] checkboxes2 = {isWeapon, isArmor, isClothing, isAccessoire, isConsumable,isRune,isEffect};
        checkboxes = checkboxes2;
        isWeapon.addActionListener(createCheckboxListener(isWeapon, checkboxes,GlobalVar.WEAPON));
        isClothing.addActionListener(createCheckboxListener(isClothing, checkboxes,GlobalVar.ARMOR));
        isArmor.addActionListener(createCheckboxListener(isArmor, checkboxes,GlobalVar.ARMOR));
        isAccessoire.addActionListener(createCheckboxListener(isAccessoire, checkboxes,GlobalVar.ACCESSOIRE));
        isConsumable.addActionListener(createCheckboxListener(isConsumable, checkboxes,GlobalVar.CONSUMABLE));
        isRune.addActionListener(createCheckboxListener(isRune, checkboxes,GlobalVar.RUNE));

    }

    private ActionListener createCheckboxListener(JCheckBox selectedCheckbox, JCheckBox[] allCheckboxes, int type) {
        //EventBus.publish(new ClearUnrelatedImagesEvent());
        return e -> {
            updatePanel(selectedCheckbox, allCheckboxes, type);
        };
    }
    private void uncheckOtherCheckboxes(JCheckBox selectedCheckbox, JCheckBox[] allCheckboxes) {
        for (JCheckBox checkbox : allCheckboxes) {
            if (checkbox != selectedCheckbox) {
                checkbox.setSelected(false);
            }
        }
    }

    private void updateRuneCut(Boolean isCut){
        if(isCut){
            EventBus.publish(new ItemImageUpdateEvent(GlobalVar.RUNECUT, "resources/misc/rune_cut.png"));
        }else{
            EventBus.publish(new ItemImageUpdateEvent(GlobalVar.RUNECUT, null));
        }
    }

    public void updatePanel(JCheckBox selected, JCheckBox[] checkboxes, int type){

        uncheckOtherCheckboxes(selected, checkboxes);
        this.remove(attributePanel);
        //String type = selected.getText().toLowerCase();
        //EventBus.publish(new ClearUnrelatedImagesEvent());
        EventBus.publish(new CardTypeUpdate(type));
        this.attributePanel = new CardAttributesPanel(type);
    
        this.add(attributePanel);
        this.repaint();
    }


}
