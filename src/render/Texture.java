package render;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Texture {

	private int id;
	private int width;
	private int height;
	
	public Texture(String path) {
		BufferedImage texture;
		try {
	    	texture = ImageIO.read(getClass().getResourceAsStream(path));
			
			width = texture.getWidth();
			height = texture.getHeight();
			
			int[] pixelsRaw = new int[width*height*4];
			pixelsRaw = texture.getRGB(0, 0, width, height, null, 0, width);
			
			ByteBuffer pixels = BufferUtils.createByteBuffer(width*height*4);
			
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					int p = pixelsRaw[i*width + j];
					pixels.put((byte)((p >> 16) & 0xFF));
					pixels.put((byte)((p >> 8) & 0xFF));
					pixels.put((byte)(p & 0xFF));
					pixels.put((byte)((p >> 24) & 0xFF));
				}
			}
			
			pixels.flip();
			
			id = glGenTextures();

			glBindTexture(GL_TEXTURE_2D, id);
			
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Texture(BufferedImage img) {
		BufferedImage texture;
		texture = img;
		
		width = texture.getWidth();
		height = texture.getHeight();
		
		int[] pixelsRaw = new int[width*height*4];
		pixelsRaw = texture.getRGB(0, 0, width, height, null, 0, width);
		
		ByteBuffer pixels = BufferUtils.createByteBuffer(width*height*4);
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				int p = pixelsRaw[i*width + j];
				pixels.put((byte)((p >> 16) & 0xFF));
				pixels.put((byte)((p >> 8) & 0xFF));
				pixels.put((byte)(p & 0xFF));
				pixels.put((byte)((p >> 24) & 0xFF));
			}
		}
		
		pixels.flip();
		
		id = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, id);
		
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
	}
	
//	@Override
//	protected void finalize() throws Throwable {
//		glDeleteTextures(id);
//		super.finalize();
//	}
	
	public void bind(int sampler) {
		if(sampler >= 0 && sampler <= 31) {
			glActiveTexture(GL_TEXTURE0 + sampler);
			glBindTexture(GL_TEXTURE_2D, id);
		}
	}
	
}
