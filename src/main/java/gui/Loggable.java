package gui;

public class Loggable {
    protected void log(Object msg){
        System.out.println("[" + java.time.LocalTime.now() + "] "+msg);
    }
}
