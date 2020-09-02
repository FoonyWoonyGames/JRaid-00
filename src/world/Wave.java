package world;

import java.util.ArrayList;

import entity.Enemy;
import entity.Entity;
import entity.Skeleton;
import entity.Spirit;
import entity.Transform;
import entity.Zombie;
import raidgame.Game;

public class Wave {

	private static ArrayList<Wave> Waves;
	
	private int waveID;
	private ArrayList<Entity> enemies;
	private static ArrayList<Integer[]> enemysets;
	private boolean started;
	private boolean ended;
	private World world;
	
	private long timeStart;
	private long timeEnd;
	
	public Wave(World world) {
		enemies = new ArrayList<Entity>();
		if(Waves == null) {
			Waves = new ArrayList<Wave>();
		}
		Waves.add(this);
		waveID = Waves.size();
		this.world = world;
		
		started = false;
		ended = false;
	}
	
	public void start() {
		if(waveID < enemysets.size() && enemysets.get(waveID) != null) {
			Game.ConsoleSend("WAVE " + waveID + " has begun");
			addEnemies(enemysets.get(waveID));
			for(int i = 0; i < getEnemies().size(); i++) {
				world.spawnEntity(getEnemies().get(i));
			}
			Game.ConsoleSend("[Loading] -- Spawning " + (getEnemyset()[0] + getEnemyset()[1] + getEnemyset()[2]) + " mobs from wave " + waveID);
			timeStart = System.currentTimeMillis();
			this.started = true;
		} else {
			Game.ConsoleSend("[ERROR] -- WAVE " + waveID + " is missing an enemyset and did not start.");
		}
	}
	
	public void forceEnd() {
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).kill();
		}
		timeEnd = System.currentTimeMillis();
		this.ended = true;
		Game.ConsoleSend("WAVE " + waveID + " has force-ended");
	}
	
	public void end() {
		Game.ConsoleSend("WAVE " + waveID + " has ended");
		timeEnd = System.currentTimeMillis();
		this.ended = true;
	}
	
	public boolean isStarted() {
		return this.started;
	}
	
	public boolean isEnded() {
		return this.ended;
	}
	
	public boolean areEnemiesDead() {
		boolean empty = true;
		
		for(int i = 0; i < enemies.size(); i++) {
			if(world.containsEntity(enemies.get(i))) {
				empty = false;
				break;
			}
		}
		return empty;
	}
	
	public long timeEnd() {
		return this.timeEnd;
	}
	
	public long timeStart() {
		return this.timeStart;
	}
	
	public void addEnemies(Integer[] enemyset) {
		for(int i = 0; i < enemyset[0]; i++) {
			enemies.add(new Zombie(new Transform()));
		}
		for(int i = 0; i < enemyset[1]; i++) {
			enemies.add(new Skeleton(new Transform()));
		}
		for(int i = 0; i < enemyset[2]; i++) {
			enemies.add(new Spirit(new Transform()));
		}
	}
	
	public ArrayList<Entity> getEnemies() {
		return enemies;
	}
	
	public Integer[] getEnemyset() {
		return enemysets.get(waveID);
	}
	
	public static void LoadWaves() {
		Integer[] wave0 = { 0,0,0 };
		Integer[] wave1 = { 2,1,0 };
		Integer[] wave2 = { 4,2,0 };
		Integer[] wave3 = { 6,3,0 };

		enemysets = new ArrayList<Integer[]>();
		enemysets.add(wave0);
		enemysets.add(wave1);
		enemysets.add(wave2);
		enemysets.add(wave3);
	}
	
	public static void Reset() {
		if(enemysets != null) {
			enemysets.clear();
			enemysets = null;
		}
		
		if(Waves != null) {
			Waves.clear();
			Waves = null;
		}
	}
}
