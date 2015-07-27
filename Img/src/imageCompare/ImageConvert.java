package imageCompare;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;



import com.pearsoneduc.ip.op.ImageFFT;
import com.pearsoneduc.ip.op.FFTException;

import javax.imageio.ImageIO;

public class ImageConvert{
	public static void main(String [] args) throws IOException{
		BufferedImage image;
		int width = 0;
		int height = 0;
		
		//Read in image
		File file = new File("lena.jpg");
		FileInputStream fis = new FileInputStream(file);
		image = ImageIO.read(fis); 
		
		//create grayscale version of image
		//
		image = new BufferedImage(1024, 1024, BufferedImage.TYPE_BYTE_GRAY);
		//Raster imageRaster = image.getData();
		//write out grayscale image
		ImageIO.write(image, "jpg", new File("img.jpg"));
		
		//read in grayscale image
		File fileGray = new File("img.jpg");
		FileInputStream fisgray = new FileInputStream(fileGray);
		BufferedImage imagegray = ImageIO.read(fisgray);
		
		//perform fft on image
		ImageFFT fft;
		BufferedImage bf = null;
		try {
			fft = new ImageFFT(imagegray, ImageFFT.HANNING_WINDOW);
			fft.transform();
			//get spectrum of image
			bf = fft.getSpectrum();
		} catch (FFTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//write spectrum image out to file
		ImageIO.write(bf, "jpg", new File("fft.jpg"));
		/*
		 * 
		 */
	}
	/*
		try {
	         File input = new File("IMG_0062.jpg");
	         image = ImageIO.read(input);
	         width = image.getWidth();
	         height = image.getHeight();
	         
	         for(int i=0; i<height; i++){
	         
	            for(int j=0; j<width; j++){
	            
	               Color c = new Color(image.getRGB(j, i));
	               int red = (int)(c.getRed() * 0.299);
	               int green = (int)(c.getGreen() * 0.587);
	               int blue = (int)(c.getBlue() *0.114);
	               Color newColor = new Color(red+green+blue,
	               
	               red+green+blue,red+green+blue);
	               
	               image.setRGB(j,i,newColor.getRGB());
	            }
	         }
	         
	         File ouptut = new File("grayscale.jpg");
	         ImageIO.write(image, "jpg", ouptut);
	         
	      } catch (Exception e) {}
		 Byte[][] image8bit = new Byte[height][width];
		 
		 File input2 = new File("grayscale.jpg");
		 BufferedImage image2 = ImageIO.read(input2);
		 ArrayList<BufferedImage> miniImages = imageSplit(image2);
		 
        BufferedImage image3 = null;
        try {
			ImageFFT fft = new ImageFFT(image2);
			image3 = fft.getSpectrum();
		} catch (FFTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ImageIO.write(image3, "jpg",  new File("fft.jpg"));
		/*
		 * 
		 */

	public static ArrayList<BufferedImage> imageSplit(BufferedImage image) throws IOException{
		int rows = 8; //constants?
		int cols = 8;
		int chunks = rows*cols;
		
		int chunkWidth = image.getWidth()/cols;
		int chunkHeight = image.getHeight()/rows;
		
		int count = 0;
		//array to hold image chunks
		BufferedImage[] imgs = new BufferedImage[chunks];
		
		for (int x=0; x< rows; x++){
			for (int y=0;y<cols;y++){
				//initialise the image array with image chunks
				imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());
				Graphics2D gr = imgs[count++].createGraphics();
				gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x +chunkHeight, null);
				gr.dispose();
			}
		}
		System.out.println("Splitting done");
		
		//writing mini images into image files
		for (int i = 0; i<imgs.length; i++){
			try {
				ImageIO.write(imgs[i], "jpg", new File("img" + i +".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Mini images created");
		 ArrayList<BufferedImage> miniImages = new ArrayList<BufferedImage>();

			for (int i = 0; i<imgs.length; i++){
				File smallImg = new File("img"+i+".jpg");
				FileInputStream smallFis;
				BufferedImage smallBI = null;
				try {
					smallFis = new FileInputStream(smallImg);
					smallBI = ImageIO.read(smallFis);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				miniImages.add(smallBI);
				//System.out.println("img"+i+" added to array");
			}
			return miniImages;
	
	}
}