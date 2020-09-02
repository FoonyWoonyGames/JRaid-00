package ai;

import org.joml.Vector2f;

import entity.Enemy;
import entity.Entity;

public class AiMoveTowardsTarget extends Ai {
	
	public AiMoveTowardsTarget(Enemy host) {
		super(host);
	}
	
	public Vector2f getMovement(Vector2f target, int movementSpeed, float delta) {

		Vector2f movement = new Vector2f();
		
		float xd = 1;
		float yd = 1;
		
		float xyd = 1;
		
		int speedX = movementSpeed;
		int speedY = movementSpeed;
		
		if(target.x > host.getX()) {
			xd = (int) (target.x - host.getX());
		} else if(target.x < host.getX()) {
			xd = host.getX() - target.x;
		} else {
			xd = 1;
		}

		if(target.y > host.getY()) {
			yd = (int) (target.y - host.getY());
		} else if(target.y < host.getY()) {
			yd = host.getY() - target.y;
		} else {
			yd = 1;
		}
		
		xyd = xd/yd;
		
		speedX = (int) (movementSpeed*xyd);
		speedY = (int) (movementSpeed/xyd);
		
		if(speedX > movementSpeed) {
			speedX = movementSpeed;
		} if(speedY > movementSpeed) {
			speedY = movementSpeed;
		}
		
		float stopDistance = 0.5f;
		
		if(target.x + stopDistance < host.getX()) {
			speedX = -speedX;
			movement.add(speedX*delta, 0);
			host.setDirection(Entity.DIRECTION_LEFT);
		} else if(target.x - stopDistance > host.getX()) {
			movement.add(speedX*delta, 0);
			host.setDirection(Entity.DIRECTION_RIGHT);
		}

		if(target.y + stopDistance < host.getY()) {
			movement.add(0, -speedY*delta);
			host.setDirection(Entity.DIRECTION_DOWN);
		} else if(target.y - stopDistance > host.getY()) {
			movement.add(0, speedY*delta);
			host.setDirection(Entity.DIRECTION_UP);
		}
		

		if(movement.x != 0 || movement.y != 0) {
			if(speedX < 0) {
				if(-speedX > speedY && speedY < 3) {
					host.setDirection(Entity.DIRECTION_LEFT);
				}
			} else if(speedX > 0) {
				if(speedX > speedY && speedY < 3) {
					host.setDirection(Entity.DIRECTION_RIGHT);
				}
			}
		}
		
		
		return movement;
	}
}
