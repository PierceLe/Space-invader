package invaders.memento;
import invaders.rendering.Renderable;
import invaders.entities.EntityView;
import invaders.gameobject.GameObject;
import java.util.List;

public class Memento {
    /**
     * saave necessary information
     */
    private int score;
    private int time;
    private List<Renderable> r;
    private List<GameObject> objectList;
    public Memento(int score, int time, List<Renderable> r, List<GameObject> objectList){
        this.score = score;
        this.time = time;
        this.r = r;
        this.objectList = objectList;
    }
    public int getScore(){
        return this.score;
    }
    public int getTime(){
        return this.time;
    }
    public List<Renderable> getRenderables(){
        return this.r;
    }
    public List<GameObject> getGameObjects(){
        return this.objectList;
    }
}
