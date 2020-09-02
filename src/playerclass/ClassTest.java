package playerclass;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_5;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_7;

import ability.Ability;
import ability.AbilityNoclip;
import ability.AbilitySummonSkeleton;
import entity.Player;
import raidgame.Game;
import render.Camera;
import util.FlowRoll;
import util.Window;
import world.Tile;
import world.World;

public class ClassTest extends ClassTemplate {

	private Ability abilityTest;
	
	public ClassTest(Player p) {
		super(p);
		nameUnlocalized = "test";
		nameLocalized = "Testing Class";
		
		ability1 = new AbilityNoclip(player);
		abilityTest = new AbilitySummonSkeleton(player);
	}
	
	int hand;
	@Override
	public void update(float delta, Window win, Camera camera, World world) {		
		if(win.getInput().isKeyPressed(GLFW_KEY_1)) {
			useAbility(delta, win, camera, world, ability1);
		}
		if(win.getInput().isKeyPressed(GLFW_KEY_2)) {
			if(hand > 0) {
				hand--;
			} else {
				hand = Tile.tiles.length-1;
				while(Tile.tiles[hand] == null) {
					hand--;
				}
			}
			Game.ConsoleSend("Used \"Change Tile To: " + hand + "("+ Tile.tiles[hand].getTexture() + ")\"");
		}
		if(win.getInput().isKeyPressed(GLFW_KEY_3)) {
			if(hand < Tile.tiles.length) {
				hand++;
				if(Tile.tiles[hand] == null) {
					hand = 0;
				}
			} else {
				hand = 0;
			}
			Game.ConsoleSend("Used \"Change Tile To: " + hand + "("+ Tile.tiles[hand].getTexture() + ")\"");
		}
		if(win.getInput().isKeyPressed(GLFW_KEY_4)) {
			useAbility(delta, win, camera, world, abilityTest);
		}
		if(win.getInput().isKeyDown(GLFW_KEY_5)) {
			world.setTile(player.getXOnMap(), player.getYOnMap(), Tile.tiles[hand]);
		}
		
		if(win.getInput().isKeyPressed(GLFW_KEY_7)) {
			FlowRoll.saveTexture("texture.png");
			FlowRoll.saveMask("mask.png");
		}
	}

}
