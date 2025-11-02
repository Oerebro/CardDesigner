package gui;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.formdev.flatlaf.FlatDarkLaf;

import events.CardLoadEvent;
import events.CardTypeUpdate;
import events.ColorUpdate;
import events.ComponentManagerInsertUpdate;
import events.EventBus;
import events.FontUpdate;
import events.ImageUpdate;
import events.RepaintPanelEvent;
import events.ResizeUpdate;
import events.TextAlignUpdate;
import events.TextUpdate;
import events.VariableUpdate;
import gui.card_types.*;
import gui.controlpanel1.*;
import gui.controlpanel2.*;
import gui.image_composers.*;
import gui.previewpanel.*;


public class CardDesignerGUI {
    public JFrame frame;
    private int loadedCardType;
    private Card imageComposer;
    private PreviewPanel previewPanel;
    ControlPanel1 controlPanel;
    ControlPanel2 controlPanel2;
    private boolean hasBleedEdge = false;
    private String preset;
    private static final ObjectMapper MAPPER = new ObjectMapper();


    public Frame getFrame(){
        return frame;
    }
  

    public CardDesignerGUI() {     
        String defaultPreset = "dnd5e\\weapon";
        
        
        frame = new JFrame("Card Designer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLayout(new BorderLayout());

        createTopMenuBar(frame);

        // Preview Panel on the left
        previewPanel = new PreviewPanel(this);
        setCardType(defaultPreset);

        // Control Panel on the right
        controlPanel = new ControlPanel1();
        controlPanel.init(this);
        //controlPanel.setBounds(600,0,1200,800);
        controlPanel.setPreferredSize(new Dimension(1300, 800));
        controlPanel.setBorder(BorderFactory.createTitledBorder("controlPanel"));

        controlPanel2 = new ControlPanel2();
        controlPanel2.init(this);
        controlPanel2.setSelected(defaultPreset);
        
        frame.add(previewPanel.panel,BorderLayout.WEST);
        frame.add(controlPanel,BorderLayout.EAST);
        frame.add(controlPanel2, BorderLayout.SOUTH);
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> {
            EventBus.publish(new CardLoadEvent());
            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    previewPanel.repaint();
                }
            });
            frame.addWindowStateListener(new WindowStateListener() {
                @Override
                public void windowStateChanged(WindowEvent e) {
                    if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
                        EventBus.publish(new ResizeUpdate(getFrameScale()));
                    } else {
                        EventBus.publish(new ResizeUpdate(getFrameScale()));
                    }
                }
            });
        });

        EventBus.subscribe(VariableUpdate.class, this::onCardTypeUpdate);
        createAutoSave();
    }

    public double getFrameScale() {
        double scaleX = (double) frame.getWidth() / 1920.0;
        double scaleY = (double) frame.getHeight() / 1080.0;
        
        return Math.min(scaleX, scaleY);
    }

    private void rescaleComponents(){
        double scale = getFrameScale();

        //previewPanel.rescale(scale);
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

        Action exportAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportCard();
            }
        };

        Action loadAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCard();
            }
        };

        KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(saveKeyStroke, "saveAction");
        frame.getRootPane().getActionMap().put("saveAction", saveAction);

        KeyStroke exportKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(exportKeyStroke, "exportAction");
        frame.getRootPane().getActionMap().put("exportAction", exportAction);

        KeyStroke loadKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(loadKeyStroke, "loadAction");
        frame.getRootPane().getActionMap().put("loadAction", loadAction);


        fileMenu.add(newCardItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
    }

    private void createAutoSave(){
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
                autoSave();
        };
        scheduler.scheduleAtFixedRate(task, 180, 180, TimeUnit.SECONDS);
    }

    private void saveCard(){
        saveCard(RenderManager.getTitleText());
    }

    private void autoSave(){
        String name = "autosave_"+RenderManager.getTitleText();
        if(name.equals("autosave_")){
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            name += timestamp + ".card";
        }else{
            name += ".card";
        }

        saveCard(name);   
    }

    private void saveCard(String name) {
        if (name.equals("")) {
            JOptionPane.showMessageDialog(
                frame,
                "Please set a card title before saving.",
                "Missing Card Title",
                JOptionPane.WARNING_MESSAGE
            );
            return; // Stop execution until title is set
        }

       
        File saveDir = new File("saved");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        File fileToSave = new File(saveDir, name);
        if(!name.contains("autosave")){
            // Get the suggested file name
            File suggestedFile = new File(saveDir, name + ".card");

            // Configure file chooser
            JFileChooser fileChooser = new JFileChooser(saveDir);
            fileChooser.setDialogTitle("Save Card Configuration");
            fileChooser.setSelectedFile(suggestedFile); // <-- This sets the default filename

            int userSelection = fileChooser.showSaveDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                fileToSave = fileChooser.getSelectedFile();

                // Ensure it ends with .card
                if (!fileToSave.getName().toLowerCase().endsWith(".card")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".card");
                }
            }
        }  

        try {
                ObjectNode node = RenderManager.saveToNode(this.preset);
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(fileToSave, node);
                System.out.println("Card saved to: " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    private void loadCard() {
        JFileChooser chooser = new JFileChooser(new File("saved"));
        chooser.setFileFilter(new FileNameExtensionFilter("Card files", "card"));
        
        int result = chooser.showOpenDialog(frame);
        if (result != JFileChooser.APPROVE_OPTION) {
            // User cancelled or closed dialog, do nothing
            return;
        }

        File selectedFile = chooser.getSelectedFile();

        if (selectedFile == null || !selectedFile.exists()) {
            System.err.println("Invalid card file selected.");
            return;
        }

        List<ComponentManagerInsertUpdate> textUpdates = new ArrayList<>();
        List<ImageUpdate> imageUpdates = new ArrayList<>();

        try {
            JsonNode root = MAPPER.readTree(selectedFile);
            //load preset
            setCardType(root.path("preset").asText(""));

            JsonNode config = root.path("config");
            if (!config.isArray()) {
                System.err.println("Expected 'config' to be a JSON array.");
                return;
            }

            for (JsonNode field : config) {
                String type = field.path("type").asText("");
                String id = field.path("id").asText("");

                if (type.equals("text")) {
                    textUpdates.add(new ComponentManagerInsertUpdate(
                        id,
                        field.path("text").asText(""),
                        Integer.toString(field.path("alignement").asInt(0)),
                        field.path("font").asText(""),
                        Boolean.toString(field.path("hasBorder").asBoolean(true)),
                        field.path("color").asText("")
                        
                    ));
                    System.out.println("Publishing Text: "+ id);
                      
                } else {
                    imageUpdates.add(new ImageUpdate(id, field.path("path").asText("")));
                    System.out.println("Publishing Image: "+ id);
                }
            }

            SwingUtilities.invokeLater(() -> {
                textUpdates.forEach(EventBus::publish);
                //imageUpdates.forEach(EventBus::publish);
            });

        } catch (IOException e) {
            System.err.println("Failed to load card: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void exportCard() {
        double scale = 1.5;

        BufferedImage finalImage = RenderManager.renderAll(imageComposer,scale, hasBleedEdge);

        try {
            File outputfile = new File("export//"+generateDateTimeString()+" "+RenderManager.getTitleText()+".png");
            ImageIO.write(finalImage, "PNG", outputfile);
            JOptionPane.showMessageDialog(frame, "Image exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error exporting image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        
    }

    public static String generateDateTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        Date now = new Date();
        return sdf.format(now);
    }

    private void onCardTypeUpdate(VariableUpdate e){
        if(e.type.equals("card type")){
            ObjectNode node = RenderManager.saveToNode(this.preset);
            setCardType((String) e.var);
            SwingUtilities.invokeLater(()->{transferText(node);});
        }
    }

    private void transferText(ObjectNode node){
        JsonNode config = node.path("config");
        if (!config.isArray()) {
            System.err.println("Error when transferring text to new Card");
            return;
        }

        for (JsonNode field : config) {
            String type = field.path("type").asText("");
            String id = field.path("id").asText("");
            String fontName= field.path("font").asText("");

            if (type.equals("text")) {
                String text = field.path("text").asText("");
                EventBus.publish(new TextUpdate(id, text));
                EventBus.publish(new FontUpdate(id, fontName));
                EventBus.publish(new TextUpdate("ComponentManager.insertText", id, text));     
            }
        }
    }

    private void setCardType(String preset){
        if(preset.equals(this.preset)){
            return;
        }
        
        this.preset = preset;
        //clear all images and text in render pipeline
        RenderManager.reset();
        //clear all input components from controlpanel
        ComponentManager.reset();
        //clear imageBrowsers
        ImageBrowserManager.reset();

        SwingUtilities.invokeLater(() -> {
            imageComposer = new Card(preset);
            if(controlPanel != null)
            controlPanel.reset();
            previewPanel.setResolution(imageComposer.getResolution());
            controlPanel.revalidate();
            controlPanel.repaint();
        });
        
        
    }
    
    

    public static void main(String[] args) {
        try {
        UIManager.setLookAndFeel(new FlatDarkLaf());
        } 
        catch (UnsupportedLookAndFeelException e) {
        }
        SwingUtilities.invokeLater(CardDesignerGUI::new);
    }

    
}
