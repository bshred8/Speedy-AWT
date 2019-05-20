package com.spa.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.image.VolatileImage;
import java.io.IOException;

public class HardwareImage extends SoftwareImage {

	private VolatileImage vImg;
	private Graphics2D g;
	
	private int width, height;
	
	public HardwareImage(int width, int height, boolean alpha) {
		super(width, height, alpha);
	}
	
	public HardwareImage(String file) throws IOException {
		super(file);
		vImg = accelerate();
	}
	
	public void setDrawColor(int color, boolean hasAlpha) {
		g.setColor(new Color(color, hasAlpha));
	}
	
	public void drawShape(Shape shape, boolean fill) {
		if(fill)
			g.fill(shape);
		else
			g.draw(shape);
	}
	
	public void drawImage(Image img, int xOff, int yOff) {
		g.drawImage(img, xOff, yOff, null);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	private int i = 0;
	public boolean render(Graphics2D other, int xOff, int yOff, boolean log) {
		int code = attemptRender(other, xOff, yOff, true);
		if(code == -1) {
			vImg = null;
			render(other, xOff, yOff, log);
			
			i++;
			
			if(i >= 5 && code == -1) {
				if(log)
					System.out.println("After 5 iterations, rendering attempts have all failed due to incompatibility.");
				i = 0;
				return false;
			} else {
				return true;				
			}
		} else if(code == 0) {
			return true;
		} else if(code == 1) {
			vImg = accelerate();
			render(other, xOff, yOff, log);
			i++;
			
			if(i >= 5 && code == 1) {
				if(log)
					System.out.println("After 5 iterations, rendering attempts have all failed due to loss of data.");
				i = 0;
				return false;
			} else {
				return true;				
			}
		}
		
		return false;
	}
	
	/**
	 * Method for attempting the rendering of this image. Used for explicit control over rendering.
	 * @param other The Graphics2D object of the surface which this image will be drawn.
	 * @param xOff The X offset of this image, relative to the Graphics2D's origin.
	 * @param yOff The Y offset of this image, relative to the Graphics2D's origin.
	 * @param log Print specific messages to the console for instructions.
	 * @return 1 = IMAGE_RESTORED; -1 = IMAGE_INCOMPATIBLE; 0 = IMAGE_OK
	 */
	public int attemptRender(Graphics2D other, int xOff, int yOff, boolean log) {
		if(vImg == null)
			vImg = accelerate();
		
		int imgState = vImg.validate(gConfig);
		if(imgState == VolatileImage.IMAGE_RESTORED) {
			if(log)
				System.out.println("Redraw image data and attempt render again.");
			return 1;
		} else if(imgState == VolatileImage.IMAGE_INCOMPATIBLE) {
			if(log)
				System.out.println("Image in an incompatible state. Recreate.");
			return -1;
		} else if(imgState == VolatileImage.IMAGE_OK) {
			other.drawImage(vImg, xOff, yOff, null);
			return 0;
		} else {
			throw new IllegalStateException("VolatileImage is in an irreparable state.");
		}
	}
	
	public void destroy() {
		g.dispose();
		vImg.flush();
		img.flush();
		pixels = null;
	}
	
}