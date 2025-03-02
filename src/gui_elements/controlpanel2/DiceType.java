package gui_elements.controlpanel2;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

public class DiceType {
        private void updateDamageDie(JCheckBox selected, JCheckBox[] checkboxes){
        for(JCheckBox x:checkboxes){
            if(x != selected){
                x.setSelected(false);
            }
        } 
        selected.setSelected(true);
        String type = selected.getText();
        try{
            switch(type) {
                case "Weapon": cardType = ImageIO.read(new File("resources/dice/d4.png")); break;
                case "Armor": cardType = ImageIO.read(new File("resources/dice/d6.png")); break;
                case "Consumable": cardType = ImageIO.read(new File("resources/dice/d8.png")); break;
                case "Clothing": cardType = ImageIO.read(new File("resources/dice/d10.png")); break;
                case "Accessoire": cardType = ImageIO.read(new File("resources/dice/d12.png")); break;
            }
        }catch (IOException e){
            JOptionPane.showMessageDialog(frame, "Error loading damage die image.","Error",JOptionPane.ERROR_MESSAGE);
        };
        previewPanel.repaint();
    }
}
