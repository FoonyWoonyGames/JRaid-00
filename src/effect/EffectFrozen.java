package effect;

import entity.Enemy;
import entity.Entity;
import entity.Particle;
import entity.Skeleton;
import entity.Zombie;
import raidgame.Game;
import render.Camera;
import util.Window;
import world.World;

public class EffectFrozen extends StatusEffect {

	public EffectFrozen(Entity sender, Entity host, float power, int speed, long duration) {
		super(sender, host, power, speed, duration);
		
		id = "effectFrozen";
		name = "Frozen";
		description = "Unable to move, and reduced defense.";
		// 3500
	}
	
	
	public void update(float delta, Window win, Camera camera, World world) {
		if(((Enemy) host).getName().equalsIgnoreCase("zombie")) {
			host.useAnimation(Zombie.ANIMATION_FROZEN);
		}
		if(((Enemy) host).getName().equalsIgnoreCase("skeleton")) {
			host.useAnimation(Skeleton.ANIMATION_FROZEN);
		}
		
		host.setDefense(host.getMaxDefense()/2);
		

		checkDuration();
	}



	@Override
	public void onEnd() {
		host.setDefense(host.getMaxDefense());
		host.getWorld().spawnParticle(Particle.PARTICLE_FROST, 4, 25, 16, 16, host.getX(), host.getY(), false, 10, 180, 5, 80, false);
	}
	
}
