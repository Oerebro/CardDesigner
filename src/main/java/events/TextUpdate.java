package events;

public class TextUpdate {
    public final String text,type;
    public final String id;

    public TextUpdate(String type, String id, String txt){
        this.type = type;
        this.text = txt;
        this.id = id;

    }

    public TextUpdate(String id, String txt){
        this.id = id;
        this.text = txt;
        this.type= null;

    }
}
