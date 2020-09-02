package entity;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;
import org.joml.Vector3f;

import ability.Ability;
import collision.AABB;
import playerclass.ClassRanger;
import playerclass.ClassSorcerer;
import playerclass.ClassTemplate;
import playerclass.ClassTest;
import playerclass.ClassWarrior;
import raidgame.Game;
import render.Animation;
import render.Camera;
import util.Window;
import world.Tile;
import world.World;

public class Player extends Entity {

	public static final int ANIMATION_IDLE_DOWN = 0;
	public static final int ANIMATION_IDLE_UP = 1;
	public static final int ANIMATION_IDLE_RIGHT = 2;
	public static final int ANIMATION_IDLE_LEFT = 3;
	public static final int ANIMATION_WALK_DOWN = 4;
	public static final int ANIMATION_WALK_UP = 5;
	public static final int ANIMATION_WALK_RIGHT = 6;
	public static final int ANIMATION_WALK_LEFT = 7;
	public static final int ANIMATION_SWIM_DOWN = 8;
	public static final int ANIMATION_SWIM_UP = 9;
	public static final int ANIMATION_SWIM_RIGHT = 10;
	public static final int ANIMATION_SWIM_LEFT = 11;
	public static final int ANIMATION_ATTACK_ONE_DOWN = 12;
	public static final int ANIMATION_ATTACK_ONE_UP = 13;
	public static final int ANIMATION_ATTACK_ONE_RIGHT = 14;
	public static final int ANIMATION_ATTACK_ONE_LEFT = 15;
	public static final int ANIMATION_ATTACK_TWO_DOWN = 16;
	public static final int ANIMATION_ATTACK_TWO_UP = 17;
	public static final int ANIMATION_ATTACK_TWO_RIGHT = 18;
	public static final int ANIMATION_ATTACK_TWO_LEFT = 19;
	public static final int ANIMATION_ATTACK_THREE_DOWN = 20;
	public static final int ANIMATION_ATTACK_ULT = 21;
	public static final int ANIMATION_SIZE = 22;
	
	private ClassTemplate playerclass;
	private boolean aiming;
	
	
	public Player(Transform transform) {
		super(ANIMATION_SIZE, transform);

		playerclass = new ClassSorcerer(this);
		
		setAnimation(ANIMATION_IDLE_DOWN, new Animation(2, 3, 32, 32, playerclass.getUnlocalizedName() + "/idleDown"));
		setAnimation(ANIMATION_IDLE_UP, new Animation(2, 3, 32, 32, playerclass.getUnlocalizedName() + "/idleUp"));
		setAnimation(ANIMATION_IDLE_RIGHT, new Animation(2, 3, 32, 32, playerclass.getUnlocalizedName() + "/idleRight"));
		setAnimation(ANIMATION_IDLE_LEFT, new Animation(2, 3, 32, 32, playerclass.getUnlocalizedName() + "/idleLeft"));
		setAnimation(ANIMATION_WALK_DOWN, new Animation(4, 8, 32, 32, playerclass.getUnlocalizedName() + "/walkDown"));
		setAnimation(ANIMATION_WALK_UP, new Animation(4, 8, 32, 32, playerclass.getUnlocalizedName() + "/walkUp"));
		setAnimation(ANIMATION_WALK_RIGHT, new Animation(4, 8, 32, 32, playerclass.getUnlocalizedName() + "/walkRight"));
		setAnimation(ANIMATION_WALK_LEFT, new Animation(4, 8, 32, 32, playerclass.getUnlocalizedName() + "/walkLeft"));
		setAnimation(ANIMATION_SWIM_DOWN, new Animation(2, 3, 32, 32, playerclass.getUnlocalizedName() + "/swimDown"));
		setAnimation(ANIMATION_SWIM_UP, new Animation(2, 3, 32, 32, playerclass.getUnlocalizedName() + "/swimUp"));
		setAnimation(ANIMATION_SWIM_RIGHT, new Animation(2, 3, 32, 32, playerclass.getUnlocalizedName() + "/swimRight"));
		setAnimation(ANIMATION_SWIM_LEFT, new Animation(2, 3, 32, 32, playerclass.getUnlocalizedName() + "/swimLeft"));
		setAnimation(ANIMATION_ATTACK_ONE_DOWN, new Animation(3, 9, 32, 32, playerclass.getUnlocalizedName() + "/attackOneDown").setLooping(false));
		setAnimation(ANIMATION_ATTACK_ONE_UP, new Animation(3, 9, 32, 32, playerclass.getUnlocalizedName() + "/attackOneUp").setLooping(false));
		setAnimation(ANIMATION_ATTACK_ONE_RIGHT, new Animation(3, 12, 32, 32, playerclass.getUnlocalizedName() + "/attackOneRight").setLooping(false));
		setAnimation(ANIMATION_ATTACK_ONE_LEFT, new Animation(3, 12, 32, 32, playerclass.getUnlocalizedName() + "/attackOneLeft").setLooping(false));
		setAnimation(ANIMATION_ATTACK_TWO_DOWN, new Animation(5, 12, 32, 32, playerclass.getUnlocalizedName() + "/attackTwoDown").setLooping(false));
		setAnimation(ANIMATION_ATTACK_TWO_UP, new Animation(5, 12, 32, 32, playerclass.getUnlocalizedName() + "/attackTwoUp").setLooping(false));
		setAnimation(ANIMATION_ATTACK_TWO_RIGHT, new Animation(5, 12, 32, 32, playerclass.getUnlocalizedName() + "/attackTwoRight").setLooping(false));
		setAnimation(ANIMATION_ATTACK_TWO_LEFT, new Animation(5, 12, 32, 32, playerclass.getUnlocalizedName() + "/attackTwoLeft").setLooping(false));
		setAnimation(ANIMATION_ATTACK_THREE_DOWN, new Animation(4, 6, 32, 32, playerclass.getUnlocalizedName() + "/attackThreeDown"));
		setAnimation(ANIMATION_ATTACK_ULT, new Animation(6, 12, 32, 32, playerclass.getUnlocalizedName() + "/attackUlt").setLooping(false));

		type = TYPE_PLAYER;
		
		name = "Player";
		
		health = healthMax = 50;
		defense = 30;
		speed = speedMax = 10;
		swimSpeed = swimSpeedMax = 5;
		
		attack = 1;

		boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x/2,transform.scale.y/1.2f));
	
		Game.ConsoleSend("[Loading] -- Starting game as " + playerclass.getName());
	}

	Tile hand = Tile.tileTest;
	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		Vector2f movement = new Vector2f();
		
		int movementSpeed;
		if(world.getTile(getXOnMap(), getYOnMap()) != null && inWater() && !noclip) {
			movementSpeed = swimSpeed;
		} else {
			movementSpeed = speed;
		}
		
		if(abilityUsing == null) {
			if(win.getInput().isKeyDown(GLFW_KEY_A)) {
				setDirection(DIRECTION_LEFT);
				movement.add(-movementSpeed*delta, 0);
			}
			if(win.getInput().isKeyDown(GLFW_KEY_D)) {
				setDirection(DIRECTION_RIGHT);
				movement.add(movementSpeed*delta, 0);
			}
			if(win.getInput().isKeyDown(GLFW_KEY_S)) {
				setDirection(DIRECTION_DOWN);
				movement.add(0, -movementSpeed*delta);
			}
			if(win.getInput().isKeyDown(GLFW_KEY_W)) {
				setDirection(DIRECTION_UP);
				movement.add(0, movementSpeed*delta);
			}
			
			move(movement);

			if(world.getTile(getXOnMap(), getYOnMap()) != null && inWater()) {
				if(direction == DIRECTION_RIGHT) {
					useAnimation(ANIMATION_SWIM_RIGHT);
				} else if(direction == DIRECTION_LEFT) {
					useAnimation(ANIMATION_SWIM_LEFT);
				} else if(direction == DIRECTION_UP) {
					useAnimation(ANIMATION_SWIM_UP);
				} else {
					useAnimation(ANIMATION_SWIM_DOWN);
				}
			} else if(movement.x != 0 || movement.y != 0) {
				if(direction == DIRECTION_RIGHT) {
					useAnimation(ANIMATION_WALK_RIGHT);
				}
				else if(direction == DIRECTION_LEFT) {
					useAnimation(ANIMATION_WALK_LEFT);
				}
				else if(direction == DIRECTION_UP) {
					useAnimation(ANIMATION_WALK_UP);
				} else {
					useAnimation(ANIMATION_WALK_DOWN);
				}
			} else {
				if(direction == DIRECTION_RIGHT) {
					useAnimation(ANIMATION_IDLE_RIGHT);
				} else if(direction == DIRECTION_LEFT) {
					useAnimation(ANIMATION_IDLE_LEFT);
				} else if(direction == DIRECTION_UP) {
					useAnimation(ANIMATION_IDLE_UP);
				} else {
					useAnimation(ANIMATION_IDLE_DOWN);
				}
			}
		}
		
		if(!inWater()) {
			playerclass.update(delta, win, camera, world);
		} else if(noclip) {
			playerclass.update(delta, win, camera, world);
		}
		for(int i = 0; i < statusEffects.size(); i++) {
			statusEffects.get(i).update(delta, win, camera, world);
		}
		
//		camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.08f);
		moveCamera(camera, world, 0.08f);
	}
	public void moveCamera(Camera camera, World world, float delay) {
		camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), delay);
	}
	
}
