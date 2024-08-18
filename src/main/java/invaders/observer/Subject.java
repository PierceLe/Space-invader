package invaders.observer;
import invaders.observer.Observer;
public interface Subject {
    public void addObserver(Observer o);
    public void notice();
}
