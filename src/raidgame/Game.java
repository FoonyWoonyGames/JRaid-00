package raidgame;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.lwjgl.Version;
import assets.Assets;
import entity.Particle;
import gamestate.GameStateManager;
import gui.Gui;
import render.Shader;
import util.Input;
import util.Screenshot;
import util.Timer;
import util.Window;
import util.WorkingDirectory;
import world.TileRenderer;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL;

public class Game {
	
	public static Game raid;
	
	public static void main(String[] args) {
		raid = new Game();
		raid.start();
	}
	
	private Window win;
	private Input input;
//	private Camera cam;
//	private World world;
	private double frameCap;
	
	public static GameStateManager gsm;
	public static Screenshot screenshooter;
	
	public static String console;
	
	public void start() {
		ConsoleSend("RAID has been started");
		ConsoleSend("Running LWJGL version: " + Version.getVersion());

		util.Window.setCallbacks();
		
		if(!glfwInit()) {
			ConsoleSend("GLFW failed to initialize!");
			System.exit(1);
		};
		
		win = new Window();
		win.setSize(640, 400);
		win.createWindow("RAID: Undeath Arisen");
		input = win.getInput();
		Particle.LoadImages();
		
		if(!new File(WorkingDirectory.getWorkingDirectory()).exists()) {
			ConsoleSend("Local Raid directory was not found. Creating one.");
			
			File dir = new File(WorkingDirectory.getWorkingDirectory());
			dir.mkdirs();
		}
		
		
		GL.createCapabilities();

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

//		cam = new Camera(win.getWidth(), win.getHeight());
		glEnable(GL_TEXTURE_2D);
		
		TileRenderer tiles = new TileRenderer();
		Assets.initAsset();
		Shader shader = new Shader("shader");
		
//		world = new World();
//		world.calculateView(win);
		gsm = new GameStateManager(win);
		screenshooter = new Screenshot(win);
		
		Gui gui = new Gui(win, "version.png");
		
		
		frameCap = 1.0/60.0;
		
		double frameTime = 0;
		int frames = 0;
		
		double time = Timer.getTime();
		double unprocessed = 0;
		
		while(!win.shouldClose()) {
			
			boolean canRender = false;
			
			double time2 = Timer.getTime();
			double passed = time2 - time;
			unprocessed += passed;
			frameTime += passed;
			
			time = time2;
			
			while(unprocessed >= frameCap) {
				if(win.hasResized()) {
//					cam.setProjection(win.getWidth(), win.getHeight());
//					world.calculateView(win);
					gsm.hasResized();
					glViewport(0, 0, win.getWidth(), win.getHeight());
					
				}

				
				canRender = true;
				unprocessed -= frameCap;


				input.check();
				win.update();
				
//				update();
				gsm.update((float) frameCap, win);
				if(frameTime > 60.0) {
					ConsoleSend("1m has passed, FPS: " + frames/60);
					frameTime = 0;
					frames = 0;
				}
			}
			
			if(canRender) {
				
				glClear(GL_COLOR_BUFFER_BIT);
				
//				shader.bind();
//				shader.setUniform("sampler", 0);
//				shader.setUniform("projection", cam.getProjection().mul(target));
//				textureTest.bind(0);
//				model.render();
				
//				world.render(tiles, shader, cam);
				gsm.render();
				
				gui.render(-1.5f,0.56f, 128, 0);
				
				win.swapBuffers();
				
				frames++;
			}
			
		}
		
		Assets.deleteAsset();
		
		glfwTerminate();
		
	}
	
	public void update() {
//		ConsoleSend("TICK");

//		world.update((float) frameCap, win, cam);
//		world.correctCamera(cam, win);
		
		
	}
	
	public Window getWindow() {
		return win;
	}
	
	public static void ConsoleSend(String str) {
		String date = LocalTime.now().toString();
		date = date.replace(".", ":");
		console = console + System.lineSeparator() + str;
		System.out.println("[Console]: (" + date + ") " + str);
	}
	
	
}
