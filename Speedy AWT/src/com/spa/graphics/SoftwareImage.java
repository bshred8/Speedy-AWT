package com.spa.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SoftwareImage implements DisplaySettings {

	/**
	 * An image, loaded primarily by the CPU, used to be drawn on via raw access to the pixel data.
	 */
	protected BufferedImage img;
	
	/**
	 * Whether or not there is an alpha channel for this image.
	 */
	private boolean alpha = false;
	
	/**
	 * w = width of the image; h = height of the image
	 */
	private int w, h;
	
	/**
	 * An array of integers that store each numerical color value for the pixels on the image.
	 */
	protected int[] pixels;
	
	/**
	 * The created graphics object from the image.
	 */
	private Graphics2D g;
	
	/**
	 * @param width Width of the image.
	 * @param height Height of the image.
	 * @param alpha Whether or not to create the image with an alpha channel or not.
	 */
	public SoftwareImage(int width, int height, boolean alpha) {
		this.alpha = alpha;
		this.w = width;
		this.h = height;
		img = alpha ? new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
				: new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		g = img.createGraphics();
	}
	
	/**
	 * @param file The path to the image file. Loaded through ImageIO.
	 * @throws IOException Used to handles ImageIO's IOException for reading the image file.
	 */
	public SoftwareImage(String file) throws IOException {
		img = ImageIO.read(new File(file));
		this.w = img.getWidth();
		this.h = img.getHeight();
		pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		g = img.createGraphics();
	}
	
	/**
	 * @param priority A value, between 0.0f and 1.0f, determining the priority of putting this image into accelerated memory.
	 */
	public void setAccelPriority(float priority) {
		img.setAccelerationPriority(priority);
	}
	
	/**
	 * Quite an expensive process, this method creates a new VolatileImage and draws the BufferedImage onto it
	 * via the Graphics2D of the VolatileImage.
	 * @return A (compatible) VolatileImage with the current BufferedImage drawn to it.
	 */
	public VolatileImage accelerate() {
		VolatileImage v = gConfig.createCompatibleVolatileImage(w, h);
		Graphics2D vg = v.createGraphics();
		vg.drawImage(img, 0, 0, null);
		return v;
	}
	
	/**
	 * @param x X coordinate of the pixel to edit.
	 * @param y Y coordinate of the pixel to edit.
	 * @param color Color of the pixel to which it will be changed.
	 */
	public void setPixel(int x, int y, int color) {
		pixels[x + y * w] = color;
	}
	
	/**
	 * @param x X coordinate of the pixel to get.
	 * @param y Y coordinate of the pixel to get.
	 * @return The integer color value of the pixel at (X, Y).
	 */
	public int getPixel(int x, int y) {
		return pixels[x + y * w];
	}
	
	/**
	 * @param color Sets all pixels in the image to the specified integer color.
	 */
	public void clear(int color) {
		for(int i=0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}
	
	/**
	 * @return The width of the image.
	 */
	public int getWidth() {
		return w;
	}
	
	/**
	 * @return The height of the image.
	 */
	public int getHeight() {
		return h;
	}
	
	/**
	 * @return The pixel array of the BufferedImage.
	 */
	public int[] getPixels() {
		return pixels;
	}
	
	/**
	 * @return True if the image has alpha, false if otherwise.
	 */
	public boolean hasAlpha() {
		return alpha;
	}
	
	/**
	 * Note that using Graphics2D is significantly slower than editing the pixel data manually.
	 * @return The Graphics2D object for this image.
	 */
	protected Graphics2D getGraphics() {
		return g;
	}
	
	/**
	 * Used to free the memory held by the image and nullify the pixels array.
	 */
	public void destroy() {
		img.flush();
		pixels = null;
	}
	
	
}