package invaders.entities;

import invaders.factory.PlayerProjectile;
import invaders.factory.PlayerProjectileFactory;
import invaders.factory.Projectile;
import invaders.factory.ProjectileFactory;
import invaders.gameobject.GameObject;
import invaders.physics.Collider;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Animator;
import invaders.rendering.Renderable;

import invaders.strategy.NormalProjectileStrategy;
import javafx.scene.image.Image;
import netscape.javascript.JSObject;

import org.json.simple.JSONObject;

import java.io.File;

public class Player implements Moveable, Renderable{

    private final Vector2D position;
    private double health;
    private double velocity;

    private final double width = 20;
    private final double height = 20;
    private final Image image;
    private int score = 0;
    private ProjectileFactory playerProjectileFactory = new PlayerProjectileFactory();
    private JSONObject playerInfo;

    public Player(JSONObject playerInfo){
        this.playerInfo = playerInfo;
        int x = ((Long)((JSONObject)(playerInfo.get("position"))).get("x")).intValue();
        int y = ((Long)((JSONObject)(playerInfo.get("position"))).get("y")).intValue();

        this.image = new Image(new File("src/main/resources/player.png").toURI().toString(), width, height, true, true);
        this.position = new Vector2D(x,y);
        this.health = ((Long) playerInfo.get("lives")).intValue();
        this.velocity = ((Long) playerInfo.get("speed")).intValue();

    }

    @Override
    public void takeDamage(double amount) {
        this.health -= amount;
    }

    @Override
    public double getHealth() {
        return this.health;
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    @Override
    public void up() {
        return;
    }

    @Override
    public void down() {
        return;
    }

    @Override
    public void left() {
        this.position.setX(this.position.getX() - this.velocity);
    }

    @Override
    public void right() {
        this.position.setX(this.position.getX() + this.velocity);
    }

    public Projectile shoot(){
        return playerProjectileFactory.createProjectile(new Vector2D(this.position.getX() + 5 ,this.position.getY() - 10),new NormalProjectileStrategy(),null);
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public String getRenderableObjectName() {
        return "Player";
    }

    public void updateScore(int addedScore){
        this.score += addedScore;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int s){
        this.score = s;
    }

    @Override
    public Renderable renderClone(){
        Player tempP = new Player(playerInfo);
        tempP.health = health;
        tempP.velocity = velocity;
        tempP.score = score;
        return (Renderable) tempP;
    }


}
