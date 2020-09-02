package effect;

import entity.Entity;
import entity.Particle;
import render.Camera;
import util.Window;
import world.World;

public class EffectBleed extends StatusEffect {
	
	private long lastHurt;

	public EffectBleed(Entity sender, Entity host, float power, int speed, long duration) {
		super(sender, host, power, speed, duration);
		
		id = "effectBleed";
		name = "Bleeding";
		description = "Bleeding. Damaged for " + (3*power) + " hp every " + speed + " milliseconds for " + (duration/60) + " seconds.";
//		5000;
	}
	
	

	public void update(float delta, Window win, Camera camera, World world) {
		
		if(System.currentTimeMillis() - lastHurt > speed) {
			lastHurt = System.currentTimeMillis();
			host.hurt(sender, 2*power, Math.random());
			world.spawnParticle(Particle.PARTICLE_BLOOD, 5, 25, 16, 16, host.getX(), host.getY(), false, 5, 0, 2, 360, false);
		}
		
		checkDuration();
	}

	@Override
	public void onEnd() {
	}
	
}
