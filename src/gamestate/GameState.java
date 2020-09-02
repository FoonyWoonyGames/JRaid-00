package gamestate;

import raidgame.Game;
import render.Shader;
import util.Window;

public abstract class GameState {
	
	protected Window window;
	
	public GameState(Window window) {
		this.window = window;
	}
	
	public abstract void init();

	public abstract void update(float delta, Window window);
	
	public abstract void render(Shader shader);

	public abstract void inputKeyboard();
	
	public abstract void hasResized();
	
	protected GameStateManager getGSM() {
		return Game.gsm;
	}
}
