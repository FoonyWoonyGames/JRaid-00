package ability;

import effect.*;
import entity.Enemy;
import entity.Entity;
import entity.Particle;
import entity.Player;
import entity.Skeleton;
import entity.Transform;
import raidgame.Game;
import render.Camera;
import util.Window;
import world.World;

public class AbilitySummonSkeleton extends Ability {

	public AbilitySummonSkeleton(Entity user) {
		super(user);
		
		name = "Raise Dead";
		description = "Raises two skeletons from the dead to fight for the caster.";
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
			Enemy s1 = new Skeleton(new Transform());
			s1.setHealth(s1.getMaxHealth());
			world.addEntity(s1);
			s1.setPosition(user.getX()*2+2, user.getY()*2);
			Enemy s2 = new Skeleton(new Transform());
			s2.setHealth(s2.getMaxHealth());
			world.addEntity(s2);
			s2.setPosition(user.getX()*2-4, user.getY()*2);
		}
	}
}
