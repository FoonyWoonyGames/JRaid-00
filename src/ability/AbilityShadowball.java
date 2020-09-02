package ability;

import entity.Entity;
import entity.Fireball;
import entity.Player;
import entity.Shadowball;
import entity.Transform;
import render.Camera;
import util.Window;
import world.World;

public class AbilityShadowball extends Ability {
	
	private boolean projectileSent;
	

	public AbilityShadowball(Entity user) {
		super(user);
		
		name = "Shadowball";
		description = "Shoots a shadowball forward.";
		cooldown = 1000;
		
		projectileSent = true;
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		if(user.hasAnimationPlayed(animationToUse) && user.abilityUsing == this) {
			user.abilityUsing = null;
		}
		
		if(!projectileSent) {
			projectileSent = true;
			Shadowball shadowball = new Shadowball(new Transform(), user);
			shadowball.setDirection(user.getDirection());
			shadowball.setPosition(user.getX()*2, user.getY()*2 + 1);
			world.addEntity(shadowball);
		}
	}

	@Override
	public void use(float delta, Window win, Camera camera, World world) {
		if(isCooled()) {
			user.abilityUsing = this;
			projectileSent = false;
		}
	}

}
