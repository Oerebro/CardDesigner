package gui_elements;

import javax.swing.*;
import gui_elements.controlpanel1.*;
import gui_elements.controlpanel2.*;
import gui_elements.previewpanel.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.awt.event.*;

public class CardDesignerGUI {
    private JFrame frame;
    //private JLabel frameLabel, backgroundLabel, textBoxLabel;
    private BufferedImage cardFrame, cardBackground, cardTextBox, cardTitleImage,cardItemImage, cardWeight, cardType, cardRune, cardEnchant, cardRange, cardTargets, cardDamageType;
    private PreviewPanel previewPanel;
    ControlPanel1 controlPanel;
    ControlPanel2 controlPanel2;

    public void loadImagePreviewPanel(String i){
        previewPanel.loadImage(i);
    }
    
    public JPanel getPreviewPanel() {
        return previewPanel.panel;
    }

    public BufferedImage getCardType(){
        return cardType;
    }

    public BufferedImage getTitleImage(){
        return cardTitleImage;
    }

    public JFrame getFrame(){
        return frame;
    }

    public BufferedImage getCardBackground(){
        return cardBackground;
    }
    public BufferedImage getCardTextbox(){
        return cardTextBox;
    }
    public BufferedImage getCardFrame(){
        return cardFrame;
    }
    public BufferedImage getCardItemImage(){
        return cardItemImage;
    }

    public void setCardFrame(BufferedImage i){
        cardFrame = i;
        previewPanel.repaint();
    }
    public void setCardTextbox(BufferedImage i){
        cardTextBox = i;
        previewPanel.repaint();
    }
    public void setCardBackground(BufferedImage i){
        cardBackground = i;
        previewPanel.repaint();
    }
    public void setCardType(BufferedImage i){
        cardType = i;
        previewPanel.repaint();
    }

    public void setCardTitleImage(BufferedImage i){
        cardTitleImage = i;
        previewPanel.repaint();
    }
    public void setCardItemImage(BufferedImage i){
        cardTitleImage = i;
        previewPanel.repaint();
    }
    public CardDesignerGUI() {

        frame = new JFrame("Card Designer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLayout(null);
        
        // Preview Panel on the left
        previewPanel = new PreviewPanel(this);
        previewPanel.loadDefault();

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
            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    rescaleComponents(); // This should be called after all components are loaded
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
        double scaleX = (double) frame.getWidth() / 1920;
        double scaleY = (double) frame.getHeight() / 1080;
        //System.out.println(Math.min(scaleX, scaleY));
        return Math.min(scaleX, scaleY);
    }

    private void rescaleComponents(){
        double scale = getFrameScale();

        previewPanel.rescale(scale);
        controlPanel.rescale(scale);
        controlPanel2.rescale(scale);
    }


    public static void main(String[] args) {
        try {
            // Set System L&F
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException e) {
        // handle exception
        }
        catch (ClassNotFoundException e) {
        // handle exception
        }
        catch (InstantiationException e) {
        // handle exception
        }
        catch (IllegalAccessException e) {
        // handle exception
        }
        SwingUtilities.invokeLater(CardDesignerGUI::new);
    }

    
}
