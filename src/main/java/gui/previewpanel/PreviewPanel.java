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

    private BufferedImage backgroundLayer,imageLayer,frameLayer,crownLayer, textboxLayer,runecutLayer,titleLayer,infoLayer, attributeLabelLayer,tierLabelLayer,runechargeLabelLayer,typeLayer;

    public PreviewPanel(CardDesignerGUI parent){
        this.parent = parent;
        init();
    }

    
    private void init() {
        EventBus.subscribe(RepaintPanelEvent.class, this::onRepaintEvent);
        scaledWidth = (int) (parent.getFrameScale() * (750*panelRatio));
        scaledHeight = (int) (parent.getFrameScale() * (1050*panelRatio));

        object = new JPanel() {
            @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    double scale = parent.getFrameScale();
                    if (backgroundLayer != null)
                        g.drawImage(backgroundLayer, 0, 0, scaledWidth, scaledHeight, this);
                    if (imageLayer != null)
                        g.drawImage(imageLayer, 0, 0, scaledWidth, scaledHeight, this);
                    if (frameLayer != null)
                        g.drawImage(frameLayer, 0, 0, scaledWidth, scaledHeight, this);
                    if (runecutLayer != null)
                        g.drawImage(runecutLayer, 0, 0, scaledWidth, scaledHeight, this);
                    if (titleLayer != null)
                        g.drawImage(titleLayer, 0, 0, scaledWidth, scaledHeight, this);
                    if (infoLayer != null)
                        g.drawImage(infoLayer, 0, 0, scaledWidth, scaledHeight, this);
                    if (attributeLabelLayer != null)
                        g.drawImage(attributeLabelLayer, 0, 0, scaledWidth, scaledHeight, this);
                    if (tierLabelLayer != null)
                        g.drawImage(tierLabelLayer, 0, 0, scaledWidth, scaledHeight, this);
                    if (runechargeLabelLayer != null)
                        g.drawImage(runechargeLabelLayer, 0, 0, scaledWidth, scaledHeight, this);
                    if (typeLayer != null)
                        g.drawImage(typeLayer, 0, 0, scaledWidth, scaledHeight, this);
                }

        };
    
        object.setLayout(null);
        object.setPreferredSize(new Dimension(scaledWidth, scaledHeight));

        panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 1100));
        panel.add(object, BorderLayout.LINE_START);
        
        rescale(1.0);  
        EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_ALL));
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
        double scale = parent.getFrameScale();
        int type = e.type;

        switch (type) {
            case GlobalVar.REPAINT_BACKGROUND:
                backgroundLayer = parent.getComposedCard(scale * panelRatio, type);
                //frameLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_FRAME);
                break;
            case GlobalVar.REPAINT_IMAGE:
                imageLayer = parent.getComposedCard(scale * panelRatio, type);
                break;
            case GlobalVar.REPAINT_CROWN:
                frameLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_FRAME);
                break;
            case GlobalVar.REPAINT_TEXTBOX:
                frameLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_FRAME);
                break;
            case GlobalVar.REPAINT_FRAME:
                frameLayer = parent.getComposedCard(scale * panelRatio, type);
                break;
            case GlobalVar.REPAINT_RUNECUT:
                runecutLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_RUNECUT);
                break;
            case GlobalVar.REPAINT_TITLE:
                titleLayer = parent.getComposedCard(scale * panelRatio, type);
                break;
            case GlobalVar.REPAINT_INFO:
                infoLayer = parent.getComposedCard(scale * panelRatio, type);
                break;
            case GlobalVar.REPAINT_ATTRIBUTE_LABEL:
                attributeLabelLayer = parent.getComposedCard(scale * panelRatio, type);
                break;
            case GlobalVar.REPAINT_TIER_LABEL:
                tierLabelLayer = parent.getComposedCard(scale * panelRatio, type);
                break;
            case GlobalVar.REPAINT_RUNECHARGE_LABEL:
                runechargeLabelLayer = parent.getComposedCard(scale * panelRatio, type);
                break;
            case GlobalVar.REPAINT_TYPE:
                typeLayer = parent.getComposedCard(scale * panelRatio, type);
                break;
            case GlobalVar.REPAINT_ALL:
                backgroundLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_BACKGROUND);
                imageLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_IMAGE);
                frameLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_FRAME);
                runecutLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_RUNECUT);
                titleLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_TITLE);
                infoLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_INFO);
                attributeLabelLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_ATTRIBUTE_LABEL);
                tierLabelLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_TIER_LABEL);
                runechargeLabelLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_RUNECHARGE_LABEL);
                typeLayer = parent.getComposedCard(scale * panelRatio, GlobalVar.REPAINT_TYPE);
                break;
        }
        
        this.repaint();
    }


}


