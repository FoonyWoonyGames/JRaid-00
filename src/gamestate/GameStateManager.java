package gamestate;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import raidgame.Game;
import render.Shader;
import util.Window;

public class GameStateManager {
	
	private int currentState = 1;
	private ArrayList<GameState> gameStates;
	
	public static final int STATE_INGAME = 0;
	public static final int STATE_MENU = 1;
	
	private Shader shader;
	
	private Window window;
	
	public GameStateManager(Window window) {
		gameStates = new ArrayList<GameState>();
		
		gameStates.add(new StateInGame(window));
		gameStates.add(new StateMenu(window));
		
		shader = new Shader("shader");

		gameStates.get(STATE_MENU).init();
		
		this.window = window;
		
	}
	
	public int getState() {
		return currentState;
	}
	
	public void setState(int i) {
		currentState = i;
		gameStates.get(currentState).init();
		Game.ConsoleSend("Switched gamestate to " + i);
	}
	
	public void update(float delta, Window window) {
		gameStates.get(currentState).update(delta, window);
		
		inputKeyboard();
	}
	
	public void render() {
		gameStates.get(currentState).render(shader);
	}
	
	public void inputKeyboard() {
		gameStates.get(currentState).inputKeyboard();

		if(window.getInput().isKeyPressed(GLFW_KEY_F1)) {
			Game.screenshooter.cap();
		}
	}
	
	public GameState getGameState(int gamestate) {
		return gameStates.get(gamestate);
	}
	
	public void hasResized() {
		gameStates.get(currentState).hasResized();
	}
}
