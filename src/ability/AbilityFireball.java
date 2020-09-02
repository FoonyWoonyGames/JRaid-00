package ability;

import entity.Entity;
import entity.Fireball;
import entity.Player;
import entity.Transform;
import render.Camera;
import util.Window;
import world.World;

public class AbilityFireball extends Ability {
	
	private boolean projectileSent;
	

	public AbilityFireball(Player user) {
		super(user);
		
		name = "Fireball";
		description = "Shoots out a fireball, that has a 5% chance of burning the victim.";
		cooldown = 400;
		
		projectileSent = true;
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		if(user.hasAnimationPlayed(animationToUse) && user.abilityUsing == this) {
			user.abilityUsing = null;
		}
		
		if(!projectileSent && user.isAnimationAtFrame(3)) {
			projectileSent = true;
			Fireball fireball = new Fireball(new Transform(), user);
			fireball.setDirection(user.getDirection());
			fireball.setPosition(user.getX()*2 - 1, user.getY()*2 + 0.5f);
			world.addEntity(fireball);
		}
	}

	@Override
	public void use(float delta, Window win, Camera camera, World world) {
		if(isCooled()) {
			user.abilityUsing = this;
			if(user.getDirection() == Entity.DIRECTION_DOWN) {
				animationToUse = Player.ANIMATION_ATTACK_TWO_DOWN;
			} else if(user.getDirection() == Entity.DIRECTION_UP) {
				animationToUse = Player.ANIMATION_ATTACK_TWO_UP;
			} else if(user.getDirection() == Entity.DIRECTION_RIGHT) {
				animationToUse = Player.ANIMATION_ATTACK_TWO_RIGHT;
			} else if(user.getDirection() == Entity.DIRECTION_LEFT) {
				animationToUse = Player.ANIMATION_ATTACK_TWO_LEFT;
			}
			user.resetAnimation(animationToUse);
			user.useAnimation(animationToUse);
			projectileSent = false;
		}
	}

}
