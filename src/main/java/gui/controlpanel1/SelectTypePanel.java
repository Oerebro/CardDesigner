package gui.controlpanel1;
import javax.swing.*;

import events.EventBus;
import events.ItemImageUpdateEvent;
import events.SelectTypePanelUpdateEvent;
import events.ClearUnrelatedImagesEvent;
//import events.ImageUpdateEvent;
import gui.CardDesignerGUI;
import gui.GlobalVar;
import gui.card_types.*;

import java.awt.*;
import java.awt.event.ActionListener;

public class SelectTypePanel extends JPanel{
    private final CardDesignerGUI parent;
    private AttributePanel attributePanel;
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

        
        this.add(attributePanel = new AttributePanel(parent, "weapon"));


        // Collect all checkboxes in this panel in an array
        JCheckBox[] checkboxes2 = {isWeapon, isArmor, isClothing, isAccessoire, isConsumable,isRune,isEffect};
        checkboxes = checkboxes2;

        // Add action listeners to each checkbox
        for (JCheckBox checkbox : checkboxes) {
            checkbox.addActionListener(createCheckboxListener(checkbox, checkboxes));
        }
    }

    private ActionListener createCheckboxListener(JCheckBox selectedCheckbox, JCheckBox[] allCheckboxes) {
        //EventBus.publish(new ClearUnrelatedImagesEvent());
        return e -> {
            updatePanel(selectedCheckbox, allCheckboxes);
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

    public void updatePanel(JCheckBox selected, JCheckBox[] checkboxes){

        uncheckOtherCheckboxes(selected, checkboxes);
        this.remove(attributePanel);
        String type = selected.getText().toLowerCase();
        //EventBus.publish(new ClearUnrelatedImagesEvent());
        EventBus.publish(new SelectTypePanelUpdateEvent(type));
        this.attributePanel = new AttributePanel(parent, type);
    
        this.add(attributePanel);
        this.repaint();
    }


}
