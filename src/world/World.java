package world;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import collision.AABB;
import entity.Enemy;
import entity.Entity;
import entity.Particle;
import entity.Skeleton;
import entity.Transform;
import entity.Zombie;
import raidgame.Game;
import render.Camera;
import render.Shader;
import util.Window;

public class World {
	
	public static final int TYPE_GRASS = 0;
	public static final int TYPE_SNOW = 1;
	public static final int TYPE_EMPTY = 2;
	
	private int worldType;
	private long seed;
	private int timesSeedUsed;
	
	private int viewX;
	private int viewY;
	private byte[] tiles;
	private AABB[] boundingBoxes;
	private List<Entity> entities;
	private List<Particle> particles;
	private int width;
	private int height;
	private int scale;
	
	private Matrix4f world;
	
	public World(long seed) {
		width = 128;
		height = 64;
		scale = 16;
		this.seed = seed;
		
		tiles = new byte[width * height];
		boundingBoxes = new AABB[width * height];
		entities = new ArrayList<Entity>();
		particles = new ArrayList<Particle>();

		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				setTile(j, i, Tile.tileTest);
			}
		}
		setWorldType(TYPE_GRASS);
		
//		generateEnvironment();
//		Transform playerTransform = new Transform();
		
		
		world = new Matrix4f().setTranslation(new Vector3f(0));
		world.scale(scale);
	}
	
	public void calculateView(Window window) {
		viewX = (window.getWidth() / (scale * 2)) + 4;
		viewY = (window.getHeight() / (scale * 2)) + 4;
	}
	public void calculateView(int w, int h) {
		viewX = (w / (scale * 2)) + 4;
		viewY = (h / (scale * 2)) + 4;
	}
	
	public Matrix4f getWorldMatrix() {
		return world;
	}
	
	public void render(TileRenderer tr, Shader shader, Camera cam) {
		
		int posX = (int) cam.getPosition().x / (scale * 2);
		int posY = (int) cam.getPosition().y / (scale * 2);
		
		for(int i = 0; i < viewX; i++) {
			for(int j = 0; j < viewY; j++) {
				Tile t = getTile(i - posX - (viewX/2) + 1, j + posY - (viewY/2));
				if(t != null) {
					tr.renderTile(t, i - posX - (viewX/2) + 1, -j - posY + (viewY/2), shader, world, cam);
				}
			}
		}
		

		for(Entity entity : entities) {
			if(entity.isBehind()) {
				entity.render(shader, cam, this);
			}
		}

		ArrayList<Entity> effects = new ArrayList<Entity>();
		for(Entity entity : entities) {
			if(!entity.isBehind()) {
				if(entity.type != Entity.TYPE_ABILITY) {
					entity.render(shader, cam, this);
				} else { effects.add(entity); }
			}
		}
		for(Entity effect : effects) {
			effect.render(shader, cam, this);
		}
		
		for(Particle particle : particles) {
			particle.render(shader, cam, this);
		}
	}
	
	public void update(float delta, Window window, Camera camera) {
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).update(delta, window, camera, this);
		}
		
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).update(delta, window, camera, this);
		}
		
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).collideWithTiles(this);
			if(entities.get(i).isDead()) {
				entities.get(i).onDeath(this);
				removeEntity(entities.get(i));
			}
		}
	}
	
	public void correctCamera(Camera camera, Window window) {
		Vector3f pos = camera.getPosition();
		
		int w = -width * scale * 2;
		int h = height * scale * 2;
		
		if(pos.x > -(window.getWidth() / 2) + scale) {
			pos.x = -(window.getWidth() / 2) + scale;
		}
		if(pos.x < w + (window.getWidth() / 2) + scale) {
			pos.x = w + (window.getWidth() / 2) + scale;
		}
		if(pos.y > h + -(window.getHeight() / 2) - scale) {
			pos.y = h + -(window.getHeight() / 2) - scale;
		}
		if(pos.y < (window.getHeight() / 2) - scale) {
			pos.y = (window.getHeight() / 2) - scale;
		}
	}
	
	public void setTile(int x, int y, Tile t) {
		try {
			if(t.getID() < Tile.not) {
				tiles[x + y * width] = t.getID();
			} else {
				tiles[x + y * width] = 0;
				Game.ConsoleSend("(ERROR) - Tile at [" + x + "," + y + "] is invalid. Tile set to 0.");
			}
			if(t.isSolid()) {
				boundingBoxes[x + y * width] = new AABB(new Vector2f(x * 2, -y * 2), new Vector2f(1,1));
			} else {
				boundingBoxes[x + y * width] = null;
			}
		} catch (Exception e) {
			Game.ConsoleSend("(ERROR) - [" + x + "," + y + "] is an invalid position.");
		}
	}
	
	public Tile getTile(int x, int y) {
		try {
			return Tile.tiles[tiles[x + y * width]];
		} catch(ArrayIndexOutOfBoundsException e) {
			return Tile.tiles[tiles[0 + 0 * width]];
		}
	}
	public AABB getTileBoundingBox(int x, int y) {
		try {
			return boundingBoxes[x + y * width];
		} catch(ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getScale() {
		return scale;
	}
	public void spawnEntity(Entity e) {
		e.randomizePosition(this);
		e.setHealth(e.getMaxHealth());
		addEntity(e);
		for(int j = 0; j < 10; j++) {
			float x = (float) (1*new Random(getSeed(true)/timesSeedUsed).nextDouble());
			float y = (float) (1*new Random(getSeed(true)/timesSeedUsed).nextDouble());
			spawnParticle(Particle.PARTICLE_UNDEATH, 3, 6, 16, 16, e.getX()-0.5f+x, e.getY()-0.5f+y, false, 1, 180, 2, 40, false);
		}
	}
	public void addEntity(Entity e) {
		entities.add(e);
	}
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
	public boolean containsEntity(Entity e) {
		return entities.contains(e);
	}
	public List<Entity> getEntityList() {
		return entities;
	}
	public void killEntities() {
		ArrayList<Entity> entitiesKilled = new ArrayList<Entity>();
		for(int i = 0; i < entities.size(); i++) {
			Entity victim = entities.get(i);
			if(victim.type != Entity.TYPE_PLAYER) {
				entitiesKilled.add(victim);
				victim.kill();
				i--;
			}
		}
		Game.ConsoleSend("[DEV] -- Killed " + entitiesKilled.size() + " entities!");
	}
	public void spawnParticle(BufferedImage texture, int frames, int fps, int w, int h, float x, float y, boolean loop, float speed, float direction, int amount, float directionVariation, boolean canCollide) {
		for(int i = 0; i < amount; i++) {
			Particle p = new Particle(texture, frames, fps, w, h, loop, new Transform());
			p.setPosition(x*2-1, y*2+0.5f);
			p.setNoclip(true);
			float dir;
			if(new Random(getSeed(true)/timesSeedUsed).nextDouble()*100 > 50) {
				dir = (float) (direction+(directionVariation*new Random(getSeed(true)/timesSeedUsed).nextDouble()));
			} else {
				dir = (float) (direction-(directionVariation*new Random(getSeed(true)/timesSeedUsed).nextDouble()));
			}
			if(speed != 0) {
				p.setMovement((float) (speed+(2.5*new Random(getSeed(true)/timesSeedUsed).nextDouble())), dir);
			}
			particles.add(p);
		}
	}
	public void spawnParticle(BufferedImage texture, int frames, int fps, int w, int h, Entity e, boolean loop, float offsetX, float offsetY, boolean canCollide) {
		Particle p = new Particle(texture, frames, fps, w, h, loop, new Transform());
		p.setEntity(e);
		p.setNoclip(true);
		p.setOffset(offsetX, offsetY);
		particles.add(p);
	}
	public void killParticle(Particle p) {
		particles.remove(p);
	}
	public void killParticles(float x, float y, float radius, BufferedImage particleType) {
		for(int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			if(p.getParticleTextureID() == particleType && p.getX() > x - radius && p.getX() < x + radius && p.getY() > y - radius && p.getY() < y + radius) {
				killParticle(p);
			}
		}
	}
	
	public void generateEnvironment() {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(i != 0 && j != 0 && j != getWidth()-1 && i != getHeight()-1) {
					setTile(j, i, Tile.tileGrass);
					
				} else {
					setTile(j, i, Tile.tileOcean);
				}
				
			}
		}
		for(int i = 0; i < 3; i++) {
			generateDirt((int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(getWidth())), (int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(getHeight())), (int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(20)+10));
		}
		for(int i = 0; i < 8; i++) {
			generateLake((int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(getWidth()-20)+10), (int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(getHeight()-20)+10), (int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(90)+60));
		}
		for(int i = 0; i < 15; i++) {
			generateBush((int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(getWidth()-20)+10), (int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(getHeight()-20)+10), (int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(1)+1));
		}
		for(int i = 0; i < 6; i++) {
			generatePath((int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(getWidth())), (int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(getHeight())), (int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(20)+30));
		}
		for(int i = 0; i < 8; i++) {
			generateRocks((int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(getWidth()-20)+10), (int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(getHeight()-20)+10), (int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*(8)+2));
		}
		if(worldType == TYPE_SNOW) setSnowy();
		try {
			loadWorld("border");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generateLake(int x, int y, int size) {
		Point tile = new Point(x, y);
		for(int i = 0; i < size; i++) {
			Point[] changingTile = {
					new Point(tile.x+1, tile.y),
					new Point(tile.x-1, tile.y),
					new Point(tile.x, tile.y+1),
					new Point(tile.x, tile.y-1)
			};
			for(int j = 0; j < changingTile.length; j++) {
				if(getTile(changingTile[j].x, changingTile[j].y) != Tile.tileWater && !getTile(changingTile[j].x, changingTile[j].y).isSolid()) {
					setTile(changingTile[j].x, changingTile[j].y, Tile.tileWater);
				}
			}
			
			
			if(new Random(getSeed(true)/timesSeedUsed).nextDouble() < 0.25) {
				tile.translate(1, 0);
			} else if(new Random(getSeed(false)/timesSeedUsed).nextDouble() > 0.75) {
				tile.translate(-1, 0);
			} else if(new Random(getSeed(false)/timesSeedUsed).nextDouble() > 0.5) {
				tile.translate(0, 1);
			} else if(new Random(getSeed(false)/timesSeedUsed).nextDouble() > 0.25) {
				tile.translate(0, -1);
			}
		}
	}
	public void generateBush(int x, int y, int size) {
		Point tile = new Point(x, y);
		for(int i = 0; i < size; i++) {
			Point[] changingTile = {
					new Point(tile.x, tile.y),
					new Point(tile.x+1, tile.y),
					new Point(tile.x-1, tile.y),
					new Point(tile.x, tile.y+1),
					new Point(tile.x, tile.y-1)
			};
			for(int j = 0; j < changingTile.length; j++) {
				if(getTile(changingTile[j].x, changingTile[j].y) != Tile.tileWater && !getTile(changingTile[j].x, changingTile[j].y).isSolid()) {
					if(new Random(getSeed(true)/timesSeedUsed).nextDouble() > 0.8) {
						setTile(changingTile[j].x, changingTile[j].y, Tile.tileLeaves);
					}
				}
			}
			
			
			if(new Random(getSeed(true)/timesSeedUsed).nextDouble() < 0.25) {
				tile.translate(1, 0);
			} else if(new Random(getSeed(false)/timesSeedUsed).nextDouble() > 0.75) {
				tile.translate(-1, 0);
			} else if(new Random(getSeed(false)/timesSeedUsed).nextDouble() > 0.5) {
				tile.translate(0, 1);
			} else if(new Random(getSeed(false)/timesSeedUsed).nextDouble() > 0.25) {
				tile.translate(0, -1);
			}
		}
	}
	public void generateDirt(int x, int y, int size) {
		Point tile = new Point(x, y);
		for(int i = 0; i < size; i++) {
			Point[] changingTile = {
					new Point(tile.x+1, tile.y),
					new Point(tile.x-1, tile.y),
					new Point(tile.x, tile.y+1),
					new Point(tile.x, tile.y-1)
			};
			for(int j = 0; j < changingTile.length; j++) {
				if(getTile(changingTile[j].x, changingTile[j].y) == Tile.tileGrass && !getTile(changingTile[j].x, changingTile[j].y).isSolid()) {
					setTile(changingTile[j].x, changingTile[j].y, Tile.tileDirt);
					if(new Random(getSeed(true)/timesSeedUsed).nextDouble() > 0.9) {
						setTile(changingTile[j].x, changingTile[j].y, Tile.tileRocksFloor);
					}
				}
			}
			
			
			if(new Random(getSeed(true)/timesSeedUsed).nextDouble() < 0.25) {
				tile.translate(1, 0);
			} else if(new Random(getSeed(false)/timesSeedUsed).nextDouble() > 0.75) {
				tile.translate(-1, 0);
			} else if(new Random(getSeed(false)/timesSeedUsed).nextDouble() > 0.5) {
				tile.translate(0, 1);
			} else if(new Random(getSeed(false)/timesSeedUsed).nextDouble() > 0.25) {
				tile.translate(0, -1);
			}
		}
	}
	
	public void generatePath(int x, int y, int size) {
		int direction = (int) (new Random(getSeed(true)/timesSeedUsed).nextDouble()*4);
		
		Point tile = new Point(x, y);
		for(int i = 0; i < size; i++) {
			Tile path = Tile.tilePath;
			if((getTile(tile.x, tile.y) == Tile.tileGrass || getTile(tile.x, tile.y) == Tile.tileWater || getTile(tile.x, tile.y) == Tile.tileDirt) && !getTile(tile.x, tile.y).isSolid()) {
				if(getTile(tile.x, tile.y) == Tile.tileWater) {
					path = Tile.tileWood;
				}
				else if(getTile(tile.x, tile.y) == Tile.tileDirt) {
					path = Tile.tileDirt;
				}
				if(new Random(getSeed(true)/timesSeedUsed).nextDouble() > 0.10) {
					setTile(tile.x, tile.y, path);
				}
				if(new Random(getSeed(true)/timesSeedUsed).nextDouble() > 0.10) {
					if(direction == Entity.DIRECTION_RIGHT) {
						setTile(tile.x, tile.y+1, path);
					} else if(direction == Entity.DIRECTION_LEFT) {
						setTile(tile.x, tile.y-1, path);
					} else if(direction == Entity.DIRECTION_UP) {
						setTile(tile.x+1, tile.y, path);
					} else if(direction == Entity.DIRECTION_DOWN) {
						setTile(tile.x-1, tile.y, path);
					}
				}
			}
			
			
			if(direction == Entity.DIRECTION_RIGHT) {
				tile.translate(1, 0);
			} else if(direction == Entity.DIRECTION_LEFT) {
				tile.translate(-1, 0);
			} else if(direction == Entity.DIRECTION_UP) {
				tile.translate(0, 1);
			} else if(direction == Entity.DIRECTION_DOWN) {
				tile.translate(0, -1);
			}
		}
	}
	public void generateRocks(int x, int y, int size) {
		Point tile = new Point(x, y);
		for(int i = 0; i < size; i++) {
			Point[] changingTile = {
					new Point(tile.x+1, tile.y),
					new Point(tile.x-1, tile.y),
					new Point(tile.x, tile.y+1),
					new Point(tile.x, tile.y-1)
			};
			setTile(tile.x, tile.y, Tile.tileRocks);
			for(int j = 0; j < changingTile.length; j++) {
				if(getTile(changingTile[j].x, changingTile[j].y) != Tile.tileWater && !getTile(changingTile[j].x, changingTile[j].y).isSolid()) {
					setTile(changingTile[j].x, changingTile[j].y, Tile.tileDirt);
					if(new Random(getSeed(true)/timesSeedUsed).nextDouble() > 0.75) {
						setTile(changingTile[j].x, changingTile[j].y, Tile.tileRocksFloor);
					}
				}
			}
			
			
			if(new Random(getSeed(true)/timesSeedUsed).nextDouble() < 0.25) {
				tile.translate(1, 0);
			} else if(new Random(getSeed(false)/timesSeedUsed).nextDouble() > 0.75) {
				tile.translate(-1, 0);
			} else if(new Random(getSeed(false)/timesSeedUsed).nextDouble() > 0.5) {
				tile.translate(0, 1);
			} else if(new Random(getSeed(false)/timesSeedUsed).nextDouble() > 0.25) {
				tile.translate(0, -1);
			}
		}
	}
	public void generatePortalRoom(int x, int y) {
		int newx = x-4;
		int newy = y-2;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 6; j++) {
				setTile(newx+i, newy+j, Tile.tileRocksFloor);
			}
		}
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 3; j++) {
				setTile(x-i, y+4+j, Tile.tileRocksFloor);
			}
		}
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 4; j++) {
				if(new Random(getSeed(true)/timesSeedUsed).nextDouble() > 0.75) {
					setTile(x-i, y+7+j, Tile.tilePath);
				} else {
					setTile(x-i, y+7+j, Tile.tileGrass);
				}
			}
		}
		for(int i = 0; i < 8; i++) {
			setTile(newx+i, newy-1, Tile.tileRocks);
		}
		for(int j = 0; j < 6; j++) {
			setTile(newx-1, newy+j, Tile.tileRocks);
		}
		for(int j = 0; j < 6; j++) {
			setTile(newx+8, newy+j, Tile.tileRocks);
		}
		setTile(newx+7, newy, Tile.tileRocks);
		setTile(newx, newy, Tile.tileRocks);
		setTile(newx, newy+5, Tile.tileRocks);
		setTile(newx+7, newy+5, Tile.tileRocks);
		setTile(newx+7, newy+5, Tile.tileRocks);
		setTile(newx+2, newy+7, Tile.tileRocks);
		setTile(newx+5, newy+7, Tile.tileRocks);
		for(int i = 0; i < 8; i++) {
			if(i != 3 && i != 4) setTile(newx+i, newy+6, Tile.tileRocks);
		}
		
	}
	public void setSnowy() {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(getTile(j, i) == Tile.tileGrass) {
					setTile(j, i, Tile.tileSnow);	
				}
				if(getTile(j, i) == Tile.tilePath) {
					setTile(j, i, Tile.tileSnowPath);	
				}
				
			}
		}
	}
	public void deleteTiles() {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				setTile(j, i, Tile.tileTest);
			}
		}
	}
	public void setWorldType(int type) {
		worldType = type;
	}
	public void fillWorld(Tile tile) {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				setTile(j, i, tile);
			}
		}
	}
	public void saveWorld() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("new-world.world", "UTF-8");
			writer.println("width: " + this.getWidth());
			writer.println("height: " + this.getHeight());
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					writer.println(j + "," + i + ": " + getTile(j, i).getID());
				}
			}
			Game.ConsoleSend("World was saved to new-world.world");
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public void loadWorld(String w) throws IOException {
		InputStream in = getClass().getResourceAsStream("/worlds/" + w + ".world");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		while((line = br.readLine()) != null) {
			if(!line.startsWith("width") && !line.startsWith("height")) {
				String[] x = line.split(",");
				String[] y = x[1].split(":");
				if(Tile.tiles[Byte.parseByte(y[1].substring(1, y[1].length()))] != Tile.tileDelete) {
					setTile(Integer.parseInt(x[0]), Integer.parseInt(y[0]), Tile.tiles[Byte.parseByte(y[1].substring(1, y[1].length()))]);
				}
			}
		}
		Game.ConsoleSend("World was loaded.");
		in.close();
	}
	
	private long getSeed(boolean b) {
		if (b) {
			timesSeedUsed++;
		}
		return seed;
	}

}
