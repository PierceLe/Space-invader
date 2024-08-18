package invaders.observer;

import java.util.ArrayList;
import java.util.List;

import invaders.observer.ScoreObserver;
import invaders.observer.TimeObserver;
import invaders.observer.Subject;

public class TimeScore implements Subject {
    /**
     * update time and score, and trigger ScoreObserver, TimeObserver instances to update score and time shown on the game window
     */
    private List<Observer> oList = new ArrayList<Observer>();

    @Override
    public void addObserver(Observer o){
        oList.add(o);
    }

    @Override
    public void notice(){
        for(Observer o : oList){
            o.update();
        }
    }

    public void scoreUpdate(int i){
        for(Observer o : oList){
            if (o instanceof ScoreObserver){
                o.selfUpdate(i);
                notice();
            }
        }
    }

    public void timeUpdate(int i){
        for(Observer o : oList){
            if (o instanceof TimeObserver){
                o.selfUpdate(i);
                notice();
            }
        }
    }
}
