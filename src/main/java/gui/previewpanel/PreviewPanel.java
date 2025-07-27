package gui.previewpanel;

import javax.swing.*;
import events.EventBus;
import events.RepaintPanelEvent;
import gui.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class PreviewPanel {
    public JPanel panel;
    private JPanel object;
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
        scaledWidth = (int) (parent.getFrameScale() * (baseWidth*panelRatio));
        scaledHeight = (int) (parent.getFrameScale() * (baseHeight*panelRatio));

        object = new JPanel() {
            @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    double scale = parent.getFrameScale();

                    for(int i = 0; i < imageLayers.length; i++){
                        System.out.print(imageLayers[i]);
                        g.drawImage(imageLayers[i], 0, 0, scaledWidth, scaledHeight, this);
                        g.drawImage(textLayers[i], 0, 0, scaledWidth, scaledHeight, this);
                    }
                    

                }

        };
    
        object.setLayout(null);
        object.setPreferredSize(new Dimension(scaledWidth, scaledHeight));

        panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 1100));
        panel.add(object, BorderLayout.LINE_START);
        
        rescale(1.0);  
        EventBus.publish(new RepaintPanelEvent());
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

    public void rescale(double scale){
        scaledWidth = (int) (parent.getFrameScale() * (750*panelRatio));
        scaledHeight = (int) (parent.getFrameScale() * (1050*panelRatio));

        panel.setBounds((int) (10*scale), (int) (10*scale), (int) (scaledWidth), (int) (scaledHeight));
        object.setPreferredSize(new Dimension((int) (scaledWidth), (int) (scaledHeight)));

        rescaleComponents(scale);
        panel.repaint();
    }

    public void rescaleComponents(double scale){
    }

    private void onRepaintEvent(RepaintPanelEvent e){
        int render = e.render;
        String type = e.type;

        double scale = parent.getFrameScale();
        scaledWidth = (int) (scale * baseWidth*panelRatio);
        scaledHeight = (int) (scale * baseHeight*panelRatio);
        System.out.println(e.type+" "+e.render);
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
            default:
            for(int i = 0; i < imageLayers.length; i++){
                imageLayers[i] = RenderManager.renderImageLayer(baseWidth, baseHeight, i, scale);
                textLayers[i] = RenderManager.renderTextLayer(baseWidth, baseHeight, i, scale);
            }
                
        }
        
        SwingUtilities.invokeLater( () -> panel.repaint());
        
    }

}


