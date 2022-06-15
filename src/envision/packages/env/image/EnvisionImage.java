package envision.packages.env.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import envision.lang.classes.ClassInstance;

/**
 * An image context usable within the Envision Java Scripting Language.
 * Internally backed by a Java::BufferedImage.
 * 
 * @author Hunter Bragg
 */
public class EnvisionImage extends ClassInstance {

	/**
	 * The internally backing buffered image.
	 */
	public BufferedImage img;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionImage(Number width, Number height) { this(width.intValue(), height.intValue()); }
	public EnvisionImage(int width, int height) {
		super(EnvisionImageClass.IMAGE_CLASS);
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public EnvisionImage(String pathIn) { this(new File(pathIn)); }
	public EnvisionImage(File fileIn) {
		super(EnvisionImageClass.IMAGE_CLASS);
		try {
			img = ImageIO.read(fileIn);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//---------
	// Methods
	//---------
	
	public BufferedImage getI() {
		return img;
	}
	
}
