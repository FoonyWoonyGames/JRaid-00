package effect;

import entity.Entity;
import entity.Particle;
import event.Event;
import raidgame.Game;
import render.Camera;
import util.Window;
import world.World;

public class EffectBoundOfDeath extends StatusEffect {
	

	public EffectBoundOfDeath(Entity sender, Entity host, float power, int speed, long duration) {
		super(sender, host, power, speed, duration);
		
		id = "effectBound";
		name = "Deathbound";
		description = "Your death is bound to a necromancer. Upon death, your life essence will strengthen the necromancer you're bound to.";
	}
	
	
	long particle;

	public void update(float delta, Window win, Camera camera, World world) {
		
		if(System.currentTimeMillis() - particle > 2000) {
			for(int i = 0; i < 8; i++) {
				world.spawnParticle(Particle.PARTICLE_DEATHBOUND, 5, 15, 16, 16, host, false, -0.5f+((float) Math.random()*2), -0.5f+((float) Math.random()*2), false);
			}
			particle = System.currentTimeMillis();
		}
		checkDuration();
	}

	@Override
	public void onEnd() {
		host.setDefense(host.getMaxDefense());
	}
	
	@Override
	public void eventHostDied(Event e) {
		sender.setAttack(sender.getAttack() + 0.2f);
	}
	
}
