package pd.util;

import java.util.*;

public class TextUtil {

	public static int[][] getDensityKeysChangePositions(int keysLength) {
		int[][] changePositions = new int[1 + keysLength + keysLength * (keysLength - 1) / 2][];
		changePositions[0] = new int[0];
		for (int i = 0; i < keysLength; i++) {
			changePositions[i + 1] = new int[] { i };
		}
		int position = keysLength + 1;
		for (int j = 0; j < keysLength; j++) {
			for (int k = j + 1; k < keysLength; k++) {
				changePositions[position] = new int[] { j, k };
				position++;
			}
		}
		return changePositions;
	}

	public static String[] getDensityPlaintexts(boolean lowDensity, int plaintextLength, int size, Random generator) {
		generator = ensureGenerator(generator);
		int letterBitLength = AlphabetUtil.calculateLetterBitLength(size);
		String[] plaintexts = new String[1 + plaintextLength + plaintextLength * (plaintextLength - 1) / 2];
		plaintexts[0] = getUniformPlaintext(lowDensity, plaintextLength, size);
		for (int i = 0; i < plaintextLength; i++) {
			plaintexts[1 + i] = perturbPlaintext(plaintexts[0], i, letterBitLength, generator);
		}
		int position = plaintextLength + 1;
		for (int j = 0; j < plaintextLength; j++) {
			for (int k = j + 1; k < plaintextLength; k++) {
				plaintexts[position] = perturbPlaintext(perturbPlaintext(plaintexts[0], j, letterBitLength, generator), k, letterBitLength, generator);
				position++;
			}
		}
		return plaintexts;
	}

	public static String getUniformPlaintext(boolean fillWithZeroes, int plaintextLength, int size) {
		char fillWith = fillWithZeroes ? (char) 0 : (char) (size - 1);
		String plaintext = "";
		for (int i = 0; i < plaintextLength; i++) {
			plaintext = plaintext + fillWith;
		}
		return plaintext;
	}

	public static String perturbPlaintext(String plaintext, int positionToPerturb, int letterBitLength, Random generator) {
		generator = ensureGenerator(generator);
		Boolean[] letterBits = getBits(new Character(plaintext.charAt(positionToPerturb)).toString(), letterBitLength);
		int bitToChange = generator.nextInt(letterBitLength);
		letterBits[bitToChange] = !letterBits[bitToChange];
		int charValue = 0, currentBitValue = 1;
		for (int i = letterBitLength - 1; i > -1; i--) {
			if (letterBits[i]) {
				charValue += currentBitValue;
			}
			currentBitValue *= 2;
		}
		char changedLetter = (char) charValue;
		return plaintext.substring(0, positionToPerturb) + changedLetter + plaintext.substring(positionToPerturb + 1);
	}

	public static Boolean[] getBits(String text, int letterBitLength) {
		List<Boolean> textBits = new LinkedList<Boolean>();
		int highestBitValue = getHighestBitValue(letterBitLength);
		for (char c : text.toCharArray()) {
			for (int i = 0; i < letterBitLength; i++) {
				if ((c & highestBitValue) == 0) {
					textBits.add(false);
				} else {
					textBits.add(true);
				}
				c <<= 1;
			}
		}
		return textBits.toArray(new Boolean[0]);
	}

	public static int getHighestBitValue(int letterBitLength) {
		return (int) Math.pow(2, letterBitLength - 1);
	}

	public static boolean[] getBitXorDifference(String firstText, String secondText, int letterBitLength) throws Exception {
		if (firstText == null || secondText == null) {
			throw new Exception("Nie Poda??e?? poprawnie tekst??w do operacji r????nicy symetrycznej.");
		}
		if (firstText.length() != secondText.length()) {
			throw new Exception("d??ugo??ci tekst??w do operacji r????nicy symetrycznej nie s?? " + "takie same.");
		}
		Boolean[] firstTextBits = getBits(firstText, letterBitLength), secondTextBits = getBits(secondText, letterBitLength);
		boolean[] difference = new boolean[firstTextBits.length];
		for (int i = 0; i < firstTextBits.length; i++) {
			difference[i] = firstTextBits[i] ^ secondTextBits[i];
		}
		return difference;
	}

	public static final String generateRandomPlaintext(Random generator, int size, int plaintextLength) {
		generator = ensureGenerator(generator);
		char[] p = new char[plaintextLength];
		for (int i = 0; i < plaintextLength; i++) {
			p[i] = (char) generator.nextInt(size);
		}
		return new String(p);
	}

	private static final Random ensureGenerator(Random generator) {
		if (generator == null) {
			System.out.println("U??ywam domy??lnego generatora liczb pseudolosowych w narz??dziach tektstowych.");
			return _defaultGenerator;
		}
		return generator;
	}

	private static final Random _defaultGenerator = RandomUtil.getRandom();

}
