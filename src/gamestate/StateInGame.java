package gamestate;

import static org.lwjgl.glfw.GLFW.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.joml.Vector3f;

import entity.BossAlzoth;
import entity.Enemy;
import entity.Entity;
import entity.Flag;
import entity.Keyper;
import entity.Particle;
import entity.Player;
import entity.Portal;
import entity.Skeleton;
import entity.Soldier;
import entity.Spirit;
import entity.Transform;
import entity.Zombie;
import raidgame.Game;
import render.Camera;
import render.Shader;
import util.FlowRoll;
import util.Window;
import world.Tile;
import world.TileRenderer;
import world.Wave;
import world.World;

public class StateInGame extends GameState {

	private World world;
	private TileRenderer tiles;
	private Camera camera;
	private Wave wave;
	
	private Player playerTest;
	private Portal portal;
	
	
	public StateInGame(Window window) {
		super(window);
	}
	
	@Override
	public void init() {
		Game.ConsoleSend("[Loading] -- Creating world");
		tiles = new TileRenderer();
		long seed = new Random().nextLong();
		world = new World(seed);
		world.calculateView(window);
		Game.ConsoleSend("[Loading] -- World created with seed: " + Math.random()*90);

		Game.ConsoleSend("[Loading] -- Creating player");
		camera = new Camera(window.getWidth(), window.getHeight());
		
		playerTest = new Player(new Transform());
		playerTest.randomizePosition(world);
		playerTest.moveCamera(camera, world, 1);
		world.addEntity(playerTest);
		world.spawnParticle(Particle.PARTICLE_AIM, 1, 1, 50, 50, playerTest, true, 0, 2.5f, true);
		world.spawnParticle(Particle.PARTICLE_CROSSHAIR, 1, 1, 25, 25, playerTest, true, -2, 3.5f, true);

		Game.ConsoleSend("[Loading] -- Finishing");

		world.generateEnvironment();
		portal = new Portal(new Transform());
		portal.setPosition(51, -50);
		world.addEntity(portal);
		world.generatePortalRoom(portal.getXOnMap(), portal.getYOnMap());
		
		addFlags();

		Wave.Reset();
		Wave.LoadWaves();
		wave = new Wave(world);
		wave.start();

		glfwSetInputMode(Game.raid.getWindow().getWindow(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
		glfwSetInputMode(Game.raid.getWindow().getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		
		Game.ConsoleSend("[Loading] -- Done!");
	}

	@Override
	public void update(float delta, Window window) {
		world.update((float) delta, window, camera);
		world.correctCamera(camera, window);
//		if(isEmpty()) {
//			portal = new Portal(new Transform());
//			portal.setPosition(51, -50);
//			world.addEntity(portal);
//			portal.setOpen(true);
//			world.spawnParticle(Particle.PARTICLE_MAGIC, 4, 8, 16, 16, portal.getX(), portal.getY()+1, false, 10, 90, 50, 360, false);
//			Soldier soldier = new Soldier(new Transform());
//			soldier.setPosition(47, -50);
//			world.addEntity(soldier);
//			Keyper keyper = new Keyper(new Transform());
//			keyper.setPosition(54, -51.5f);
//			world.addEntity(keyper);
//			Game.ConsoleSend("All enemies have been defeated");
//			Game.ConsoleSend("Portal has spawned");
//		}
		if(wave != null) {
			if(wave.isStarted() && wave.areEnemiesDead() && !wave.isEnded()) {
				wave.end();
			}
			if(wave.isEnded()) {
				if(System.currentTimeMillis() - wave.timeEnd() > 5000) {
					wave = new Wave(world);
					wave.start();
				}
			} else {
				if(wave.isStarted() && System.currentTimeMillis() - wave.timeStart() > 300000) {
					wave = new Wave(world);
					wave.start();
				}
			}
		}
	}

	@Override
	public void render(Shader shader) {
		world.render(tiles, shader, camera);
		
	}

	@Override
	public void inputKeyboard() {
		if(window.getInput().isKeyPressed(GLFW_KEY_L)) {
			Game.ConsoleSend("[DEV] -- Player's location is (" + playerTest.getX()*2 + ", " + playerTest.getY()*2 + ")");
		}
		if(window.getInput().isKeyPressed(GLFW_KEY_G)) {
			playerTest.randomizePosition(world);
		}
		if(window.getInput().isKeyPressed(GLFW_KEY_K) && window.getInput().isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
			world.killEntities();
		}
		if(window.getInput().isKeyPressed(GLFW_KEY_KP_1)) {
			world.addEntity(new Zombie(new Transform()));
			Game.ConsoleSend("[DEV] -- Spawned Zombie");
		}
		if(window.getInput().isKeyPressed(GLFW_KEY_KP_2)) {
			world.addEntity(new Skeleton(new Transform()));
			Game.ConsoleSend("[DEV] -- Spawned Skeleton");
		}
		if(window.getInput().isKeyPressed(GLFW_KEY_R)) {
			init();
			Game.ConsoleSend("RAID was reset!");
		}
		if(window.getInput().isKeyPressed(GLFW_KEY_P)) {
			Game.ConsoleSend("[DEV] -- Temporary Testing");
			world.saveWorld();
		}
	}

	@Override
	public void hasResized() {
		camera.setProjection(window.getWidth(), window.getHeight());
		world.calculateView(window);
	}
	
	public World getCurrentWorld() {
		return world;
	}
	
	public boolean isEmpty() {
		boolean b = false;
		for(int i = 0; i < getCurrentWorld().getEntityList().size(); i++) {
			if(getCurrentWorld().getEntityList().get(i).type == Entity.TYPE_ENEMY) {
				b = false;
				break;
			}
			else {
				b = true;
			}
		}
		return b;
	}
	
	private void addFlags() {
		Flag flagBoss = new Flag(new Transform());
		flagBoss.setFlag(Flag.ANIMATION_FLAG_PROGRAM);
		flagBoss.setPosition(10, -10);
		flagBoss.setAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Enemy boss = new BossAlzoth(new Transform());
				boss.setPosition(10, -10);
				boss.setHealth(boss.getMaxHealth());
				world.addEntity(boss);
			}
		});
		flagBoss.setMessage("Boss Alzoth was spawned");
		world.addEntity(flagBoss);

		Flag flagPortal = new Flag(new Transform());
		flagPortal.setFlag(Flag.ANIMATION_FLAG_PROGRAM);
		flagPortal.setPosition(49, -51);
		flagPortal.setAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					world.loadWorld("boss");
					world.killEntities();
					Flag flagFight = new Flag(new Transform());
					flagFight.setFlag(Flag.ANIMATION_FLAG_INTEREST);
					flagFight.setPosition(68, -70);
					flagFight.setMessage("Alzoth turns around. Boss fight. Players are stunned. Screen goes black. Baldwynn rushes in. Alzoth is captured. Game has been completed.");
					world.addEntity(flagFight);
				} catch (IOException exc) {
					exc.printStackTrace();
				}
			}
		});
		flagPortal.setMessage("Loaded Boss world");
		world.addEntity(flagPortal);

		Flag flagTower = new Flag(new Transform());
		flagTower.setFlag(Flag.ANIMATION_FLAG_GRAPHIC);
		flagTower.setPosition(49, -60);
		flagTower.setMessage("Use new round tower");
		world.addEntity(flagTower);
	}
}
