package effect;

import entity.Entity;
import render.Camera;
import util.Window;
import world.World;

public class EffectWounded extends StatusEffect {

	public EffectWounded(Entity sender, Entity host, float power, int speed, long duration) {
		super(sender, host, power, speed, duration);
		
		id = "effectWounded";
		name = "Wounded";
		description = "Moving slower.";
	}
	

	public void update(float delta, Window win, Camera camera, World world) {
		
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
