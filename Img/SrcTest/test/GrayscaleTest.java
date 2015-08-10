/**
 * 
 */
package test;

import static org.junit.Assert.*;
import imageCompare.Grayscale;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.pearsoneduc.ip.io.ImageDecoder;
import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.io.ImageFile;

/**
 * @author laura
 *
 */
public class GrayscaleTest {
	
	Grayscale gs = new Grayscale();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		
	}

	@Test
	public void makeGreyTest() {
		fail("Not yet implemented");
	}
	@Test
	public void getGreyLevelsForBlackImageTest() throws IOException, ImageDecoderException {
		// maybe take in a black image and compare it that way?
		ImageDecoder input = ImageFile.createImageDecoder("lena.jpg");
		BufferedImage image = input.decodeAsBufferedImage();
		WritableRaster blackImage = image.getRaster();
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int[] colours = {0, 0, 0};
				blackImage.setPixel(i,j,colours);
			}
		}
		int[][] actual = gs.getGreyLevels(image);
		int[][] expected = new int[image.getWidth()][image.getHeight()];
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				expected[i][j] = 0;
			}
		}
		assertSame(actual, expected);
	}
	@Test
	public void getGreyLevelsForWhiteImageTest() throws IOException, ImageDecoderException {
		// maybe take in a white image and compare it that way?
		ImageDecoder input = ImageFile.createImageDecoder("lena.jpg");
		BufferedImage image = input.decodeAsBufferedImage();
		WritableRaster blackImage = image.getRaster();
		
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int[] colours = {225, 225, 225};
				blackImage.setPixel(i,j,colours);
			}
		}
		int[][] actual = gs.getGreyLevels(image);
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				System.out.printf("%d\t", actual[i][j]);
			}
			System.out.println();
		}
		int[][] expected = new int[image.getWidth()][image.getHeight()];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				expected[i][j] = 255;
			}
		}
		assertEquals(actual, expected);
	}
	@Test
	public void createImageTest() {
		fail("Not yet implemented");
	}

}
