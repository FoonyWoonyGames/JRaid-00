package playerclass;

import static org.lwjgl.glfw.GLFW.*;
import ability.Ability;
import ability.AbilityFireball;
import ability.AbilityFroststorm;
import ability.AbilityManaNova;
import ability.AbilityMelee;
import ability.AbilityPoison;
import ability.AbilityShoot;
import entity.Player;
import render.Camera;
import util.FlowRoll;
import util.Window;
import world.World;

public class ClassRanger extends ClassTemplate {
	
	public ClassRanger(Player p) {
		super(p);
		nameUnlocalized = "test";
		nameLocalized = "Ranger";
		
		
		ability1 = new AbilityMelee(player);
		ability2 = new AbilityShoot(player);
		ability3 = new AbilityPoison(player);
	}
	
	byte hand;
	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		if(win.getInput().isKeyPressed(GLFW_KEY_1) && player.abilityUsing == null) {
			useAbility(delta, win, camera, world, ability1);
		}
		else if(win.getInput().isKeyPressed(GLFW_KEY_2) && player.abilityUsing == null) {
			useAbility(delta, win, camera, world, ability2);
		}
		else if(win.getInput().isKeyPressed(GLFW_KEY_3) && player.abilityUsing == null) {
			useAbility(delta, win, camera, world, ability3);
		}
	
		ability1.update(delta, win, camera, world);
		ability2.update(delta, win, camera, world);
		ability3.update(delta, win, camera, world);
	}

}
