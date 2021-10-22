package pd.core;

import java.util.*;

/**
 * @author £ukasz Jêda
 * 
 */
public class Mix {

	Mix(Alphabet alphabet, Map<CharacterArray, Character> letterMap) throws Exception {
		_alphabet = alphabet;
		if (letterMap == null) {
			throw new Exception("Nie zdefiniowano miksa.");
		}
		Set<CharacterArray> keySet = letterMap.keySet();
		int alphabetSize = alphabet.getSize();
		LinkedList<Character> letters = new LinkedList<Character>();
		Set<CharacterArray> bijectValuesSet = new HashSet<CharacterArray>();
		for (int i = 0; i < alphabetSize; i++) {
			Character letter = alphabet.getLetter(i);
			letters.add(letter);
			for (int j = 0; j < alphabetSize; j++) {
				bijectValuesSet.add(new CharacterArray(letter, alphabet.getLetter(j)));
			}
		}
		for (CharacterArray key : keySet) {
			Character value = letterMap.get(key);
			if (!letters.contains(key.getState()) || !letters.contains(key.getLetter()) || !letters.contains(value)) {
				throw new Exception("Miks zawiera litery spoza alfabetu.");
			}
			bijectValuesSet.remove(new CharacterArray(key.getState(), value));
		}
		if (!bijectValuesSet.isEmpty()) {
			CharacterArray notBiject = bijectValuesSet.iterator().next();
			throw new Exception("Miks dla stanu '" + notBiject.getState() + "' nie jest bijekcj¹.");
		}
		_letterMap = letterMap;
		fillRevertLetterMap();
	}

	Mix(Alphabet alphabet, int startingPosition) {
		_alphabet = alphabet;
		generateLetterMap(_alphabet, startingPosition);
		fillRevertLetterMap();
	}

	private void generateLetterMap(Alphabet alphabet, int startingPosition) {
		_letterMap = new HashMap<CharacterArray, Character>(alphabet.getSize());
		int alphabetSize = alphabet.getSize();
		for (int i = 0; i < alphabetSize; i++) {
			int currentValue = (startingPosition + i) % alphabetSize;
			Character firstKeyPart = alphabet.getLetter(i);
			for (int j = 0; j < alphabetSize; j++) {
				_letterMap.put(new CharacterArray(firstKeyPart, alphabet.getLetter(j)), alphabet.getLetter(currentValue));
				currentValue = (currentValue + 1) % alphabetSize;
			}
		}
	}

	private void fillRevertLetterMap() {
		_revertLetterMap = new HashMap<CharacterArray, Character>(_letterMap.size());
		for (CharacterArray c : _letterMap.keySet()) {
			if (c.getState() == null || _letterMap.get(c) == null || c.getLetter() == null) {
				System.out.println("ps");
			}
			_revertLetterMap.put(new CharacterArray(c.getState(), _letterMap.get(c)), c.getLetter());
		}
	}

	public Character getMix(Character state, Character letter) {
		return _letterMap.get(new CharacterArray(state, letter));
	}

	public Character getRevertMix(Character state, Character letter) {
		return _revertLetterMap.get(new CharacterArray(state, letter));
		// if ((int)state.charValue() == 124 && (int)letter.charValue() == 8364)
		// {
		// System.out.println("egeeg");
		// }
		// if (_revertLetterMap.get(new CharacterArray(state, letter))==null) {
		// System.out.println(_revertLetterMap.containsKey(new
		// CharacterArray(state, letter)));
		// System.out.println((int)state.charValue());
		// System.out.println((int)letter.charValue());
		// System.out.println("uops");
		// }
		// for (Character c : _alphabet.getLetters()) {
		// if (letter.equals(_letterMap.get(new CharacterArray(state, c)))) {
		// System.out.println((int)state.charValue());
		// System.out.println((int)letter.charValue());
		// System.out.println((int)c.charValue());
		// System.out.println(_revertLetterMap.containsValue(c));
		// for (Character c1 : _alphabet.getLetters()) {
		// if (c.equals(_revertLetterMap.get(new CharacterArray(state, c1)))) {
		// System.out.println((int)state.charValue());
		// System.out.println((int)c1.charValue());
		// System.out.println((int)c.charValue());
		// }
		// }
		// return c;
		// }
		// }
		// return null;
	}

	protected Map<CharacterArray, Character> getLetterMapCopy() {
		return new HashMap<CharacterArray, Character>(_letterMap);
	}

	private Alphabet _alphabet;
	private Map<CharacterArray, Character> _letterMap;
	private Map<CharacterArray, Character> _revertLetterMap;
}