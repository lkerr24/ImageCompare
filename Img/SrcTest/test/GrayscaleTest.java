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
	/**
	 * not right
	 * @throws IOException
	 * @throws ImageDecoderException
	 */
	@Test
	public void makeGreyTest() throws IOException, ImageDecoderException {
		BufferedImage greyTest = gs.makeGrey("IMG_0062.jpg");
		// for each pixel, if it is not a grey value, the test fails
		for (int i = 0; i < greyTest.getWidth(); i++){
			for(int j = 0; j < greyTest.getHeight(); j++){
				Color c = new Color(greyTest.getRGB(i,j));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				int grey = (red+green+blue)/3;
				if (grey <=0 || grey >= 255){
					fail("Not all pixels greyscale");
				}
			}
		}
	}
	/**
	 * not right
	 * @throws IOException
	 * @throws ImageDecoderException
	 */
	@Test
	public void makeGreyTestInvalidImage() throws IOException, ImageDecoderException {
		ImageDecoder input = ImageFile.createImageDecoder("IMG_0062.jpg");
		BufferedImage greyTest = input.decodeAsBufferedImage();
		for (int i = 0; i < greyTest.getWidth(); i++){
			for(int j = 0; j < greyTest.getHeight(); j++){
				Color c = new Color(greyTest.getRGB(i,j));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				int grey = (red+green+blue)/3;
				System.out.println(grey);
				if (grey <=0 || grey >= 255){
					fail("Not all pixels greyscale");
				}
			}
		}
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
		assertArrayEquals(actual, expected);
	}
	@Test
	public void getGreyLevelsForWhiteImageTest() throws IOException, ImageDecoderException {
		// maybe take in a white image and compare it that way?
		ImageDecoder input = ImageFile.createImageDecoder("lena.jpg");
		BufferedImage image = input.decodeAsBufferedImage();
		WritableRaster blackImage = image.getRaster();
		
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int[] colours = {255, 255, 255};
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
		assertArrayEquals(actual, expected);
	}
	@Test
	public void createImageTest() {
		fail("Not yet implemented");
	}

}
