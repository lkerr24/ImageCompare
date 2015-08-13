package test;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import imageCompare.Convolve;
import imageCompare.Grayscale;

import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.op.ConvolutionOp;

import org.junit.Before;
import org.junit.Test;

public class ConvolveTest {

	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConvolve() throws IOException, ImageDecoderException {
		Grayscale gs = new Grayscale();
		Convolve cv = new Convolve();
		BufferedImage image = gs.makeGrey("lenaColour.jpg");
		int[][] greyPixels = gs.calculateGreyLevels(image);
		int[][] actualPixels = cv.convolve(greyPixels, 1.0f);
		BufferedImage actual = gs.createImage(actualPixels);
		BufferedImage expected = ConvolutionOp.gaussianBlur(image, 1.0f, 1);
		
		JFrame frameForActual= new JFrame();
		JLabel view1 = new JLabel(new ImageIcon(actual));
		frameForActual.add(view1);
		frameForActual.pack();
		frameForActual.setVisible(true);
		
		JFrame frameForExpected = new JFrame();
		JLabel view2 = new JLabel(new ImageIcon(expected));
		frameForExpected.add(view2);
		frameForExpected.pack();
		frameForExpected.setVisible(true);
	
	}

	@Test
	public void testCompare() {
		Convolve cv = new Convolve();
		int[][] array1 = {{1,2,3,4,5},{6,7,8,9,10},{11,12,13,14,15}};
		int[][] array2 = {{15,14,13,12,11}, {10,9,8,7,6} ,{5,4,3,2,1}};
		
		int[][] actual = cv.compare(array1, array2);
		int[][] expected = {{14,12,10,8,6},{4,2,0,-2,-4}, {-6,-8,-10,-12,-14}};
		
		assertArrayEquals(actual, expected);
		
	}

}
