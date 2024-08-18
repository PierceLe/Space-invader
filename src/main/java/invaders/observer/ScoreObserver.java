package invaders.observer;
import invaders.observer.Observer;
import javafx.scene.text.Text;
public class ScoreObserver implements Observer {
    /**
     * update score shown in game window
     */
    private Text scoreText;
    private int score;
    public ScoreObserver(Text scoreText, int score){
        this.scoreText = scoreText;
        this.score = score;
    }
    @Override
    public void update(){
        scoreText.setText("Score: " + score);
    }
    @Override
    public void selfUpdate(int i){
        this.score = i;
    }
}
