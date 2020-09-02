package entity;

import org.joml.Vector2f;

import ai.AiMoveTowardsTarget;
import collision.AABB;
import effect.EffectArcaneProtection;
import render.Animation;
import render.Camera;
import util.Window;
import world.World;

public class Soldier extends Enemy {
	
	public static final int ANIMATION_IDLE_DOWN = 0;
	public static final int ANIMATION_MAX = 1;
	

	public Soldier(Transform transform) {
		super(ANIMATION_MAX, transform);
		
		setAnimation(ANIMATION_IDLE_DOWN, new Animation(2, 2, 32, 32, "soldier/idleDown"));
		
		type = TYPE_ENEMY;
		name = "Soldier";

		health = healthMax = 30;
		defense = defenseMax = 35;
		speed = speedMax = 5;
		swimSpeed = swimSpeedMax = 3;

		boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x/2,transform.scale.y/1.2f));
		
		addStatusEffect(new EffectArcaneProtection(this, this, 99, 0, 0));
		
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		
		Vector2f movement = new Vector2f();
		
		int movementSpeed;

		if(world.getTile(getXOnMap(), getYOnMap()) != null && inWater() && !noclip) {
			movementSpeed = swimSpeed;
		} else {
			movementSpeed = speed;
		}
		

		super.update(delta, win, camera, world);
		
		move(movement);

	}

}
