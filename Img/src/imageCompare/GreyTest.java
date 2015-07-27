package imageCompare;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.pearsoneduc.ip.op.*;
import com.pearsoneduc.ip.io.ImageDecoder;
import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.io.ImageFile;
import com.pearsoneduc.ip.op.ImageFFT;
/**
 * Trying buffered image
 * This is the class I sent to Paul to show taking in an image, converting it to grayscale and the output
 * @author laura
 */
public class GreyTest extends JFrame{

	public static void main(String[] args) throws IOException, ImageDecoderException, FFTException{
		/* 
		//Test for Grayscale class
		String filename = "IMG_0062.jpg";
		JFrame frame = new Grayscale(filename);
		frame.pack();
		frame.setVisible(true);
		*/ 
		// Array of blocks
		/*
		String filename1 = "IMG_0062.jpg";
		BufferedImage greyImage1 = Grayscale.makeGrey(filename1);
		BufferedImage[][] imgs1 = DivideIntoBlocks.divide(greyImage1);
		
		String filename2 = "lena.jpg";
		BufferedImage greyImage2 = Grayscale.makeGrey(filename2);
		BufferedImage[][] imgs2 = DivideIntoBlocks.divide(greyImage2);
		*/
		//Display the images in a 2D array
		/*
		int rows = imgs.length;
		int cols = imgs[0].length;
		JLabel[][] imgsLabel = new JLabel[rows][cols];
		JFrame frameForMinis = new JFrame();
		frameForMinis.setLayout(new GridLayout(rows, cols));
		ImageIcon[][] imgsIcons = new ImageIcon[rows][cols];
		for (int i = 0; i< rows; i++){
			for (int j = 0; j< cols; j++){
				imgsIcons[i][j] = new ImageIcon(imgs[i][j]);
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
		
		
		
		String filename1 = "lena.jpg";
		BufferedImage greyImage1 = Grayscale.makeGrey(filename1);
		BufferedImage[][] imgs1 = DivideIntoBlocks.divide(greyImage1);
		
		String filename2 = "lena.jpg";
		BufferedImage greyImage2 = Grayscale.makeGrey(filename2);
		BufferedImage[][] imgs2 = DivideIntoBlocks.divide(greyImage2);
		
		double[] mags1 = FFT.computeMagnitude(imgs1);
		double[] mags2 = FFT.computeMagnitude(imgs2);
		
		if (mags1.length>mags2.length){
			double[] mags1copy = new double[mags2.length];
			for (int i=0; i< mags1copy.length;i++){
				mags1copy[i] = mags1[i];
			}
			FFT.calculateCosineAngle(mags1copy, mags2);
		} else if (mags1.length<mags2.length){
			double[] mags2copy = new double[mags1.length];
			for (int i=0; i< mags2copy.length;i++){
				mags2copy[i] = mags2[i];
			}
			FFT.calculateCosineAngle(mags1, mags2copy);
		} else {
			FFT.calculateCosineAngle(mags1, mags2);
		}
		/*
		 * validation. only comparing the same number of pixels 
		if (mags1.length>=mags2.length && mags1[0].length>=mags2[0].length){
			FFT.computeCosineAngle(mags1,mags2,mags2.length, mags2[0].length);
		} else if (mags1.length>=mags2.length && mags1[0].length<=mags2[0].length){
			FFT.computeCosineAngle(mags1,mags2,mags2.length, mags1[0].length);
		} else if (mags1.length<=mags2.length && mags1[0].length>=mags2[0].length){
			FFT.computeCosineAngle(mags1,mags2,mags1.length, mags2[0].length);
		} else {
			FFT.computeCosineAngle(mags1,mags2,mags1.length, mags1[0].length);
		}
		*/
		
		
	}
}
