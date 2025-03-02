package gui_elements.controlpanel1;
import javax.imageio.ImageIO;
import javax.swing.*;

import gui_elements.CardDesignerGUI;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class SelectTypePanel {
    private final CardDesignerGUI parent;
    public JPanel menu;

    public SelectTypePanel(CardDesignerGUI o) {
        this.parent = o; 
        menu = createCheckBoxPanel();
    }

    public JPanel createCheckBoxPanel() {
        JPanel checkboxPanel = new JPanel(new GridLayout(2, 3, 5, 5));

        JCheckBox isWeapon = new JCheckBox("Weapon", false);
        JCheckBox isArmor = new JCheckBox("Armor", false);
        JCheckBox isClothing = new JCheckBox("Clothing", false);
        JCheckBox isAccessoire = new JCheckBox("Accessoire", false);
        JCheckBox isConsumable = new JCheckBox("Consumable", false);

        // Add the checkboxes to the panel
        checkboxPanel.add(isWeapon);
        checkboxPanel.add(isArmor);
        checkboxPanel.add(isClothing);
        checkboxPanel.add(isAccessoire);
        checkboxPanel.add(isConsumable);

        // Collect all checkboxes in this panel in an array
        JCheckBox[] checkboxes = {isWeapon, isArmor, isClothing, isAccessoire, isConsumable};

        // Add action listeners to each checkbox
        for (JCheckBox checkbox : checkboxes) {
            checkbox.addActionListener(createCheckboxListener(checkbox, checkboxes));
        }

        return checkboxPanel;
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
        selectedCheckbox.setSelected(true);
    }

    public void updateImage(JCheckBox selected, JCheckBox[] checkboxes){
        for(JCheckBox x:checkboxes){
            if(x != selected){
                x.setSelected(false);
            }
        } 
        selected.setSelected(true);
        String type = selected.getText();
        try{
            switch(type) {
                case "Weapon": parent.setCardType(ImageIO.read(new File("resources/weapon.png"))); break;
                case "Armor": parent.setCardType(ImageIO.read(new File("resources/armor.png"))); break;
                case "Consumable": parent.setCardType(ImageIO.read(new File("resources/consumable.png"))); break;
                case "Clothing": parent.setCardType(ImageIO.read(new File("resources/clothing.png"))); break;
                case "Accessoire": parent.setCardType(ImageIO.read(new File("resources/accessoire.png"))); break;
            }
        }catch (IOException e){
            JOptionPane.showMessageDialog(parent.getFrame(), "Error loading type image.","Error",JOptionPane.ERROR_MESSAGE);
        };
    }


}
