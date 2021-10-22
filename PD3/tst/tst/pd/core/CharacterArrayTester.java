package tst.pd.core;

import static org.junit.Assert.*;

import org.junit.Test;

import pd.core.CharacterArray;

public class CharacterArrayTester {
	Character state = 'a';
	Character letter = 'b';
	
	CharacterArray characterArray = new CharacterArray(state, letter);
	
	@Test
	public void constructorTest() {
		assertEquals(characterArray.getState(), state);
		assertEquals(characterArray.getLetter(), letter);

		assertEquals(characterArray.getState(), new Character(state));
		assertEquals(characterArray.getLetter(), new Character(letter));
	}

	@Test
	public void equalsTest() {
		Character a = 'a';
		Character b = 'b';
		Character c = 'c';
		Character d = 'd';

		CharacterArray clone = new CharacterArray(state, letter);
		CharacterArray equal = new CharacterArray(a, b);
		CharacterArray reverted = new CharacterArray(letter, state);
		CharacterArray ba = new CharacterArray(b, a);
		CharacterArray wrongState = new CharacterArray(c, letter);
		CharacterArray wrongLetter = new CharacterArray(state, c);
		CharacterArray wrongAll = new CharacterArray(c, d);

		assertEquals(characterArray, characterArray);
		assertEquals(characterArray, clone);
		assertEquals(clone, characterArray);
		assertEquals(clone, equal);
		assertEquals(equal, clone);
		assertEquals(ba, reverted);
		assertEquals(reverted, ba);
		assertNotEquals(characterArray, reverted);
		assertNotEquals(reverted, characterArray);
		assertNotEquals(characterArray, ba);
		assertNotEquals(ba, characterArray);
		assertNotEquals(characterArray, wrongState);
		assertNotEquals(wrongState, characterArray);
		assertNotEquals(characterArray, wrongLetter);
		assertNotEquals(wrongLetter, characterArray);
		assertNotEquals(characterArray, wrongAll);
		assertNotEquals(wrongAll, characterArray);
	}
	
}
