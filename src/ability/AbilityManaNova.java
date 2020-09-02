package ability;

import entity.Entity;
import entity.Fireball;
import entity.Particle;
import entity.Player;
import entity.Transform;
import render.Camera;
import util.Window;
import world.World;

public class AbilityManaNova extends Ability {
	

	public AbilityManaNova(Player user) {
		super(user);
		
		name = "[ULT] Mana Nova";
		description = "Releases a huge amount of Mana and transforms it into destruction-magic.";
		cooldown = 500;
	}
	
	boolean active;
	long activeTime;

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		if(user.isUsingAnimation(Player.ANIMATION_ATTACK_ULT) && user.isAnimationAtFrame(3)) {
			active = true;
			activeTime = System.currentTimeMillis();
		}
		if(user.hasAnimationPlayed(animationToUse) && user.abilityUsing == this) {
			user.abilityUsing = null;
		}
		
		if(active && System.currentTimeMillis() - activeTime < 500) {
			world.spawnParticle(Particle.PARTICLE_MANANOVA, 5, 5, 16, 16, user.getX(), user.getY(), false, 30, 180, 15, 360, false);
		}
	}

	@Override
	public void use(float delta, Window win, Camera camera, World world) {
		if(isCooled()) {
			if(user.getDirection() == Entity.DIRECTION_DOWN) {
				animationToUse = Player.ANIMATION_ATTACK_ULT;
			} else if(user.getDirection() == Entity.DIRECTION_UP) {
				animationToUse = Player.ANIMATION_ATTACK_ULT;
			} else if(user.getDirection() == Entity.DIRECTION_RIGHT) {
				animationToUse = Player.ANIMATION_ATTACK_ULT;
			} else if(user.getDirection() == Entity.DIRECTION_LEFT) {
				animationToUse = Player.ANIMATION_ATTACK_ULT;
			}
			user.resetAnimation(animationToUse);
			user.useAnimation(animationToUse);
			user.abilityUsing = this;
		}
	}

}
