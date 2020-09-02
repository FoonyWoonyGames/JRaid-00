package entity;

import org.joml.Vector2f;

import render.Camera;
import util.Window;
import world.World;

public class Projectile extends Entity {

	public static final int ANIMATION_DOWN = 0;
	public static final int ANIMATION_UP = 1;
	public static final int ANIMATION_RIGHT = 2;
	public static final int ANIMATION_LEFT = 3;
	public static final int ANIMATION_DIE = 4;
	public static final int ANIMATION_MAX = 5;
	
	protected int damage;
	protected boolean hasHit;
	protected Entity sender;

	public Projectile(int maxAnimations, Transform transform, Entity sender) {
		super(maxAnimations, transform);
		

		type = TYPE_ABILITY;
		
		speed = 10;

		hasHit = false;
		
		this.sender = sender;
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		Vector2f movement = new Vector2f();

		
		
//		if(!hasHit) {
//			for(int i = 0; i < world.getEntityList().size(); i++) {
//				Entity e = world.getEntityList().get(i);
//				if(e.type == Entity.TYPE_ENEMY && isColliding(e)) {
//					hasHit = true;
//				}
//			}
//		}
		
		if(!hasHit) {
			if(direction == DIRECTION_RIGHT) {
				useAnimation(ANIMATION_RIGHT);
				movement.add(speed*delta, 0);
			} else if(direction == DIRECTION_LEFT) {
				useAnimation(ANIMATION_LEFT);
				movement.add(-speed*delta, 0);
			} else if(direction == DIRECTION_UP) {
				useAnimation(ANIMATION_UP);
				movement.add(0, speed*delta);
			} else {
				useAnimation(ANIMATION_DOWN);
				movement.add(0, -speed*delta);
			}
		}
		
		move(movement);


		if(isColliding()) {
			hasHit = true;
		}
	}
	
	public boolean hasHit() {
		return hasHit;
	}

}
