package entity;

import static org.lwjgl.opengl.GL11.glColor3f;

import java.awt.Color;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import ability.Ability;
import assets.Assets;
import collision.AABB;
import collision.Collision;
import effect.StatusEffect;
import event.Event;
import event.EventEntityDamaged;
import raidgame.Game;
import render.Animation;
import render.Camera;
import render.Shader;
import util.Window;
import world.Tile;
import world.World;

public abstract class Entity {
	protected AABB boundingBox;
//	private Texture texture;
	protected Animation[] animations;
	private int useAnimation;
	
	protected Transform transform;
	
	protected Window window;
	
	public static final int DIRECTION_DOWN = 0;
	public static final int DIRECTION_UP = 2;
	public static final int DIRECTION_RIGHT = 1;
	public static final int DIRECTION_LEFT = 3;
	
	public static final int TYPE_PLAYER = 0;
	public static final int TYPE_ENEMY = 1;
	public static final int TYPE_ABILITY = 2;
	
	protected String name;
	public int type;
	protected ArrayList<StatusEffect> statusEffects;
	
	protected boolean collidingWithTiles = false;
	protected World world;
	protected float deltaTime;
	
	protected int direction;
	protected boolean noclip;
	protected int speed;
	protected int speedMax;
	protected int swimSpeed;
	protected int swimSpeedMax;
	protected float health;
	protected float healthMax;
	protected int defense;
	protected int defenseMax;
	protected float attack;
	
	protected boolean wasLastCrit;
	protected float lastVariation;
	public Ability abilityUsing;

	private boolean flinching;
	private long flinchTimer;
	private boolean behind;

	public Entity(int maxAnimations, Transform transform) {
		this.animations = new Animation[maxAnimations];
		
		this.window = Game.raid.getWindow();

		transform.scale.x = 2;
		transform.scale.y = 2;
		this.transform = transform;
		name = "0";
		
		direction = 0;
		this.useAnimation = 0;
		
		boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x,transform.scale.y));
		
		statusEffects = new ArrayList<StatusEffect>();
		
		health = healthMax = 50;
		defense = defenseMax = 50;
		attack = 1;
		speed = speedMax = 10;
		swimSpeed = swimSpeedMax = 10;
		
		noclip = false;
		
	}
	
	protected void setAnimation(int index, Animation animation) {
		animations[index] = animation;
	}
	
	public void useAnimation(int index) {
		try {
			if(animations[index] != null) {
				useAnimation = index;
			}
			else {
				Game.ConsoleSend("ERROR -- Animation does not at exist (Name:" + name + " | Animation: " + index + ")");
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			Game.ConsoleSend("ERROR -- Animation does not at exist (Name:" + name + " | Animation: " + index + ")");
		}
	}
	
	public void move(Vector2f direction) {
		transform.pos.add(new Vector3f(direction, 0));
	}
	
	public void collideWithTiles(World world) {
		boundingBox.getCenter().set(transform.pos.x, transform.pos.y);
		
		AABB[] boxes = new AABB[25];
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				boxes[i + j * 5] = world.getTileBoundingBox(
						(int) (((transform.pos.x / 2) + 0.5f - (5/2)) + i),
						(int) (((-transform.pos.y / 2) + 0.5f - (5/2)) + j)
						);
			}
		}
		
		AABB box = null;
		for(int i = 0; i < boxes.length; i++) {
			if(boxes[i] != null) {
				if(box == null) {
					box
					= boxes[i];
				}
				
				Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
				Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
				
				if(length1.lengthSquared() > length2.lengthSquared()) {
					box = boxes[i];
				}
			}
		}
		
		if(box != null) {
			Collision data = boundingBox.getCollision(box);
			if(data.isIntersecting && !noclip) {
				collidingWithTiles = true;
				boundingBox.correctPosition(box, data);
				transform.pos.set(boundingBox.getCenter(), 0);
			} else { collidingWithTiles = false; }

			for(int i = 0; i < boxes.length; i++) {
				if(boxes[i] != null) {
					if(box == null) {
						box = boxes[i];
					}
					
					Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
					Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
					
					if(length1.lengthSquared() > length2.lengthSquared()) {
						box = boxes[i];
					}
				}
			}
			
			data = boundingBox.getCollision(box);
			if(data.isIntersecting && !noclip) {
				boundingBox.correctPosition(box, data);
				transform.pos.set(boundingBox.getCenter(), 0);
			}
		}
	}
	
	public abstract void update(float delta, Window win, Camera camera, World world);
	
	public void render(Shader shader, Camera camera, World world) {
		
		if(this.world != world) {
			this.world = world;
		}
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 50 % 2 == 0) {
				return;
			}
			if(elapsed > 100) {
				flinching = false;
			}
		}
		
		Matrix4f target = camera.getProjection();
		target.mul(world.getWorldMatrix());
		
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", transform.getProjection(target));
		animations[useAnimation].bind(0);
		Assets.getModel().render();
		
	}
	
	public float getX() {
		return transform.pos.x/2 +0.5f;
	}
	public float getY() {
		return transform.pos.y/2 - 0.75f;
	}
	
	public int getXOnMap() {
		return (int) getX();
	}
	
	public int getYOnMap() {
		return (int) -getY();
	}
	

	public boolean isColliding(Entity entity) {
		boolean colliding = false;
		Collision collision = boundingBox.getCollision(entity.boundingBox);
		
		if(collision.isIntersecting) {
			colliding = true;
		}
		return colliding;
	}
	
	public boolean isColliding() {
		return collidingWithTiles;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int d) {
		direction = d;
	}
	
	public String getName() {
		return name;
	}
	
	public void setNoclip(boolean b) {
		noclip = b;
	}
	
	public boolean hasNoclip() {
		return noclip;
	}
	
	public void setPosition(float x, float y) {
		transform.pos.x = x;
		transform.pos.y = y;
	}
	
	public void addStatusEffect(StatusEffect effect) {
		statusEffects.add(effect);
	}
	
	public void removeStatusEffect(String id) {
		if(hasStatusEffect(id)) {
			StatusEffect effect = null;
			for(int i = 0; i < statusEffects.size(); i++) {
				if(statusEffects.get(i).getID().equals(id)) {
					effect = statusEffects.get(i);
				}
			}
			effect.onEnd();
			statusEffects.remove(effect);
		} else {
			Game.ConsoleSend("(ERROR) - Entity does not have status effect: " + id);
		}
	}
	
	public boolean hasStatusEffect(String id) {
		boolean hasEffect = false;
		for(int i = 0; i < statusEffects.size(); i++) {
			if(statusEffects.get(i).getID().equals(id)) {
				hasEffect = true;
				break;
			}
		}
		return hasEffect;
	}
	
	public void setHealth(float hp) {
		health = hp;
	}
	
	public float getHealth() {
		return health;
	}
	
	public float getMaxHealth() {
		return healthMax;
	}
	
	public void hurt(Entity attacker, float hp, double crit) {
		
		flinching = true;
		flinchTimer = System.nanoTime();
		
		
		double critChance = 7;
		
		if(crit >= 1 - (critChance/100)) {
			hp = hp * 1.8f;
			wasLastCrit = true;
		} else {
			wasLastCrit = false;
		}
		
		float damage = (hp * (1 - (getDefense() * 0.5f) / getMaxHealth()));
		
		float variationFloat = Float.parseFloat(Math.random() + "");
		float variation = variationFloat;
		
		if(Math.random() > 0.5) {
			variation = -variationFloat;
		}
		lastVariation = variation;
		
		float healthToLose = (damage + variation)*attacker.getAttack();
		
		EventEntityDamaged e = new EventEntityDamaged(healthToLose, this, attacker, wasLastCrit, healthToLose >= getHealth());
		for(int i = 0; i < statusEffects.size(); i++) {
			statusEffects.get(i).eventHostDamaged(e);
		}
		if(!e.isCancelled()) {
			if(healthToLose > 0) {
				setHealth(getHealth() - healthToLose);
			}
			if(e.isOverkill()) {
				for(int i = 0; i < statusEffects.size(); i++) {
					statusEffects.get(i).eventHostDied(e);
				}
			}
			if(type == TYPE_ENEMY) {
				reportDamage(e);
			}
		}
	}
	public void reportDamage(EventEntityDamaged e) {

//		float damage = hp * (1 - (getDefense() * 0.5f) / getMaxHealth());
		
		String report = e.getAttacker().getName() + " hit " + name + " for " + e.getDamageDealt() + " damage-points against a " + defense + " defense. Health is now " + getHealth();
		if(e.isOverkill()) {
			report = report + " (" + -getHealth() + " Overkill)";
			if(type == TYPE_ENEMY) {
				getWorld().spawnParticle(Particle.PARTICLE_SMOKE, 8, 32, 32, 32, this, false, 0, 0, false);
			}
		}
		if(e.isCriticalHit()) {
			report = report + " - A critical hit!";
			e.getVictim().getWorld().spawnParticle(Particle.PARTICLE_CRIT, 6, 12, 16, 16, e.getVictim().getX(), e.getVictim().getY(), false, 1, 180, 1, 0, false);
		}
		
		Game.ConsoleSend(report);
	}
	
	public boolean isDead() {
		return health <= 0;
	}
	
	public void setDefense(int d) {
		defense = d;
	}
	
	public int getDefense() {
		return defense;
	}
	
	public int getMaxDefense() {
		return defenseMax;
	}
	
	public void setAttack(float a) {
		attack = a;
	}
	
	public float getAttack() {
		return attack;
	}
	
	public void setSpeed(int s) {
		speed = s;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getMaxSpeed() {
		return speedMax;
	}
	
	public void setSwimSpeed(int s) {
		swimSpeed = s;
	}
	
	public int getSwimSpeed() {
		return swimSpeed;
	}
	
	public int getMaxSwimSpeed() {
		return swimSpeedMax;
	}
	
	public void push(float delta, int direction, int force) {

		
		Event e = new Event();
		for(int i = 0; i < statusEffects.size(); i++) {
			statusEffects.get(i).eventHostPushed(e);
		}
		if(!e.isCancelled()) {

			Vector2f movement = new Vector2f();
			
			if(direction == DIRECTION_LEFT) {
				movement.add(-force*delta, 0);
			}
			else if(direction == DIRECTION_RIGHT) {
				movement.add(force*delta, 0);
			}
			else if(direction == DIRECTION_UP) {
				movement.add(0, force*delta);
			}
			else {
				movement.add(0, -force*delta);
			}

			move(movement);
		}
	}

	public boolean hasAnimationPlayed(int i) {
		return animations[i].hasPlayedOnce();
	}
	public void resetAnimation(int i) {
		animations[i].reset();
	}
	public boolean isUsingAnimation(int i) {
		return useAnimation == i;
	}
	public boolean isAnimationAtFrame(int i) {
		return animations[useAnimation].getFrame() == i;
	}
	
	public void onDeath(World w) {
		
	}
	
	public boolean isWithinMap(World w) {
		return getX() > 0 && getY() < 0 && getY() > -w.getHeight() && getX() < w.getWidth();
	}
	public boolean isWithinBorder(World w) {
		return getX() > 5 && getY() < -5 && getY() > -w.getHeight()+5 && getX() < w.getWidth()-5;
	}
	
	public void randomizePosition(World w) {
		float x = (float) (Math.random()*(w.getWidth()*2));
		float y = (float) -(Math.random()*(w.getHeight()*2));
		
		setPosition(x, y);
		
		if(!isWithinBorder(w) || !isWithinMap(w)) {
			Game.ConsoleSend("[ERROR] -- Entity (" + name + ") spawned out of borders. Retrying.");
			randomizePosition(w);
		}
		
		if(world != null && world.getTile(getXOnMap(), getYOnMap()).isSolid()) {
			Game.ConsoleSend("[ERROR] -- Entity (" + name + ") spawned on an invalid position (" + getXOnMap() + ", " + getYOnMap() + "). Retrying.");
			randomizePosition(w);
		}
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public boolean inWater() {
		boolean inWater = false;
		if (getWorld() != null && getWorld().getTile(this.getXOnMap(), this.getYOnMap()) != null) {
			inWater = getWorld().getTile(this.getXOnMap(), this.getYOnMap()) == Tile.tileWater;
		}
		return inWater;
	}
	
	public boolean isCloser(Entity target, Entity opponent) {
		boolean result = false;
		float distanceX;
		float distanceY;
		
		distanceX = getX() - target.getX();
		distanceY = getY() - target.getY();
		
		if(distanceX < 0) distanceX = -distanceX;
		if(distanceY < 0) distanceY = -distanceY;
		
		float distanceXOpp;
		float distanceYOpp;
		
		distanceXOpp = opponent.getX() - target.getX();
		distanceYOpp = opponent.getY() - target.getY();
		
		if(distanceXOpp < 0) distanceXOpp = -distanceXOpp;
		if(distanceYOpp < 0) distanceYOpp = -distanceYOpp;
		
		float distance = distanceX + distanceY;
		float distanceOpp = distanceXOpp + distanceYOpp;
		
		if(distance < distanceOpp) result = true;
		
		return result;
	}
	
	public void kill() {
		getWorld().removeEntity(this);
	}
	
	public ArrayList<Entity> getEntitiesInFront(float front, float side, float back) {
		ArrayList<Entity> entities = new ArrayList<Entity>();
		
		if(front == 0) front = 2.5f;
		if(side == 0) side = 1.5f;
		if(back ==0) back = 0.1f;

		float xLeft = 0;
		float yDown = 0;
		float xRight = 0;
		float yUp = 0;
		if(getDirection() == Entity.DIRECTION_DOWN) {
			xLeft = getX() - side;
			yDown = getY() - front;
			xRight = getX() + side;
			yUp = getY() + back;
		} else if(getDirection() == Entity.DIRECTION_UP) {
			xLeft = getX() - side;
			yDown = getY() - back;
			xRight = getX() + side;
			yUp = getY() + front;
		} else if(getDirection() == Entity.DIRECTION_RIGHT) {
			xLeft = getX() - back;
			yDown = getY() - side;
			xRight = getX() + front;
			yUp = getY() + side;
		} else if(getDirection() == Entity.DIRECTION_LEFT) {
			xLeft = getX() - front;
			yDown = getY() - side;
			xRight = getX() + back;
			yUp = getY() + side;
		}
		
		for(int i = 0; i < world.getEntityList().size(); i++) {
			Entity e = world.getEntityList().get(i);
			if(e.getX() > xLeft && e.getX() < xRight && e.getY() > yDown && e.getY() < yUp) {
				entities.add(e);
			}
		}
		
		return entities;
	}
	
	public void collidingWithParticle(Particle p) {
		
	}
	
	public boolean isBehind()  {
		return behind;
	}
	
	public void setBehind(boolean b) {
		behind = b;
	}
	
	public int getEnemy() {
		int enemy;
		if(type == TYPE_PLAYER) {
			enemy = TYPE_ENEMY;
		} else if(type == TYPE_ENEMY) {
			enemy = TYPE_PLAYER;
		} else {
			enemy = TYPE_ENEMY;
		}
		return enemy;
	}
	
	protected Window getWindow() {
		return this.window;
	}
}
