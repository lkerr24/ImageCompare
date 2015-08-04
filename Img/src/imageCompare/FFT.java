package imageCompare;

import java.awt.image.BufferedImage;

import com.pearsoneduc.ip.op.FFTException;
import com.pearsoneduc.ip.op.ImageFFT;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class FFT {

	public FFT() {
	}
	
	public static double[] computeMagnitude(BufferedImage[][] imgs) throws FFTException{
		
		//This next block of code is to compute the FFT of the images and print out the magnitude
		double[] ffts = new double[imgs.length*imgs[0].length]; 
		int m = 0;
		for (int i = 0; i< imgs.length;i++){
			for (int j=0; j<imgs[0].length;j++){
				ImageFFT fft = new ImageFFT(imgs[i][j], ImageFFT.HANNING_WINDOW);
				fft.transform();
				ffts[m] = fft.getMagnitude(i,j);
				m++;
				//System.out.println(ffts[i][j]);
			}
		}/*
		for (int i = 0; i< imgs.length; i++){
			for (int j=0; j<imgs[0].length;j++){
					System.out.printf("%.6f \t", ffts[i][j]);
			}
			System.out.println();
		}
		*/
		
		return ffts;	
	}
	/*
	public static void computeCosineAngle(double[] mags1, double[] mags2){
		PearsonsCorrelation PC = new PearsonsCorrelation();
		System.out.println(PC.correlation(mags1, mags2));
	}
	*/
	/**
	 * takes in arrays of magnitudes for the whole image
	 * @param mags1
	 * @param mags2
	 * @return
	 */
	public static double calculateCosineAngle(double[] mags1, double[] mags2){
		// calculate inner product for the 2 vectors
		double innerProduct = 0;
		for (int i = 0; i< mags1.length; i++){
			innerProduct += (mags1[i]*mags2[i]);
		}
		double total1= 0;
		// square each of the values
		for (int i = 0; i<mags1.length;i++){
			total1 += (mags1[i]*mags1[i]);
		}
		double total2= 0;
		for (int i = 0; i<mags1.length;i++){
			total2 += (mags2[i]*mags2[i]);
		}
		//find the square root, which is the magnitude
		double magnitude1 = Math.sqrt(total1);
		double magnitude2 = Math.sqrt(total2);
		//find the cosine angle
		double cosineAngle = innerProduct/(magnitude1*magnitude2);
		System.out.println(cosineAngle);
		return cosineAngle;
	}
	
	/*
	public static void computeCosineAngle(float[][] mags1, float[][] mags2, int width, int height){
		int m = 0;
		float total = 0;
		float[] cosineAngle = new float[width*height];
		for (int i= 0; i< width; i++){
			for (int j = 0; j< height; j++){
				cosineAngle[m] = (mags1[i][j]*mags2[i][j])/(mags1[i][j]+mags2[i][j]);
				System.out.printf("%.6f \t",cosineAngle[m]);
				total+=cosineAngle[0];
				m++;
			}
			System.out.println();
		}
		float aveCosAngle = total/cosineAngle.length;
		System.out.println(cosineAngle.length);
		System.out.println(aveCosAngle);
	}
	*/
	

}
