package com.spa.graphics;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public interface DisplaySettings {
	
	public static final GraphicsEnvironment gEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
	public static final GraphicsConfiguration gConfig = gEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
	public static final GraphicsDevice defaultGDevice = gEnvironment.getDefaultScreenDevice();
	
	public static final int WIDTH = (int)gConfig.getBounds().getWidth();
	public static final int HEIGHT = (int)gConfig.getBounds().getHeight();
	
}