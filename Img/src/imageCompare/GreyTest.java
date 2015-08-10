package imageCompare;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.pearsoneduc.ip.op.*;
import com.pearsoneduc.ip.io.ImageDecoderException;
/**
 * Trying buffered image
 * This is the class I sent to Paul to show taking in an image, converting it to grayscale and the output
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
	
		String filename1 = "IMG_0062.jpg";
		BufferedImage greyImage1 = gs.makeGrey(filename1);
		int[][] greyPixels1 = gs.getGreyLevels(greyImage1);
		int[][] blurPixels1 = cv.convolve(greyPixels1, 1.0f);
		BufferedImage blurImage1 = gs.createImage(blurPixels1);
		BufferedImage[][] imgs1 = dib.divide(blurImage1,8,8);
		
		String filename2 = "IMG_0062.jpg";
		BufferedImage greyImage2 = gs.makeGrey(filename2);
		int[][] greyPixels2 = gs.getGreyLevels(greyImage2);
		int[][] blurPixels2 = cv.convolve(greyPixels2, 2.0f);
		BufferedImage blurImage2 = gs.createImage(blurPixels2);
		BufferedImage[][] imgs2 = dib.divide(blurImage2,8,8);
		
		double aveCosineAngle = dib.aveCosAngle(imgs1, imgs2);
		System.out.println(aveCosineAngle);
		
		int[][] blurDiff = cv.compare(blurPixels1, blurPixels2);
		BufferedImage difference = gs.createImage(blurDiff);

		JFrame frameForBlur1 = new JFrame();
		JLabel view1 = new JLabel(new ImageIcon(blurImage1));
		frameForBlur1.add(view1);
		frameForBlur1.pack();
		frameForBlur1.setVisible(true);
		
		JFrame frameForBlur2 = new JFrame();
		JLabel view2 = new JLabel(new ImageIcon(blurImage2));
		frameForBlur2.add(view2);
		frameForBlur2.pack();
		frameForBlur2.setVisible(true);	
		
		JFrame frameForDiff = new JFrame();
		JLabel view3 = new JLabel(new ImageIcon(difference));
		frameForDiff.add(view3);
		frameForDiff.pack();
		frameForDiff.setVisible(true);	
		
		/*
		//Display the images in a 2D array
		int rows = imgs1.length;
		int cols = imgs1[0].length;
		JLabel[][] imgsLabel = new JLabel[rows][cols];
		JFrame frameForMinis = new JFrame();
		frameForMinis.setLayout(new GridLayout(rows, cols));
		ImageIcon[][] imgsIcons = new ImageIcon[rows][cols];
		for (int i = 0; i< rows; i++){
			for (int j = 0; j< cols; j++){
				imgsIcons[i][j] = new ImageIcon(imgs1[i][j]);
			}
		}
		for (int k = 0; k< rows; k++){
			for (int l = 0; l< cols; l++){
				imgsLabel[k][l] = new JLabel(imgsIcons[k][l]);
			}
		}
		for (int m = 0; m< rows; m++){
			for (int n = 0; n< cols; n++){
				frameForMinis.add(imgsLabel[m][n]);
			}
		}
		frameForMinis.pack();
		frameForMinis.setVisible(true);
		*/
	}
}
