package gui.image_composers.components;

import javax.swing.JComboBox;

import events.EventBus;
import events.FontUpdate;

import java.awt.Dimension;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FontSelection extends JComboBox<String> {
    private String id;
    public FontSelection(String id) {
        this.id = id;
        File folder = new File("resources/misc/fonts");
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Font folder not found.");
            return;
        }

        Pattern pattern = Pattern.compile("(.+)-(?i)(regular|bold|italic)\\.(ttf|otf)$");
        Set<String> familyNames = new HashSet<>();

        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            String name = file.getName();
            Matcher matcher = pattern.matcher(name);
            if (matcher.matches()) {
                familyNames.add(matcher.group(1));
            }
        }

        List<String> sortedList = new ArrayList<>(familyNames);
        Collections.sort(sortedList);
        for (String fontName : sortedList) {
            this.addItem(fontName);
        }
        Dimension dim = new Dimension(100,80);
        this.setPreferredSize(dim);
        this.setPreferredSize(dim);
        this.setMaximumSize(dim);
        setSelectedItem("PlantinMT Pro");
        addActionListener(e -> {EventBus.publish(new FontUpdate(this.id,this.getSelectedItem().toString()));});
    }
}
