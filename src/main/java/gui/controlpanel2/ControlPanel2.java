package gui.controlpanel2;

import java.io.File;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import gui.CardDesignerGUI;
import abstractclasses.*;
import events.EventBus;
import events.VariableUpdate;


public class ControlPanel2 extends ControlPanel{

    private JLabel textboxLabel;
    private JButton  exportButton;
    private CardDesignerGUI parent;

    public void init(CardDesignerGUI parent){
        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        compose();
        rescale(1.0);
    }

    public void compose(){
        add(getExportButton());
        add(getPresetSelection());
    }
    

    public void rescale(double scale){
        setBounds((int)(10*scale),(int)(755*scale),(int)(750*scale),(int)(100*scale));
    }

    private JButton getExportButton(){
        exportButton = new JButton();
        exportButton.addActionListener(e -> parent.exportImage());
        return exportButton;
    }

    private JComboBox<String> getPresetSelection(){
        JComboBox<String> presetSelection = new JComboBox<>();
        // get main folder
        File folder1 = new File("resources/card_presets/");
        if (!folder1.exists() || !folder1.isDirectory()) {
            System.out.println("Preset folder not found.");
            return null;
        }
        
        // get all subfolders in that folder (arkham, dnd5e)
        File[] subfolders = folder1.listFiles(File::isDirectory);
        if (subfolders != null) {
            for (File subfolder : subfolders) {
                String folderName = subfolder.getName();
                // get the json files in those subfolders
                File[] jsonFiles = subfolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
                if (jsonFiles != null) {
                    for (File jsonFile : jsonFiles) {
                        // add the names of those json files to the JComboBox
                        presetSelection.addItem(folderName+"\\"+jsonFile.getName().replace(".json", ""));
                    }
                }
            }
        }

        presetSelection.setBorder(new TitledBorder("Select Card Preset"));

        presetSelection.addActionListener(e ->
            EventBus.publish(new VariableUpdate("card type", "", presetSelection.getSelectedItem()))
        );

        return presetSelection;
        
    }
}
