package effect;

import entity.Entity;
import render.Camera;
import util.Window;
import world.World;

public class EffectPoisoned extends StatusEffect {
	
	private long lastHurt;

	public EffectPoisoned(Entity sender, Entity host, float power, int speed, long duration) {
		super(sender, host, power, speed, duration);
		
		id = "effectPoisoned";
		name = "Poisoned";
		description = "Poisoned. Damaged for " + (3*power) + " hp every" + speed + " milliseconds for " + duration/60 + " seconds.";
	}
	
	

	public void update(float delta, Window win, Camera camera, World world) {
		
		if(System.currentTimeMillis() - lastHurt > speed) {
			lastHurt = System.currentTimeMillis();
			host.hurt(sender, 3*power, Math.random());
		}
		
		checkDuration();
	}

	@Override
	public void onEnd() {
		host.setDefense(host.getMaxDefense());
	}
	
}
