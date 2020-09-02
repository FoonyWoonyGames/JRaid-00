package entity;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import raidgame.Game;
import render.Animation;
import render.Camera;
import render.Shader;
import util.Window;
import world.World;

public class Particle extends Entity {
	
	public static final int ANIMATION_IDLE = 0;

	
	private static boolean hasLoaded = false;
	public static BufferedImage PARTICLE_POISON;
	public static BufferedImage PARTICLE_FIRE;
	public static BufferedImage PARTICLE_BLOOD;
	public static BufferedImage PARTICLE_UNDEATH;
	public static BufferedImage PARTICLE_FROST;
	public static BufferedImage PARTICLE_FROSTSTORM;
	public static BufferedImage PARTICLE_RAIN;
	public static BufferedImage PARTICLE_MANANOVA;
	public static BufferedImage PARTICLE_SHIELD;
	public static BufferedImage PARTICLE_MAGIC;
	public static BufferedImage PARTICLE_CRIT;
	public static BufferedImage PARTICLE_PLAGUE;
	public static BufferedImage PARTICLE_BINDINGOFDEATH;
	public static BufferedImage PARTICLE_DEATHBOUND;
	public static BufferedImage PARTICLE_SMOKE;
	public static BufferedImage PARTICLE_AIM;
	public static BufferedImage PARTICLE_CROSSHAIR;
	
	
	private BufferedImage particleTexture;
	private float particleSpeed;
	private float particleDirection;
	private boolean looping;
	private Entity entityToFollow;
	private float offsetX;
	private float offsetY;

	public Particle(BufferedImage texture, int frames, int fps, int w, int h, boolean looping, Transform transform) {
		super(1, transform);

		transform.scale.x = 1;
		transform.scale.y = 1;
		
		type = TYPE_ABILITY;
		this.particleTexture = texture;

		transform.scale.x = w/16;
		transform.scale.y = h/16;

		
		setAnimation(ANIMATION_IDLE, new Animation(frames, fps, w, h, this.particleTexture).setLooping(looping));

//		boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x/2,transform.scale.y/1.2f));
		
		this.looping = looping;
	}
	
	public static void LoadImages() {
		if(!hasLoaded) {
			try {
				PARTICLE_POISON = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/poisonArrow.png"));
				PARTICLE_FIRE = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/fireballTail.png"));
				PARTICLE_BLOOD = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/blood.png"));
				PARTICLE_UNDEATH = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/undeath.png"));
				PARTICLE_FROST = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/frost.png"));
				PARTICLE_FROSTSTORM = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/frostStorm.png"));
				PARTICLE_RAIN = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/rain.png"));
				PARTICLE_MANANOVA = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/manaNova.png"));
				PARTICLE_SHIELD = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/shield.png"));
				PARTICLE_MAGIC = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/magic.png"));
				PARTICLE_CRIT = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/crit.png"));
				PARTICLE_PLAGUE = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/plague.png"));
				PARTICLE_BINDINGOFDEATH = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/bindingofdeath.png"));
				PARTICLE_DEATHBOUND = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/deathbound.png"));
				PARTICLE_SMOKE = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/death.png"));
				PARTICLE_AIM = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/aimDown.png"));
				PARTICLE_CROSSHAIR = ImageIO.read(Particle.class.getResourceAsStream("/textures/entities/particles/crosshair.png"));
				hasLoaded = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void render(Shader shader, Camera camera, World world) {
		
		super.render(shader, camera, world);
	}

	@Override
	public void update(float delta, Window win, Camera camera, World world) {
		if(!looping) {
			if(hasAnimationPlayed(ANIMATION_IDLE)) {
				kill();
			}
		}
		if(this.particleTexture == Particle.PARTICLE_CROSSHAIR) {
			if(entityToFollow.getDirection() == Entity.DIRECTION_DOWN) {
				calculateCrosshair(-2.5f, 2.5f, 1.5f, 4.5f, false, 3.75, 0.75f);
			}
		}
		if(entityToFollow == null) {
			Vector2f movement = new Vector2f();
			
			float dir = getParticleDirection();
			float max = getParticleSpeed();
			float min = max*0.8f;
			float rest = max - min;
			
			if(particleDirection <= 45) {
				movement.add(new Vector2f((0+((min/45)*dir))*delta, -(max-((rest/45)*dir))*delta));
			} else if(particleDirection > 45 && particleDirection <= 90) {
				movement.add(new Vector2f((min+(rest/45)*(dir-45))*delta, -(min-(min/45)*(dir-45))*delta));
			} else if(particleDirection > 90 && particleDirection <= 135) {
				movement.add(new Vector2f((max-(rest/45)*(dir-90))*delta, (0+(min/45)*(dir-90))*delta));
			} else if(particleDirection > 135 && particleDirection <= 180) {
				movement.add(new Vector2f((min-(min/45)*(dir-135))*delta, (min+(rest/45)*(dir-135))*delta));
			} else if(particleDirection > 180 && particleDirection <= 225) {
				movement.add(new Vector2f((min-(min/45)*(dir-135))*delta, (max-(rest/45)*(dir-180))*delta));
			} else if(particleDirection > 225 && particleDirection <= 270) {
				movement.add(new Vector2f(-(min+(rest/45)*(dir-225))*delta, (min-(min/45)*(dir-225))*delta));
			} else if(particleDirection > 270 && particleDirection <= 315) {
				movement.add(new Vector2f(-(max-(rest/45)*(dir-270))*delta, -(0+(min/45)*(dir-270))*delta));
			} else if(particleDirection > 315 && particleDirection <= 360) {
				movement.add(new Vector2f(-(min-(min/45)*(dir-315))*delta, -(min+(rest/45)*(dir-315))*delta));
			}
			
			if(particleSpeed != 0) {
				move(movement);
			}
			
			for(int i = 0; i < world.getEntityList().size(); i++) {
				Entity e = world.getEntityList().get(i);
				if(getX() > e.getX() - 1 && getX() < e.getX() + 1) {
					if(getY() > e.getY() - 1 && getY() < e.getY() + 1) {
						e.collidingWithParticle(this);
					}
				}
			}
		} else {
			setPosition(entityToFollow.getX()*2-1+offsetX, entityToFollow.getY()*2+1-offsetY);
		}
	}
	
	public void setMovement(float speed, float direction) {
		this.particleSpeed = speed;
		this.particleDirection = direction;
		if(direction < 0) {
			this.particleDirection = -direction;
		}
		while(this.particleDirection > 360) {
			this.particleDirection = this.particleDirection - 360;
		}
	}
	
	public void setEntity(Entity e) {
		entityToFollow = e;
	}
	
	public float getParticleSpeed() {
		return particleSpeed;
	}
	
	public float getParticleDirection() {
		return particleDirection;
	}
	
	public BufferedImage getParticleTextureID() {
		return particleTexture;
	}
	
	public void setOffset(float x, float y) {
		offsetX = x;
		offsetY = y;
	}
	
	@Override
	public void kill() {
		getWorld().killParticle(this);
	}
	
	private void calculateCrosshair(float minX, float maxX, float minY, float maxY, boolean onSide, double curve, float ratioRestriction) {
		offsetX = (float) (getWindow().getInput().getMouseX() - getWindow().getWidth()/2)/16;
		offsetY = (float) (getWindow().getInput().getMouseY() - getWindow().getHeight()/2)/16;
		
		if(offsetX > maxX) {
			offsetX = maxX;
			glfwSetCursorPos(getWindow().getWindow(), maxX*16 + getWindow().getWidth()/2, getWindow().getInput().getMouseY());
		}
		if(offsetX < minX) {
			offsetX = minX;
			glfwSetCursorPos(getWindow().getWindow(), minX*16 + getWindow().getWidth()/2, getWindow().getInput().getMouseY());
		}
		if(offsetY < minY) {
			offsetY = minY;
			glfwSetCursorPos(getWindow().getWindow(), getWindow().getInput().getMouseX(), minY*16 + getWindow().getHeight()/2);
		}
		if(offsetY > maxY) {
			offsetY = maxY;
			glfwSetCursorPos(getWindow().getWindow(), getWindow().getInput().getMouseX(), maxY*16 + getWindow().getHeight()/2);
		}
		float offset;
		if(onSide) offset = offsetX;
		else offset = offsetY;
		if(offset <= curve) {
			double ratio = offsetX/offsetY;
			if(ratio < 0) ratio = ratio * -1;
			if(ratio > ratioRestriction) {
				if(offsetX < 0) offsetX = offsetY * -ratioRestriction;
				else offsetX = offsetY * ratioRestriction;
			}
		}
	}
}
