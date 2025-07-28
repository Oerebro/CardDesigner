package abstractclasses;

import javax.swing.JPanel;

public interface TextComponent {
    JPanel getInputComponent();
    String getText();
    boolean hasBorder();
}
