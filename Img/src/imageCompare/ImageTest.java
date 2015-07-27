package imageCompare;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import com.pearsoneduc.ip.io.ImageDecoderException;
/**
 * Synchronous loading of image data into an Image object
 * useful to ensure image has loaded before any image processing can be performed.
 * @author laura
 *
 */
public class ImageTest {
	/*
	public static Image readImage(String file){
		Image image = Toolkit.getDefaultToolkit().getImage(file);
		MediaTracker tracker = new MediaTracker(new Component() {});
		// component is short hand for creating an instance of TemporaryComponent
		//which extends abstract class Component, which must be used with MediaTracker
		tracker.addImage(image,  0);
		try {
			tracker.waitForID(0);
		} catch (InterruptedException e){
		}
		return image;
	}
	/*
	 * 
	 */
	public static void main(String[] args) throws IOException, ImageDecoderException {
		String filename = "IMG_0062.jpg";
		JFrame frame = new Grayscale(filename);
		frame.pack();
		frame.setVisible(true);
		
		DivideIntoBlocks.divide("grayscale.jpg");
		
		/*
		//File file = new File("IMG_0062");
		//if (args.length > 0){
			Image image = readImage("IMG_0062");
			// do something with image...
		System.out.println("Image Read");
		ImageCanvas ic = new ImageCanvas(image);
		ic.paint();
		System.out.println("image displayed");
		/*
		 * 
		 */
	}

}
