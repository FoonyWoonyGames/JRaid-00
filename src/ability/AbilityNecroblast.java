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

public class AbilityNecroblast extends Ability {
	
	public AbilityNecroblast(Entity user) {
		super(user);
		
		name = "Necroblast";
		description = "Blasts a target in front of the caster with a necromantic force.";
		cooldown = 15000;
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		

		if(System.currentTimeMillis() - getLastUse() > 1) {
			user.abilityUsing = null;
		}
	}

	@Override
	public void use(float delta, Window win, Camera camera, World world) {
		if(isCooled()) {
			try {
				for(int i = 0; i < user.getWorld().getEntityList().size(); i++) {
					Entity e = user.getWorld().getEntityList().get(i);
					if(user.getEntitiesInFront(5, 5, 0.1f).contains(e)) {
						if(e.type == user.getEnemy()) {
							world.spawnParticle(Particle.PARTICLE_UNDEATH, 5, 21, 16, 16, e.getX(), e.getY(), false, 8, 180, 10, 360, false);
						}
						break;
					}
				}
			} catch(NullPointerException e) {
//				e.printStackTrace();
			}
		}
	}

}
