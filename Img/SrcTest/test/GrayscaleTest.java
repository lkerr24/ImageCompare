/**
 * 
 */
package test;

import static org.junit.Assert.*;
import imageCompare.Grayscale;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
	 * Checks that every pixel in the image is grayscale
	 * @throws IOException
	 * @throws ImageDecoderException
	 */
	@Test
	public void makeGreyTest() throws IOException, ImageDecoderException {
		BufferedImage greyTest = gs.makeGrey("IMG_0062.jpg");
		assertNotNull(greyTest);
		// for each pixel, if it is not a grey value, the test fails
		for (int i = 0; i < greyTest.getWidth(); i++){
			for(int j = 0; j < greyTest.getHeight(); j++){
				Color c = new Color(greyTest.getRGB(i,j));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				int grey = (red+green+blue)/3;
				if (red!=grey || green!= grey || blue!=grey){
					fail("Not all pixels greyscale");
				}
			}
		}
	}
	/**
	 * Test to check that the grey levels are calculated correctly
	 * for a greyscale image
	 * @throws IOException
	 * @throws ImageDecoderException
	 */
	@Test
	public void calculateGreyLevelsGreyscaleImage() throws IOException, ImageDecoderException {
		BufferedImage greyTest = gs.makeGrey("lena.jpg");
		for (int i = 0; i < greyTest.getWidth(); i++){
			for(int j = 0; j < greyTest.getHeight(); j++){
				Color c = new Color(greyTest.getRGB(i,j));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				int grey = (red+green+blue)/3;
				//System.out.println(grey);
				if (red!=grey || green!= grey || blue!=grey){
					fail("Not all pixels greyscale");
				}
			}
		}
	}
	/**
	 * 
	 * 
	 * @throws IOException
	 * @throws ImageDecoderException
	 */
	@Test
	public void calculateGreyLevelsForRGBImage() throws IOException, ImageDecoderException {
		String filename = "lenaColour.jpg";
		ImageDecoder input = ImageFile.createImageDecoder(filename);
		BufferedImage imageRGB = input.decodeAsBufferedImage();
		BufferedImage imageGrey = new BufferedImage(imageRGB.getWidth(),imageRGB.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
		imageGrey = gs.makeGrey(filename);
		System.out.println(imageRGB.getRGB(0,0));
		System.out.println(imageGrey.getRGB(0,0));
		for (int i = 0; i < imageRGB.getWidth(); i++){
			for(int j = 0; j < imageRGB.getHeight(); j++){
				Color c1 = new Color(imageRGB.getRGB(i,j));
				int red1 = c1.getRed();
				int green1 = c1.getGreen();
				int blue1 = c1.getBlue();
				int grey1 = (red1+green1+blue1)/3;
				System.out.println(grey1);
				
				Color c2 = new Color(imageGrey.getRGB(i,j));
				int red2 = c2.getRed();
				int green2 = c2.getGreen();
				int blue2 = c2.getBlue();
				int grey2 = (red2+green2+blue2)/3;
				System.out.println(grey2);
				
				if (grey1!=grey2) {
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
		int[][] actual = gs.calculateGreyLevels(image);
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
		int[][] actual = gs.calculateGreyLevels(image);
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				//System.out.printf("%d\t", actual[i][j]);
			}
			//System.out.println();
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
	public void createImageTest() throws IOException, ImageDecoderException {
		ImageDecoder input = ImageFile.createImageDecoder("lena.jpg");
		BufferedImage image = input.decodeAsBufferedImage();
		int[][] greyPixels = gs.calculateGreyLevels(image);
		BufferedImage image2 = gs.createImage(greyPixels);
		assertNotNull(image2);
	}
}
