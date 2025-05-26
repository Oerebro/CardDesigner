package gui.controlpanel1;
import javax.swing.*;

import gui.CardDesignerGUI;

import java.awt.*;
import java.awt.event.ActionListener;

public class SelectTypePanel extends JPanel{
    private final CardDesignerGUI parent;
    private AttributePanel attributePanel;

    public SelectTypePanel(CardDesignerGUI parent) {
        this.parent = parent; 
        createCheckBoxPanel();
    }

    public void setRangeAndACFont(String font){
        attributePanel.setRangeAndACFont(font);
    }

    public void createCheckBoxPanel() {
        JPanel typePanel = new JPanel();
        typePanel.setBorder(BorderFactory.createTitledBorder("Item Type"));
        typePanel.setLayout(new GridLayout(0, 2, 5, 5));
        JCheckBox isWeapon = new JCheckBox("Weapon", true);
        JCheckBox isArmor = new JCheckBox("Armor", false);
        JCheckBox isClothing = new JCheckBox("Clothing", false);
        JCheckBox isAccessoire = new JCheckBox("Accessoire", false);
        JCheckBox isConsumable = new JCheckBox("Consumable", false);
        JCheckBox isRune = new JCheckBox("Rune", false);
        JCheckBox hasRuneCut = new JCheckBox("Show Rune Cut Line", false);

        hasRuneCut.addActionListener(e->{parent.updateRuneCut(hasRuneCut.isSelected());});
        // Add the checkboxes to the panel
        typePanel.add(isWeapon);
        typePanel.add(isArmor);
        typePanel.add(isClothing);
        typePanel.add(isAccessoire);
        typePanel.add(isConsumable);
        typePanel.add(isRune);
        typePanel.add(hasRuneCut);

        this.add(typePanel);

        
        this.add(attributePanel = new AttributePanel(parent, "weapon"));


        // Collect all checkboxes in this panel in an array
        JCheckBox[] checkboxes = {isWeapon, isArmor, isClothing, isAccessoire, isConsumable,isRune};

        // Add action listeners to each checkbox
        for (JCheckBox checkbox : checkboxes) {
            checkbox.addActionListener(createCheckboxListener(checkbox, checkboxes));
        }
    }

    private ActionListener createCheckboxListener(JCheckBox selectedCheckbox, JCheckBox[] allCheckboxes) {
        return e -> {
            uncheckOtherCheckboxes(selectedCheckbox, allCheckboxes);
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

    public void updatePanel(JCheckBox selected, JCheckBox[] checkboxes){

        uncheckOtherCheckboxes(selected, checkboxes);
        this.remove(attributePanel);
        String type = selected.getText();
        switch(type) {
            case "Weapon":      parent.onButtonWeapon(); 
                                this.attributePanel = new AttributePanel(parent, "weapon");
                                break;
            case "Armor":       parent.onButtonArmor(); 
                                this.attributePanel = new AttributePanel(parent, "armor");
                                break;
            case "Consumable":  parent.onButtonConsumable(); 
                                this.attributePanel = new AttributePanel(parent, "consumable"); 
                                break;
            case "Clothing":    parent.onButtonClothing(); 
                                this.attributePanel = new AttributePanel(parent, "other");
                                break;
            case "Accessoire":  parent.onButtonAccessoire(); 
                                this.attributePanel = new AttributePanel(parent, "other");
                                break;
            case "Rune":        parent.onButtonRune();
                                this.attributePanel = new AttributePanel(parent, "rune");
                                break;
        }
        this.add(attributePanel);
        this.repaint();
        ;
    }


}
