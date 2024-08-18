package invaders.memento;
import java.util.ArrayList;
import java.util.List;

import invaders.memento.Memento;
import invaders.rendering.Renderable;
import invaders.entities.EntityView;
import invaders.factory.Projectile;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
public class Originator {
    /**
     * take information
     */
    private int score;
    private int time;
    private List<Renderable> r;
    private List<GameObject> objectList;
    public Originator(int score, int time, List<Renderable> r, List<GameObject> objectList){
        this.score = score;
        this.time = time;
        this.r = r;
        this.objectList = objectList;
    }
    public void setTime(int t){
        this.time = t;
    }
    public void setScore(int s){
        this.score = s;
    }
    public void setRen(List<Renderable> rens){
        this.r = rens;
    }
    public void setGameObjectList(List<GameObject> g){
        this.objectList = g;
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
    public Memento createMemento(){
        List<Renderable> newRenList = new ArrayList<Renderable>();
        List<Enemy> newEnemyList = new ArrayList<Enemy>();
        for(Renderable r : this.r){
            if (r.getRenderableObjectName().equals("Player")){
                newRenList.add(r);
                continue;
            }else if(r.getRenderableObjectName().equals("EnemyProjectile") || r.getRenderableObjectName().equals("PlayerProjectile")){
                continue;
            }
            Renderable tempE = r.renderClone();
            newRenList.add(tempE);
            if(r.getRenderableObjectName().equals("Enemy")){
                newEnemyList.add((Enemy)tempE);
            }
        }
        for(Enemy e: newEnemyList){
            for(Projectile p: e.getEnemyProjectile()){
                newRenList.add((Renderable) p);
            }
        }
        List<GameObject> newObjectList = new ArrayList<GameObject>();
        for(Renderable newR: newRenList){
            if(newR instanceof GameObject){
                newObjectList.add((GameObject)newR);
            }
        }
        return new Memento(this.score, this.time, newRenList, newObjectList);
    }
    public void restoreFromMemento(Memento m){
        this.score = m.getScore();
        this.time = m.getTime();
        this.r = m.getRenderables();
        this.objectList = m.getGameObjects();
    }
}
