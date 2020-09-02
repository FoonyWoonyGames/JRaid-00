package effect;

import entity.Entity;
import event.Event;
import event.EventEntityDamaged;
import raidgame.Game;
import render.Camera;
import render.Shader;
import util.Window;
import world.World;

public abstract class StatusEffect {
	
	protected String id;
	protected String name;
	protected String description;
	protected long birth;
	protected long duration;
	protected int speed;
	protected float power;
	protected Entity sender;
	protected Entity host;
	
	private boolean ended;

	public StatusEffect(Entity sender, Entity host, float power, int speed, long duration) {
		
		id = "effect";
		name = "Status Effect 0";
		description = "No description available.";
		birth = System.currentTimeMillis();
		ended = false;
		this.duration = duration;
		this.speed = speed;
		this.power = power;
		this.sender = sender;
		this.host = host;
		

		ended = false;
	}
	
	public void checkDuration() {
		if(duration != 0 && System.currentTimeMillis() - birth > duration) {
			stop();
		}
	}
	
	public void stop() {
		host.removeStatusEffect(this.id);
		onEnd();
	}
	
	public void render(Shader shader, Camera camera, World world) {
		
	}
	
	public abstract void update(float delta, Window win, Camera camera, World world);
	
	public abstract void onEnd();
	
	public String getName() {
		return name;
	}
	
	public String getID() {
		return id;
	}
	
	
	public void eventHostDamaged(EventEntityDamaged e) {
		
	}

	public void eventHostPushed(Event e) {
		
	}

	public void eventHostDied(Event e) {
		
	}
}
