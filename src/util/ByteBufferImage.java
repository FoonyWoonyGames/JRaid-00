package util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class ByteBufferImage {
	private int width;
	private int height;
	
	private ByteBuffer bb;
	BufferedImage texture;
	

	public ByteBufferImage(String path) {
    	try {
			texture = ImageIO.read(getClass().getResourceAsStream(path));
	    	
	        final byte[] buffer = new byte[texture.getWidth() * texture.getHeight() * 4];
	        int counter = 0;
	        for (int i = 0; i < texture.getHeight(); i++) {
	            for (int j = 0; j < texture.getWidth(); j++) {
	                final int c = texture.getRGB(j, i);
	                buffer[counter + 0] = (byte) (c << 8 >> 24);
	                buffer[counter + 1] = (byte) (c << 16 >> 24);
	                buffer[counter + 2] = (byte) (c << 24 >> 24);
	                buffer[counter + 3] = (byte) (c >> 24);
	                counter += 4;
	            }
	        }
	        ByteBuffer bybu = BufferUtils.createByteBuffer(buffer.length);
	        bybu.put(buffer);
	        bybu.flip();
	        bb = bybu;
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ByteBuffer getByteBufferImage() {
		return bb;
	}
	
	public BufferedImage getBufferedImage() {
		return texture;
	}
}
