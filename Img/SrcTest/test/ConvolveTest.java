package test;

import static org.junit.Assert.*;
import imageCompare.Convolve;

import org.junit.Before;
import org.junit.Test;

public class ConvolveTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConvolve() {
		fail("Not yet implemented");
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
