package pd.test;

import java.util.*;

import pd.core.*;
import pd.util.*;

/**
 * @author Łukasz Jęda
 * 
 */
public class TestPerformer implements CJProxy {

	public static final int highestTestMode = 11;

	TestPerformer(int blackBoxesNumber, int testMode, int desiredBitArraySize, int encryptionOrder) {
		if (encryptionOrder < 0) {
			System.err.println("Liczba iteracji algorytmu powinna być liczbą nieujemną.");
			resetTest();
			return;
		}
		init(blackBoxesNumber, testMode, desiredBitArraySize, encryptionOrder);
	}

	TestPerformer(int blackBoxesNumber, int testMode, int desiredBitArraySize) {
		init(blackBoxesNumber, testMode, desiredBitArraySize, _defaultEncryptionOrder);
	}

	private void init(int blackBoxesNumber, int testMode, int desiredBitArraySize, int encryptionOrder) {
		if (!validateInput(blackBoxesNumber, testMode, desiredBitArraySize)) {
			resetTest();
			return;
		}

		Alphabet alphabet = null;
		try {
			alphabet = AlphabetUtil.generateAlphabet(_size);
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
			resetTest();
			return;
		}
		List<Boolean> r = new ArrayList<Boolean>();
		List<Boolean> k = new ArrayList<Boolean>();
		_bits = new boolean[desiredBitArraySize];
		_testMode = testMode;
		switch (testMode) {
		case 1:
			_blackBoxesGenerator = new BlackBoxesGenerator(_generator, blackBoxesNumber, alphabet, encryptionOrder, _keysLength, true);
			break;
		case 2:
			for (int i = 0; i < _keysLength; i++) {
				r.add(false);
			}
			k.addAll(r);
			_blackBoxesGenerator = new BlackBoxesGenerator(_generator, blackBoxesNumber, alphabet, encryptionOrder, k, r);
			break;
		case 3:
			_blackBoxesGenerator = new BlackBoxesGenerator(_generator, blackBoxesNumber, alphabet, encryptionOrder, _keysLength, true);
			break;
		case 4:
			_blackBoxesGenerator = new BlackBoxesGenerator(_generator, blackBoxesNumber, alphabet, encryptionOrder, _keysLength, true);
			break;
		case 5:
			_blackBoxesGenerator = new BlackBoxesGenerator(_generator, blackBoxesNumber, alphabet, encryptionOrder, _keysLength, true);
			break;
		case 6:
			_blackBoxesGenerator = new BlackBoxesGenerator(_generator, blackBoxesNumber, alphabet, encryptionOrder, _keysLength, true);
			break;
		case 7:
			_blackBoxesGenerator = new BlackBoxesGenerator(_generator, blackBoxesNumber, alphabet, encryptionOrder, _keysLength, true);
			break;
		case 8:
			for (int i = 0; i < _keysLength; i++) {
				r.add(false);
			}
			k.addAll(r);
			_blackBoxesGenerator = new BlackBoxesGenerator(_generator, blackBoxesNumber, alphabet, encryptionOrder, k, r);
			break;
		case 9:
			for (int i = 0; i < _keysLength; i++) {
				r.add(true);
			}
			k.addAll(r);
			_blackBoxesGenerator = new BlackBoxesGenerator(_generator, blackBoxesNumber, alphabet, encryptionOrder, k, r);
			break;
		case 10:
			_blackBoxesGenerator = new BlackBoxesGenerator(_generator, blackBoxesNumber, alphabet, encryptionOrder, _keysLength, true);
			break;
		case 11:
			_blackBoxesGenerator = new BlackBoxesGenerator(_generator, blackBoxesNumber, alphabet, encryptionOrder, _keysLength, true);
			break;
		default:
			resetTest();
		}
	}

	private void resetTest() {
		_blackBoxesGenerator = null;
		_testMode = 0;
	}

	public boolean areTestsReady() {
		return _blackBoxesGenerator != null;
	}

	private boolean validateInput(int blackBoxesNumber, int testMode, int desiredBitArraySize) {
		if (blackBoxesNumber < 1) {
			System.err.println("Liczba powtórzeń testu powinna być większa od 0.");
			return false;
		}
		if (testMode < 1 || testMode > 11) {
			System.err.println("Numer testu powinien być liczbą naturalną z zakresu 1 - 11.");
			return false;
		}
		if (desiredBitArraySize < 1) {
			System.err.println("Długość ciągu bitów powinna być większa od 0.");
			return false;
		}
		return true;
	}

	@Override
	public Object execute(Object args) throws Exception {
		if (!(args instanceof int[])) {
			throw new Exception("Argument metody 'execute' powinien być tablicą prymitywów typu int.");
		}
		int[] arguments = (int[]) args;
		if (arguments.length < 1) {
			throw new Exception("Zbyt mało argumentów przekazanych w tablicy będącej argumentem " + "metody 'execute'.");
		}

		int blackBoxNumber = arguments[0];
		if (_blackBoxesGenerator == null) {
			throw new Exception("Wystąpił błąd podczas inicjalizacji testu.");
		}
		if (blackBoxNumber >= _blackBoxesGenerator.getSize()) {
			throw new Exception("Numer powtórzenia testu jest większy niż całkowita liczba powtórzeń.");
		}

		executeTestMode(blackBoxNumber);
		return _bits;
	}

	private void executeTestMode(int blackBoxNumber) throws Exception {
		System.gc();
		switch (_testMode) {
		case 1:
			executeTest1(blackBoxNumber);
			break;
		case 2:
			executeTest2(blackBoxNumber);
			break;
		case 3:
			executeTest3(blackBoxNumber);
			break;
		case 4:
			executeTest4(blackBoxNumber);
			break;
		case 5:
			executeTest5(blackBoxNumber);
			break;
		case 6:
			executeTest6(blackBoxNumber);
			break;
		case 7:
			executeTest7(blackBoxNumber);
			break;
		case 8:
			executeTest8(blackBoxNumber);
			break;
		case 9:
			executeTest9(blackBoxNumber);
			break;
		case 10:
			executeTest10(blackBoxNumber);
			break;
		case 11:
			executeTest11(blackBoxNumber);
			break;
		default:
			_bits = null;
		}
	}

	public void setDebugModeDisabled(boolean debugModeDisabled) {
		_debugModeDisabled = debugModeDisabled;
	}

	private void updateProcessingTime(int blackBoxNumber, long startTime) {
		if (_debugModeDisabled) {
			return;
		}
		LastBlackBoxProcessingTime = (System.currentTimeMillis() - startTime) / 1000;
		MeanBlackBoxProcessingTime = (blackBoxNumber * MeanBlackBoxProcessingTime + LastBlackBoxProcessingTime) / (blackBoxNumber + 1);
	}

	private void printDebugLog(int blackBoxNumber, int i, int j, int loopsCount, long startTime) {
		if (_debugModeDisabled) {
			return;
		}
		System.out.println("BB: " + blackBoxNumber + " loopsCount: " + loopsCount + " i: " + i + " j: " + j + ". upłynęło: "
				+ (System.currentTimeMillis() - startTime) / 1000 + " sekund. LBBPT: " + LastBlackBoxProcessingTime + " MBBPT: " + MeanBlackBoxProcessingTime);
	}

	private void executeTest1(int blackBoxNumber) throws Exception {
		int position = 0, loopsCount = (int) Math.ceil((double) _bits.length / (double) (2 * _keysLength * (_plaintextLength + 1) * _letterBitLength));
		String plaintext = TextUtil.getUniformPlaintext(true, _plaintextLength, _size);
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loopsCount; i++) {
			for (int j = 0; j < _keysLength; j++) {
				printDebugLog(blackBoxNumber, i, j, loopsCount, startTime);
				for (int p = 0; p < 2; p++) {
					String cryptotextOriginal = _blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintext);
					int[] changePositions = new int[] { j };
					_blackBoxesGenerator.getBlackBox(blackBoxNumber).changeKeysForTests(p == 0, changePositions);
					String cryptotextWithChangedKey = _blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintext);
					_blackBoxesGenerator.getBlackBox(blackBoxNumber).changeKeysForTests(p == 0, changePositions);
					boolean[] bits = TextUtil.getBitXorDifference(cryptotextOriginal, cryptotextWithChangedKey, _letterBitLength);
					for (int k = 0; k < bits.length; k++) {
						_bits[position] = bits[k];
						position++;
						if (position == _bits.length) {
							updateProcessingTime(blackBoxNumber, startTime);
							return;
						}
					}
				}
			}
		}
	}

	private void executeTest2(int blackBoxNumber) throws Exception {
		int position = 0, loopsCount = (int) Math.ceil((double) _bits.length / (double) ((_plaintextLength + 1) * _plaintextLength * _letterBitLength));
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loopsCount; i++) {
			String plaintext = TextUtil.generateRandomPlaintext(_generator, _size, _plaintextLength);
			for (int j = 0; j < _plaintextLength; j++) {
				printDebugLog(blackBoxNumber, i, j, loopsCount, startTime);
				String perturbedPlaintext = TextUtil.perturbPlaintext(plaintext, i, _letterBitLength, _generator);
				boolean[] bits = TextUtil.getBitXorDifference(_blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintext),
						_blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(perturbedPlaintext), _letterBitLength);
				for (int k = 0; k < bits.length; k++) {
					_bits[position] = bits[k];
					position++;
					if (position == _bits.length) {
						updateProcessingTime(blackBoxNumber, startTime);
						return;
					}
				}
			}
		}
	}

	private void executeTest3(int blackBoxNumber) throws Exception {
		int position = 0, loopsCount = (int) Math.ceil((double) _bits.length / (double) ((_plaintextLength + 1) * _letterBitLength));
		String[] plaintexts = new String[loopsCount];
		for (int i = 0; i < loopsCount; i++) {
			plaintexts[i] = TextUtil.generateRandomPlaintext(_generator, _size, _plaintextLength);
		}
		long startTime = System.currentTimeMillis();
		for (int j = 0; j < loopsCount; j++) {
			printDebugLog(blackBoxNumber, -1, j, loopsCount, startTime);
			boolean[] bits = TextUtil.getBitXorDifference(((char) 0) + plaintexts[j], _blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintexts[j]),
					_letterBitLength);
			for (int k = 0; k < bits.length; k++) {
				_bits[position] = bits[k];
				position++;
				if (position == _bits.length) {
					updateProcessingTime(blackBoxNumber, startTime);
					return;
				}
			}
		}
	}

	private void executeTest4(int blackBoxNumber) throws Exception {
		int position = 0, loopsCount = (int) Math.ceil((double) _bits.length / (double) ((_plaintextLength + 1) * _letterBitLength));
		String plaintext = TextUtil.getUniformPlaintext(true, _plaintextLength, _size);
		long startTime = System.currentTimeMillis();
		for (int j = 0; j < loopsCount; j++) {
			printDebugLog(blackBoxNumber, -1, j, loopsCount, startTime);
			String cryptotext = _blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintext);
			Boolean[] bits = TextUtil.getBits(cryptotext, _letterBitLength);
			plaintext = cryptotext;
			for (int k = 0; k < bits.length; k++) {
				_bits[position] = bits[k];
				position++;
				if (position == _bits.length) {
					updateProcessingTime(blackBoxNumber, startTime);
					return;
				}
			}
		}
	}

	private void executeTest5(int blackBoxNumber) throws Exception {
		int position = 0, loopsCount = (int) Math.ceil((double) _bits.length / (double) ((_plaintextLength + 1) * _letterBitLength));
		String[] plaintexts = new String[loopsCount];
		for (int i = 0; i < loopsCount; i++) {
			plaintexts[i] = TextUtil.generateRandomPlaintext(_generator, _size, _plaintextLength);
		}
		long startTime = System.currentTimeMillis();
		for (int j = 0; j < loopsCount; j++) {
			printDebugLog(blackBoxNumber, -1, j, loopsCount, startTime);
			Boolean[] bits = TextUtil.getBits(_blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintexts[j]), _letterBitLength);
			for (int k = 0; k < bits.length; k++) {
				_bits[position] = bits[k];
				position++;
				if (position == _bits.length) {
					updateProcessingTime(blackBoxNumber, startTime);
					return;
				}
			}
		}
	}

	private void executeTest6(int blackBoxNumber) throws Exception {
		int position = 0, loopsCount = (int) Math.ceil((double) _bits.length
				/ (double) ((_plaintextLength + 1) * _letterBitLength * (1 + _plaintextLength + (_plaintextLength - 1) * _plaintextLength / 2)));
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loopsCount; i++) {
			String[] plaintexts = TextUtil.getDensityPlaintexts(true, _plaintextLength, _size, _generator);
			for (int j = 0; j < plaintexts.length; j++) {
				printDebugLog(blackBoxNumber, i, j, loopsCount, startTime);
				Boolean[] bits = TextUtil.getBits(_blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintexts[j]), _letterBitLength);
				for (int k = 0; k < bits.length; k++) {
					_bits[position] = bits[k];
					position++;
					if (position == _bits.length) {
						updateProcessingTime(blackBoxNumber, startTime);
						return;
					}
				}
			}
		}
	}

	private void executeTest7(int blackBoxNumber) throws Exception {
		int position = 0, loopsCount = (int) Math.ceil((double) _bits.length
				/ (double) ((_plaintextLength + 1) * _letterBitLength * (1 + _plaintextLength + (_plaintextLength - 1) * _plaintextLength / 2)));
		String[] plaintexts = TextUtil.getDensityPlaintexts(false, _plaintextLength, _size, _generator);
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loopsCount; i++) {
			for (int j = 0; j < plaintexts.length; j++) {
				printDebugLog(blackBoxNumber, i, j, loopsCount, startTime);
				Boolean[] bits = TextUtil.getBits(_blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintexts[j]), _letterBitLength);
				for (int k = 0; k < bits.length; k++) {
					_bits[position] = bits[k];
					position++;
					if (position == _bits.length) {
						updateProcessingTime(blackBoxNumber, startTime);
						return;
					}
				}
			}
		}
	}

	private void executeTest8(int blackBoxNumber) throws Exception {
		int position = 0, loopsCount = (int) Math.ceil(
				(double) _bits.length / (double) ((_plaintextLength + 1) * _letterBitLength * 2 * (1 + _keysLength + (_keysLength - 1) * _keysLength / 2)));
		int[][] changePositions = TextUtil.getDensityKeysChangePositions(_keysLength);
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loopsCount; i++) {
			String plaintext = TextUtil.generateRandomPlaintext(_generator, _size, _plaintextLength);
			for (int j = 0; j < changePositions.length; j++) {
				printDebugLog(blackBoxNumber, i, j, loopsCount, startTime);
				for (int p = 0; p < 2; p++) {
					_blackBoxesGenerator.getBlackBox(blackBoxNumber).changeKeysForTests(p == 0, changePositions[j]);
					Boolean[] bits = TextUtil.getBits(_blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintext), _letterBitLength);
					_blackBoxesGenerator.getBlackBox(blackBoxNumber).changeKeysForTests(p == 0, changePositions[j]);
					for (int k = 0; k < bits.length; k++) {
						_bits[position] = bits[k];
						position++;
						if (position == _bits.length) {
							updateProcessingTime(blackBoxNumber, startTime);
							return;
						}
					}
				}
			}
		}
	}

	private void executeTest9(int blackBoxNumber) throws Exception {
		int position = 0, loopsCount = (int) Math.ceil(
				(double) _bits.length / (double) ((_plaintextLength + 1) * _letterBitLength * 2 * (1 + _keysLength + (_keysLength - 1) * _keysLength / 2)));
		int[][] changePositions = TextUtil.getDensityKeysChangePositions(_keysLength);
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loopsCount; i++) {
			String plaintext = TextUtil.generateRandomPlaintext(_generator, _size, _plaintextLength);
			for (int j = 0; j < changePositions.length; j++) {
				printDebugLog(blackBoxNumber, i, j, loopsCount, startTime);
				for (int p = 0; p < 2; p++) {
					_blackBoxesGenerator.getBlackBox(blackBoxNumber).changeKeysForTests(p == 0, changePositions[j]);
					Boolean[] bits = TextUtil.getBits(_blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintext), _letterBitLength);
					_blackBoxesGenerator.getBlackBox(blackBoxNumber).changeKeysForTests(p == 0, changePositions[j]);
					for (int k = 0; k < bits.length; k++) {
						_bits[position] = bits[k];
						position++;
						if (position == _bits.length) {
							updateProcessingTime(blackBoxNumber, startTime);
							return;
						}
					}
				}
			}
		}
	}

	private void executeTest10(int blackBoxNumber) throws Exception {
		int position = 0, loopsCount = (int) Math.ceil(
				(double) _bits.length / (double) ((_plaintextLength + 1) * _letterBitLength * (_size * (_size - 1) / 2 + _size * (_size - 3) / 2 * _size)));
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loopsCount; i++) {
			String plaintext = TextUtil.generateRandomPlaintext(_generator, _size, _plaintextLength);
			String cryptotext = _blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintext);
			int[] swaps = new int[_size];
			for (int j = 0; j < _size; j++) {
				printDebugLog(blackBoxNumber, i, j, loopsCount, startTime);
				BlackBox[] blackBoxes = _blackBoxesGenerator.getBlackBox(blackBoxNumber).getBlackBoxesWithChangedMixesQYForTests(j, swaps);
				for (int p = 0; p < blackBoxes.length; p++) {
					boolean[] bits = TextUtil.getBitXorDifference(cryptotext, blackBoxes[p].encrypt(plaintext), _letterBitLength);
					for (int k = 0; k < bits.length; k++) {
						_bits[position] = bits[k];
						position++;
						if (position == _bits.length) {
							updateProcessingTime(blackBoxNumber, startTime);
							return;
						}
					}
				}
			}
		}
	}

	private void executeTest11(int blackBoxNumber) throws Exception {
		int position = 0, loopsCount = (int) Math.ceil(
				(double) _bits.length / (double) ((_plaintextLength + 1) * _letterBitLength * (_size * (_size - 1) / 2 + _size * (_size - 3) / 2 * _size * 2)));
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loopsCount; i++) {
			String plaintext = TextUtil.generateRandomPlaintext(_generator, _size, _plaintextLength);
			String cryptotext = _blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintext);
			int[] swaps = new int[_size];
			for (int j = 0; j < _size; j++) {
				printDebugLog(blackBoxNumber, i, j, loopsCount, startTime);
				BlackBox[] blackBoxes = _blackBoxesGenerator.getBlackBox(blackBoxNumber).getBlackBoxesWithChangedMixesQYZForTests(j, swaps);
				for (int p = 0; p < blackBoxes.length; p++) {
					boolean[] bits = TextUtil.getBitXorDifference(cryptotext, blackBoxes[p].encrypt(plaintext), _letterBitLength);
					for (int k = 0; k < bits.length; k++) {
						_bits[position] = bits[k];
						position++;
						if (position == _bits.length) {
							updateProcessingTime(blackBoxNumber, startTime);
							return;
						}
					}
				}
			}
		}
	}

	// private void executeTest10(int blackBoxNumber) throws Exception {
	// int position = 0, loopsCount =
	// (int)Math.ceil((double)_bits.length /
	// (double)(_plaintextLength * _letterBitLength *
	// (_size * (_size - 1) / 2 + _size * (_size - 3) / 2 * _size)));
	// long startTime = System.currentTimeMillis();
	// for (int i = 0; i < loopsCount; i++) {
	// System.out.println("i: " + i);
	// String plaintext = TextUtil.generateRandomPlaintext(_generator, _size,
	// _plaintextLength);
	// String cryptotext =
	// _blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintext);
	// BlackBox[] blackBoxes =
	// _blackBoxesGenerator.getBlackBox(blackBoxNumber).getBlackBoxesWithChangedMixesQYForTests();
	// for (int j = 0; j < blackBoxes.length; j++) {
	// System.out.println("BB: " + blackBoxNumber + " loopsCount: " + loopsCount
	// + " i: " +
	// i + " j: " + j + ". upłynęło: " + (System.currentTimeMillis() -
	// startTime) /
	// 1000 + " sekund. LBBPT: " + LastBlackBoxProcessingTime + " MBBPT: " +
	// MeanBlackBoxProcessingTime);
	// boolean[] bits = getBitXorDifference(cryptotext,
	// blackBoxes[j].encrypt(plaintext));
	// for (int k = 0; k < bits.length; k++) {
	// _bits[position] = bits[k];
	// position++;
	// if (position == _bits.length) {
	// LastBlackBoxProcessingTime =
	// (System.currentTimeMillis() - startTime) / 1000;
	// MeanBlackBoxProcessingTime =
	// (blackBoxNumber * MeanBlackBoxProcessingTime +
	// LastBlackBoxProcessingTime) / (blackBoxNumber + 1);
	// return;
	// }
	// }
	// }
	// }
	// }
	//
	// private void executeTest11(int blackBoxNumber) throws Exception {
	// int position = 0, loopsCount =
	// (int)Math.ceil((double)_bits.length /
	// (double)(_plaintextLength * _letterBitLength *
	// (_size * (_size - 1) / 2 + _size * (_size - 3) / 2 * _size * 2)));
	// long startTime = System.currentTimeMillis();
	// for (int i = 0; i < loopsCount; i++) {
	// System.out.println("i: " + i);
	// String plaintext = TextUtil.generateRandomPlaintext(_generator, _size,
	// _plaintextLength);
	// String cryptotext =
	// _blackBoxesGenerator.getBlackBox(blackBoxNumber).encrypt(plaintext);
	// BlackBox[] blackBoxes =
	// _blackBoxesGenerator.getBlackBox(blackBoxNumber).getBlackBoxesWithChangedMixesQYZForTests();
	// for (int j = 0; j < blackBoxes.length; j++) {
	// System.out.println("BB: " + blackBoxNumber + " loopsCount: " + loopsCount
	// + " i: " +
	// i + " j: " + j + ". upłynęło: " + (System.currentTimeMillis() -
	// startTime) /
	// 1000 + " sekund. LBBPT: " + LastBlackBoxProcessingTime + " MBBPT: " +
	// MeanBlackBoxProcessingTime);
	// boolean[] bits = getBitXorDifference(cryptotext,
	// blackBoxes[j].encrypt(plaintext));
	// for (int k = 0; k < bits.length; k++) {
	// _bits[position] = bits[k];
	// position++;
	// if (position == _bits.length) {
	// LastBlackBoxProcessingTime =
	// (System.currentTimeMillis() - startTime) / 1000;
	// MeanBlackBoxProcessingTime =
	// (blackBoxNumber * MeanBlackBoxProcessingTime +
	// LastBlackBoxProcessingTime) / (blackBoxNumber + 1);
	// return;
	// }
	// }
	// }
	// }
	// }

	private int _testMode;
	private static final int _defaultEncryptionOrder = 128;
	private static final int _size = 32;
	private static final int _keysLength = 128;
	private static final int _plaintextLength = 128;
	private static final int _letterBitLength = AlphabetUtil.calculateLetterBitLength(_size);

	// TODO: global generator

	// private final Random _generator = new Random(16 + 10 + 2010);
	private final Random _generator = RandomUtil.getRandom();

	private BlackBoxesGenerator _blackBoxesGenerator;
	private boolean[] _bits;
	private long LastBlackBoxProcessingTime = 0;
	private long MeanBlackBoxProcessingTime = 0;
	private boolean _debugModeDisabled = false;
}
