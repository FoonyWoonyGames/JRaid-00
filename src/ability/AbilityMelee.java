package ability;

import effect.*;
import entity.Enemy;
import entity.Entity;

import entity.Player;
import render.Camera;
import util.Window;
import world.World;

public class AbilityMelee extends Ability {

	public AbilityMelee(Player user) {
		super(user);
		
		name = "Melee";
		description = "Lets out a punch, which stuns the victim for 250 milliseconds.";
		cooldown = 300;
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
			float xLeft = 0;
			float yDown = 0;
			float xRight = 0;
			float yUp = 0;
			if(user.getDirection() == Entity.DIRECTION_DOWN) {
				animationToUse = Player.ANIMATION_ATTACK_ONE_DOWN;
				xLeft = user.getX() - 1;
				yDown = user.getY() - 1;
				xRight = user.getX() + 1;
				yUp = user.getY() + 0.1f;
			} else if(user.getDirection() == Entity.DIRECTION_UP) {
				animationToUse = Player.ANIMATION_ATTACK_ONE_UP;
				xLeft = user.getX() - 1;
				yDown = user.getY() - 0.1f;
				xRight = user.getX() + 1;
				yUp = user.getY() + 1;
			} else if(user.getDirection() == Entity.DIRECTION_RIGHT) {
				animationToUse = Player.ANIMATION_ATTACK_ONE_RIGHT;
				xLeft = user.getX() - 0.1f;
				yDown = user.getY() - 1;
				xRight = user.getX() + 1;
				yUp = user.getY() + 1;
			} else if(user.getDirection() == Entity.DIRECTION_LEFT) {
				animationToUse = Player.ANIMATION_ATTACK_ONE_LEFT;
				xLeft = user.getX() - 1;
				yDown = user.getY() - 1;
				xRight = user.getX() + 0.1f;
				yUp = user.getY() + 1;
			}
			
			user.useAnimation(animationToUse);
			user.resetAnimation(animationToUse);
			
			Entity enemyToHit = null;
			for(int i = 0; i < user.getEntitiesInFront(0, 0, 0).size(); i++) {
				Entity e = user.getEntitiesInFront(0, 0, 0).get(i);
				if(e.type == user.getEnemy()) {
					if(enemyToHit == null || e.isCloser(user, enemyToHit)) enemyToHit = e;
				}
			}
			if(enemyToHit != null) {
				enemyToHit.hurt(user, 5, Math.random());
				enemyToHit.push(delta, user.getDirection(), 80);
				enemyToHit.addStatusEffect(new EffectStunned(user, enemyToHit, 1.0f, 1, 250));
			}
		}
	}
}
