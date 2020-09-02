package effect;

import entity.Entity;
import raidgame.Game;
import render.Camera;
import util.Window;
import world.World;

public class EffectPlagued extends StatusEffect {
	
	private long lastHurt;

	public EffectPlagued(Entity sender, Entity host, float power, int speed, long duration) {
		super(sender, host, power, speed, duration);
		
		id = "effectPlagued";
		name = "Plagued";
		description = "Poisoned. Damaged for 8 hp every 100 milliseconds for 10 seconds.";
	}
	
	

	public void update(float delta, Window win, Camera camera, World world) {
		
		if(System.currentTimeMillis() - lastHurt > 10000) {
			lastHurt = System.currentTimeMillis();
			host.hurt(sender, 8, Math.random());
		}
		checkDuration();
	}

	@Override
	public void onEnd() {
		host.setDefense(host.getMaxDefense());
	}
	
}
