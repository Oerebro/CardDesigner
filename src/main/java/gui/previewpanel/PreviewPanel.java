package gui.previewpanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import events.EventBus;
import events.RepaintPanelEvent;
import events.ResizeUpdate;
import gui.*;
import gui.image_composers.Card;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class PreviewPanel {
    public JPanel panel;
    private JPanel previewImage;
    private CardDesignerGUI parent;
    private double panelRatio = 0.7;
    private int scaledWidth,scaledHeight;
    private int baseWidth = 750;
    private int baseHeight = 1050;

    private BufferedImage[] imageLayers = new BufferedImage[6];
    private BufferedImage[] textLayers = new BufferedImage[6];

    public PreviewPanel(CardDesignerGUI parent){
        this.parent = parent;
        init();
    }

    
    private void init() {
        EventBus.subscribe(RepaintPanelEvent.class, this::onRepaintEvent);
        EventBus.subscribe(ResizeUpdate.class, this::onResizeUpdate);
        scaledWidth = (int) (parent.getFrameScale() * (baseWidth*panelRatio));
        scaledHeight = (int) (parent.getFrameScale() * (baseHeight*panelRatio));

        previewImage = new JPanel() {
            @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    for(int i = 0; i < imageLayers.length; i++){
                        g.drawImage(imageLayers[i], 0, 0, this.getWidth(), this.getHeight(), this);
                        g.drawImage(textLayers[i], 0, 0, this.getWidth(), this.getHeight(), this);
                    }
                    

                }

        };

        System.out.println("w:"+scaledWidth+" h:"+scaledHeight);
        Dimension dim = new Dimension(scaledWidth,scaledHeight);
        previewImage.setPreferredSize(dim);
        previewImage.setMaximumSize(dim);
    
        previewImage.setLayout(null);
        //previewImage.setPreferredSize(new Dimension(scaledWidth, scaledHeight));

        panel = new JPanel();
        panel.setPreferredSize(dim);
        panel.setMaximumSize(dim);
        panel.add(previewImage);
        previewImage.setBorder(new TitledBorder("Card Image"));
        panel.setBorder(new TitledBorder("Preview Panel"));
        
        //rescale(1.0);  
        EventBus.publish(new RepaintPanelEvent());
    }
    public void setResolution(int[] res){
        baseWidth = res[0];
        baseHeight = res[1];
    }
    public int[] getScaledDimensions(int imageWidth, int imageHeight, int maxWidth, int maxHeight) {
        // Compute scale factors for width and height
        double scaleX = (double) maxWidth / imageWidth;
        double scaleY = (double) maxHeight / imageHeight;
    
        // Choose the smaller scale factor to maintain aspect ratio
        double scaleFactor = Math.min(scaleX, scaleY);
    
        // Apply scaling, making sure neither dimension exceeds maxWidth or maxHeight
        int newWidth = (int) (imageWidth * scaleFactor*parent.getFrameScale());
        int newHeight = (int) (imageHeight * scaleFactor*parent.getFrameScale());
    
        // Return the scaled dimensions
        return new int[]{newWidth, newHeight};
    }

    public void repaint(){
        panel.repaint();
    }


    public void onResizeUpdate(ResizeUpdate e){
        Dimension dim = new Dimension((int)(baseWidth*e.scale),(int)(baseHeight*e.scale));
        previewImage.setPreferredSize(dim);
        previewImage.setMaximumSize(dim);

        panel.setPreferredSize(dim);
        panel.setMaximumSize(dim);
    }

    private void onRepaintEvent(RepaintPanelEvent e){
        int render = e.render;
        String type = e.type;

        double scale = parent.getFrameScale();
        scaledWidth = (int) (scale * baseWidth*panelRatio);
        scaledHeight = (int) (scale * baseHeight*panelRatio);
        if(render == -1){
            type = "all";
        }
        switch(type){
            case "image": {
                imageLayers[render] = RenderManager.renderImageLayer(baseWidth, baseHeight, render, scale);
                break;
            }
            case "text":
                textLayers[render] = RenderManager.renderTextLayer(baseWidth, baseHeight, render, scale);
                break;
            case "clearAllLayers":
                for(int i = 0; i<imageLayers.length;i++){
                    imageLayers[i] = null;
                    textLayers[i] = null;
                }
            default:
            for(int i = 0; i < imageLayers.length; i++){
                imageLayers[i] = RenderManager.renderImageLayer(baseWidth, baseHeight, i, scale);
                textLayers[i] = RenderManager.renderTextLayer(baseWidth, baseHeight, i, scale);
            }
                
        }
        
        SwingUtilities.invokeLater( () -> panel.repaint());
        
    }

}


