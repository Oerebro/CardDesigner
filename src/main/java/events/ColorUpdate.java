package events;

import java.awt.Color;
public class ColorUpdate {
    public final Color color;
    public final String id;

    public ColorUpdate(String id,Color color) {
        this.color = color;
        this.id = id;
    }
}