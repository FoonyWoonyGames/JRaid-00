package entity;

import java.awt.event.ActionListener;

import org.joml.Vector2f;

import ai.AiMoveTowardsTarget;
import collision.AABB;
import effect.EffectArcaneProtection;
import raidgame.Game;
import render.Animation;
import render.Camera;
import util.Window;
import world.World;

public class Flag extends Enemy {
	
	public static final int ANIMATION_FLAG_BUG = 0;
	public static final int ANIMATION_FLAG_GRAPHIC = 1;
	public static final int ANIMATION_FLAG_INTEREST = 2;
	public static final int ANIMATION_FLAG_PROGRAM = 3;
	public static final int ANIMATION_MAX = 4;
	
	private ActionListener action;
	private String message;
	

	public Flag(Transform transform) {
		super(ANIMATION_MAX, transform);
		
		setAnimation(ANIMATION_FLAG_BUG, new Animation(1, 1, 32, 32, "flag/flagBug"));
		setAnimation(ANIMATION_FLAG_GRAPHIC, new Animation(1, 1, 32, 32, "flag/flagGraphic"));
		setAnimation(ANIMATION_FLAG_INTEREST, new Animation(1, 1, 32, 32, "flag/flagInterest"));
		setAnimation(ANIMATION_FLAG_PROGRAM, new Animation(1, 1, 32, 32, "flag/flagProgram"));
		
		type = TYPE_ENEMY;
		name = "DEV-Flag";

		health = healthMax = 1;
		defense = defenseMax = 0;
		speed = speedMax = 1;
		swimSpeed = swimSpeedMax = 1;

		boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x/2,transform.scale.y/1.2f));
		
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		super.update(delta, win, camera, world);

	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setAction(ActionListener action) {
		this.action = action;
	}

	public void hurt(Entity attacker, float hp, double crit) {
		super.hurt(attacker, hp, crit);
		if(message != null) {
			Game.ConsoleSend("[DEV FLAG] -- MESSAGE: " + this.message);
		}
		if(action != null) {
			this.action.actionPerformed(null);
		}
		if(message == null && action == null) {
			Game.ConsoleSend("[DEV FLAG] -- This flag is empty!");
		}
	}
	
	public void setFlag(int flag) {
		this.useAnimation(flag);
	}

}
