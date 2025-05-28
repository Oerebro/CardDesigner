package events;

import java.util.*;

public class EventBus {
    private static final Map<Class<?>, List<EventListener<?>>> listeners = new HashMap<>();

    public static <T> void subscribe(Class<T> eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public static <T> void publish(T event) {
        List<EventListener<?>> listenerList = listeners.get(event.getClass());
        if (listenerList != null) {
            for (EventListener<?> l : listenerList) {
                @SuppressWarnings("unchecked")
                EventListener<T> typedListener = (EventListener<T>) l;
                typedListener.onEvent(event);
            }
        }
    }
}
