package imageCompare;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.pearsoneduc.ip.io.ImageDecoder;
import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.io.ImageFile;

public class Grayscale extends JFrame {
	
	/**
	 * This method takes in a bufferedimage and creates a new greyscale bufferedImage from it
	 * @param BufferedImage image
	 * @return BufferedImage imageGrey
	 * @throws ImageDecoderException 
	 * @throws IOException 
	 */
	public static BufferedImage makeGrey(String filename) throws IOException, ImageDecoderException {
		
		final double RED_VALUE = 0.299;
		final double GREEN_VALUE = 0.587;
		final double BLUE_VALUE = 0.114;
		
		ImageDecoder input = ImageFile.createImageDecoder(filename);
		BufferedImage image = input.decodeAsBufferedImage();
		// checking width and height of image
		int width = image.getWidth();
		int height = image.getHeight();
		System.out.println(width + " width");
		System.out.println(height + " height");
		// create a 
		BufferedImage imageGrey = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster imageWritableRaster = imageGrey.getRaster();

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Color c = new Color(image.getRGB(j, i));
				int red = (int) (c.getRed() * RED_VALUE);
				int green = (int) (c.getGreen() * GREEN_VALUE );
				int blue = (int) (c.getBlue() * BLUE_VALUE);
				int[] colours = {red, green, blue};
				imageWritableRaster.setPixel(j,i,colours);;
			}
		}
		// could this next block for displaying go in a different method?
		//JLabel view = new JLabel(new ImageIcon(imageGrey));
		//getContentPane().add(view);
		//addWindowListener(new WindowAdapter() {
			//public void windowClosing(WindowEvent event) {
				//System.exit(0);
			//}
		//});
		File output = new File("grayscale.jpg");
		ImageIO.write(image, "jpg", output);
		return imageGrey;
	}
}
