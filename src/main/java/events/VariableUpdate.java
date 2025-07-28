package events;

public class VariableUpdate {
    public Object var;
    public String type, id;
        public VariableUpdate(String type, String id, Object var){
            this.id = id;
            this.type = type;
            this.var = var;
        }
}
