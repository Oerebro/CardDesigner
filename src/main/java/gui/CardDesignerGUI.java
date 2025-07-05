package gui;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.io.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.FlatDarkLaf;

import events.CardLoadEvent;
import events.CardTypeUpdate;
import events.EventBus;
import events.RepaintPanelEvent;
import gui.card_types.*;
import gui.controlpanel1.*;
import gui.controlpanel2.*;
import gui.image_composers.*;
import gui.image_composers.cardTypes.CharacterCardComposer;
import gui.image_composers.cardTypes.EffectCardComposer;
import gui.image_composers.cardTypes.itemTypes.AccessoireCardComposer;
import gui.image_composers.cardTypes.itemTypes.ConsumableCardComposer;
import gui.image_composers.cardTypes.itemTypes.RuneCardComposer;
import gui.image_composers.cardTypes.itemTypes.equippableTypes.ArmorCardComposer;
import gui.image_composers.cardTypes.itemTypes.equippableTypes.weaponTypes.WeaponMeleeCardComposer;
import gui.image_composers.cardTypes.itemTypes.equippableTypes.weaponTypes.WeaponRangedCardComposer;
import gui.image_composers.cardTypes.itemTypes.equippableTypes.weaponTypes.WeaponThrowableCardComposer;
import gui.previewpanel.*;


public class CardDesignerGUI {
    public JFrame frame;
    private CardComposer imageComposer;
    private PreviewPanel previewPanel;
    ControlPanel1 controlPanel;
    ControlPanel2 controlPanel2;


    public Frame getFrame(){
        return frame;
    }
  

    public CardDesignerGUI() {     
        EventBus.subscribe(CardTypeUpdate.class, this::onCardTypeUpdate);   
        setImageComposerType(GlobalVar.W_MELEE);
        
        frame = new JFrame("Card Designer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLayout(null);

        createTopMenuBar(frame);

        // Preview Panel on the left
        previewPanel = new PreviewPanel(this);
        //previewPanel.loadDefault();
        
        

        // Control Panel on the right
        controlPanel = new ControlPanel1();
        controlPanel.init(this);

        controlPanel2 = new ControlPanel2();
        controlPanel2.init(this);
        
        frame.add(previewPanel.panel);
        frame.add(controlPanel);
        frame.add(controlPanel2, BorderLayout.SOUTH);
        frame.setVisible(true);

        frame.setLayout(null);

        SwingUtilities.invokeLater(() -> {
            EventBus.publish(new CardLoadEvent());
            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    rescaleComponents();
                }
            });
            frame.addWindowStateListener(new WindowStateListener() {
                @Override
                public void windowStateChanged(WindowEvent e) {
                    if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
                        rescaleComponents();
                    } else {
                        rescaleComponents();
                    }
                }
            });
        });
    }

    public double getFrameScale() {
        double scaleX = (double) frame.getWidth() / 1920.0;
        double scaleY = (double) frame.getHeight() / 1080.0;

        return Math.min(scaleX, scaleY);
    }

    private void rescaleComponents(){
        double scale = getFrameScale();

        previewPanel.rescale(scale);
        controlPanel.rescale(scale);
        controlPanel2.rescale(scale);
    }

    private void createTopMenuBar(JFrame frame){
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newCardItem = new JMenuItem("New Card...");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");
        JMenuItem exitItem = new JMenuItem("Exit");

        newCardItem.addActionListener(e -> {
            EventBus.publish(new CardLoadEvent());});

        saveItem.addActionListener(e -> {
            saveCard();
        });

        loadItem.addActionListener(e -> {
            loadCard();
        });

        // 1. Create an action for saving
        Action saveAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCard();
            }
        };

        // 2. Bind Ctrl+S to the action
        KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(saveKeyStroke, "saveAction");
        frame.getRootPane().getActionMap().put("saveAction", saveAction);


        fileMenu.add(newCardItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
    }

    private void saveCard(){
        File saveDir = new File("saved");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        JFileChooser fileChooser = new JFileChooser(saveDir);
        fileChooser.setDialogTitle("Save Card Configuration");

        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Ensure it ends with .card
            if (!fileToSave.getName().toLowerCase().endsWith(".card")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".card");
            }
            

            try {
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(fileToSave, imageComposer.saveConfig() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void loadCard() {
        File savedDir = new File("saved");
        if (!savedDir.exists()) {
            savedDir.mkdirs(); // Ensure the directory exists
        }

        JFileChooser fileChooser = new JFileChooser(savedDir);
        fileChooser.setDialogTitle("Load Card Configuration");

        // Filter for .card files only
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Card Files (*.card)", "card");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(frame); // 'frame' is your main JFrame

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imageComposer.loadFromConfig(selectedFile);
        }
    }

    public void exportImage() {
        /*int targetWidth = 750;
        int targetHeight = 1050;

        double previewScaleWidth = 750.0*0.7;
        double previewScaleHeight = 1050.0*0.7;*/

        //WHY DOES THIS STUPID THING WORK PERFECTLY WITH 0.7 RESOLUTION BUT NOT ANYTHING ELSE??? The stupid effing font just doesnt scale up.
        double scale = 2.0;

        BufferedImage finalImage = getComposedCard(scale);

        Graphics2D g2d = finalImage.createGraphics();

        g2d.setColor(Color.WHITE);
        //g2d.fillRect(0, 0, targetWidth, targetHeight);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        try {
            File outputfile = new File("export//"+generateDateTimeString()+".png");
            ImageIO.write(finalImage, "PNG", outputfile);
            JOptionPane.showMessageDialog(frame, "Image exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error exporting image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }


        rescaleComponents();
        
    }

    public static String generateDateTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        Date now = new Date();
        return sdf.format(now);
    }

    public void updateTitleTextDisplay(String str, Font font) {
        previewPanel.updateTitleTextDisplay(str, font);
    }
    

    public void updateRange(String str, Font font) {
        previewPanel.updateRangeText(str, font);
    }

    private void onCardTypeUpdate(CardTypeUpdate e){
        setImageComposerType(e.type);
    }

    private void setImageComposerType(int type){
        switch(type){
            case GlobalVar.W_MELEE: imageComposer = new WeaponMeleeCardComposer(); break;
            case GlobalVar.W_RANGED: imageComposer = new WeaponRangedCardComposer(); break;
            case GlobalVar.W_THROWABLE: imageComposer = new WeaponThrowableCardComposer(); break;
            case GlobalVar.CHARACTER: imageComposer = new CharacterCardComposer(); break;
            case GlobalVar.EFFECT: imageComposer = new EffectCardComposer(); break;
            case GlobalVar.ARMOR: imageComposer = new ArmorCardComposer(); break;
            case GlobalVar.CONSUMABLE: imageComposer = new ConsumableCardComposer(); break;
            case GlobalVar.RUNE: imageComposer = new RuneCardComposer(); break;
            case GlobalVar.ACCESSOIRE: imageComposer = new AccessoireCardComposer(); break;

        }

        //EventBus.publish(new RepaintPanelEvent());
    }
    
    

    public BufferedImage getComposedCard(double scale){
        return imageComposer.composeCard(scale);
    }



    public static void main(String[] args) {
        try {
            // Set System L&F
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.setLookAndFeel(new FlatDarkLaf());
        } 
        catch (UnsupportedLookAndFeelException e) {
        // handle exception
        }

        //run ui thread
        SwingUtilities.invokeLater(CardDesignerGUI::new);
    }

    
}
