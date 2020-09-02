package entity;

import java.util.ArrayList;

import ability.Ability;
import ai.Ai;
import effect.EffectFreezing;
import effect.EffectStunned;
import event.EventEntityDamaged;
import raidgame.Game;
import render.Camera;
import util.Window;
import world.World;

public class Enemy extends Entity {
	
	protected Entity target;
	protected boolean moving;
	protected ArrayList<Ai> aiList;
	protected ArrayList<Ability> abilitySet;

	public Enemy(int maxAnimations, Transform transform) {
		super(maxAnimations, transform);
		
		type = TYPE_ENEMY;
		
		name = "Enemy";
		aiList = new ArrayList<Ai>();
		abilitySet = new ArrayList<Ability>();
		
		// Spawning-Fatigue
		addStatusEffect(new EffectStunned(this, this, 1.0f, 0, 800));
		
		
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {

		if(!isWithinMap(world)) {
			randomizePosition(world);
		}
		for(int i = 0; i < statusEffects.size(); i++) {
			statusEffects.get(i).update(delta, win, camera, world);
		}
		
		if(target == null) {
			for(int i = 0; i < world.getEntityList().size(); i++) {
				if(world.getEntityList().get(i).type == TYPE_PLAYER) {
					target = world.getEntityList().get(i);
				}
			}
		}

		for(int i = 0; i < world.getEntityList().size(); i++) {
			Entity e = world.getEntityList().get(i);
			if(isColliding(e) && (e.type == TYPE_ENEMY || e.type == TYPE_PLAYER) && !e.hasStatusEffect("effectFrozen") && !e.hasNoclip() && !e.hasStatusEffect("effectStunned")) {
				if(e.getX() < getX() && e.getX() > getX() - 0.25f) {
					e.push(delta, DIRECTION_LEFT, 3);
				} else if(e.getX() > getX() && e.getX() < getX() + 0.25f) {
					e.push(delta, DIRECTION_RIGHT, 3);
				}
				if(e.getY() < getY() && e.getY() > getY() - 0.25f) {
					e.push(delta, DIRECTION_DOWN, 3);
				} else if(e.getY() > getY() && e.getY() < getY() + 0.25f) {
					e.push(delta, DIRECTION_UP, 3);
				}
			}
		}

//		if(!hasStatusEffect(2)) {
//			movement.add(speed*delta, 0);
//		}
		
		
	}
	


	@Override
	public void onDeath(World w) {
		Game.ConsoleSend(this.getName() + " has died.");
	}
	
	public void addAi(Ai ai) {
		aiList.add(ai);
	}
	
	public void removeAi(Ai ai) {
		aiList.remove(ai);
	}
	
	protected void addAbility(Ability a) {
		abilitySet.add(a);
	}

	public void useAbility(float delta, Window win, Camera camera, World world, int slot) {
		if(abilitySet.get(slot) != null) {
			if(abilitySet.get(slot).isCooled()) {
				abilitySet.get(slot).use(delta, win, camera, world);
				abilitySet.get(slot).setLastUse();
				Game.ConsoleSend(name + " used \"" + abilitySet.get(slot).getName() + "\"");
			}
		} else {
			Game.ConsoleSend("[ERROR] -- " + name + " does not have an ability in slot " + slot);
		}
	}

	@Override
	public void collidingWithParticle(Particle p) {
//		if(p.getParticleTextureID() == Particle.PARTICLE_FROSTSTORM) {
//			if(!hasStatusEffect("effectFreezing")) {
//				addStatusEffect(new EffectFreezing(this, 1.0f, 1000, 0));
//			}
//		}
//		if(p.getParticleTextureID() == Particle.PARTICLE_MANANOVA) {
//			hurt(0.5f, Math.random()*2);
//		}
	}

}
