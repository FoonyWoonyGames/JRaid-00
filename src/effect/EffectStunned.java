package effect;

import entity.Entity;
import render.Camera;
import util.Window;
import world.World;

public class EffectStunned extends StatusEffect {

	public EffectStunned(Entity sender, Entity host, float power, int speed, long duration) {
		super(sender, host, power, speed, duration);
		
		id = "effectStunned";
		name = "Stunned";
		description = "Unable to move.";
	}
	
	

	public void update(float delta, Window win, Camera camera, World world) {
		checkDuration();
	}



	@Override
	public void onEnd() {
	}
	
}
