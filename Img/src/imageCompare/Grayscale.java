package imageCompare;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;

import javax.swing.JFrame;
import com.pearsoneduc.ip.io.ImageDecoder;
import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.io.ImageFile;

public class Grayscale extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constant used for obtaining the strength of grey from the red level
	 */
	public static final double RED_VALUE = 0.299;
	/**
	 * Constant used for obtaining the strength of grey from the green level
	 */
	public static final double GREEN_VALUE = 0.587;
	/**
	 * Constant used for obtaining the strength of grey from the blue level
	 */
	public static final double BLUE_VALUE = 0.114;
	
	/**
	 * This method takes in a bufferedimage and creates a new greyscale bufferedImage from it
	 * @param BufferedImage image
	 * @return BufferedImage imageGrey
	 * @throws ImageDecoderException 
	 * @throws IOException 
	 */
	public BufferedImage makeGrey(String filename) throws IOException, ImageDecoderException {
		ImageDecoder input = ImageFile.createImageDecoder(filename);
		BufferedImage image = input.decodeAsBufferedImage();
		// checking width and height of image
		int width = image.getWidth();
		int height = image.getHeight();
		System.out.println(width + " width");
		System.out.println(height + " height");
		// create a grayscale BufferedImage "shell" to input values for image
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
		//write out image to file if needed
		//File output = new File("grayscale.jpg");
		//ImageIO.write(image, "jpg", output);
		return imageGrey;
	}
	public int[][] getGreyLevels(BufferedImage image) {
		// create a 2d array of grey levels - each pixel
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int[][] greyPixels = new int[imageWidth][imageHeight];
		// get the grey level for each pixel and add it to the array
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				Color c = new Color(image.getRGB(x, y));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				greyPixels[x][y] = (red + green + blue) / 3;
				// trace statement
				// System.out.printf("\t%d",greyPixels[x][y]);
			}
			// trace statement
			// System.out.println();
		}
		return greyPixels;
	}
	
	public BufferedImage createImage(int[][] greyPixels){
		int imageWidth = greyPixels.length;
		int imageHeight = greyPixels[0].length;
		BufferedImage imageBlur = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster imageWritableRaster = imageBlur.getRaster();
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				int[] colors = { greyPixels[x][y], greyPixels[x][y],
						greyPixels[x][y] };
				imageWritableRaster.setPixel(x, y, colors);
			}
		}
		return imageBlur;
	}

}
