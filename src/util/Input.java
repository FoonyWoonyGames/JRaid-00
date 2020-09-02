package util;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

import gamestate.GameStateManager;
import gamestate.StateMenu;
import raidgame.Game;

public class Input {
	
	private long window;
	
	private boolean keys[];
	
	public Input(Window win) {
		window = win.getWindow();
		this.keys = new boolean[GLFW_KEY_LAST];
		for(int i = 0; i < GLFW_KEY_LAST; i++) {
			keys[i] = false;
		}
		Game.ConsoleSend("\"Input\" started.");
	}
	
	public double[] getMousePosition() {
	    DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
	    DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
	    glfwGetCursorPos(window, posX, posY);
	    return new double[] { 
	    	posX.get(0),
	    	posY.get(0)
	    };
	}
	public double getMouseX() {
	    return getMousePosition()[0];
	}
	public double getMouseY() {
	    return getMousePosition()[1];
	}
	public void setMousePos(double x, double y) {
		glfwSetCursorPos(Game.raid.getWindow().getWindow(), x, y);
	}
	
	public void check() {
		if(isKeyPressed(GLFW_KEY_ESCAPE)) {
			if(Game.gsm.getState() == GameStateManager.STATE_INGAME) {
				Game.gsm.setState(GameStateManager.STATE_MENU);
				StateMenu.setState(2);
			} else {
				glfwSetWindowShouldClose(window, true);
				Game.ConsoleSend("RAID has been shut down");
			}
		}
		
		if(isKeyDown(GLFW_KEY_LEFT_SHIFT) && isKeyPressed(GLFW_KEY_T)) {
			Game.ConsoleSend("Input Test");
		}
		
	}
	
	public boolean isKeyDown(int k) {
		return glfwGetKey(window, k) == 1;
	}
	
	public boolean isKeyPressed(int key) {
		return (isKeyDown(key)) && !keys[key];
	}
	public boolean isKeyReleased(int key) {
		return (!isKeyDown(key)) && keys[key];
	}

	public boolean isMouseButtonDown(int b) {
		return glfwGetMouseButton(window, b) == 1;
	}
	
	public void update() {
		for(int i = 32; i < GLFW_KEY_LAST; i++) {
			keys[i] = isKeyDown(i);
		}
	}
}
