package invaders.strategy;

import invaders.factory.Projectile;

public class NormalProjectileStrategy implements ProjectileStrategy{
    @Override
    public void update(Projectile p) {
        double newYPos = p.getPosition().getY() - 2;
        p.getPosition().setY(newYPos);
    }

    @Override
    public String getStrategyName(){
        return "Normal";
    }

    @Override
    public ProjectileStrategy clone(){
        return new NormalProjectileStrategy();
    }
}
