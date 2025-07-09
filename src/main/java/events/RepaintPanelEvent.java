package events;

import gui.GlobalVar;

public class RepaintPanelEvent {
    public final int type;

        public RepaintPanelEvent(){
            this.type=GlobalVar.REPAINT_ALL;
        }

        public RepaintPanelEvent(int type){
            this.type = type;
        }
}


