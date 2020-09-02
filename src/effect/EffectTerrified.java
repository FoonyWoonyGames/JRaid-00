package effect;

import entity.Entity;
import render.Camera;
import util.Window;
import world.World;

public class EffectTerrified extends StatusEffect {

	public EffectTerrified(Entity sender, Entity host, float power, int speed, long duration) {
		super(sender, host, power, speed, duration);
		
		id = "effectDisorientated";
		name = "Disorientated";
		description = "Wanders around confusingly.";
//		500;
	}
	
	

	public void update(float delta, Window win, Camera camera, World world) {
		checkDuration();
	}



	@Override
	public void onEnd() {
	}
	
}
