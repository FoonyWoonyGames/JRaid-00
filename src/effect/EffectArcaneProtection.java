package effect;

import entity.Enemy;
import entity.Entity;
import entity.Particle;
import entity.Zombie;
import event.Event;
import event.EventEntityDamaged;
import raidgame.Game;
import render.Camera;
import util.Window;
import world.World;

public class EffectArcaneProtection extends StatusEffect {
	
	private int timesDamaged;

	public EffectArcaneProtection(Entity sender, Entity host, float power, int speed, long duration) {
		super(sender, host, power, speed, duration);
		
		id = "effectShield";
		name = "Arcane Protection";
		description = "Protected from all damage.";
		
		timesDamaged = 0;
	}
	
	

	public void update(float delta, Window win, Camera camera, World world) {
		checkDuration();	
	}

	@Override
	public void eventHostDamaged(EventEntityDamaged e) {
		if(timesDamaged >= power) {
			stop();
		} else {
			e.setCancelled(true);
			host.getWorld().spawnParticle(Particle.PARTICLE_SHIELD, 46, 250, 32, 32, host.getX(), host.getY()+0.4f, false, 0, 0, 1, 0, false);
			timesDamaged++;
		}
	}

	@Override
	public void eventHostPushed(Event e) {
		e.setCancelled(true);
	}


	@Override
	public void onEnd() {
		host.getWorld().spawnParticle(Particle.PARTICLE_MAGIC, 4, 16, 16, 16, host.getX(), host.getY(), false, 8, 180, 10, 120, false);
	}
	
}
