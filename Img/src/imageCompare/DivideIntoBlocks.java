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

import com.pearsoneduc.ip.io.ImageDecoder;
import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.io.ImageFile;

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
	
	} 

