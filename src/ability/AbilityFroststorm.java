package ability;

import effect.EffectFreezing;
import entity.Entity;
import entity.Froststorm;
import entity.Particle;
import entity.Player;
import entity.Transform;
import render.Camera;
import util.Window;
import world.World;

public class AbilityFroststorm extends Ability {

	private boolean active;
	private Froststorm storm;
	
	public AbilityFroststorm(Player user) {
		super(user);
		
		name = "Frost Storm";
		description = "Releases a storm of frost, that freezes all enemies within it.";
		storm = new Froststorm(new Transform());
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		
		if(active) {
			world.spawnParticle(Particle.PARTICLE_FROSTSTORM, 4, 4, 16, 16, user.getX(), user.getY(),
					false, 8, 360+(90*user.getDirection()), 5, 20, false);
			try {
				for(int i = 0; i < user.getWorld().getEntityList().size(); i++) {
					Entity e = user.getWorld().getEntityList().get(i);
					if(user.getEntitiesInFront(5, 1, 0.1f).contains(e)) {
						if(e.type == user.getEnemy()) {
							if(!e.hasStatusEffect("effectFreezing")) {
								e.addStatusEffect(new EffectFreezing(user, e, 1.0f, 1000, 0));
							}
						}
					} else {
						if(e.hasStatusEffect("effectFreezing")) {
							e.removeStatusEffect("effectFreezing");
						}
					}
				}
			} catch(NullPointerException e) {
				e.printStackTrace();
			}
			user.useAnimation(Player.ANIMATION_ATTACK_THREE_DOWN);
		} else {
			try {
				for(int i = 0; i < user.getWorld().getEntityList().size(); i++) {
					Entity e = user.getWorld().getEntityList().get(i);
					if(e.hasStatusEffect("effectFreezing")) {
						e.removeStatusEffect("effectFreezing");
					}
				}
			} catch(NullPointerException e) {
//				e.printStackTrace();
			}
		}
		active = false;

		if(System.currentTimeMillis() - getLastUse() > 1 && System.currentTimeMillis() - getLastUse() < 100 && user.abilityUsing == this) {
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
