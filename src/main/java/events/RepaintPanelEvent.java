package events;

public class RepaintPanelEvent {
    public final String type;
    public final int render;

//repaint all
        public RepaintPanelEvent(){
            this.type = "all";
            this.render = -1;
        }
//repaint speicifc layer
//type is either image or text
        public RepaintPanelEvent(String type, int render){
            this.type = type;
            this.render = render;
        }
}


