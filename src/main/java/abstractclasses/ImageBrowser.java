package abstractclasses;

import javax.swing.*;

import events.EventBus;
import events.ImageUpdate;
import gui.CardDesignerGUI;
import gui.controlpanel1.IconLabel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;;

public class ImageBrowser extends ImagePublisher {

    protected CardDesignerGUI parent;
    protected JPanel filePanel;
    protected JScrollPane scrollPane;
    protected int width = 0, height = 0, iconWidth = 66, iconHeight = 88, x, y;
    protected String name,type, sourcePath;


    public ImageBrowser(String name, String sourcePath, String id){
        System.out.println("Browser id: "+id);
        this.name = name;
        this.id = id;
        this.sourcePath = sourcePath;
    }

    public void init() {
        System.out.println("ImageBrowser init");
        //filePanel = new JPanel(new GridLayout(0, 4, 1, 0));
        filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        filePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));

        new SwingWorker<Void, IconLabel>() {
            @Override
            protected Void doInBackground() throws IOException {
                File folder = new File(sourcePath);
                File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));

                if (files == null) {
                    return null;
                }

                for (File file : files) {
                    ImageIcon icon = new ImageIcon(new ImageIcon(file.getAbsolutePath())
                            .getImage()
                            .getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));

                    IconLabel label = new IconLabel(icon);
                    label = addMouseListener(label, file.getPath());
                    publish(label);
                }
                return null;
            }

            @Override
            protected void process(java.util.List<IconLabel> chunks) {
                for (IconLabel label : chunks) {
                    filePanel.add(label);
                }
                filePanel.revalidate();
                filePanel.repaint();
            }
        }.execute();

        JPanel container = new JPanel();
        container.add(filePanel);

        scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0, 0, width, height);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        container.revalidate();
        container.repaint();

    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void rescale(double scale) {
        scrollPane.setBounds(0, 0, (int) (width * scale), (int) (scale * scale));
        scrollPane.setPreferredSize(new Dimension((int) (width * scale), (int) (height * scale)));
    }

    protected IconLabel addMouseListener(IconLabel label, String path){
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventBus.publish(new ImageUpdate(id,path));
            }
        });
        return label;
    }

    // Abstract methods for subclasses to implement
    
}
