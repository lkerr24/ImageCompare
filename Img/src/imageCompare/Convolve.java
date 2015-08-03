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

	public Convolve() {
		
	}

	public static void main(String[] args) throws IOException, ImageDecoderException {
		GaussianKernel gKernel = new GaussianKernel(1.0f);
		//gKernel.write(new OutputStreamWriter(System.out));
		String filename = "lena.jpg";
		BufferedImage image = Grayscale.makeGrey(filename);
		//create a 2d array of grey levels - each pxiel
		int[][] greyPixels = new int[image.getWidth()][image.getHeight()];
		//get the grey level for each pixel and add it to the array
		for (int x=0; x<image.getWidth();x++){
			for (int y=0; y<image.getHeight();y++){
				Color c = new Color(image.getRGB(x, y));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				greyPixels[x][y] = (red+green+blue)/3; 
				//System.out.printf("\t%d",greyPixels[x][y]);
			}
			//System.out.println();
		}
		
		//  
		float[] kernelCoeff = gKernel.getKernelData(null);
		//for (int i = 0; i<kernelCoeff.length;i++){
		//	System.out.printf("\n%.4f",kernelCoeff[i]);
		//}
		int width = gKernel.getWidth();
		int height = gKernel.getHeight();
		float[][] kernel2d = new float[width][height];
		int p = 0;
		for (int i = 0; i<width; i++){
			for (int j = 0; j< height;j++){
				kernel2d[i][j] = kernelCoeff[p];
				p++;
				//System.out.printf("\t%.4f",kernel2d[i][j]);
			}
			//System.out.println();
		}
		int m2 = width/2;
		int n2 = height/2;
		float sum = 0;
		for (int y=m2; y<greyPixels.length-m2-1; y++){
			for (int x=n2; x<greyPixels[0].length-n2-1;x++){
				for (int k=-m2; k<=m2; k++){
					for (int j=-n2; j<=n2;j++){
						sum+=kernel2d[j+n2][k+m2]*greyPixels[x-j][y-k];
						}
				}
				greyPixels[x][y]=(int) sum;
				System.out.printf("\t%d",greyPixels[x][y]);
				sum=0;
			}
			System.out.println();
		}
		BufferedImage imageBlur = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster imageWritableRaster = imageBlur.getRaster();
		for (int x = 0; x<greyPixels.length;x++){
			for (int y =0; y<greyPixels[0].length;y++){
				int[] colors = {greyPixels[x][y], greyPixels[x][y], greyPixels[x][y]};
				imageWritableRaster.setPixel(x,y,colors);
			}
		}
		JFrame frameForBlur = new JFrame();
		//frameForBlur.setLayout(new GridLayout(imageBlur.getWidth(), imageBlur.getHeight()));
		JLabel view = new JLabel(new ImageIcon(imageBlur));
		frameForBlur.add(view);
		frameForBlur.pack();
		frameForBlur.setVisible(true);
		//File output = new File("C:\\Users\\laura\\Desktop\\blur.jpg");
		//ImageIO.write(imageBlur, "jpg", output);

		/* PSEUDO CODE FOR CONVOLUTION AT A SINGLE PIXEL
		 * Create an array h, indexed from 0 to m-1 horizontally and 0 to n-1 vertically
		 * Fill h with kernel coefficients
		 * m2 = [m/2]
		 * n2 = [n/2]
		 * sum = 0
		 * for k = -n2 to n2 do
		 * 	for j = -m2 to m2 do
		 * 		sum = sum+h(j +m2, k+n2)f(x-j,y-k)
		 * 	end for
		 * end for
		 * g(x,y) = sum
		 *  
		 */
	}

}
