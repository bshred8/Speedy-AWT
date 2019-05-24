# Speedy-AWT
Made in Java using only the AWT library to create and manage images in the fastest possible way for the best result.
<br/>
SpeedyAWT does not (yet) handle window creation or management. As of now, it is important that you create your own window to render images to.

<h1>Purpose</h1>
Speedy AWT, or "spa" for short, is nothing more than a wrapper for the AWT BufferedImage and VolatileImage. It creates an image
created and rendered on the CPU by default, and accelerates it per the user's request.
<br/><br/>
Along with accelerated images, it has access to the basic graphical display settings via the Graphics* classes, interfaced in DisplaySettings.

<h1>Usage</h1>
<h2>SoftwareImage</h2>
The SoftwareImage constructor can create a new, blank image with a specified width, height, and alpha channel; or it may take a
String parameter for the image you would like to load. Examples:
</br>
<code>SoftwareImage sImg = new SoftwareImage(width, height, createWithAlpha);</code>
<br/>
<code>SoftwareImage sImg = new SoftwareImage("C:/path/to/image/test.jpg");</code>
<b><p>The image may be located anywhere, and the image type may be different than .jpg, but it is not guaranteed
to work. Images are loaded via ImageIO. File extension is required.</p></b>
<br/>
Because SoftwareImage is a wrapper for the BufferedImage class, it has many similar functions. Rather than <code>setRGB(x, y, color)</code>
in BufferedImage, it uses direct access to pixel data and edits it quicker. That function is <code>setPixel(x, y, color)</code>.
<br/>
And, just as many other programs, if you may set it you may get it. <code>getPixel(x, y)</code> returns the int color value of the pixel at
(x, y).
<br/>
The <code>clear(color)</code> method takes one integer value and sets all of the data in the pixels array to that number. Essentially, it
clears the entire screen to that color.
<br/>
Other than more getters, this class has one more major functionality and that is its acceleration.
<br/>
<code>setAccelPriority(floatVal)</code> takes a float value between 0.0f and 1.0f to determine how important it is to accelerate this image
to use graphical memory. This is seen to the BufferedImage as a request, and not as a necessity. However, there <b>is</b> a way to convert
this to a VolatileImage to directly put it to graphical memory. This method is <code>accelerate()</code>. It takes no parameters and returns
a VolatileImage, which is important for HardwareImage's use as it is a wrapper for it. This creates a compatible VolatileImage from the
default GraphicsConfiguration, and uses its Graphics2D to draw the BufferedImage onto it, then returns the result.
