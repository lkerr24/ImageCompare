package imageCompare;

import java.awt.image.BufferedImage;

import com.pearsoneduc.ip.op.*;
/**
 * Replica of GaussianFilter class from Efford book
 * @author laura
 *
 */
public class GaussianFilter1 extends StandardKernel {

	public GaussianFilter1() {
		this(1.0f);
	}
	public GaussianFilter1(float sigma) {
		super(getSize(sigma), getSize(sigma), createKernalData(sigma));
	}

	private static int getSize(float sigma) {
		int radius = (int) Math.ceil(4.0f*sigma);
		return 2*radius+1;
	}
	private static float[] createKernalData(float sigma) {
		int n = (int) Math.ceil(4.0f*sigma);
		int size = 2*n+1;
		float[] data = new float[size*size];
		
		double r, s = 2.0*sigma*sigma;
		float norm = 0.0f;
		int i = 0;
		for (int y = -n; y<=n; ++y){
			for (int x = -n; x<=n; ++x, ++i){
				r = Math.sqrt(x*x + y*y);
				data[i] = (float) Math.exp(-r*r/s);
				norm += data[i];
			}
		}
		for (i = 0; i< size*size; ++i){
			data[i]/=norm;
		}
		return null;
	}
	public static void GaussianFilter(){
		 
		
		
		
		
	}

}
