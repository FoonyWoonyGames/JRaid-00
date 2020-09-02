package world;

import raidgame.Game;

public class Tile {
	
	public static byte not = 0;
	
	public static Tile tiles[] = new Tile[32];
	
	public static final Tile tileTest = new Tile("test");
	public static final Tile tileGrass = new Tile("grass");
	public static final Tile tileWater = new Tile("water");
	public static final Tile tileRocks = new Tile("rocks").setSolid();
	public static final Tile tilePath = new Tile("path");
	public static final Tile tileBricks = new Tile("bricks").setSolid();
	public static final Tile tileSnow = new Tile("snow");
	public static final Tile tileSnowPath = new Tile("snowpath");
	public static final Tile tileWood = new Tile("wood");
	public static final Tile tileLeaves = new Tile("leaves").setSolid();
	public static final Tile tileDirt = new Tile("dirt");
	public static final Tile tileRocksFloor = new Tile("dirtrocks");
	public static final Tile tileOcean = new Tile("ocean").setSolid();
	public static final Tile tileBossFloor = new Tile("floor");
	public static final Tile tileBossWallTop = new Tile("wallTop").setSolid();
	public static final Tile tileBossWallBottom = new Tile("wallBottom");
	public static final Tile tileBossGateLeftTop = new Tile("gateLT").setSolid();
	public static final Tile tileBossGateLeftBottom = new Tile("gateLB");
	public static final Tile tileBossGateRightTop = new Tile("gateRT").setSolid();
	public static final Tile tileBossGateRightBottom = new Tile("gateRB");
	public static final Tile tileGrassWall = new Tile("grasswall").setSolid();
	public static final Tile tileGrassWallRight = new Tile("grasswallRight").setSolid();
	public static final Tile tileGrassWallTurnRightOut = new Tile("grasswallTurnRightOut").setSolid();
	public static final Tile tileGrassWallTurnRightIn = new Tile("grasswallTurnRightIn").setSolid();
	public static final Tile tileGrassWallLeft = new Tile("grasswallLeft").setSolid();
	public static final Tile tileGrassWallTurnLeftOut = new Tile("grasswallTurnLeftOut").setSolid();
	public static final Tile tileGrassWallTurnLeftIn = new Tile("grasswallTurnLeftIn").setSolid();
	public static final Tile tileVoid = new Tile("void").setSolid();
	public static final Tile tileDelete = new Tile("delete");
	
	private byte id;
	private boolean solid;
	private String texture;
	public Tile(String texture) {
		this.id = not;
		not++;
		this.texture = texture;
		this.solid = false;
		if(tiles[id] != null) {
			Game.ConsoleSend("ERROR - Tile ID: " + id +" is already being used");
			throw new IllegalStateException("Tile ID: " + id +" is already being used");
		}
		tiles[id] = this;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public Tile setSolid() {
		this.solid = true;
		return this;
	}
	
	public byte getID() {
		return id;
	}
	public String getTexture() {
		return texture;
	}
}
