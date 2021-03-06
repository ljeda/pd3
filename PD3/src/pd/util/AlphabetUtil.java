package pd.util;

import java.util.LinkedHashSet;

import pd.core.*;

public class AlphabetUtil {

	public static final Alphabet generateAlphabet(int size) throws Exception {
		if (size < 1) {
			throw new Exception("Alfabet musi się składać z co najmniej jednej litery.");
		}
		if (size > 256) {
			throw new Exception("Generowany alfabet może mieć co najwyżej 256 liter.");
		}
		LinkedHashSet<Character> letterSet = new LinkedHashSet<Character>();
		for (int i = 0; i < size; i++) {
			letterSet.add((char) i);
		}
		return new Alphabet(letterSet);
	}

	public static final void printAlphabet(Alphabet alphabet) {
		Character[] letters = alphabet.getLetters();
		for (int i = 0; i < letters.length; i++) {
			System.out.println("Litera nr " + i + "\t: " + letters[i]);
		}
	}

	public static int calculateLetterBitLength(int size) {
		return (int) Math.floor(Math.log(size - 1) / Math.log(2)) + 1;
	}
}
