package gui;

import org.joml.Matrix4f;
import assets.Assets;
import render.Camera;
import render.Shader;
import render.Tilesheet;
import util.Window;

public class Gui {
	private Shader shader;
	private Camera camera;
	private Tilesheet sheet;
	
	
	public Gui(Window window, String resource) {
		shader = new Shader("gui");
		camera = new Camera(window.getWidth(), window.getHeight());
		sheet = new Tilesheet(resource, 1);
	}
	
	public void resizeCamera(Window window) {
		camera.setProjection(window.getWidth(), window.getHeight());
	}
	
	public void render(float x, float y, int scale, int index) {
		Matrix4f mat = new Matrix4f();
		camera.getProjection().scale(scale, mat);
		mat.translate(x,y,index);
		shader.bind();
		
		shader.setUniform("projection", mat);
		
		sheet.bindTile(shader, 1);
//		shader.setUniform("color", new Vector4f(0,0,0,0.4f));
		
		Assets.getModel().render();
	}

}
