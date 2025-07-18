package abstractclasses;

import gui.controlpanel1.IconLabel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class ImageBrowser extends ImagePublisher {

    protected JPanel filePanel;
    protected JScrollPane scrollPane;
    protected int width = 400, height = 300, iconwidth = 66, iconHeight = 88;
    protected int x, y;

    protected void init() {
        

        this.height += 50;
        filePanel = new JPanel(new GridLayout(0, 3, 4, 4));
        filePanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        ImageIcon placeholderIcon = new ImageIcon(
            new BufferedImage(iconwidth, iconHeight, BufferedImage.TYPE_INT_ARGB));

        new SwingWorker<Void, IconLabel>() {
            @Override
            protected Void doInBackground() throws IOException {
                File folder = new File(path);
                File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
                if (files == null) return null;
                for (File file : files) {
                    IconLabel label = createIconLabel(placeholderIcon, file);
                    publish(label);
                }
                for (File file : files) {
                    ImageIcon thumbnail = loadScaledIcon(file, iconwidth, iconHeight);
                    if (thumbnail != null) {
                        SwingUtilities.invokeLater(() -> {
                            for (Component comp : filePanel.getComponents()) {
                                if (comp instanceof IconLabel) {
                                    IconLabel label = (IconLabel) comp;
                                    if (label instanceof FileIconLabel &&
                                        ((FileIconLabel) label).getFile().equals(file)) {
                                        label.setIcon(thumbnail);
                                        label.repaint();
                                        break;
                                    }
                                }
                            }
                        });
                    }
                }
                return null;
            }

            @Override
            protected void process(List<IconLabel> chunks) {
                for (IconLabel label : chunks) {
                    label = addMouseListener(label, ((FileIconLabel) label).getFile());
                    filePanel.add(label);
                }
                filePanel.revalidate();
                filePanel.repaint();
            }
        }.execute();

        JPanel container = new JPanel(new BorderLayout());
        container.add(filePanel, BorderLayout.CENTER);
        filePanel.setBounds(x, y, width, height);
        container.setBounds(x, y, width, height);
        scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(x, y, width, height);
        scrollPane.setPreferredSize(new Dimension(width, height));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(64);

    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void rescale(double scale) {
        scrollPane.setPreferredSize(new Dimension((int)(width * scale), (int)(height * scale)));
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    private IconLabel createIconLabel(ImageIcon icon, File file) {
        return new FileIconLabel(icon, file);
    }

    private ImageIcon loadScaledIcon(File file, int width, int height) {
        try {
            Image original = ImageIO.read(file);
            if (original == null) return null;
            Image scaled = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class FileIconLabel extends IconLabel {
        private final File file;

        public FileIconLabel(ImageIcon icon, File file) {
            super(icon);
            this.file = file;
        }

        public File getFile() {
            return file;
        }
    }

    protected IconLabel addMouseListener(IconLabel label, File file) {
        label.setOpaque(false);
        label.setBackground(Color.LIGHT_GRAY);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                publishImageUpdate(type, file.getAbsolutePath());
            }
        });
        return label;
    }
}
