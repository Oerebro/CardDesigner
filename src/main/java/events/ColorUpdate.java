package events;

import java.awt.Color;
public class ColorUpdate {
    public final Color color;
    public final int type;

    public ColorUpdate(int type,Color color) {
        this.color = color;
        this.type = type;
    }
}