package imageCompare;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.pearsoneduc.ip.op.*;
import com.pearsoneduc.ip.io.ImageDecoderException;
/**
 * Main method for testing purposes
 * @author laura
 */
public class GreyTest extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws IOException, ImageDecoderException, FFTException{ 
		Grayscale gs = new Grayscale();
		Convolve cv = new Convolve();
		DivideIntoBlocks dib = new DivideIntoBlocks();
		
		// Creating greyscale image for the cosine angle calculation
		String filename1 = "lenaColour.jpg";
		BufferedImage greyImage1 = gs.makeGrey(filename1);
		// Creating 2D array of grey levels for convolution
		int[][] greyPixels1 = gs.calculateGreyLevels(greyImage1);
		int[][] blurPixels1 = cv.convolve(greyPixels1, 1.0f);
		BufferedImage blurImage1 = gs.createImage(blurPixels1);
		// Creating 2D array of grey levels for convolution
		int[][] greyPixels2 = gs.calculateGreyLevels(greyImage1);
		int[][] blurPixels2 = cv.convolve(greyPixels2, 2.0f);
		BufferedImage blurImage2 = gs.createImage(blurPixels2);
		// Perform the convolution
		int[][] blurDiff1 = cv.compare(blurPixels1, blurPixels2);
		BufferedImage difference1 = gs.createImage(blurDiff1);
		BufferedImage[][] diffs1 = dib.divide(difference1, 8, 8);
		
		// Creating greyscale image for the cosine angle calculation
		String filename2 = "lenaColour.jpg";
		BufferedImage greyImage2 = gs.makeGrey(filename2);
		// Creating 2D array of grey levels for convolution
		int[][] greyPixels3 = gs.calculateGreyLevels(greyImage2);
		int[][] blurPixels3 = cv.convolve(greyPixels3, 1.0f);
		BufferedImage blurImage3 = gs.createImage(blurPixels1);
		// Creating 2D array of grey levels for convolution
		int[][] greyPixels4 = gs.calculateGreyLevels(greyImage2);
		int[][] blurPixels4 = cv.convolve(greyPixels4, 2.0f);
		BufferedImage blurImage4 = gs.createImage(blurPixels2);
		// Perform the convolution
		int[][] blurDiff2 = cv.compare(blurPixels3, blurPixels4);
		BufferedImage difference2 = gs.createImage(blurDiff2);
		BufferedImage[][] diffs2 = dib.divide(difference2, 8, 8);
		
		//Calculate the average cosine angle for the 2 grey images
		double aveCosineAngle = dib.aveCosAngle(diffs1,diffs2);
		System.out.printf("%.10f",aveCosineAngle);
		
		// Display the first grey image
		JFrame frameForGrey1 = new JFrame();
		JLabel viewGrey1 = new JLabel(new ImageIcon(greyImage1));
		frameForGrey1.add(viewGrey1);
		frameForGrey1.pack();
		frameForGrey1.setVisible(true);
		
		// Display the second grey image
		JFrame frameForGrey2 = new JFrame();
		JLabel viewGrey2 = new JLabel(new ImageIcon(greyImage2));
		frameForGrey2.add(viewGrey2);
		frameForGrey2.pack();
		frameForGrey2.setVisible(true);

		// Display the first blurred image
		JFrame frameForBlur1 = new JFrame();
		JLabel view1 = new JLabel(new ImageIcon(blurImage1));
		frameForBlur1.add(view1);
		frameForBlur1.pack();
		frameForBlur1.setVisible(true);
		
		// Display the second blurred image
		JFrame frameForBlur2 = new JFrame();
		JLabel view2 = new JLabel(new ImageIcon(blurImage2));
		frameForBlur2.add(view2);
		frameForBlur2.pack();
		frameForBlur2.setVisible(true);	
		
		// Display the third blurred image
		JFrame frameForBlur3 = new JFrame();
		JLabel view3 = new JLabel(new ImageIcon(blurImage3));
		frameForBlur3.add(view3);
		frameForBlur3.pack();
		frameForBlur3.setVisible(true);
		
		// Display the fourth blurred image
		JFrame frameForBlur4 = new JFrame();
		JLabel view4 = new JLabel(new ImageIcon(blurImage4));
		frameForBlur4.add(view4);
		frameForBlur4.pack();
		frameForBlur4.setVisible(true);	
		
		// Display the difference in the two blurred images
		JFrame frameForDiff1 = new JFrame();
		JLabel viewDiff1 = new JLabel(new ImageIcon(difference1));
		frameForDiff1.add(viewDiff1);
		frameForDiff1.pack();
		frameForDiff1.setVisible(true);	
		
		// Display the difference in the two blurred images
		JFrame frameForDiff2 = new JFrame();
		JLabel viewDiff2 = new JLabel(new ImageIcon(difference2));
		frameForDiff2.add(viewDiff2);
		frameForDiff2.pack();
		frameForDiff2.setVisible(true);
	
	}
}
