package pd.test;

import java.util.*;

import pd.core.*;
import pd.util.*;

/**
 * @author Łukasz Jęda
 * 
 */
public class BlackBoxesGenerator {

	public BlackBoxesGenerator(Random generator, int blackBoxesNumber, Alphabet alphabet, int n, int keysLength, boolean generateSeparateKeys) {
		validateCommonEntries(generator, blackBoxesNumber, alphabet, n);
		if (keysLength < 1) {
			System.err.println("Długość kluczy powinna być dodatnia. " + "Ustawiam domyślną wartość (128).");
			_keysLength = 128;
		} else {
			_keysLength = keysLength;
		}
		if (!generateSeparateKeys) {
			generateKeys();
		}
		generateBlackBoxes(generateSeparateKeys);
	}

	public BlackBoxesGenerator(Random generator, int blackBoxesNumber, Alphabet alphabet, int n, List<Boolean> k, List<Boolean> r) {
		validateCommonEntries(generator, blackBoxesNumber, alphabet, n);
		if (k == null || k.isEmpty()) {
			System.err.println("Źle zdefiniowany klucz k. Ustawiam wartość domyślną.");
			_k = new ArrayList<Boolean>();
			_k.add(true);
			_k.add(false);
		} else {
			_k = k;
		}
		if (r == null || r.isEmpty()) {
			System.err.println("Źle zdefiniowany klucz r. Ustawiam wartość domyślną.");
			_r = new ArrayList<Boolean>();
			_r.add(true);
			_r.add(false);
		} else {
			_r = r;
		}
		generateBlackBoxes(false);
	}

	private void validateCommonEntries(Random generator, int blackBoxesNumber, Alphabet alphabet, int n) {
		if (n < 0) {
			System.err.println("Liczba iteracji algorytmu powinna być nieujemna. " + "Ustawiam wartość domyślną (1).");
			_n = 1;
		} else {
			_n = n;
		}
		if (blackBoxesNumber < 1) {
			System.err.println("Liczba miksów do wygenerowania powinna być dodatnia. " + "Generuję jeden miks.");
			_blackBoxesNumber = 1;
		} else {
			_blackBoxesNumber = blackBoxesNumber;
		}
		if (alphabet == null || alphabet.getSize() < 2) {
			System.err.println("Alphabet jest za mały by założenia co do miksów mogły być " + "spełnione. Używam liter strony kodowej CP1250.");
			_alphabet = new Alphabet();
		} else {
			_alphabet = alphabet;
		}
		if (generator == null) {
			System.err.println("Nie podano generatora liczb pseudolosowych. Używam domyślnego w generowaniu.");
			_generator = new Random(16 + 10 + 2010);
		} else {
			_generator = generator;
		}

		// TODO: global generator
		_generator = RandomUtil.getRandom();

	}

	private void generateBlackBoxes(boolean generateSeparateKeys) {
		_qMixGenerator = new QMixGenerator(_alphabet, _generator);
		_blackBoxes = new BlackBox[_blackBoxesNumber];
		for (int i = 0; i < _blackBoxesNumber; i++) {
			Map<CharacterArray, Character> qMap = _qMixGenerator.generateMix(), yMap = new HashMap<CharacterArray, Character>(),
					zMap = new HashMap<CharacterArray, Character>();
			for (int j = 0; j < _alphabet.getSize(); j++) {
				Character state = _alphabet.getLetter(j);
				List<Character> yAvailableLetters = new LinkedList<Character>(), zAvailableLetters = new LinkedList<Character>();
				for (Character letter : _alphabet.getLetters()) {
					yAvailableLetters.add(letter);
					zAvailableLetters.add(letter);	
				}
				for (int k = 0; k < _alphabet.getSize(); k++) {
					Character letter = _alphabet.getLetter(k);
					Character yLetter = yAvailableLetters.get(_generator.nextInt(yAvailableLetters.size()));
					yAvailableLetters.remove(yLetter);
					int yLetterPositionInZAvailableLetters = zAvailableLetters.indexOf(yLetter);
					if (yLetterPositionInZAvailableLetters != -1) {
						zAvailableLetters.remove(yLetterPositionInZAvailableLetters);
					}
					int yLastLetterPositionInZAvailableLetters = -1;
					Character removedLetterFromZAvailableLetters = null;
					if (yAvailableLetters.size() == 1) {
						yLastLetterPositionInZAvailableLetters = zAvailableLetters.indexOf(yAvailableLetters.get(0));
						if (yLastLetterPositionInZAvailableLetters != -1 && zAvailableLetters.size() != 1) {
							removedLetterFromZAvailableLetters = zAvailableLetters.remove(1 - yLastLetterPositionInZAvailableLetters);
						} else {
							yLastLetterPositionInZAvailableLetters = -1;
						}
					}
					Character zLetter = zAvailableLetters.get(_generator.nextInt(zAvailableLetters.size()));
					if (yLastLetterPositionInZAvailableLetters != -1) {
						zAvailableLetters.add(1 - yLastLetterPositionInZAvailableLetters, removedLetterFromZAvailableLetters);
					}
					if (yLetterPositionInZAvailableLetters != -1) {
						zAvailableLetters.add(yLetterPositionInZAvailableLetters, yLetter);
					}
					zAvailableLetters.remove(zLetter);
					yMap.put(new CharacterArray(state, letter), yLetter);
					zMap.put(new CharacterArray(state, letter), zLetter);
				}
			}
			if (generateSeparateKeys) {
				generateKeys();
			}
			_blackBoxes[i] = new BlackBox(_alphabet, qMap, yMap, zMap, _k, _r, _n);
		}
	}

	private void generateKeys() {
		for (int p = 0; p < _keysLength; p++) {
			_k.add(_generator.nextBoolean());
			_r.add(_generator.nextBoolean());
		}
	}

	public BlackBox getBlackBox(int i) throws Exception {
		if (_blackBoxes == null) {
			throw new Exception("Instancje zostały zainicjalizowane niepoprawnie.");
		}
		if (i < 0 || i >= _blackBoxes.length) {
			throw new Exception("Zażądano instancji spoza wygenerowanego zakresu, " + "możliwe wartości to 0 - " + _blackBoxes.length + ".");
		}
		return _blackBoxes[i];
	}

	public int getSize() {
		return _blackBoxes == null ? 0 : _blackBoxes.length;
	}

	private int _blackBoxesNumber, _keysLength, _n;
	private List<Boolean> _k = new ArrayList<Boolean>(), _r = new ArrayList<Boolean>();
	private Alphabet _alphabet;
	private BlackBox[] _blackBoxes;
	private Random _generator;
	private QMixGenerator _qMixGenerator;
}
