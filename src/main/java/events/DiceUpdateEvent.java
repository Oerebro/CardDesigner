package events;

public class DiceUpdateEvent {
    public final int dice;

    public DiceUpdateEvent(int dice) {
        this.dice = dice;
    }
}