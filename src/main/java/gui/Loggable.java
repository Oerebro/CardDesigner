package gui;

public class Loggable {
    public void log(Object msg){
        System.out.println("[" + java.time.LocalTime.now() + "] "+msg);
    }
}
