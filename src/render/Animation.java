package render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import raidgame.Game;
import util.Timer;

public class Animation {

	private Texture[] frames;
	private int pointer;
	
	private double elapsedTime;
	private double currentTime;
	private double lastTime;
	private double fps;
	private boolean played;
	private boolean looping;
	private boolean startedOver;
	
	public Animation(int amount, int fps, int w, int h, String path) {
		this.pointer = 0;
		this.elapsedTime = 0;
		this.currentTime = 0;
		this.lastTime = Timer.getTime();
		this.fps = 1.0/(double)fps;
		played = false;
		looping = true;
		
		this.frames = new Texture[amount];

		BufferedImage texture = null;
    	try {
			texture = ImageIO.read(getClass().getResourceAsStream("/textures/entities/" + path + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < amount; i++) {
			this.frames[i] = new Texture(texture.getSubimage(0+(i*w), 0, w, h));
		}
	}
	public Animation(int amount, int fps, int w, int h, BufferedImage texture) {
		this.pointer = 0;
		this.elapsedTime = 0;
		this.currentTime = 0;
		this.lastTime = Timer.getTime();
		this.fps = 1.0/(double)fps;
		played = false;
		looping = true;
		
		this.frames = new Texture[amount];
		
		for(int i = 0; i < amount; i++) {
			this.frames[i] = new Texture(texture.getSubimage(0+(i*w), 0, w, h));
		}
	}
	
	public void bind() {
		bind(0);
	}
	
	public Animation setLooping(boolean b) {
		looping = b;
		return this;
	}
	
	public void bind(int sampler) {
		this.currentTime = Timer.getTime();
		this.elapsedTime += currentTime - lastTime;
		
		if(!looping) {
			if(!startedOver) {
				elapsedTime = 0;
				startedOver = true;
			}
		}
		
		if(elapsedTime >= fps) {
			elapsedTime = 0;
			pointer++;
		}
		if(pointer >= frames.length) {
			played = true;
			pointer = 0;
		}
		if(!looping && played) {
			elapsedTime = 0;
			pointer = frames.length - 1;
		}
		
		
		this.lastTime = currentTime;
		
		frames[pointer].bind(sampler);
	}
	
	public boolean hasPlayedOnce() {
		return played;
	}
	
	public int getFrame() {
		return pointer;
	}
	
	public void reset() {
		pointer = 0;
		elapsedTime = 0;
		played = false;
		startedOver = false;
	}
}
