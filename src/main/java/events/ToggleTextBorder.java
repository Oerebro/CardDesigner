package events;

public class ToggleTextBorder {
    public final Boolean bool;
    public final int type;

    public ToggleTextBorder(int type, Boolean bool) {
        this.bool = bool;
        this.type = type;
    }
}