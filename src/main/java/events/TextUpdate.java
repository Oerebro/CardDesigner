package events;

public class TextUpdate {
    public String text;
    public int type;
    public TextUpdate(int type, String txt){
        this.type = type;
        this.text = txt;

    }
}
