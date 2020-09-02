package gamestate;


import static org.lwjgl.glfw.GLFW.*;

import java.util.Random;

import gui.Gui;
import raidgame.Game;
import render.Shader;
import util.Window;

public class StateMenu extends GameState {

	private Gui backgroundTower;
	private Gui backgroundMountains;
	private Gui backgroundLogo;
	private Gui backgroundFWG;
	
	private static int state = 0;
	
	
	public StateMenu(Window window) {
		super(window);
		backgroundTower = new Gui(window, "menuTower.png");
		backgroundMountains = new Gui(window, "menuBackground.png");
		backgroundLogo = new Gui(window, "menuLogo.png");
		backgroundFWG = new Gui(window, "menuFWG.png");
	}

	@Override
	public void init() {
		state = 0;
		
		glfwSetInputMode(Game.raid.getWindow().getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}

	@Override
	public void update(float delta, Window window) {
		
	}


	float xMountains = 0;
	long lastMovedMountains;
	float tickMountains = 0;
	float x = 0;
	long lastMoved;
	float tick = 0;

	@Override
	public void render(Shader shader) {
		if(backgroundTower != null) {
			if(state == 0) {
				backgroundMountains.render(0.25f,-0.375f, 435, 0);
				backgroundTower.render(0,-0.14893617f, 235, 0);
				backgroundLogo.render(0,-0.8f, 100, 0);
			}
			else if(state == 1) {
				backgroundMountains.render(0.25f - x/4,-0.375f, 435, 0);
				if(System.currentTimeMillis() - lastMoved > 10) {
					tick++;
					float movementSpeed = tick/5000;
					// 50 ticks
					if(movementSpeed > 0.01f) movementSpeed = 0.01f;
					
					if(tick > 80) {
						movementSpeed = (130-tick)/5000;
					}
					if(movementSpeed < 0) movementSpeed = 0.001f;
					
					x = x + movementSpeed;
					lastMoved = System.currentTimeMillis();
					
					if(x > 0.8f) {
						x = 0.8f;
						state = 2;
					}
				}
				backgroundTower.render(x,-0.14893617f, 235, 0);
				backgroundLogo.render(0 + x*2.5f,-0.8f, 100, 0);
				backgroundFWG.render(-2.0f + x*2.5f,-0.375f, 320, 0);
			}
			else if(state == 2) {
				backgroundMountains.render(0.25f - x/4,-0.375f, 435, 0);
				backgroundTower.render(0.8f,-0.14893617f, 235, 0);
				backgroundLogo.render(0 + x*2.5f,-0.8f, 100, 0);
				backgroundFWG.render(0,-0.375f, 320, 0);
			}
		}
	}

	@Override
	public void inputKeyboard() {
		if(window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
			if(state == 0) {
				state++;
			}
			else if(state == 1) {
				state++;
			}
			else if(state == 2) {
				getGSM().setState(GameStateManager.STATE_INGAME);
			}
		}
	}

	@Override
	public void hasResized() {
		// TODO Auto-generated method stub
		
	}
	
	public static void setState(int s) {
		if(state < 0 || state > 2) {
			Game.ConsoleSend("[ERROR] -- STATE " + state + " is not a valid menu state.");
		} else {
			state = s;
		}
	}
	
}
