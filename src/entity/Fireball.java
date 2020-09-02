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

public class Fireball extends Projectile {
	
	private boolean hasHit;
	
	public Fireball(Transform transform, Entity sender) {
		super(ANIMATION_MAX, transform, sender);

		transform.scale.x = 1;
		transform.scale.y = 1;

		boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(0.5f,transform.scale.y));
		
		setAnimation(ANIMATION_DOWN, new Animation(4, 12, 16, 16, "abilities/fireballDown"));
		setAnimation(ANIMATION_UP, new Animation(4, 12, 16, 16, "abilities/fireballUp"));
		setAnimation(ANIMATION_RIGHT, new Animation(4, 12, 16, 16, "abilities/fireballRight"));
		setAnimation(ANIMATION_LEFT, new Animation(4, 12, 16, 16, "abilities/fireballLeft"));
		setAnimation(ANIMATION_DIE, new Animation(4, 12, 16, 16, "abilities/fireballExplosion").setLooping(false));

		
		speed = 30;
		direction = DIRECTION_DOWN;
		health = 1;
		
	}

	boolean particleSpawned = false;
	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		super.update(delta, win, camera, world);
		
		if(hasHit()) {
			if(!particleSpawned) {
				particleSpawned = true;
				world.spawnParticle(Particle.PARTICLE_FIRE, 3, 18, 16, 16, getX(), getY(), false, 10, 0, 15, 360, false);			}
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
			world.spawnParticle(Particle.PARTICLE_FIRE, 3, 36, 16, 16, getX(), getY()+0.6f, false, 3, 0, 10, 90, false);
			if(!isWithinMap(world)) {
				hasHit = true;
			}
			for(int i = 0; i < world.getEntityList().size(); i++) {
				Entity e = world.getEntityList().get(i);
				if(e.type == Entity.TYPE_ENEMY && isColliding(e)) {
					Entity enemy = e;
					hasHit = true;
					enemy.push(delta, direction, 80);
					enemy.hurt(sender, 12, Math.random());
					if(Math.random()*100 <= 5) {
						e.addStatusEffect(new EffectBurned(sender, enemy, 1.0f, 1, 1500));
						Game.ConsoleSend(enemy.getName() + " was burned.");
					}
				}
			}
		} else {
			world.killParticles(getX(), getY(), 2, Particle.PARTICLE_FIRE);
		}
		
	}

}
