package ability;

import effect.EffectBoundOfDeath;
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

public class AbilityBindingOfDeath extends Ability {
	
	public AbilityBindingOfDeath(Entity user) {
		super(user);
		
		name = "Binding of Death";
		description = "Binds near targets to the caster, which upon death, will increase the caster's damage.";
		cooldown = 5000;
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
						world.spawnParticle(Particle.PARTICLE_BINDINGOFDEATH, 9, 9, 32, 32, e, false, 0, 0, false);
						e.addStatusEffect(new EffectBoundOfDeath(user, e, 1, 1, 10000));
					}
				}
			} catch(NullPointerException e) {
//				e.printStackTrace();
			}
		}
	}

}
