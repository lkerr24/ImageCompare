package pageAlert.pagealert;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.op.FFTException;
import com.pearsoneduc.ip.op.ImageFFT;

public class CompareImage{
	
	private String prefix = System.getProperty("user.dir")+ "/resources/sites/";
	private ArrayList<File> files; // files to search
	File image1, image2;
	double aveCosineAngle;
	int widthPixels = 8;
	int heightPixels = 8;
	
	public CompareImage(){
	}
	
	public CompareImage(String image1Path) throws IOException, ImageDecoderException, FFTException{
		image1=null;
		image2=null;
		files = new ArrayList<File>();
		ArrayList<File> imageList = new ReadDirectory(prefix+"/images/").getFilesList();
		BufferedImage[][] imgs1 = divide(image1Path, widthPixels, heightPixels);
		
		for (int i=0; i<imageList.size(); i++){
			aveCosineAngle = aveCosAngle(imgs1, imgs2);
		}
		
	}
	
	public boolean compare2(File image1Path, File image2Path){
		image1 = image1Path;
		image2 = image2Path;
		boolean compareResult = false;
		try {
			compareResult = FileUtils.contentEquals(image1, image2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return compareResult;
	}

	/**
 	 * 	 
 	 * @param BufferedImage greyImage: Image to be divided into blocks
 	 * @param int widthPixels: width of each block after division
 	 * @param int heightPixels: height of each block after division
 	 * @return BufferedImage[][]: blocks of size widthPixels*heightPixels
 	 * @throws IOException
 	 * @throws ImageDecoderException
 	 */
	public BufferedImage[][] divide(String filename, int widthPixels, int heightPixels) throws IOException,
	ImageDecoderException{
		Grayscale gs = new Grayscale();
		BufferedImage greyImage = gs.makeGrey(filename);
		 Raster rasterImage = greyImage.getData();
		 int width = rasterImage.getWidth();
		 int height = rasterImage.getHeight();
		 int rows = height/heightPixels;
		 int cols = width/widthPixels;
		 BufferedImage imgs[][] = new BufferedImage[rows][cols];
		 for (int x = 0; x < rows; x++) {  
	            for (int y = 0; y < cols; y++) {  
	                //Initialize the image array with image chunks  
	                imgs[x][y] = new BufferedImage(widthPixels, heightPixels,
	                		greyImage.getType());  
	                // draws the image chunk  
	                Graphics2D gr = imgs[x][y].createGraphics();  
	                gr.drawImage(greyImage, 0, 0, widthPixels, heightPixels,
	                		widthPixels * y, heightPixels * x, 
	                		widthPixels * y + widthPixels,
	                		heightPixels * x + heightPixels, null);  
	                gr.dispose();  
	            }  
	        } 
		 	// trace statement
	        //System.out.println("Splitting done"); 
	        return imgs;
	    }
	/**
	 * How to test?
	 * Takes in a BufferedImage chunk and creates an array of Magnitudes for each pixel
	 * @param chunk
	 * @param size
	 * @return
	 * @throws FFTException
	 */
	public double[] vectorConversion(BufferedImage chunk, int size) throws FFTException{
		ImageFFT fft = new ImageFFT(chunk);
		fft.transform();
		double[] vector = new double[size];
		int m = 0;
		for (int row = 0; row< chunk.getHeight(); row++){
			for (int col = 0; col< chunk.getWidth(); col++){
				vector[m] = fft.getMagnitude(row,col);
				m++;
			}
		}
		return vector;
	}
	
	/**
	 * Takes in 2 corresponding chunks of images and calculates the cosine angle between them
	 * @param chunk1
	 * @param chunk2
	 * @return
	 * @throws FFTException
	 */
	//for every pair of corresponding blocks... for loop looping through each pair
	public double calculateCosineAngles(double[] vector1, double[] vector2) throws FFTException{
		
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
	public double aveCosAngle(BufferedImage[][] imgs1, BufferedImage[][] imgs2) throws FFTException{
		double[] cosAngles = new double[imgs1.length*imgs1[0].length];
		int n = 0;
		double total = 0;
		for (int row = 0; row< imgs1.length; row++){
			for (int col = 0; col< imgs1[0].length; col++){
				double[] vector1 = vectorConversion(imgs1[row][col],  64);
				double[] vector2 = vectorConversion(imgs2[row][col], 64);
				cosAngles[n] = calculateCosineAngles(vector1, vector2);
				total += cosAngles[n];
				n++;
			}
		}
		return total/cosAngles.length;
	}
}
