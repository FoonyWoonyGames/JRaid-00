package effect;

import entity.Entity;
import render.Camera;
import util.Window;
import world.World;

public class EffectBurned extends StatusEffect {

	public EffectBurned(Entity sender, Entity host, float power, int speed, long duration) {
		super(sender, host, power, speed, duration);
		
		id = "effectBurned";
		name = "Burned";
		description = "Reduced defense.";
	}
	
	

	public void update(float delta, Window win, Camera camera, World world) {
		
		host.setDefense((int) (host.getMaxDefense()/(2*power)));
		
		checkDuration();
	}

	@Override
	public void onEnd() {
		host.setDefense(host.getMaxDefense());
	}
	
}
