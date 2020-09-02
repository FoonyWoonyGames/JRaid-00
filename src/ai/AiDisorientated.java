package ai;

import org.joml.Vector2f;

import entity.Enemy;

public class AiDisorientated extends Ai {

	public AiDisorientated(Enemy host) {
		super(host);
	}

	@Override
	public Vector2f getMovement(Vector2f target, int movementSpeed, float delta) {
		Vector2f movement = new Vector2f();
		
		double random = Math.random();
		if(random <= 0.25) {
			movement.add(movementSpeed/2, 0);
		} else if(random > 0.25) {
			movement.add(-movementSpeed/2, 0);
		} else if(random > 0.5) {
			movement.add(0, movementSpeed/2);
		} else if(random > 0.75) {
			movement.add(0, -movementSpeed/2);
		}
		
		
		return movement;
	}

}
