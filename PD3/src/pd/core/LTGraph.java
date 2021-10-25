package pd.core;

import java.util.*;

/**
 * @author Łukasz Jęda
 * 
 */
public class LTGraph {

	LTGraph(Alphabet alphabet, Map<CharacterArray, Character> qMap, Map<CharacterArray, Character> yMap, Map<CharacterArray, Character> zMap) {
		_alphabet = alphabet;
		_threeMixes = new ThreeMixes(_alphabet, qMap, yMap, zMap);
	}

	LTGraph(Alphabet alphabet) {
		_alphabet = alphabet;
		_threeMixes = new ThreeMixes(_alphabet);
	}

	public Character N(Character state, Character letter, boolean pchoice) {
		if (pchoice) {
			return _threeMixes.getRevertQMix(state, _alphabet.getNextLetter(letter));
		} else {
			return _threeMixes.getRevertQMix(state, letter);
		}
	}

	public Character L(Character state, Character letter, boolean sChoice, boolean pChoice) {
		if (sChoice) {
			return _threeMixes.getZMix(state, N(state, letter, pChoice));
		} else {
			return _threeMixes.getYMix(state, N(state, letter, pChoice));
		}
	}

	public Character L(Character state, Character letter, boolean sChoice) {
		if (sChoice) {
			return _threeMixes.getZMix(state, letter);
		} else {
			return _threeMixes.getYMix(state, letter);
		}
	}

	public Character P(Character state, Character letter, boolean sChoice) {
		if (sChoice) {
			return _threeMixes.getRevertZMix(state, letter);
		} else {
			return _threeMixes.getRevertYMix(state, letter);
		}
	}

	public Character D(Character state, Character letter, boolean pChoice) {
		if (pChoice) {
			return _alphabet.getPreviousLetter(_threeMixes.getQMix(state, letter));
		} else {
			return _threeMixes.getQMix(state, letter);
		}
	}

	protected Map<CharacterArray, Character> getQMixLetterMap() {
		return _threeMixes.getQLetterMap();
	}

	protected Map<CharacterArray, Character> getYMixLetterMap() {
		return _threeMixes.getYLetterMap();
	}

	protected Map<CharacterArray, Character> getZMixLetterMap() {
		return _threeMixes.getZLetterMap();
	}

	private ThreeMixes _threeMixes;
	private Alphabet _alphabet;
}
