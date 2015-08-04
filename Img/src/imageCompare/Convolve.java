package imageCompare;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.op.GaussianKernel;

public class Convolve {

	public static int[][] getGreyLevels(BufferedImage image) {
		// create a 2d array of grey levels - each pxiel
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int[][] greyPixels = new int[imageWidth][imageHeight];
		// get the grey level for each pixel and add it to the array
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				Color c = new Color(image.getRGB(x, y));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				greyPixels[x][y] = (red + green + blue) / 3;
				// System.out.printf("\t%d",greyPixels[x][y]);
			}
			// System.out.println();
		}
		return greyPixels;
	}

	public static BufferedImage convolve(int[][] greyPixels, float sigma)
			throws IOException, ImageDecoderException {
		
		int imageWidth = greyPixels.length;
		int imageHeight = greyPixels[0].length;
		GaussianKernel gKernel = new GaussianKernel(sigma);
		// gKernel.write(new OutputStreamWriter(System.out));
		float[] kernelCoeff = gKernel.getKernelData(null);
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
				// System.out.printf("\t%.4f",kernel2d[i][j]);
			}
			// System.out.println();
		}
		int m2 = kernelWidth / 2;
		//System.out.println(m2);
		int n2 = kernelHeight / 2;
		//System.out.println(n2);
		float sum = 0;
		for (int y = m2; y < (imageWidth - m2 - 1); y++) {
			for (int x = n2; x < (imageHeight - n2 - 1); x++) {
				for (int k = -m2; k <= m2; k++) {
					for (int j = -n2; j <= n2; j++) {
						sum += kernel2d[j + n2][k + m2]
								* greyPixels[y - j][x - k];
					}
				}
				greyPixels[y][x] = (int) sum;
				// System.out.printf("\t%d",greyPixels[y][x]);
				sum = 0;
			}
			// System.out.println();
		}
		BufferedImage imageBlur = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster imageWritableRaster = imageBlur.getRaster();
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				int[] colors = { greyPixels[x][y], greyPixels[x][y],
						greyPixels[x][y] };
				imageWritableRaster.setPixel(x, y, colors);
			}
		}

		return imageBlur;
	}

	public static void main(String[] args) throws IOException,
			ImageDecoderException {
		BufferedImage image1 = Grayscale.makeGrey("lena.jpg");
		BufferedImage image2 = Grayscale.makeGrey("lena.jpg");
		BufferedImage blur1 = convolve(getGreyLevels(image1), 1.0f);
		BufferedImage blur2 = convolve(getGreyLevels(image2), 2.0f);

		JFrame frameForBlur1 = new JFrame();
		JLabel view1 = new JLabel(new ImageIcon(blur1));
		frameForBlur1.add(view1);
		frameForBlur1.pack();
		frameForBlur1.setVisible(true);
		
		JFrame frameForBlur2 = new JFrame();
		JLabel view2 = new JLabel(new ImageIcon(blur2));
		frameForBlur2.add(view2);
		frameForBlur2.pack();
		frameForBlur2.setVisible(true);
		
		int[][] greyLevels1 = getGreyLevels(blur1);
		int[][] greyLevels2 = getGreyLevels(blur2);
		
		int [][] difference = new int[greyLevels1.length][greyLevels1[0].length];
		
		for (int i = 0; i<greyLevels1.length; i++){
			for (int j = 0; j<greyLevels1[0].length; j++){
				//2 - 1 or 1 -2?
				difference[i][j] = greyLevels2[i][j] - greyLevels1[i][j];
				System.out.printf("%d\t", difference[i][j]);
			}
			System.out.println();
		}	
	}
}
