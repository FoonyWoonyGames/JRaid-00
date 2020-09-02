package world;

import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;

public class TileRenderer {

	private HashMap<String, Texture> tileTextures;
	private Model model;
	
	public TileRenderer() {
		tileTextures = new HashMap<String, Texture>();
		float[] vertices = new float[] {
				-1f, 1f, 0,
				1f, 1f, 0,
				1f, -1f, 0,
				-1f, -1f, 0
				
		};
		
		float[] texture = new float[] {
				0,0,
				1,0,
				1,1,
				0,1
		};
		
		int[] indices = new int[] {
			0,1,2,
			2,3,0
		};
		
		model = new Model(vertices, texture, indices);
		
		for(int i = 0; i < Tile.tiles.length; i++) {
			if(Tile.tiles[i] != null) {
				if(!tileTextures.containsKey(Tile.tiles[i].getTexture())) {
					String tiletex = Tile.tiles[i].getTexture();
					tileTextures.put(tiletex, new Texture("/textures/tiles/" + tiletex + ".png"));
				}
			}
		}
	}
	
	public void renderTile(Tile tile, int x, int y, Shader shader, Matrix4f world, Camera camera) {
		shader.bind();
		if(tileTextures.containsKey(tile.getTexture())) {
			tileTextures.get(tile.getTexture()).bind(0);
		}
		
		Matrix4f tilePosition = new Matrix4f().translate(new Vector3f(x*2, y*2, 0));
		Matrix4f target = new Matrix4f();
		
		camera.getProjection().mul(world, target);
		target.mul(tilePosition);
		
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", target);
		
		model.render();
	}
}
