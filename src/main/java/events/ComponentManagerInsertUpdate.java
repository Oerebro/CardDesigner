package events;

public class ComponentManagerInsertUpdate {
    public final String text,alignement,fontName,hasBorder,color;
    public final String id;

    public ComponentManagerInsertUpdate(String id, String text,String alignement,String fontName,String hasBorder,String color){
        this.text = text;
        this.id = id;
        this.alignement = alignement;
        this.fontName = fontName;
        this.hasBorder = hasBorder;
        this.color = color;

    }

}
