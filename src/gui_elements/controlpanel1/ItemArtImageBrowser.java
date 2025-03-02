package gui_elements.controlpanel1;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import gui_elements.CardDesignerGUI;

public class ItemArtImageBrowser{
    private final CardDesignerGUI parent;
    private JPanel filePanel;
    private JScrollPane scrollPane;
    private String path;
    private int width;
    private int height;
    private char type;

    public ItemArtImageBrowser(CardDesignerGUI parent, String path, int width, int height,char type) {
        this.type=type;
        this.width=width;
        this.height=height;
        this.path = path;
        this.parent = parent;
        init();
        rescale(1.0);
    }

    private void init() {
       filePanel = new JPanel();
        filePanel.setLayout(new GridLayout(0, 4, 1, 0)); // 3 columns, unlimited rows
        filePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0,1)); // Spacing

        loadImagesFromPath(path);

        JPanel container = new JPanel();
        //container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(filePanel);

        scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0,0,440,350); // Fits 3 rows
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        rescale(1.0);
    }

    private void loadImagesFromPath(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));

        if (files == null || files.length == 0) {
            filePanel.add(new JLabel("No Files Found"));
            return;
        }

        for (File file : files) {
            ImageIcon icon = new ImageIcon(new ImageIcon(file.getAbsolutePath()).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            IconLabel label = new IconLabel(icon);

            // Limit file name to 10 characters
            String fileName = file.getName().replace(".png", "");
            if (fileName.length() > 10) {
                fileName = fileName.substring(0, 10) + "...";
            }

            label.setIcon(icon);
            //label.setText(fileName);
            label.setVerticalTextPosition(JLabel.BOTTOM);
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);

            // Add click event to update image
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    updateImage(file);
                }
            });

            filePanel.add(label);
        }
    }

    private void updateImage(File file) {
        try {
            System.out.println("updateImage");
            parent.setCardItemImage(ImageIO.read(file));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent.getFrame(), "Error loading new image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void rescale(double scale){
        scrollPane.setBounds((int) (600*scale), (int) (10*scale), (int) (width*scale), (int) (scale*scale));
        scrollPane.setPreferredSize(new Dimension((int)(440*scale),(int)(350*scale)));
        //filePanel.setPreferredSize(new Dimension((int) (width*scale), (int) (height*scale)));
    }
}
