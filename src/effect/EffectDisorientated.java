package effect;

import entity.Entity;
import render.Camera;
import util.Window;
import world.World;

public class EffectDisorientated extends StatusEffect {

	public EffectDisorientated(Entity sender, Entity host, float power, int speed, long duration) {
		super(sender, host, power, speed, duration);
		
		id = "effectTerrified";
		name = "Terrified";
		description = "Too terrified to move.";
	}
	
	

	public void update(float delta, Window win, Camera camera, World world) {
		checkDuration();
		host.setSpeed(host.getSpeed()-host.getMaxSpeed()/3);
	}



	@Override
	public void onEnd() {
		host.setSpeed(host.getSpeed()+host.getMaxSpeed()/3);
	}
	
}
