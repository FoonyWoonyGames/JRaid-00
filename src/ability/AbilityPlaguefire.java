package ability;

import effect.EffectFreezing;
import effect.EffectPlagued;
import entity.Entity;
import entity.Froststorm;
import entity.Particle;
import entity.Player;
import entity.Transform;
import raidgame.Game;
import render.Camera;
import util.Window;
import world.World;

public class AbilityPlaguefire extends Ability {

	private boolean active;
	private Froststorm storm;
	
	public AbilityPlaguefire(Entity user) {
		super(user);
		
		name = "Plaguefire";
		description = "Spews a burning plague forward.";
		cooldown = 30000;
	}

	long particleSpawned;
	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		
		if(active) {
			if(System.currentTimeMillis() - particleSpawned >= 100) {
				particleSpawned = System.currentTimeMillis();
				float x = (float) (1*Math.random());
				float y = (float) (1*Math.random());
				world.spawnParticle(Particle.PARTICLE_PLAGUE, 3, 3, 16, 16, user.getX()+0.5f, user.getY()+1,
						false, 15, 360+(90*user.getDirection()), 10, 20, false);
			
			}
			try {
				for(int i = 0; i < user.getWorld().getEntityList().size(); i++) {
					Entity e = user.getWorld().getEntityList().get(i);
					if(user.getEntitiesInFront(5, 1, 0.1f).contains(e)) {
						if(e.type == user.getEnemy()) {
							if(!e.hasStatusEffect("effectPlagued")) {
								e.addStatusEffect(new EffectPlagued(user, e, 1.0f, 100, 5000));
							}
						}
					}
				}
			} catch(NullPointerException e) {
				e.printStackTrace();
			}
		}

		if(System.currentTimeMillis() - getLastUse() > 5000) {
			active = false;
			user.abilityUsing = null;
		}
	}

	@Override
	public void use(float delta, Window win, Camera camera, World world) {
		if(isCooled()) {
			active = true;
		}
	}

}
