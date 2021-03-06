package tst.pd.core;

import static org.junit.Assert.*;

import java.util.LinkedHashSet;

import org.junit.*;

import pd.core.*;

public class AlphabetTester {
	
	@Test
	public void equalsTest() {
		Character[] letters1 = new Character[] {'a', 'b', 'c'};
		Character[] letters2 = new Character[] {'a', 'b', 'c'};
		LinkedHashSet<Character> letterSet1 = new LinkedHashSet<Character>();
		LinkedHashSet<Character> letterSet2 = new LinkedHashSet<Character>();
		LinkedHashSet<Character> letterSet3 = new LinkedHashSet<Character>();
		for (Character letter : letters1) {
			letterSet1.add(letter);
			letterSet2.add(letter);
		}
		for (Character letter : letters2) {
			letterSet3.add(letter);
		}
		Alphabet alphabet1 = new Alphabet(letterSet1);
		Alphabet alphabet2 = new Alphabet(letterSet1);
		Alphabet alphabet3 = new Alphabet(letterSet2);
		Alphabet alphabet4 = new Alphabet(letterSet3);

		assertEquals(alphabet1, alphabet1);
		assertEquals(alphabet1, alphabet2);
		assertEquals(alphabet1, alphabet3);
		assertEquals(alphabet1, alphabet4);
		assertEquals(alphabet2, alphabet3);
		assertEquals(alphabet2, alphabet4);
		assertEquals(alphabet3, alphabet4);
		assertEquals(alphabet4, alphabet3);
		assertEquals(alphabet4, alphabet2);
		assertEquals(alphabet4, alphabet1);
		assertEquals(alphabet3, alphabet2);
		assertEquals(alphabet3, alphabet1);
		assertEquals(alphabet2, alphabet1);
	}
	
	@Test
	public void notEqualsTest() {
		Character[] letters1 = new Character[] {'a', 'b', 'c'};
		Character[] letters2 = new Character[] {'a', 'c', 'b'};
		LinkedHashSet<Character> letterSet1 = new LinkedHashSet<Character>();
		LinkedHashSet<Character> letterSet2 = new LinkedHashSet<Character>();
		LinkedHashSet<Character> letterSet3 = new LinkedHashSet<Character>();
		for (Character letter : letters1) {
			letterSet1.add(letter);
			letterSet2.add(letter);
		}
		letterSet2.add('d');
		for (Character letter : letters2) {
			letterSet3.add(letter);
		}
		Alphabet alphabet1 = new Alphabet(letterSet1);
		letterSet1.add('e');
		Alphabet alphabet2 = new Alphabet(letterSet1);
		Alphabet alphabet3 = new Alphabet(letterSet2);
		Alphabet alphabet4 = new Alphabet(letterSet3);

		assertNotEquals(alphabet1, null);
		assertNotEquals(alphabet1, alphabet2);
		assertNotEquals(alphabet1, alphabet3);
		assertNotEquals(alphabet1, alphabet4);
		assertNotEquals(alphabet2, alphabet3);
		assertNotEquals(alphabet2, alphabet4);
		assertNotEquals(alphabet3, alphabet4);
		assertNotEquals(alphabet4, "");
		assertNotEquals("", alphabet4);
		assertNotEquals(alphabet4, alphabet3);
		assertNotEquals(alphabet4, alphabet2);
		assertNotEquals(alphabet4, alphabet1);
		assertNotEquals(alphabet3, alphabet2);
		assertNotEquals(alphabet3, alphabet1);
		assertNotEquals(alphabet2, alphabet1);
		assertNotEquals(null, alphabet1);
	}
	
	@Test
	public void defaultConstructorTest() {
		int alphabetSize = 223;
		
		Alphabet alphabet = new Alphabet();
		
		Character[] letters = alphabet.getLetters();
		
		testAlphabet(alphabet, letters, alphabetSize);
	}
	
	@Test
	public void customConstructorTest() {
		Character[] letters = new Character[] {'a', 'b', 'c'};
		LinkedHashSet<Character> letterSet = new LinkedHashSet<Character>();
		for (Character letter : letters) {
			letterSet.add(letter);
		}
		int alphabetSize = letters.length;
		
		Alphabet alphabet = new Alphabet(letterSet);
		
		testAlphabet(alphabet, letters, alphabetSize);
	}
	
	@Test
	public void nonUniqueValuesConstructorTest() {
		Character[] letters = new Character[] {'a', 'b', 'c'};
		LinkedHashSet<Character> letterSet = new LinkedHashSet<Character>();
		for (Character letter : letters) {
			letterSet.add(letter);
		}
		letterSet.add('a');
		int alphabetSize = letters.length;
		
		Alphabet alphabet = new Alphabet(letterSet);
		
		testAlphabet(alphabet, letters, alphabetSize);
	}

	private void testAlphabet(Alphabet alphabet, Character[] letters, int alphabetSize) {
		assertEquals(alphabet.getSize(), alphabetSize);
		for (int i = 0; i < alphabetSize; i++) {
			testLetter(alphabet, i, letters[i]);
		}

		for (int i = 0; i < alphabetSize - 1; i++) {
			testNextPreviousLetter(alphabet, letters[i], letters[i + 1]);
		}
		testNextPreviousLetter(alphabet, letters[alphabetSize - 1], letters[0]);
	}

	private void testNextPreviousLetter(Alphabet alphabet, Character letter1, Character letter2) {
		assertEquals(alphabet.getNextLetter(letter1), letter2);
		assertEquals(alphabet.getPreviousLetter(letter2), letter1);
	}

	private void testLetter(Alphabet alphabet, int i, Character letter) {
		assertTrue(alphabet.hasLetter(letter));
		assertTrue(alphabet.hasLetter(alphabet.getLetter(i)));
		assertEquals(alphabet.getLetter(i), letter);
	}

}
