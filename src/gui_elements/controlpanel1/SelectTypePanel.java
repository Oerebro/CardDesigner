package gui_elements.controlpanel1;
import javax.swing.*;

import gui_elements.CardDesignerGUI;

import java.awt.*;
import java.awt.event.ActionListener;

public class SelectTypePanel extends JPanel{
    private final CardDesignerGUI parent;

    public SelectTypePanel(CardDesignerGUI parent) {
        this.parent = parent; 
        createCheckBoxPanel();
    }

    public void createCheckBoxPanel() {
        this.setLayout(new GridLayout(0, 2, 5, 5));
        JCheckBox isWeapon = new JCheckBox("Weapon", false);
        JCheckBox isArmor = new JCheckBox("Armor", false);
        JCheckBox isClothing = new JCheckBox("Clothing", false);
        JCheckBox isAccessoire = new JCheckBox("Accessoire", false);
        JCheckBox isConsumable = new JCheckBox("Consumable", false);
        JCheckBox isTwoHanded = new JCheckBox("Two-Handed", false);

        // Add the checkboxes to the panel
        this.add(isWeapon);
        this.add(isArmor);
        this.add(isClothing);
        this.add(isAccessoire);
        this.add(isConsumable);
        this.add(isTwoHanded);

        // Collect all checkboxes in this panel in an array
        JCheckBox[] checkboxes = {isWeapon, isArmor, isClothing, isAccessoire, isConsumable,isTwoHanded};

        // Add action listeners to each checkbox
        for (JCheckBox checkbox : checkboxes) {
            checkbox.addActionListener(createCheckboxListener(checkbox, checkboxes));
        }
    }

    private ActionListener createCheckboxListener(JCheckBox selectedCheckbox, JCheckBox[] allCheckboxes) {
        return e -> {
            uncheckOtherCheckboxes(selectedCheckbox, allCheckboxes);
            updateImage(selectedCheckbox, allCheckboxes);
        };
    }
    private void uncheckOtherCheckboxes(JCheckBox selectedCheckbox, JCheckBox[] allCheckboxes) {
        for (JCheckBox checkbox : allCheckboxes) {
            if (checkbox != selectedCheckbox) {
                checkbox.setSelected(false);
            }
        }
    }

    public void updateImage(JCheckBox selected, JCheckBox[] checkboxes){

        uncheckOtherCheckboxes(selected, checkboxes);

        String type = selected.getText();
        switch(type) {
            case "Weapon": parent.onButtonWeapon(); break;
            case "Armor": parent.onButtonArmor(); break;
            case "Consumable": parent.onButtonConsumable(); break;
            case "Clothing": parent.onButtonClothing(); break;
            case "Accessoire": parent.onButtonAccessoire(); break;
            case "Two-Handed":  parent.onButtonTwoHanded(selected.isSelected());System.out.println(selected.isSelected());break;
        };
    }


}
