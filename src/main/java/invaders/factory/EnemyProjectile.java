package invaders.factory;

import javax.swing.text.Position;

import invaders.engine.GameEngine;
import invaders.gameobject.GameObject;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import invaders.strategy.ProjectileStrategy;
import javafx.scene.image.Image;

public class EnemyProjectile extends Projectile{
    private ProjectileStrategy strategy;

    public EnemyProjectile(Vector2D position, ProjectileStrategy strategy, Image image) {
        super(position,image);
        this.strategy = strategy;
    }

    @Override
    public void update(GameEngine model) {
        strategy.update(this);

        if(this.getPosition().getY()>= model.getGameHeight() - this.getImage().getHeight()){
            this.takeDamage(1);
        }

    }
    @Override
    public String getRenderableObjectName() {
        return "EnemyProjectile";
    }

    public ProjectileStrategy getProjectile(){
        return this.strategy;
    }
    @Override
    public Renderable renderClone(){
        EnemyProjectile curE = new EnemyProjectile(new Vector2D(this.getPosition().getX(),this.getPosition().getY()),(ProjectileStrategy) this.strategy.clone(), this.getImage());
        return (Renderable) curE;
    }
}
