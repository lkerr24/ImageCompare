package pageAlert.pagealert;

import java.io.IOException;
import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.op.GaussianKernel;

public class Convolve {

	public int[][] convolve(int[][] greyPixels, float sigma)
			throws IOException, ImageDecoderException {
		
		int imageWidth = greyPixels.length;
		int imageHeight = greyPixels[0].length;
		GaussianKernel gKernel = new GaussianKernel(sigma);
		// trace statement
		// gKernel.write(new OutputStreamWriter(System.out));
		float[] kernelCoeff = gKernel.getKernelData(null);
		// trace statement
		// for (int i = 0; i<kernelCoeff.length;i++){
		// System.out.printf("\n%.4f",kernelCoeff[i]);
		// }
		int kernelWidth = gKernel.getWidth();
		int kernelHeight = gKernel.getHeight();
		float[][] kernel2d = new float[kernelWidth][kernelHeight];
		int p = 0;
		for (int i = 0; i < kernelWidth; i++) {
			for (int j = 0; j < kernelHeight; j++) {
				kernel2d[i][j] = kernelCoeff[p];
				p++;
				// trace statement
				// System.out.printf("\t%.4f",kernel2d[i][j]);
			}
			// trace statement
			// System.out.println();
		}
		int m2 = kernelWidth / 2;
		// trace statement
		//System.out.println(m2);
		int n2 = kernelHeight / 2;
		// trace statement
		//System.out.println(n2);
		float sum = 0;
		for (int x = m2; x < (imageWidth - m2 - 1); x++) {
			for (int y = n2; y < (imageHeight - n2 - 1); y++) {
				for (int k = -m2; k <= m2; k++) {
					for (int j = -n2; j <= n2; j++) {
						sum += kernel2d[j + n2][k + m2]
								* greyPixels[x - j][y - k];
					}
				}
				greyPixels[x][y] = (int) sum;
				// trace statement
				// System.out.printf("\t%d",greyPixels[x][y]);
				sum = 0;
			}
			// trace statement
			// System.out.println();
		}
		return greyPixels;
	}
	public int[][] compare(int[][] greyPixels1, int[][] greyPixels2){
		
		int [][] difference = new int[greyPixels1.length][greyPixels1[0].length];
		
		for (int i = 0; i<greyPixels1.length; i++){
			for (int j = 0; j<greyPixels1[0].length; j++){
				//2 - 1 or 1 -2?
				//gaussian wiki
				difference[i][j] = greyPixels1[i][j] - greyPixels2[i][j];
				// trace statement
				//System.out.printf("%d\t", difference[i][j]);
			}
			//trace statement
			//System.out.println();
		}	
		return difference;
	}
	

}
