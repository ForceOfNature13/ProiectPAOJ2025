package observer;

public interface Subject {
    void registerObserver(Observer o);
    void notifyObservers(String actionName);
}
