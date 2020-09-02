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

public class Portal extends Entity {
	

	public static final int ANIMATION_OFF = 0;
	public static final int ANIMATION_ON = 1;
	public static final int ANIMATION_MAX = 2;
	
	private boolean open;
	
	public Portal(Transform transform) {
		super(ANIMATION_MAX, transform);

		transform.scale.x = 3;
		transform.scale.y = 3;

		boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(0.5f,transform.scale.y));

		setAnimation(ANIMATION_OFF, new Animation(1, 1, 46, 46, "abilities/portalEmpty"));
		setAnimation(ANIMATION_ON, new Animation(8, 6, 46, 46, "abilities/portal"));

		noclip = true;
		type = Entity.TYPE_ABILITY;
		
		speed = 30;
		direction = DIRECTION_DOWN;
		health = 1;
		
		setBehind(true);
		
	}

	long particleSpawned;
	boolean entered = false;
	
	public void update(float delta, Window win, Camera camera, World world) {
		
		if(open) {
			this.useAnimation(ANIMATION_ON);
			if(System.currentTimeMillis() - particleSpawned >= 200) {
				particleSpawned = System.currentTimeMillis();
				float x = (float) (1*Math.random());
				float y = (float) (1*Math.random());
				world.spawnParticle(Particle.PARTICLE_MAGIC, 4, 4, 16, 16, getX()-0.5f+x, getY()-0.5f+y, false, 1.2f, 0, 2, 20, false);
			
			}

			for(int i = 0; i < world.getEntityList().size(); i++) {
				Entity e = world.getEntityList().get(i);
				if(e.type == Entity.TYPE_PLAYER) {
					if(isColliding(e)) {
//						e.randomizePosition(world);

						float x = (float) (1*Math.random());
						float y = (float) (1*Math.random());
						if(!entered) {
							entered = true;
							world.spawnParticle(Particle.PARTICLE_MAGIC, 4, 12, 16, 16, e.getX()-0.5f+x, e.getY()-0.5f+y, false, 8, 180, 50, 300, false);
						}
					} else {
						entered = false;
					}
				}
			}
		}
		else {
			this.useAnimation(ANIMATION_OFF);
		}
	}
	public void setOpen(boolean b) {
		open = b;
	}

}
