package entity;

import org.joml.Vector2f;

import ai.AiMoveTowardsTarget;
import collision.AABB;
import render.Animation;
import render.Camera;
import util.Window;
import world.World;

public class Skeleton extends Enemy {
	
	public static final int ANIMATION_IDLE_DOWN = 0;
	public static final int ANIMATION_IDLE_UP = 1;
	public static final int ANIMATION_IDLE_RIGHT = 2;
	public static final int ANIMATION_IDLE_LEFT = 3;
	public static final int ANIMATION_WALK_DOWN = 4;
	public static final int ANIMATION_WALK_UP = 5;
	public static final int ANIMATION_WALK_RIGHT = 6;
	public static final int ANIMATION_WALK_LEFT = 7;
	public static final int ANIMATION_SWIM_DOWN = 8;
	public static final int ANIMATION_SWIM_UP = 9;
	public static final int ANIMATION_SWIM_RIGHT = 10;
	public static final int ANIMATION_SWIM_LEFT = 11;
	public static final int ANIMATION_FROZEN = 12;
	public static final int ANIMATION_MAX = 13;

	private AiMoveTowardsTarget ai_mtt;

	public Skeleton(Transform transform) {
		super(ANIMATION_MAX, transform);
		
		setAnimation(ANIMATION_IDLE_DOWN, new Animation(1, 1, 32, 32, "skeleton/idleDown"));
		setAnimation(ANIMATION_IDLE_UP, new Animation(1, 1, 32, 32, "skeleton/idleUp"));
		setAnimation(ANIMATION_IDLE_RIGHT, new Animation(1, 1, 32, 32, "skeleton/idleRight"));
		setAnimation(ANIMATION_IDLE_LEFT, new Animation(1, 1, 32, 32, "skeleton/idleLeft"));
		setAnimation(ANIMATION_WALK_DOWN, new Animation(4, 4, 32, 32, "skeleton/walkDown"));
		setAnimation(ANIMATION_WALK_UP, new Animation(4, 4, 32, 32, "skeleton/walkUp"));
		setAnimation(ANIMATION_WALK_RIGHT, new Animation(4, 4, 32, 32, "skeleton/walkRight"));
		setAnimation(ANIMATION_WALK_LEFT, new Animation(4, 4, 32, 32, "skeleton/walkLeft"));
		setAnimation(ANIMATION_SWIM_DOWN, new Animation(2, 3, 32, 32, "skeleton/swimDown"));
		setAnimation(ANIMATION_SWIM_UP, new Animation(2, 3, 32, 32, "skeleton/swimUp"));
		setAnimation(ANIMATION_SWIM_RIGHT, new Animation(2, 3, 32, 32, "skeleton/swimRight"));
		setAnimation(ANIMATION_SWIM_LEFT, new Animation(2, 3, 32, 32, "skeleton/swimLeft"));
		setAnimation(ANIMATION_FROZEN, new Animation(1, 1, 32, 32, "skeleton/frozen"));
		
		type = TYPE_ENEMY;
		name = "Skeleton";

		health = healthMax = 30;
		defense = defenseMax = 	25;
		speed = speedMax = 5;
		swimSpeed = swimSpeedMax = 3;

		boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x/2,transform.scale.y/1.2f));
		
		ai_mtt = new AiMoveTowardsTarget(this);
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		useAnimation(ANIMATION_IDLE_DOWN);
		
		Vector2f movement = new Vector2f();
		
		int movementSpeed;

		if(world.getTile(getXOnMap(), getYOnMap()) != null && inWater() && !noclip) {
			movementSpeed = swimSpeed;
		} else {
			movementSpeed = speed;
		}

		if(!hasStatusEffect("effectFrozen") && target != null && !hasStatusEffect("effectStunned")) {
			movement.add(ai_mtt.getMovement(new Vector2f(target.getX(), target.getY()), movementSpeed, delta));
		}


		if(world.getTile(getXOnMap(), getYOnMap()) != null && inWater()) {
			if(direction == DIRECTION_RIGHT) {
				useAnimation(ANIMATION_SWIM_RIGHT);
			} else if(direction == DIRECTION_LEFT) {
				useAnimation(ANIMATION_SWIM_LEFT);
			} else if(direction == DIRECTION_UP) {
				useAnimation(ANIMATION_SWIM_UP);
			} else {
				useAnimation(ANIMATION_SWIM_DOWN);
			}
		} else if(movement.x != 0 || movement.y != 0) {
			if(direction == DIRECTION_RIGHT) {
				useAnimation(ANIMATION_WALK_RIGHT);
			} else if(direction == DIRECTION_LEFT) {
				useAnimation(ANIMATION_WALK_LEFT);
			} else if(direction == DIRECTION_UP) {
				useAnimation(ANIMATION_WALK_UP);
			} else {
				useAnimation(ANIMATION_WALK_DOWN);
			}
		} else {
			if(direction == DIRECTION_RIGHT) {
				useAnimation(ANIMATION_IDLE_RIGHT);
			} else if(direction == DIRECTION_LEFT) {
				useAnimation(ANIMATION_IDLE_LEFT);
			} else if(direction == DIRECTION_UP) {
				useAnimation(ANIMATION_IDLE_UP);
			} else {
				useAnimation(ANIMATION_IDLE_DOWN);
			}
		}
		
		move(movement);
		
		super.update(delta, win, camera, world);
	}

}
