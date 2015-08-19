package imageCompare;

import java.io.IOException;
import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.op.GaussianKernel;

public class Convolve {

	public int[][] convolve(int[][] greyPixels, float sigma)
			throws IOException, ImageDecoderException {
		
		int imageWidth = greyPixels.length;
		int imageHeight = greyPixels[0].length;

		float[] kernelCoeff = GaussianKernel.createKernelData(sigma);
		//float[] kernelCoeff = {1/9,1/9,1/9, 1/9,1/9,1/9, 1/9,1/9,1/9};
		int length = kernelCoeff.length;
		int width = (int) Math.sqrt(length);
		int height = width;
		float[][] kernel2d = new float[width][height];
		int p = 0;
		float total = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				kernel2d[i][j] = kernelCoeff[p];
				p++;
				total+=kernel2d[i][j];
				// trace statement
				// System.out.printf("\t%.4f",kernel2d[i][j]);
			}
			// trace statement
			// System.out.println();
		}
		System.out.printf("\n%.10f",total);
		int m2 = width / 2;
		// trace statement
		//System.out.println(m2);
		int n2 = height / 2;
		// trace statement
		//System.out.println(n2);
		float sum = 0;
		for (int x = m2; x < (imageWidth - m2 - 1); x++) {
			for (int y = n2; y < (imageHeight - n2 - 1); y++) {
				for (int k = -m2; k <= m2; k++) {
					for (int j = -n2; j <= n2; j++) {
						sum += kernel2d[j + n2][k + m2]* greyPixels[x - j][y - k];
					}
				}
				greyPixels[x][y] = (int)sum;
				// trace statement
				// System.out.printf("\t%d",greyPixels[x][y]);
				sum = 0;
			}
			// trace statement
			// System.out.println();
		}
		return greyPixels;
	}
	public int[][] compare(int[][] blurPixels1, int[][] blurPixels2){
		
		int[][] difference = new int[blurPixels1.length][blurPixels1[0].length];
		
		for (int i = 0; i<blurPixels1.length; i++){
			for (int j = 0; j<blurPixels1[0].length; j++){
				difference[i][j] = blurPixels1[i][j] - blurPixels2[i][j];
				// trace statement
				//System.out.printf("%d\t", difference[i][j]);
			}
			//trace statement
			//System.out.println();
		}	
		return difference;
	}
	

}
