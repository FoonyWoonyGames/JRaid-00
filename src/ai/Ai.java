package ai;

import org.joml.Vector2f;

import entity.Enemy;

public abstract class Ai {

	protected Enemy host;
	
	public Ai(Enemy host) {
		this.host = host;
	}

	public abstract Vector2f getMovement(Vector2f target, int movementSpeed, float delta);
}
