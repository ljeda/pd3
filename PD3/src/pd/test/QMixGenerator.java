package pd.test;

import java.util.*;

import pd.core.*;
import pd.util.*;

/**
 * @author Łukasz Jęda
 * 
 */
public class QMixGenerator {

	QMixGenerator(Alphabet alphabet, Random generator) {
		_n = alphabet.getSize();
		_alphabet = alphabet;
		_generator = generator;

		// TODO: blobal generator
		_generator = RandomUtil.getRandom();

	}

	Map<CharacterArray, Character> generateMix() {
		Map<CharacterArray, Character> mix = new HashMap<CharacterArray, Character>();
		@SuppressWarnings("unchecked")
		List<Character>[] qAvailableLetters = (LinkedList<Character>[]) new LinkedList[_n];
		for (int p = 0; p < _n; p++) {
			qAvailableLetters[p] = new LinkedList<Character>();
			for (Character letter : _alphabet.getLetters()) {
				qAvailableLetters[p].add(letter);
			}
		}
		for (int i = 0; i < _n; i++) {
			Character[] nextPermutation = null;
			while (nextPermutation == null) {
				nextPermutation = generatePermutation(qAvailableLetters);
				if (nextPermutation == null) {
					continue;
				}
				for (int s = 0; s < i; s++) {
					for (int t = 0; t < _n; t++) {
						if (mix.get(new CharacterArray(_alphabet.getLetter(s), _alphabet.getLetter(t))).equals(nextPermutation[t])) {
							nextPermutation = null;
							s = i;
							break;
						}
					}
				}
			}
			for (int j = 0; j < _n; j++) {
				mix.put(new CharacterArray(_alphabet.getLetter(i), _alphabet.getLetter(j)), nextPermutation[j]);
			}
		}
		return mix;
	}

	private Character[] generatePermutation(List<Character>[] qAvailableLetters) {
		List<Character> notUsedLetters = new LinkedList<Character>();
		for (Character letter : _alphabet.getLetters()) {
			notUsedLetters.add(letter);
		}
		LinkedList<Character> permutation = new LinkedList<Character>();
		for (int i = 0; i < _n; i++) {
			List<Character> currentList = new LinkedList<Character>();
			currentList.addAll(notUsedLetters);
			currentList.retainAll(qAvailableLetters[i]);
			if (currentList.size() == 0) {
				return null;
			}
			Character chosenLetter = currentList.get(_generator.nextInt(currentList.size()));
			permutation.add(i, chosenLetter);
			notUsedLetters.remove(chosenLetter);
		}
		for (int j = 0; j < _n; j++) {
			qAvailableLetters[j].remove(permutation.get(j));
		}
		return permutation.toArray(new Character[0]);
	}

	private int _n;
	private Alphabet _alphabet;
	private Random _generator;
}
