package ability;

import entity.Player;
import render.Camera;
import util.Window;
import world.World;

public class AbilityNoclip extends Ability {

	public AbilityNoclip(Player user) {
		super(user);
		
		name = "Toggle Noclip";
		description = "DEV - Toggle noclip on or off.";
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void use(float delta, Window win, Camera camera, World world) {
		if(isCooled()) {
			user.setNoclip(!user.hasNoclip());
		}
	}

}
