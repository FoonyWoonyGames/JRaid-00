package ability;

import entity.Entity;
import entity.Player;
import render.Camera;
import util.Window;
import world.World;

public abstract class Ability {

	protected long cooldown;
	protected long lastUse;
	protected String name;
	protected String description;
	protected Entity user;

	protected int animationToUse = 0;
	
	public Ability(Entity user) {
		this.user = user;
		name = "Ability 0";
		description = "No description available.";
		cooldown = 0;
		lastUse = 0;
	}
	
	public abstract void update(float delta, Window win, Camera camera, World world);
	
	public abstract void use(float delta, Window win, Camera camera, World world);
	
	public String getName() {
		return name;
	}
	
	public void setLastUse() {
		lastUse = System.currentTimeMillis();
	}
	
	public long getLastUse() {
		return lastUse;
	}
	
	public long getCooldown() {
		return cooldown;
	}
	
	public boolean isCooled() {
		return (System.currentTimeMillis() - getLastUse() >= cooldown);
	}
}
