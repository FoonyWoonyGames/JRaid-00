package util;

import static org.lwjgl.glfw.GLFW.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWImage.Buffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {
	
	private long window;
	
	private int width, height;
	private boolean fullscreen;
	private boolean hasResized;
	private GLFWWindowSizeCallback windowSizeCallback;
	
	private Input input;
	
	public static void setCallbacks() {
		glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
	}
	
	private void setLocalCallbacks() {
		windowSizeCallback = new GLFWWindowSizeCallback() {

			@Override
			public void invoke(long window, int w, int h) {
				width = w;
				height = h;
				hasResized = true;
			}
		};
		
		glfwSetWindowSizeCallback(window, windowSizeCallback);
	}

	public Window() {
		setSize(640, 400);
		setFullscreen(false);
		hasResized = false;
	}
	
	public void createWindow(String title) {
		
		window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0, 0);
		
		if(window == 0) {
			throw new IllegalStateException("Failed to create window");
		}
		
		if(!fullscreen) {
			GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window, (vid.width()-width)/2, (vid.height()-height)/2);
			
			glfwShowWindow(window);
		}
		
		glfwMakeContextCurrent(window);
		
		input = new Input(this);
		setLocalCallbacks();
	}
	
	public void cleanUp() {
		windowSizeCallback.close();
	}
	
	public void update() {
		hasResized = false;
		input.update();
		glfwPollEvents();
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public boolean hasResized() {
		return hasResized;
	}
	public void setFullscreen(boolean b) {
		fullscreen = b;
	}
	public boolean isFullscreen() {
		return fullscreen;
	}
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	public void swapBuffers() {
		glfwSwapBuffers(window);
	}
	public Input getInput() {
		return input;
	}
	public long getWindow() {
		return window;
	}
}
