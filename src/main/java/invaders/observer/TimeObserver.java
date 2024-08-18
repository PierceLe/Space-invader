package invaders.observer;
import javafx.scene.text.Text;
import invaders.observer.Observer;;
public class TimeObserver implements Observer {
    /**
     * update time shown in game window
     */
    private Text timerText;
    private int time;
    public TimeObserver(Text timerText, int time){
        this.timerText = timerText;
        this.time = time;
    }
    @Override
    public void update(){
        timerText.setText("Time : " + time);
    }
    @Override
    public void selfUpdate(int updateTime){
        this.time = updateTime;
    }

    
}
