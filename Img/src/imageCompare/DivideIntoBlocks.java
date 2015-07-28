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
import com.pearsoneduc.ip.io.*;
import com.pearsoneduc.ip.io.ImageDecoder;
import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.io.ImageFile;
import com.pearsoneduc.ip.op.FFTException;
import com.pearsoneduc.ip.op.*;
import com.pearsoneduc.ip.op.ImageFFT;

public class DivideIntoBlocks extends JFrame{
	
	static int width;
	static int height;
	static int rows;
	static int cols;
 		 
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
	public static double[] VectorConversion(BufferedImage chunk) throws FFTException{
		ImageFFT fft = new ImageFFT(chunk);
		double[] vector = new double[64];
		int i = 0;
		for (int row = 0; row< chunk.getHeight(); row++){
			for (int col = 0; col< chunk.getWidth(); col++){
				vector[i] = fft.getMagnitude(row,col);
				i++;
			}
		}
		return vector;
	}
	public static void Array2DChunkToVector(BufferedImage[][] imgs){
		for (int row = 0; row< imgs.length; row++){
			for (int col = 0; col< imgs[0].length; col++){
				VectorConversion(imgs[row][col]);
			}
	}
	
} 

