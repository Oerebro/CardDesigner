package events;

public class TextLoadEvent {
    public static final int TITLE = 1;
    public static final int TYPE = 2;
    public static final int INFO = 3;
    public static final int RANGE = 4;
    public static final int USES = 5;
    public String text;
    public int type;
    public TextLoadEvent(String text, int type){
        this.type = type;
        this.text = text;
    }
}
