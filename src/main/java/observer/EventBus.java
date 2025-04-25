package observer;

import audit.AuditAction;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus implements Subject {

    private static EventBus instance;
    private EventBus() { }
    public static EventBus getInstance() {
        if (instance == null) instance = new EventBus();
        return instance;
    }

    private final List<Observer> observers = new CopyOnWriteArrayList<>();
    @Override public void registerObserver(Observer o)   { observers.add(o); }

    @Override public void notifyObservers(String act)    { observers.forEach(o -> o.update(act)); }

    public static void publish(AuditAction act) {
        getInstance().notifyObservers(act.name());
    }
}
