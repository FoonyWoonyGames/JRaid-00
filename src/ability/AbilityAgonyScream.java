package ability;

import effect.EffectBoundOfDeath;
import effect.EffectFreezing;
import effect.EffectPlagued;
import effect.EffectTerrified;
import entity.Entity;
import entity.Froststorm;
import entity.Particle;
import entity.Player;
import entity.Transform;
import raidgame.Game;
import render.Camera;
import util.Window;
import world.World;

public class AbilityAgonyScream extends Ability {
	
	public AbilityAgonyScream(Entity user) {
		super(user);
		
		name = "Agonizing Scream";
		description = "Slows nearby enemies down.";
		cooldown = 12000;
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
						e.addStatusEffect(new EffectTerrified(user, e, 1, 1, 2000));
					}
				}
			} catch(NullPointerException e) {
//				e.printStackTrace();
			}
		}
	}

}
