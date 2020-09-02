package effect;

import entity.Entity;
import render.Camera;
import util.Window;
import world.World;

public class EffectFreezing extends StatusEffect {

	public EffectFreezing(Entity sender, Entity host, float power, int speed, long duration) {
		super(sender, host, power, speed, duration);
		
		id = "effectFreezing";
		name = "Freezing";
		description = "Getting frozen and reduced movement speed.";
	}
	

	public void update(float delta, Window win, Camera camera, World world) {
		if(System.currentTimeMillis() - birth > speed) {
			if(!host.hasStatusEffect("effectFrozen")) {
				host.addStatusEffect(new EffectFrozen(sender, host, 1.0f, 1, 2500));
			}
		}
		
		host.setSpeed((int) (host.getMaxSpeed()/(2*power)));
		host.setSwimSpeed((int) (host.getMaxSwimSpeed()/(2*power)));
		
		checkDuration();
	}


	@Override
	public void onEnd() {
		host.setSpeed(host.getMaxSpeed());
		host.setSwimSpeed(host.getMaxSwimSpeed());
	}


}
