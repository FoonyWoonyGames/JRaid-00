package entity;

import org.joml.Vector2f;

import ability.AbilityBindingOfDeath;
import ability.AbilityNecroblast;
import ability.AbilityPlaguefire;
import ability.AbilityShadowball;
import ability.AbilitySummonSkeleton;
import ai.AiMoveTowardsTarget;
import collision.AABB;
import effect.EffectArcaneProtection;
import render.Animation;
import render.Camera;
import util.Window;
import world.World;

public class BossAlzoth extends Enemy {
	
	public static final int ANIMATION_IDLE_DOWN = 0;
	public static final int ANIMATION_IDLE_UP = 1;
	public static final int ANIMATION_MAX = 2;

	public BossAlzoth(Transform transform) {
		super(ANIMATION_MAX, transform);

		transform.scale.x = 4;
		transform.scale.y = 4;
		
		setAnimation(ANIMATION_IDLE_DOWN, new Animation(4, 4, 64, 64, "bossAlzoth/idleDown"));
		setAnimation(ANIMATION_IDLE_UP, new Animation(4, 4, 64, 64, "bossAlzoth/idleUp"));
		
		type = TYPE_ENEMY;
		name = "Boss Alzoth";

		health = healthMax = 500;
		defense = defenseMax = 200;
		speed = speedMax = 5;
		swimSpeed = swimSpeedMax = 3;

		boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x/4,transform.scale.y/2.4f));
		
//		addAbility(new AbilitySummonSkeleton(this));
//		addAbility(new AbilityShadowball(this));
//		addAbility(new AbilityPlaguefire(this));
//		addAbility(new AbilityNecroblast(this));
		addAbility(new AbilityBindingOfDeath(this));
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		super.update(delta, win, camera, world);
		useAnimation(ANIMATION_IDLE_UP);
		direction = DIRECTION_UP;
		
		for(int i = 0; i < abilitySet.size(); i++) {
			abilitySet.get(i).update(delta, win, camera, world);
			useAbility(delta, win, camera, world, i);
		}
		

	}

}
