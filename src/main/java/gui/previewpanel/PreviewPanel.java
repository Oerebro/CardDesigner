package gui.previewpanel;

import javax.swing.*;
import events.EventBus;
import events.RepaintPanelEvent;
import gui.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class PreviewPanel {
    public JPanel panel;
    private JPanel object;
    private CardDesignerGUI parent;
    private double panelRatio = 0.7;
    private int scaledWidth,scaledHeight;
    private int baseWidth = 750;
    private int baseHeight = 1050;

    private BufferedImage imageLayer0,imageLayer1,imageLayer2,imageLayer3,imageLayer4, imageLayer5;
    private BufferedImage textLayer0,textLayer1,textLayer2,textLayer3,textLayer4,textLayer5;

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

                    g.drawImage(imageLayer0, 0, 0, scaledWidth, scaledHeight, this);
                    g.drawImage(textLayer0, 0, 0, scaledWidth, scaledHeight, this);

                    g.drawImage(imageLayer1, 0, 0, scaledWidth, scaledHeight, this);
                    g.drawImage(textLayer1, 0, 0, scaledWidth, scaledHeight, this);

                    g.drawImage(imageLayer2, 0, 0, scaledWidth, scaledHeight, this);
                    g.drawImage(textLayer2, 0, 0, scaledWidth, scaledHeight, this);

                    g.drawImage(imageLayer3, 0, 0, scaledWidth, scaledHeight, this);
                    g.drawImage(textLayer3, 0, 0, scaledWidth, scaledHeight, this);

                    g.drawImage(imageLayer4, 0, 0, scaledWidth, scaledHeight, this);
                    g.drawImage(textLayer4, 0, 0, scaledWidth, scaledHeight, this);
                    
                    g.drawImage(imageLayer5, 0, 0, scaledWidth, scaledHeight, this);
                    g.drawImage(textLayer5, 0, 0, scaledWidth, scaledHeight, this);
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
        System.out.println("trying to render layer: "+render+" with type: "+type);

        switch(type){
            case "image": {
                renderImages(render);
                break;
            }
            case "text":
                renderText(render);
                break;
            default:
                renderImages(render);
                renderText(render);
        }
        
        this.repaint();
    }

    private void renderImages(int render){
        double scale = parent.getFrameScale();
        scaledWidth = (int) (scale * baseWidth*panelRatio);
        scaledHeight = (int) (scale * baseHeight*panelRatio);
        
        
        switch (render) {
                    case 0:
                        imageLayer0 = RenderManager.renderLayer(baseWidth, baseHeight, 0, scale);
                        break;
                    case 1:
                        imageLayer1 = RenderManager.renderLayer(baseWidth, baseHeight, 1, scale);
                        break;
                    case 2:
                        imageLayer2 = RenderManager.renderLayer(baseWidth, baseHeight, 2, scale);
                        break;
                    case 3:
                        imageLayer3 = RenderManager.renderLayer(baseWidth, baseHeight, 3, scale);
                        break;
                    case 4:
                        imageLayer4 = RenderManager.renderLayer(baseWidth, baseHeight, 4, scale);
                        break;
                    case 5:
                        imageLayer5 = RenderManager.renderLayer(baseWidth, baseHeight, 5, scale);
                        break;
                    default:
                        imageLayer0 = RenderManager.renderLayer(baseWidth, baseHeight, 0, scale);
                        imageLayer1 = RenderManager.renderLayer(baseWidth, baseHeight, 0, scale);
                        imageLayer2 = RenderManager.renderLayer(baseWidth, baseHeight, 0, scale);
                        imageLayer3 = RenderManager.renderLayer(baseWidth, baseHeight, 0, scale);
                        imageLayer4 = RenderManager.renderLayer(baseWidth, baseHeight, 0, scale);
                        imageLayer5 = RenderManager.renderLayer(baseWidth, baseHeight, 0, scale);

                        break;
                }
    }

    private void renderText(int render){
        scaledWidth = (int) (parent.getFrameScale() * (750*panelRatio));
        scaledHeight = (int) (parent.getFrameScale() * (1050*panelRatio));
        return;
    }

}


