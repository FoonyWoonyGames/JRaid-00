package ability;

import effect.*;
import entity.Enemy;
import entity.Entity;
import entity.Particle;
import entity.Player;
import render.Camera;
import util.Window;
import world.World;

public class AbilityFleshWound extends Ability {

	public AbilityFleshWound(Player user) {
		super(user);
		
		name = "Hack";
		description = "Swings a powerful hit with your axe.";
		cooldown = 10000;
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		if(user.hasAnimationPlayed(animationToUse) && user.abilityUsing == this) {
			user.abilityUsing = null;
		}
	}

	@Override
	public void use(float delta, Window win, Camera camera, World world) {
		if(isCooled()) {
			user.abilityUsing = this;
			if(user.getDirection() == Entity.DIRECTION_DOWN) {
				animationToUse = Player.ANIMATION_ATTACK_ONE_DOWN;
			} else if(user.getDirection() == Entity.DIRECTION_UP) {
				animationToUse = Player.ANIMATION_ATTACK_ONE_UP;
			} else if(user.getDirection() == Entity.DIRECTION_RIGHT) {
				animationToUse = Player.ANIMATION_ATTACK_ONE_RIGHT;
			} else if(user.getDirection() == Entity.DIRECTION_LEFT) {
				animationToUse = Player.ANIMATION_ATTACK_ONE_LEFT;
			}
			
			user.useAnimation(animationToUse);
			user.resetAnimation(animationToUse);
			
			Entity enemyToHit = null;
			for(int i = 0; i < user.getEntitiesInFront(0, 0, 0).size(); i++) {
				Entity e = user.getEntitiesInFront(0, 0, 0).get(i);
				if(e.type == user.getEnemy()) {
					enemyToHit = e;
					enemyToHit.hurt(user, 8, Math.random());
					enemyToHit.push(delta, user.getDirection(), 125);
					enemyToHit.addStatusEffect(new EffectBleed(user, enemyToHit, 1.0f, 250, 5000));
					user.getWorld().spawnParticle(Particle.PARTICLE_BLOOD, 5, 25, 16, 16, enemyToHit.getX(), enemyToHit.getY(), false, 5, 0, 15, 360, false);
				}
			}
			if(enemyToHit != null) {
			}
		}
	}
}
