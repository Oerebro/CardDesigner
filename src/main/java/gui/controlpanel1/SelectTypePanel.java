package gui.controlpanel1;
import javax.swing.*;

import abstractclasses.ControlPanel;
import events.EventBus;
import events.ImageUpdate;
import events.CardTypeUpdate;
//import events.ImageUpdateEvent;
import gui.CardDesignerGUI;
import gui.GlobalVar;

import java.awt.*;
import java.awt.event.ActionListener;

public class SelectTypePanel extends JPanel{
    private final ControlPanel1 parent;
    
    //private JCheckBox isWeapon, isArmor, isClothing, isAccessoire, isConsumable, isRune,isEffect, hasRuneCut;
    JCheckBox[] checkboxes;
    int[] buttonSize = {40,40};

    public SelectTypePanel(ControlPanel1 parent) {
        this.parent = parent; 
        createCheckBoxPanel();
    }


    public void createCheckBoxPanel() {
        JPanel typePanel = new JPanel();
        typePanel.setBorder(BorderFactory.createTitledBorder("Item Type"));
        typePanel.setLayout(new GridLayout(0, 2, 5, 5));
        //this.setPreferredSize(new Dimension(300,300));

        JButton isWeapon = new JButton("Weapon",loadIcon("resources/glyphs/buttons/weapon.png", buttonSize[0], buttonSize[1]));
        JButton isArmor = new JButton("Armor", loadIcon("resources/glyphs/buttons/armor.png", buttonSize[0], buttonSize[1]));
        JButton isAccessoire = new JButton("Accessoire", loadIcon("resources/glyphs/buttons/accessoire.png", buttonSize[0], buttonSize[1]));
        JButton isConsumable = new JButton("Consumable", loadIcon("resources/glyphs/buttons/potion.png", buttonSize[0], buttonSize[1]));
        JButton isRune = new JButton("Rune", loadIcon("resources/glyphs/buttons/rune.png", buttonSize[0], buttonSize[1]));
        JButton isEffect = new JButton("Effect", loadIcon("resources/glyphs/buttons/effect.png", buttonSize[0], buttonSize[1]));

        isWeapon.setHorizontalAlignment(SwingConstants.LEFT);
        isArmor.setHorizontalAlignment(SwingConstants.LEFT);
        isAccessoire.setHorizontalAlignment(SwingConstants.LEFT);
        isConsumable.setHorizontalAlignment(SwingConstants.LEFT);
        isRune.setHorizontalAlignment(SwingConstants.LEFT);
        isEffect.setHorizontalAlignment(SwingConstants.LEFT);

        typePanel.add(isWeapon);
        typePanel.add(isArmor);
        typePanel.add(isAccessoire);
        typePanel.add(isConsumable);
        typePanel.add(isRune);
        typePanel.add(isEffect);

        this.add(typePanel);

        
        

        // Collect all checkboxes in this panel in an array
        isWeapon.addActionListener(e -> {parent.updatePanel(GlobalVar.WEAPON);});
        isArmor.addActionListener(e -> {parent.updatePanel(GlobalVar.ARMOR);});
        isAccessoire.addActionListener(e -> {parent.updatePanel(GlobalVar.ACCESSOIRE);});
        isConsumable.addActionListener(e -> {parent.updatePanel(GlobalVar.CONSUMABLE);});
        isRune.addActionListener(e -> {parent.updatePanel(GlobalVar.RUNE);});
        isEffect.addActionListener(e -> {parent.updatePanel(GlobalVar.EFFECT);});

    }

    private ImageIcon loadIcon(String path, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(path);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    


}
