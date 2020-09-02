package entity;

import java.util.ArrayList;

import org.joml.Vector2f;

import collision.AABB;
import effect.EffectBurned;
import raidgame.Game;
import render.Animation;
import render.Camera;
import util.Window;
import world.World;

public class Shadowball extends Projectile {
	
	private boolean hasHit;
	
	public Shadowball(Transform transform, Entity sender) {
		super(ANIMATION_MAX, transform, sender);

		transform.scale.x = 2;
		transform.scale.y = 2;

		boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(1,transform.scale.y));
		
		setAnimation(ANIMATION_DOWN, new Animation(4, 12, 16, 16, "abilities/shadowballDown"));
		setAnimation(ANIMATION_UP, new Animation(4, 12, 16, 16, "abilities/shadowballUp"));
		setAnimation(ANIMATION_RIGHT, new Animation(4, 12, 16, 16, "abilities/shadowballRight"));
		setAnimation(ANIMATION_LEFT, new Animation(4, 12, 16, 16, "abilities/shadowballLeft"));
		setAnimation(ANIMATION_DIE, new Animation(4, 12, 16, 16, "abilities/shadowballExplosion").setLooping(false));

		
		speed = 50;
		direction = DIRECTION_DOWN;
		health = 1;
		
	}

	boolean particleSpawned = false;
	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		super.update(delta, win, camera, world);
		
		if(hasHit()) {
			useAnimation(ANIMATION_DIE);
			if(animations[ANIMATION_DIE].hasPlayedOnce()) {
				health = 0;
			}
		}
		
		if(!hasHit) {
			float py = 0;
			if(getDirection() == DIRECTION_UP) {
				py = getY() + 1;
			} else if(getDirection() == DIRECTION_DOWN) {
				py = getY() + 0;
			} else {
				py = getY() + 0.6f;
			}
			if(!isWithinMap(world)) {
				hasHit = true;
			}
			for(int i = 0; i < world.getEntityList().size(); i++) {
				Entity e = world.getEntityList().get(i);
				if(e.type == Entity.TYPE_PLAYER && isColliding(e)) {
					Entity enemy = e;
					hasHit = true;
					enemy.push(delta, direction, 80);
					enemy.hurt(sender, 15, Math.random());
				}
			}
		}
		
	}

}
