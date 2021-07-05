package bleach.hack.event.events;

import bleach.hack.event.Event;
import net.minecraft.util.Hand;

public class EventSwing extends Event {
    public Hand hand;

    public EventSwing(Hand hand) {
        this.hand = hand;
    }
}
