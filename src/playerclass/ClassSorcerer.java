package playerclass;

import static org.lwjgl.glfw.GLFW.*;
import ability.Ability;
import ability.AbilityFireball;
import ability.AbilityFroststorm;
import ability.AbilityManaNova;
import ability.AbilityMelee;
import entity.Player;
import render.Camera;
import util.FlowRoll;
import util.Window;
import world.World;

public class ClassSorcerer extends ClassTemplate {

	
	public ClassSorcerer(Player p) {
		super(p);
		nameUnlocalized = "sorcerer";
		nameLocalized = "Sorcerer";
		
		
		ability1 = new AbilityMelee(player);
		ability2 = new AbilityFireball(player);
		ability3 = new AbilityFroststorm(player);
		abilityUlt = new AbilityManaNova(player);
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
		else if(win.getInput().isKeyDown(GLFW_KEY_3) && player.abilityUsing != ability1  && player.abilityUsing != ability2) {
			useAbility(delta, win, camera, world, ability3);
		}	
		else if(win.getInput().isKeyDown(GLFW_KEY_5) && player.abilityUsing != ability1  && player.abilityUsing != ability2) {
			useAbility(delta, win, camera, world, abilityUlt);
		}
		
		ability1.update(delta, win, camera, world);
		ability2.update(delta, win, camera, world);
		ability3.update(delta, win, camera, world);
		abilityUlt.update(delta, win, camera, world);
	}

}
