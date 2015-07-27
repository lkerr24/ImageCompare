package imageCompare;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.awt.*;

import com.pearsoneduc.ip.op.ImageFFT;
import com.pearsoneduc.ip.op.FFTException;


public class ImageSplit {
	public static void main(String [] args) throws IOException{
		File file = new File("grayscale.jpg");
		FileInputStream fis = new FileInputStream(file);
		BufferedImage image = ImageIO.read(fis); //reading the image file
		
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
			ImageIO.write(imgs[i], "jpg", new File("img" + i +".jpg"));
		}
		System.out.println("Mini images created");
		//adding mini images to an array
		ArrayList<BufferedImage> miniImages = new ArrayList<BufferedImage>();

		for (int i = 0; i<imgs.length; i++){
			File smallImg = new File("img"+i+".jpg");
			FileInputStream smallFis = new FileInputStream(smallImg);
			BufferedImage smallBI = ImageIO.read(smallFis);
			miniImages.add(smallBI);
			//System.out.println("img"+i+" added to array");
		}
		int j = 0;
		for (BufferedImage imgs2: miniImages){
			try {
				BufferedImage imgspec = computeSpectrum(imgs2);
				ImageIO.write(imgspec,"jpg", new File("gray"+j+".jpg"));
				j++;
			} catch (FFTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*listing 8.2 from efford
	public static BufferedImage createSinusoid() throws FFTException{
		BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_BYTE_GRAY);
		ImageFFT fft = new ImageFFT(image);
		fft.transform();
		final float mag = (float) 1.0e6;
		fft.setMagnitude(20, 8, mag);
		fft.setMagnitude(108, 120, mag);
		fft.transform();
		fft.toImage(image, 128);
		return image;
	}
	/*
	 * 
	 */
	//listing 8.1 from Efford
	public static BufferedImage computeSpectrum(BufferedImage image) throws FFTException{
		ImageFFT fft = new ImageFFT(image, ImageFFT.HANNING_WINDOW);
		fft.transform();
		return fft.getSpectrum();	
	}
	/*
	 * 
	 */
}

