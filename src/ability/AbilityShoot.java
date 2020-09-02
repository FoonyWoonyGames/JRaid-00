package ability;

import entity.Arrow;
import entity.Entity;
import entity.Fireball;
import entity.Player;
import entity.Transform;
import render.Camera;
import util.Window;
import world.World;

public class AbilityShoot extends Ability {
	
	private boolean projectileSent;
	

	public AbilityShoot(Player user) {
		super(user);
		
		name = "Shoot";
		description = "Shoots an arrow.";
		cooldown = 200;
		
		projectileSent = true;
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		if(user.hasAnimationPlayed(animationToUse) && user.abilityUsing == this) {
			user.abilityUsing = null;
		}
		
		if(!projectileSent && user.isAnimationAtFrame(3)) {
			projectileSent = true;
			Arrow arrow = new Arrow(new Transform(), user);
			arrow.setDirection(user.getDirection());
			arrow.setPosition(user.getX()*2 - 1, user.getY()*2 + 0.5f);
			world.addEntity(arrow);
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
