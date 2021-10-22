package tst.pd.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import pd.util.*;
import tst.pd.helper.*;

public class TextUtilTester {
	
	@Test
	public void getDensityPlaintextsTest() {
		int plaintextLength;
		int size;
		int letterBitLength;
		Boolean[] bitsArray;
		boolean lowDensity;
		String[] plaintexts;
		Random generator = new NonRandom();
		
		plaintextLength = 3;
		size = 32;
		lowDensity = true;
		
		letterBitLength = AlphabetUtil.calculateLetterBitLength(size);
		plaintexts = TextUtil.getDensityPlaintexts(lowDensity, plaintextLength, size, generator);

		assertEquals(plaintexts.length, 7);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		testBits(plaintexts[0], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[0] = lowDensity;
		testBits(plaintexts[1], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[5] = lowDensity;
		testBits(plaintexts[2], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[10] = lowDensity;
		testBits(plaintexts[3], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[0] = lowDensity;
		bitsArray[5] = lowDensity;
		testBits(plaintexts[4], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[0] = lowDensity;
		bitsArray[10] = lowDensity;
		testBits(plaintexts[5], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[5] = lowDensity;
		bitsArray[10] = lowDensity;
		testBits(plaintexts[6], letterBitLength, bitsArray);
		
		lowDensity = true;
		plaintextLength = 2;
		size = 31;
		
		letterBitLength = AlphabetUtil.calculateLetterBitLength(size);
		plaintexts = TextUtil.getDensityPlaintexts(lowDensity, plaintextLength, size, generator);

		assertEquals(plaintexts.length, 4);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		testBits(plaintexts[0], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[0] = lowDensity;
		testBits(plaintexts[1], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[5] = lowDensity;
		testBits(plaintexts[2], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[0] = lowDensity;
		bitsArray[5] = lowDensity;
		testBits(plaintexts[3], letterBitLength, bitsArray);
		
		lowDensity = false;
		plaintextLength = 4;
		size = 8;

		letterBitLength = AlphabetUtil.calculateLetterBitLength(size);
		plaintexts = TextUtil.getDensityPlaintexts(lowDensity, plaintextLength, size, generator);

		assertEquals(plaintexts.length, 11);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		testBits(plaintexts[0], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[0] = lowDensity;
		testBits(plaintexts[1], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[3] = lowDensity;
		testBits(plaintexts[2], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[6] = lowDensity;
		testBits(plaintexts[3], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[9] = lowDensity;
		testBits(plaintexts[4], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[0] = lowDensity;
		bitsArray[3] = lowDensity;
		testBits(plaintexts[5], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[0] = lowDensity;
		bitsArray[6] = lowDensity;
		testBits(plaintexts[6], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[0] = lowDensity;
		bitsArray[9] = lowDensity;
		testBits(plaintexts[7], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[3] = lowDensity;
		bitsArray[6] = lowDensity;
		testBits(plaintexts[8], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[3] = lowDensity;
		bitsArray[9] = lowDensity;
		testBits(plaintexts[9], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[6] = lowDensity;
		bitsArray[9] = lowDensity;
		testBits(plaintexts[10], letterBitLength, bitsArray);

		lowDensity = false;
		plaintextLength = 2;
		size = 31;
		
		letterBitLength = AlphabetUtil.calculateLetterBitLength(size);
		plaintexts = TextUtil.getDensityPlaintexts(lowDensity, plaintextLength, size, generator);

		assertEquals(plaintexts.length, 4);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[4] = lowDensity;
		bitsArray[9] = lowDensity;
		testBits(plaintexts[0], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[0] = lowDensity;
		bitsArray[4] = lowDensity;
		bitsArray[9] = lowDensity;
		testBits(plaintexts[1], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[4] = lowDensity;
		bitsArray[5] = lowDensity;
		bitsArray[9] = lowDensity;
		testBits(plaintexts[2], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, !lowDensity);
		bitsArray[0] = lowDensity;
		bitsArray[4] = lowDensity;
		bitsArray[5] = lowDensity;
		bitsArray[9] = lowDensity;
		testBits(plaintexts[3], letterBitLength, bitsArray);

		lowDensity = false;
		plaintextLength = 2;
		size = 17;
		
		letterBitLength = AlphabetUtil.calculateLetterBitLength(size);
		plaintexts = TextUtil.getDensityPlaintexts(lowDensity, plaintextLength, size, generator);

		assertEquals(plaintexts.length, 4);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, lowDensity);
		bitsArray[0] = !lowDensity;
		bitsArray[5] = !lowDensity;
		testBits(plaintexts[0], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, lowDensity);
		bitsArray[5] = !lowDensity;
		testBits(plaintexts[1], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, lowDensity);
		bitsArray[0] = !lowDensity;
		testBits(plaintexts[2], letterBitLength, bitsArray);
		
		bitsArray = getMonoBooleanArray(plaintextLength * letterBitLength, lowDensity);
		testBits(plaintexts[3], letterBitLength, bitsArray);
	}

	@Test
	public void getBitsTest() {
		int letterBitLength;
		Boolean[] expectedBits;
		String text;

		char zero = 0;
		letterBitLength = 8;
				
		text = "" + zero;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, false);
		testBits(text, letterBitLength, expectedBits);

		text = "" + zero + zero + zero;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, false);
		testBits(text, letterBitLength, expectedBits);

		letterBitLength = 7;
				
		text = "" + zero;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, false);
		testBits(text, letterBitLength, expectedBits);

		text = "" + zero + zero + zero;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, false);
		testBits(text, letterBitLength, expectedBits);

		letterBitLength = 2;
				
		text = "" + zero;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, false);
		testBits(text, letterBitLength, expectedBits);

		text = "" + zero + zero + zero;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, false);
		testBits(text, letterBitLength, expectedBits);

		char twoFiveFive = 255;
		letterBitLength = 8;
		
		text = "" + twoFiveFive;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, true);
		testBits(text, letterBitLength, expectedBits);

		text = "" + twoFiveFive + twoFiveFive + twoFiveFive;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, true);
		testBits(text, letterBitLength, expectedBits);

		char sixtyThree = 63;
		letterBitLength = 6;
		
		text = "" + sixtyThree;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, true);
		testBits(text, letterBitLength, expectedBits);

		text = "" + sixtyThree + sixtyThree + sixtyThree;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, true);
		testBits(text, letterBitLength, expectedBits);
		
		letterBitLength = 8;
		
		text = "" + sixtyThree;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, true);
		expectedBits[0] = false;
		expectedBits[1] = false;
		testBits(text, letterBitLength, expectedBits);

		text = "" + sixtyThree + sixtyThree + sixtyThree;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, true);
		expectedBits[0] = false;
		expectedBits[1] = false;
		expectedBits[8] = false;
		expectedBits[9] = false;
		expectedBits[16] = false;
		expectedBits[17] = false;
		testBits(text, letterBitLength, expectedBits);
		
		char six = 6;
		letterBitLength = 4;
		
		text = "" + six;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, false);
		expectedBits[1] = true;
		expectedBits[2] = true;
		testBits(text, letterBitLength, expectedBits);
		
		text = "" + six + six + six;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, false);
		expectedBits[1] = true;
		expectedBits[2] = true;
		expectedBits[5] = true;
		expectedBits[6] = true;
		expectedBits[9] = true;
		expectedBits[10] = true;
		testBits(text, letterBitLength, expectedBits);

		letterBitLength = 8;

		text = "" + six;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, false);
		expectedBits[5] = true;
		expectedBits[6] = true;
		testBits(text, letterBitLength, expectedBits);
		
		text = "" + six + six + six;
		expectedBits = getMonoBooleanArray(text.length() * letterBitLength, false);
		expectedBits[5] = true;
		expectedBits[6] = true;
		expectedBits[13] = true;
		expectedBits[14] = true;
		expectedBits[21] = true;
		expectedBits[22] = true;
		testBits(text, letterBitLength, expectedBits);
	}
	
	private Boolean[] getMonoBooleanArray(int size, boolean fillWith) {
		Boolean[] retVal = new Boolean[size];
		for (int i = 0; i < size; i++) {
			retVal[i] = fillWith;
		}
		return retVal;
	}
	
	private boolean[] getMonoPrimitiveBooleanArray(int size, boolean fillWith) {
		boolean[] retVal = new boolean[size];
		for (int i = 0; i < size; i++) {
			retVal[i] = fillWith;
		}
		return retVal;
	}
	
	private void testBits(String input, int letterBitLength, Boolean[] expectedBits) {
		Boolean[] bits = TextUtil.getBits(input, letterBitLength);
		assertEquals(input.length() * letterBitLength, bits.length);
		assertArrayEquals(expectedBits, bits);
	}

	@Test
	public void getUniformPlaintextTest() {
		int letterBitLength;
		Boolean[] expectedBits;
		int plaintextLength;
		String plaintext;
		int size;

		plaintextLength = 1;
		letterBitLength = 8;
		size = (int) Math.pow(2, letterBitLength);
		
		plaintext = TextUtil.getUniformPlaintext(true, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, false);
		testBits(plaintext, letterBitLength, expectedBits);
		
		plaintext = TextUtil.getUniformPlaintext(false, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, true);
		testBits(plaintext, letterBitLength, expectedBits);
		
		plaintextLength = 3;
		
		plaintext = TextUtil.getUniformPlaintext(true, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, false);
		testBits(plaintext, letterBitLength, expectedBits);
		
		plaintext = TextUtil.getUniformPlaintext(false, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, true);
		testBits(plaintext, letterBitLength, expectedBits);

		plaintextLength = 1;
		letterBitLength = 5;
		size = (int) Math.pow(2, letterBitLength);
		
		plaintext = TextUtil.getUniformPlaintext(true, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, false);
		testBits(plaintext, letterBitLength, expectedBits);
		
		plaintext = TextUtil.getUniformPlaintext(false, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, true);
		testBits(plaintext, letterBitLength, expectedBits);

		plaintextLength = 3;
		
		plaintext = TextUtil.getUniformPlaintext(true, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, false);
		testBits(plaintext, letterBitLength, expectedBits);
		
		plaintext = TextUtil.getUniformPlaintext(false, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, true);
		testBits(plaintext, letterBitLength, expectedBits);

		plaintextLength = 1;
		letterBitLength = 5;
		size = 31;
		
		plaintext = TextUtil.getUniformPlaintext(true, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, false);
		testBits(plaintext, letterBitLength, expectedBits);
		
		// when size is not equal to the power of 2 then we cannot generate plaintext with all ones
		// this is not real case, we should always set size equal to the power of 2 since otherwise
		// we would not be able to test the algorithm as a random bit generator
		plaintext = TextUtil.getUniformPlaintext(false, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, true);
		expectedBits[4] = false;
		testBits(plaintext, letterBitLength, expectedBits);
		
		plaintextLength = 3;
		
		plaintext = TextUtil.getUniformPlaintext(true, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, false);
		testBits(plaintext, letterBitLength, expectedBits);
		
		// when size is not equal to the power of 2 then we cannot generate plaintext with all ones
		// this is not real case, we should always set size equal to the power of 2 since otherwise
		// we would not be able to test the algorithm as a random bit generator
		plaintext = TextUtil.getUniformPlaintext(false, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, true);
		expectedBits[4] = false;
		expectedBits[9] = false;
		expectedBits[14] = false;
		testBits(plaintext, letterBitLength, expectedBits);

		plaintextLength = 1;
		letterBitLength = 5;
		size = 29;
		
		plaintext = TextUtil.getUniformPlaintext(true, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, false);
		testBits(plaintext, letterBitLength, expectedBits);
		
		// when size is not equal to the power of 2 then we cannot generate plaintext with all ones
		// this is not real case, we should always set size equal to the power of 2 since otherwise
		// we would not be able to test the algorithm as a random bit generator
		plaintext = TextUtil.getUniformPlaintext(false, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, true);
		expectedBits[3] = false;
		expectedBits[4] = false;
		testBits(plaintext, letterBitLength, expectedBits);
		
		plaintextLength = 3;
		
		plaintext = TextUtil.getUniformPlaintext(true, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, false);
		testBits(plaintext, letterBitLength, expectedBits);
		
		// when size is not equal to the power of 2 then we cannot generate plaintext with all ones
		// this is not real case, we should always set size equal to the power of 2 since otherwise
		// we would not be able to test the algorithm as a random bit generator
		plaintext = TextUtil.getUniformPlaintext(false, plaintextLength, size);
		expectedBits = getMonoBooleanArray(plaintextLength * letterBitLength, true);
		expectedBits[3] = false;
		expectedBits[4] = false;
		expectedBits[8] = false;
		expectedBits[9] = false;
		expectedBits[13] = false;
		expectedBits[14] = false;
		testBits(plaintext, letterBitLength, expectedBits);
	}

	@Test
	public void getBitsXorDifferenceTest() {
		String test1;
		String test2;
		int letterBitLength;
		boolean[] expectedBits;

		test1 = "zasxcdfvbghnmjk";
		test2 = test1;
		letterBitLength = 8;
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, false);
		testXor(test1, test2, letterBitLength, expectedBits);

		test1 = "zasxcdfvbghnmjkqwertyuiopasdfghjklzxcvbnm";
		test2 = test1;
		letterBitLength = 8;
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, false);
		testXor(test1, test2, letterBitLength, expectedBits);

		char zero = 0;
		char one = 1;
		test1 = "" + zero;
		test2 = "" + one;
		letterBitLength = 1;
		
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, true);
		testXor(test1, test2, letterBitLength, expectedBits);
		
		test1 = "" + zero + zero + zero;
		test2 = "" + one + one + one;
		
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, true);
		testXor(test1, test2, letterBitLength, expectedBits);

		test1 = "" + zero;
		test2 = "" + one;
		letterBitLength = 3;
		
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, false);
		expectedBits[2] = true;
		testXor(test1, test2, letterBitLength, expectedBits);
		
		test1 = "" + zero + zero + zero;
		test2 = "" + one + one + one;
		
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, false);
		expectedBits[2] = true;
		expectedBits[5] = true;
		expectedBits[8] = true;
		testXor(test1, test2, letterBitLength, expectedBits);

		char seven = 7;
		test1 = "" + zero;
		test2 = "" + seven;
		
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, true);
		testXor(test1, test2, letterBitLength, expectedBits);
		
		test1 = "" + zero + zero + zero;
		test2 = "" + seven + seven + seven;
		
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, true);
		testXor(test1, test2, letterBitLength, expectedBits);

		char four = 4;
		test1 = "" + zero;
		test2 = "" + four;
		
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, false);
		expectedBits[0] = true;
		testXor(test1, test2, letterBitLength, expectedBits);
		
		test1 = "" + zero + zero + zero;
		test2 = "" + four + four + four;
		
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, false);
		expectedBits[0] = true;
		expectedBits[3] = true;
		expectedBits[6] = true;
		testXor(test1, test2, letterBitLength, expectedBits);

		char two = 2;
		test1 = "" + two;
		test2 = "" + four;
		
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, true);
		expectedBits[2] = false;
		testXor(test1, test2, letterBitLength, expectedBits);
		
		test1 = "" + two + two + two;
		test2 = "" + four + four + four;
		
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, true);
		expectedBits[2] = false;
		expectedBits[5] = false;
		expectedBits[8] = false;
		testXor(test1, test2, letterBitLength, expectedBits);

		char three = 3;
		char six = 6;
		test1 = "" + three;
		test2 = "" + six;
		
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, true);
		expectedBits[1] = false;
		testXor(test1, test2, letterBitLength, expectedBits);
		
		test1 = "" + three + three + three;
		test2 = "" + six + six + six;
		
		expectedBits = getMonoPrimitiveBooleanArray(test1.length() * letterBitLength, true);
		expectedBits[1] = false;
		expectedBits[4] = false;
		expectedBits[7] = false;
		testXor(test1, test2, letterBitLength, expectedBits);
	}

	private void testXor(String test1, String test2, int letterBitLength, boolean[] expectedBits) {
		try {
			boolean[] xorDiff = TextUtil.getBitXorDifference(test1, test2, letterBitLength);
			assertArrayEquals(xorDiff, expectedBits);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
