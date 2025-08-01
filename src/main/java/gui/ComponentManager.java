package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import events.EventBus;
import events.TextUpdate;

//lists to keep track which textcomponents has registered which input field, to avoid unnecessary loading
/*keeps track of
    *all created input fields (to avoid re-creating them when switching card)

*/

public class ComponentManager {
    private static final Map<String, JPanel> allComponents = new HashMap<>();

//initialize the panels with layout
    private static JPanel leftSide;
    private static JPanel rightSide;

    private static final Map<String, JPanel> attributesMap = new HashMap<>();
    private static List<JPanel> attributes = new ArrayList<>();

    static {
        leftSide = new JPanel();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));
        leftSide.setBorder(BorderFactory.createTitledBorder("leftSide"));
        rightSide = new JPanel();
        rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));
        rightSide.setBorder(BorderFactory.createTitledBorder("rightSide"));
        EventBus.subscribe(TextUpdate.class, e -> onTextUpdate(e));
    }

    public static void reset(){
        attributesMap.clear();
        leftSide.removeAll();
        rightSide.removeAll();
    }

    private static void onTextUpdate(TextUpdate e){
        if(e.type != null && (e.type.equals("ComponentManager.insertText"))){
            System.out.println("Trying to insert into: "+e.id);
            for(Map.Entry<String,JPanel> entry : allComponents.entrySet()){
                if(entry.getKey().equals(e.id)){
                    System.out.println(e.id);
                    ((JTextComponent) entry.getValue().getComponent(1)).setText(e.text);
                }
            }
        }
    }

    public static void registerComponents(String componentID, JPanel component, String targetPanel) {
        if(!isComponentRegistered(componentID)){
            allComponents.put(componentID, component);
        }

        switch (targetPanel) {
            case "leftSide":
                leftSide.add(componentID, allComponents.get(componentID));
                leftSide.revalidate();
                leftSide.repaint();
                break;
            case "rightSide":
                rightSide.add(componentID, allComponents.get(componentID));
                rightSide.revalidate();
                rightSide.repaint();
                break;
        }
    }

    public static void registerCardAttribute(String updateID, JComponent comp){
        if(!attributesMap.containsKey(updateID)){
            //attributes with new updateID will be put into new JPanel
            JPanel panel = new JPanel(new FlowLayout());
            panel.add(comp);
            panel.setBorder(new TitledBorder(updateID));
            panel.setPreferredSize(new Dimension(400,200));
            panel.setMaximumSize(new Dimension(400,150));
            //add new Panel to map
            attributesMap.put(updateID,panel);
            rightSide.add(panel);
            rightSide.revalidate();
            rightSide.repaint();
            
        }else{
            //attributes with the same updateID will be grouped into the same JPanel
            JPanel panel = attributesMap.get(updateID);
            panel.add(comp);
            rightSide.revalidate();
            rightSide.repaint();
        }
        
    }

    public static void removeComponent(String componentID, String targetPanel) {
        JPanel panel = allComponents.get(componentID);
        if (panel == null) return;
        switch (targetPanel) {
            case "leftSide":
                leftSide.remove(panel);
                leftSide.revalidate();
                leftSide.repaint();
                break;
            case "rightSide":
                rightSide.remove(panel);
                rightSide.revalidate();
                rightSide.repaint();
                break;
        }
    }

    public static void deregisterComponent(String componentID) {
        JPanel panel = allComponents.remove(componentID);
        if (panel == null) return;
        leftSide.remove(panel);
        rightSide.remove(panel);
        rightSide.revalidate();
        rightSide.repaint();
        leftSide.revalidate();
        leftSide.repaint();
    }

    public static boolean isComponentRegistered(String componentID) {
        return allComponents.containsKey(componentID);
    }

    public static JPanel getLeftSide() { return leftSide; }
    public static JPanel getRightSide() { return rightSide; }
}

