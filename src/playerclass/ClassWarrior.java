package playerclass;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_7;
import ability.Ability;
import ability.AbilityFireball;
import ability.AbilityFleshWound;
import ability.AbilityFroststorm;
import ability.AbilityHack;
import ability.AbilityMelee;
import entity.Player;
import render.Camera;
import util.FlowRoll;
import util.Window;
import world.World;

public class ClassWarrior extends ClassTemplate {
	
	public ClassWarrior(Player p) {
		super(p);
		nameUnlocalized = "warrior";
		nameLocalized = "Warrior";
		
		
		ability1 = new AbilityMelee(player);
		ability2 = new AbilityHack(player);
		ability3 = new AbilityFleshWound(player);
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
		else if(win.getInput().isKeyPressed(GLFW_KEY_3) && player.abilityUsing != ability1  && player.abilityUsing != ability2) {
			useAbility(delta, win, camera, world, ability3);
		}
		
		ability1.update(delta, win, camera, world);
		ability2.update(delta, win, camera, world);
		ability3.update(delta, win, camera, world);
	}

}
