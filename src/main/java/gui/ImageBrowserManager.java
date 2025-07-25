package gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTabbedPane;

import abstractclasses.ImageBrowser;

//lists to keep track which imagebrowser has registered which tab, to avoid unnecessary loading
/*keeps track of
    *all created tabs (to avoid re-creating them when switching card)
    *currently registered tabs in cardComponents
    *currently registered tabs in cardImages
*/

public class ImageBrowserManager {
    private static final Map<String, ImageBrowser> allComponents = new HashMap<>();
    private static final JTabbedPane cardComponents = new JTabbedPane();
    private static final JTabbedPane cardImages = new JTabbedPane();

    public static void registerTab(String tabName, ImageBrowser tab, String targetBrowser) {
        if(!isTabRegistered(tabName)){
            allComponents.put(tabName, tab);
        }
        
        switch (targetBrowser) {
            case "cardComponent":
                cardComponents.addTab(tabName, tab.getScrollPane());
                cardComponents.revalidate();
                cardComponents.repaint();
                break;
            case "cardImage":
                cardImages.addTab(tabName, tab.getScrollPane());
                cardImages.revalidate();
                cardImages.repaint();
                break;
        }
    }

    public static void removeTab(String tabName, String targetBrowser) {
        ImageBrowser browser = allComponents.get(tabName);
        if (browser == null) return;
        switch (targetBrowser) {
            case "cardComponent":
                cardComponents.remove(browser.getScrollPane());
                cardComponents.revalidate();
                cardComponents.repaint();
                break;
            case "cardImage":
                cardImages.remove(browser.getScrollPane());
                cardImages.revalidate();
                cardImages.repaint();
                break;
        }
    }

    public static void deregisterTab(String tabName) {
        ImageBrowser browser = allComponents.remove(tabName);
        if (browser == null) return;
        cardComponents.remove(browser.getScrollPane());
        cardImages.remove(browser.getScrollPane());
        cardImages.revalidate();
        cardImages.repaint();
        cardComponents.revalidate();
        cardComponents.repaint();
    }

    public static boolean isTabRegistered(String tabName) {
        return allComponents.containsKey(tabName);
    }

    public static JTabbedPane getCardComponents() { return cardComponents; }
    public static JTabbedPane getCardImages() { return cardImages; }
}

