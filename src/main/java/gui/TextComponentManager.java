package gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

//lists to keep track which textcomponents has registered which input field, to avoid unnecessary loading
/*keeps track of
    *all created input fields (to avoid re-creating them when switching card)

*/

public class TextComponentManager {
    private static final Map<String, JPanel> allComponents = new HashMap<>();


//initialize the panels with layout
    private static final JPanel leftSide;
    private static final JPanel rightSide;
    static {
        leftSide = new JPanel();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));
        leftSide.setBorder(BorderFactory.createTitledBorder("leftSide"));
        rightSide = new JPanel();
        rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));
        rightSide.setBorder(BorderFactory.createTitledBorder("rightSide"));
    }

    public static void registerComponents(String componentID, JPanel component, String targetPanel) {
        if(!isComponentRegistered(componentID)){
            allComponents.put(componentID, component);
        }
        switch (targetPanel) {
            case "leftSide":
                leftSide.add(componentID, component);
                leftSide.revalidate();
                leftSide.repaint();
                break;
            case "rightSide":
                rightSide.add(componentID, component);
                rightSide.revalidate();
                rightSide.repaint();
                break;
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

