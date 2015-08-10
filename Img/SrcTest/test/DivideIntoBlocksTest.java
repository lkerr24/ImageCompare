package test;

import static org.junit.Assert.*;
import imageCompare.DivideIntoBlocks;

import org.junit.Before;
import org.junit.Test;

import com.pearsoneduc.ip.op.FFTException;

public class DivideIntoBlocksTest {
	
	@Before
	public void setUp(){
		

		
		
	}
	/**What part of this should be tested?
	 * This method creates a 2d array of small blocks of the image
	 * Could test that when the width and height of each block are input,
	 * the correct number of blocks are created 
	 */
	@Test
	public void testDivide() {
		fail("Not yet implemented");
	}
	/**
	 * This can be tested by taking in 2 random arrays
	 */
	@Test
	public void testVectorConversion() {
		fail("Not yet implemented");
	}
	@Test
	public void calculateCosineAnglesSame() throws FFTException{
		DivideIntoBlocks dib = new DivideIntoBlocks();
		double[] vectorSame1 = {1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0};
		double[] vectorSame2 = {1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0};
		double actual = dib.calculateCosineAngles(vectorSame1, vectorSame2);
		double expected = 1.00;
		
		assertEquals(actual, expected, 0.5);
	}
	@Test
	public void calculateCosineAnglesOrth() throws FFTException{
		DivideIntoBlocks dib = new DivideIntoBlocks();
		double[] vectorOrth1 = {};
		double[] vectorOrth2 = {};
		double actual = dib.calculateCosineAngles(vectorOrth1, vectorOrth2);
		double expected = 0;
		
		assertEquals(actual, expected, 0.5);
	}
	@Test
	public void calculateCosineAnglesDiff() throws FFTException{
		DivideIntoBlocks dib = new DivideIntoBlocks();
		double[] vectorDiff1 = {6.93, 7.29, 1.20, 9.34, 6.76, 0.87, 5.44, 6.82};
		double[] vectorDiff2 = {9.83, 9.04, 3.34, 5.56, 8.84, 9.13, 7.45, 3.98};
		double actual = dib.calculateCosineAngles(vectorDiff1, vectorDiff2);
		double innerProduct = 0;
		for (int j=0; j<vectorDiff1.length;j++){
			innerProduct += (vectorDiff1[j]*vectorDiff2[j]);
		}
		double total1= 0;
		// square each of the values
		for (int i = 0; i<vectorDiff1.length;i++){
			total1 += (vectorDiff1[i]*vectorDiff1[i]);
		}
		double total2= 0;
		for (int i = 0; i<vectorDiff1.length;i++){
			total2 += (vectorDiff2[i]*vectorDiff2[i]);
		}
		//find the square root, which is the magnitude
		double mathMagnitude1 = Math.sqrt(total1);
		double mathMagnitude2 = Math.sqrt(total2);
		//find the cosine angle
		double expected = innerProduct/(mathMagnitude1*mathMagnitude2);
		
		assertEquals(actual, expected, 0.5);
	}
	
	/**
	 * 
	 */
	@Test
	public void testAveCosAngle() {
		double[] values = {6.93, 7.29, 1.20, 9.34, 6.76, 0.87, 5.44, 6.82};
		double expected = 5.58125;
		double total =0;
		for (int i = 0; i<values.length;i++){
			total+=values[i];
		}
		double actual = total/values.length;
		
		assertEquals(actual, expected, 0.5);
	}
}
