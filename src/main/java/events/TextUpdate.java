package events;

public class TextUpdate {
    public String text;
    public int type;
    public String id;
    public TextUpdate(int type, String txt){
        this.type = type;
        this.text = txt;

    }

    public TextUpdate(String id, String txt){
        this.id = id;
        this.text = txt;

    }
}
