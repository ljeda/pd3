package pd.core;

import java.nio.*;
import java.nio.charset.*;
import java.util.*;

/**
 * @author Łukasz Jęda
 * 
 */
public class Alphabet {

	public Alphabet(LinkedHashSet<Character> letters) {
		if (letters != null) {
			_letters = letters.toArray(new Character[] {});
//			for (int i = 0; i < letters.size(); i++) {
//				_letterMap.put(i, _letters[i]);
//			}
			for (int i = 0; i < _letters.length - 1; i++) {
				_nextLetterMap.put(_letters[i], _letters[i + 1]);
				_previousLetterMap.put(_letters[i + 1], _letters[i]);
			}
			_nextLetterMap.put(_letters[_letters.length - 1], _letters[0]);
			_previousLetterMap.put(_letters[0], _letters[_letters.length - 1]);
		}
	}

	public Alphabet() {
		Set<Character> letterSet = new LinkedHashSet<Character>();
		final Charset charset = Charset.forName("CP1250");
		for (int i = Character.MIN_VALUE; i < Character.MAX_VALUE; i++) {
			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.putInt(i);
			String trimmedString = new String(bb.array(), charset).trim();
			if (!trimmedString.isEmpty()) {
				letterSet.add(trimmedString.charAt(0));
			}
		}
		letterSet.add(' ');
		letterSet.add('\n');
		letterSet.add('\r');
		letterSet.add('\t');

		Character[] letterArray = letterSet.toArray(new Character[0]);
//		for (int i = 0; i < letterArray.length; i++) {
//			_letterMap.put(i, letterArray[i]);
//		}
		_letters = letterArray;
		for (int i = 0; i < letterArray.length - 1; i++) {
			_nextLetterMap.put(letterArray[i], letterArray[i + 1]);
			_previousLetterMap.put(letterArray[i + 1], letterArray[i]);
		}
		_nextLetterMap.put(letterArray[letterArray.length - 1], letterArray[0]);
		_previousLetterMap.put(letterArray[0], letterArray[letterArray.length - 1]);
	}

	public Character getLetter(int i) {
		if (i < 0 || i > getSize()) {
			System.err.println("Brak litery o indeksie " + i + " w alfabecie.");
			return null;
		}
		return _letters[i];
//		Character c = _letterMap.get(i);
//		if (c == null) {
//			System.err.println("Brak litery o indeksie " + i + " w alfabecie.");
//		}
//		return c;
	}

	public Character getNextLetter(Character letter) {
		return _nextLetterMap.get(letter);
		// for (int i = 0; i < _letterMap.size(); i++) {
		// if (_letterMap.get(i).equals(letter)) {
		// return _letterMap.get((i + 1) % _letterMap.size());
		// }
		// }
		// return null;
	}

	public Character getPreviousLetter(Character letter) {
		return _previousLetterMap.get(letter);
		// for (int i = 0; i < _letterMap.size(); i++) {
		// if (_letterMap.get(i).equals(letter)) {
		// if (i == 0) {
		// return _letterMap.get(_letterMap.size() - 1);
		// } else {
		// return _letterMap.get(i - 1);
		// }
		// }
		// }
		// return null;
	}

	public Character[] getLetters() {
//		return _letterMap.values();
		return _letters.clone();
	}

	public int getSize() {
//		return _letterMap.size();
		return _letters.length;
	}

	public boolean hasLetter(char letter) {
//		return _letterMap.containsValue(letter);
		for (int i = 0; i < getSize(); i++) {
			if (_letters[i].equals(letter)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Alphabet)) {
			return false;
		}
		Alphabet alphabet = (Alphabet) obj;
		if (this.getSize() != alphabet.getSize()) {
			return false;
		}
		boolean equal = true;
		for (int i = 0; i < this.getSize(); i++) {
			if (this.getLetter(i) != alphabet.getLetter(i)) {
				equal = false;
				break;
			}
		}
		return equal;
	}

	private Character[] _letters = null;
//	private HashMap<Integer, Character> _letterMap = new HashMap<Integer, Character>();
	private HashMap<Character, Character> _nextLetterMap = new HashMap<Character, Character>();
	private HashMap<Character, Character> _previousLetterMap = new HashMap<Character, Character>();
}
