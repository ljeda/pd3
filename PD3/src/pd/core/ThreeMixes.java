package pd.core;

import java.util.*;

/**
 * @author Łukasz Jęda
 * 
 */
public class ThreeMixes {

	ThreeMixes(Alphabet alphabet, Map<CharacterArray, Character> qMap, Map<CharacterArray, Character> yMap, Map<CharacterArray, Character> zMap) {
		try {
			_qMix = new Mix(alphabet, qMap);
		} catch (Exception e) {
			System.err.println(e.getMessage() + " Używam domyślnych ustawień miksa Q.");
			generateQMix(alphabet);
		}
		try {
			_yMix = new Mix(alphabet, yMap);
			_zMix = new Mix(alphabet, zMap);
		} catch (Exception e) {
			System.err.println(e.getMessage() + " Używam domyślnych ustawień miksów Y i Z.");
			generateYZMixes(alphabet);
		}
		int alphabetSize = alphabet.getSize();
		boolean generateDefaultYZMixes = false, generateDefaultQMix = false;
		for (int i = 0; i < alphabetSize; i++) {
			for (int j = 0; i < alphabetSize; i++) {
				Character state = alphabet.getLetter(i), letter = alphabet.getLetter(j);
				if (_yMix.getMix(state, letter).equals(_zMix.getMix(state, letter)) && !generateDefaultYZMixes) {
					System.err.println("Miksy Y i Z nie spełniają założeń. Używam domyślnych " + "ustawień miksów Y i Z.");
					generateDefaultYZMixes = true;
				}
				for (int k = i + 1; k < alphabetSize; k++) {
					if (_qMix.getMix(state, letter).equals(_qMix.getMix(alphabet.getLetter(k), letter)) && !generateDefaultQMix) {
						System.err.println("Miks Q nie spełnia założeń. Używam domyślnych " + "ustawień miksa Q.");
						generateDefaultQMix = true;
					}
				}
			}
		}
		if (generateDefaultYZMixes) {
			generateYZMixes(alphabet);
		}
		if (generateDefaultQMix) {
			generateQMix(alphabet);
		}
	}

	ThreeMixes(Alphabet alphabet) {
		generateQMix(alphabet);
		generateYZMixes(alphabet);
	}

	private void generateQMix(Alphabet alphabet) {
		_qMix = new Mix(alphabet, 1);
	}

	private void generateYZMixes(Alphabet alphabet) {
		_yMix = new Mix(alphabet, 2);
		_zMix = new Mix(alphabet, 3);
	}

	public Character getQMix(Character state, Character letter) {
		return _qMix.getMix(state, letter);
	}

	public Character getYMix(Character state, Character letter) {
		return _yMix.getMix(state, letter);
	}

	public Character getZMix(Character state, Character letter) {
		return _zMix.getMix(state, letter);
	}

	public Character getRevertQMix(Character state, Character letter) {
		return _qMix.getRevertMix(state, letter);
	}

	public Character getRevertYMix(Character state, Character letter) {
		return _yMix.getRevertMix(state, letter);
	}

	public Character getRevertZMix(Character state, Character letter) {
		return _zMix.getRevertMix(state, letter);
	}

	protected Map<CharacterArray, Character> getQLetterMap() {
		return _qMix.getLetterMapCopy();
	}

	protected Map<CharacterArray, Character> getYLetterMap() {
		return _yMix.getLetterMapCopy();
	}

	protected Map<CharacterArray, Character> getZLetterMap() {
		return _zMix.getLetterMapCopy();
	}

	private Mix _qMix;
	private Mix _yMix;
	private Mix _zMix;
}
