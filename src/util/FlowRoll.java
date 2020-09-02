package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import raidgame.Game;

public class FlowRoll {

	
	private static BufferedImage img;
	private static Graphics2D g;
	
	public static void saveShieldtexture() {
		img = new BufferedImage(1504, 32, BufferedImage.TYPE_INT_ARGB);
		
		g = (Graphics2D) img.getGraphics();

		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		for(int i = 1; i < 48; i++) {
			BufferedImage image = null;
			try {
			    image = ImageIO.read(new File("shield/" + i + ".png"));
			    g.drawImage(image, (i-1)*32, 0, null);
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				
			    File outputfile = new File("shieldTexture.png");
			    ImageIO.write(img, "png", outputfile);
			    Game.ConsoleSend("Saved image");
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
	}
	
	public static void saveTexture(String file) {
		img = new BufferedImage(1024, 32, BufferedImage.TYPE_INT_ARGB);
		
		g = (Graphics2D) img.getGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1024, 32);
		
		BufferedImage image = null;
		try {
		    image = ImageIO.read(new File(file));
		} catch (IOException e) {
		}
		
		for(int i = 0; i < 33; i++) {
			g.drawImage(image, 0+(i*32), 0+(i*1), null);
			if(i != 0) {
				BufferedImage bottom = image.getSubimage(0, 32-(i*1), 32, (i*1));
				g.drawImage(bottom, 0+(i*32), 0, null);
			}
		}
		
		try {
			
		    File outputfile = new File("frostTexture.png");
		    ImageIO.write(img, "png", outputfile);
		    Game.ConsoleSend("Saved image");
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public static void saveMask(String file) {
		img = new BufferedImage(1024, 32, BufferedImage.TYPE_INT_ARGB);
		
		g = (Graphics2D) img.getGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1024, 32);
		
		BufferedImage image = null;
		try {
		    image = ImageIO.read(new File(file));
		} catch (IOException e) {
		}
		
		for(int i = 0; i < 33; i++) {
			g.drawImage(image, 0+(i*32), 0, null);
		}
		
		try {
			
		    File outputfile = new File("frostMask.png");
		    ImageIO.write(img, "png", outputfile);
		    Game.ConsoleSend("Saved image");
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
