package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import raidgame.Game;

public class Screenshot {
	
	private Window window;
	
	public Screenshot(Window window) {
		this.window = window;
	}

	public void cap() {
		GL11.glReadBuffer(GL11.GL_FRONT);
		int width = window.getWidth();
		int height = window.getHeight();
		int bpp = 4;
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );
		
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		   
		for(int x = 0; x < width; x++) 
		{
		    for(int y = 0; y < height; y++)
		    {
		        int i = (x + (width * y)) * bpp;
		        int r = buffer.get(i) & 0xFF;
		        int g = buffer.get(i + 1) & 0xFF;
		        int b = buffer.get(i + 2) & 0xFF;
		        image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
		    }
		}
		String date = LocalDateTime.now().toString();
		String name = "SCR-" + date.replaceAll(":", ".").replaceAll("T", "_");
		File dir = new File(WorkingDirectory.getWorkingDirectory() + "/screenshots/" + name + ".png");
		
		if(!dir.exists()) {
			dir.mkdirs();
		}
		try {
		    ImageIO.write(image, "png", dir);
		    Game.ConsoleSend("Screenshot saved.");
		} catch (IOException e) { 
			e.printStackTrace();
			Game.ConsoleSend("(ERROR) - Could not save screenshot.");
			Game.ConsoleSend("(ERROR) - " + e.getLocalizedMessage());
		}
	}
}
