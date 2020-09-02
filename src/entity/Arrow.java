package entity;

import org.joml.Vector2f;

import collision.AABB;
import effect.EffectWounded;
import raidgame.Game;
import render.Animation;
import render.Camera;
import util.Window;
import world.World;

public class Arrow extends Projectile {
	
	public static final int ANIMATION_DOWN = 0;
	public static final int ANIMATION_UP = 1;
	public static final int ANIMATION_RIGHT = 2;
	public static final int ANIMATION_LEFT = 3;
	public static final int ANIMATION_DIE = 4;
	public static final int ANIMATION_MAX = 5;
	
	public Arrow(Transform transform, Entity sender) {
		super(ANIMATION_MAX, transform, sender);

		transform.scale.x = 1;
		transform.scale.y = 1;

		boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(0.5f,transform.scale.y));
		
		setAnimation(ANIMATION_DOWN, new Animation(1, 12, 16, 16, "abilities/arrowDown"));
		setAnimation(ANIMATION_UP, new Animation(1, 12, 16, 16, "abilities/arrowDown"));
		setAnimation(ANIMATION_RIGHT, new Animation(1, 12, 16, 16, "abilities/arrowDown"));
		setAnimation(ANIMATION_LEFT, new Animation(1, 12, 16, 16, "abilities/arrowDown"));
		setAnimation(ANIMATION_DIE, new Animation(1, 12, 16, 16, "abilities/arrowDown"));

		type = TYPE_ABILITY;
		
		speed = 50;
		direction = DIRECTION_DOWN;
		health = 1;
	
		
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		Vector2f movement = new Vector2f();
		if(!hasHit) {
			for(int i = 0; i < world.getEntityList().size(); i++) {
				Entity e = world.getEntityList().get(i);
				if(e.type == Entity.TYPE_ENEMY && isColliding(e)) {
					hasHit = true;
					e.hurt(sender, 9, Math.random());
					e.push(delta, this.getDirection(), 60);
					if(Math.random() > 0.5) {
						float power = 1.0f;
						if(Math.random() > 0.9) {
							power = 2.0f;
						}
						e.addStatusEffect(new EffectWounded(this, e, power, 0, 2000));
						Game.ConsoleSend(e.getName() + " was wounded.");
					}
				}
			}
		}
		
		
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
		if(hasHit) {
			useAnimation(ANIMATION_DIE);
			if(animations[ANIMATION_DIE].hasPlayedOnce()) {
				health = 0;
			}
		}
	}

}
