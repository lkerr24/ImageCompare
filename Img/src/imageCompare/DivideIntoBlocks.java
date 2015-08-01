package imageCompare;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.op.FFTException;
import com.pearsoneduc.ip.op.ImageFFT;

public class DivideIntoBlocks extends JFrame{
	
	static int width;
	static int height;
	static int rows;
	static int cols;
 	/**
 	 * 	 
 	 * @param greyImage
 	 * @return
 	 * @throws IOException
 	 * @throws ImageDecoderException
 	 */
	public static BufferedImage[][] divide(BufferedImage greyImage) throws IOException,
	ImageDecoderException{
		 Raster rasterImage = greyImage.getData();
		 width = rasterImage.getWidth();
		 height = rasterImage.getHeight();
		 rows = height/8;
		 cols = width/8;
		 int chunkWidth = 8; // determines the chunk width and height  
	     int chunkHeight = 8; 
		 BufferedImage imgs[][] = new BufferedImage[rows][cols];
		 for (int x = 0; x < rows; x++) {  
	            for (int y = 0; y < cols; y++) {  
	                //Initialize the image array with image chunks  
	                imgs[x][y] = new BufferedImage(chunkWidth, chunkHeight,
	                		greyImage.getType());  
	                // draws the image chunk  
	                Graphics2D gr = imgs[x][y].createGraphics();  
	                gr.drawImage(greyImage, 0, 0, chunkWidth, chunkHeight,
	                		chunkWidth * y, chunkHeight * x, 
	                		chunkWidth * y + chunkWidth,
	                		chunkHeight * x + chunkHeight, null);  
	                gr.dispose();  
	            }  
	        }  
	        System.out.println("Splitting done"); 
	        return imgs;
	    }
	/**
	 * Takes in 2 corresponding chunks of images and calculates the cosine angle between them
	 * @param chunk1
	 * @param chunk2
	 * @return
	 * @throws FFTException
	 */
	//for every pair of corresponding blocks... for loop looping through each pair
	public static double VectorConversion(BufferedImage chunk1, BufferedImage chunk2) throws FFTException{
		ImageFFT fft1 = new ImageFFT(chunk1);
		fft1.transform();
		ImageFFT fft2 = new ImageFFT(chunk2);
		fft2.transform();
		double[] vector1 = new double[64];
		double[] vector2 = new double[64];
		int m = 0;
		for (int row = 0; row< chunk1.getHeight(); row++){
			for (int col = 0; col< chunk1.getWidth(); col++){
				vector1[m] = fft1.getMagnitude(row,col);
				vector2[m] = fft2.getMagnitude(row, col);
				m++;
			}
		}
		double innerProduct = 0;
		for (int j=0; j<vector1.length;j++){
			innerProduct += (vector1[j]*vector2[j]);
		}
		double total1= 0;
		// square each of the values
		for (int i = 0; i<vector1.length;i++){
			total1 += (vector1[i]*vector1[i]);
		}
		double total2= 0;
		for (int i = 0; i<vector1.length;i++){
			total2 += (vector2[i]*vector2[i]);
		}
		//find the square root, which is the magnitude
		double mathMagnitude1 = Math.sqrt(total1);
		double mathMagnitude2 = Math.sqrt(total2);
		//find the cosine angle
		double cosineAngle = innerProduct/(mathMagnitude1*mathMagnitude2);
		return cosineAngle;
		
	}

	/**
	 * Takes in the bufferedimage 2d array of chunks, for both images
	 * calls the vector conversion method
	 * returns average cosine angle between the images
	 * @param imgs1
	 * @param imgs2
	 * @return
	 * @throws FFTException
	 */
	public static double aveCosAngle(BufferedImage[][] imgs1, BufferedImage[][] imgs2) throws FFTException{
		double[] cosAngles = new double[imgs1.length*imgs1[0].length];
		int n = 0;
		double total = 0;
		for (int row = 0; row< imgs1.length; row++){
			for (int col = 0; col< imgs1[0].length; col++){
				cosAngles[n] = VectorConversion(imgs1[row][col], imgs2[row][col]);
				total += cosAngles[n];
				n++;
			}
		}
		return total/cosAngles.length;
	}	
} 

