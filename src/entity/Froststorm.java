package entity;

import org.joml.Vector2f;

import collision.AABB;
import effect.EffectFreezing;
import render.Animation;
import render.Camera;
import util.Window;
import world.World;

public class Froststorm extends Entity {

	public static final int ANIMATION_DOWN = 0;
	public static final int ANIMATION_UP = 1;
	public static final int ANIMATION_RIGHT = 2;
	public static final int ANIMATION_LEFT = 3;
	public static final int ANIMATION_MAX = 4;
	
	private boolean active;

	public Froststorm(Transform transform) {
		super(ANIMATION_MAX, transform);

		transform.scale.x = 3;
		transform.scale.y = 3;

		boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(1,1));
		
		setAnimation(ANIMATION_DOWN, new Animation(32, 256, 32, 32, "abilities/froststormDown"));
		setAnimation(ANIMATION_UP, new Animation(4, 12, 16, 16, "abilities/froststormUp"));
		setAnimation(ANIMATION_RIGHT, new Animation(4, 12, 16, 16, "abilities/froststormRight"));
		setAnimation(ANIMATION_LEFT, new Animation(4, 12, 16, 16, "abilities/froststormLeft"));

		type = TYPE_ABILITY;
		
		speed = 0;
		direction = DIRECTION_DOWN;
		health = 1;
		noclip = true;
		
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
//		if(direction == DIRECTION_RIGHT) {
//			useAnimation(ANIMATION_RIGHT);
//		} else if(direction == DIRECTION_LEFT) {
//			useAnimation(ANIMATION_LEFT);
//		} else if(direction == DIRECTION_UP) {
//			useAnimation(ANIMATION_UP);
//		} else {
//			useAnimation(ANIMATION_DOWN);
//		}
//
//		
//		for(int i = 0; i < world.getEntityList().size(); i++) {
//			if(world.getEntityList().get(i).type == Entity.TYPE_ENEMY) {
//				Entity e = world.getEntityList().get(i);
//				if(isColliding(e) && active) {
//					if(!e.hasStatusEffect("effectFreezing")) {
//						e.addStatusEffect(new EffectFreezing(e, 1.0f, 1500, 1));
//					}
//				} else {
//					if(e.hasStatusEffect("effectFreezing")) {
//						e.removeStatusEffect("effectFreezing");
//					}
//				}
//			}
//		}
	}
	
	public void setActive(boolean b) {
		active = b;
	}

}
