package pd.core;

import java.util.*;

import pd.util.*;

/**
 * @author Łukasz Jęda
 * 
 */
public class BlackBox {

	public BlackBox(Alphabet alphabet, Map<CharacterArray, Character> qMap, Map<CharacterArray, Character> yMap, Map<CharacterArray, Character> zMap,
			List<Boolean> k, List<Boolean> r, int n) {
		_alphabet = checkAlphabet(alphabet);
		_ltGraph = new LTGraph(_alphabet, qMap, yMap, zMap);
		validateAndSetCommonEntries(k, r, n);
	}

	public BlackBox(Alphabet alphabet, List<Boolean> k, List<Boolean> r, int n) {
		_alphabet = checkAlphabet(alphabet);
		_ltGraph = new LTGraph(_alphabet);
		validateAndSetCommonEntries(k, r, n);
	}

	public BlackBox(Alphabet alphabet, int n) {
		_alphabet = checkAlphabet(alphabet);
		_ltGraph = new LTGraph(_alphabet);
		validateAndSetOrder(n);
	}

	public BlackBox(List<Boolean> k, List<Boolean> r, int n) {
		_alphabet = new Alphabet();
		_ltGraph = new LTGraph(_alphabet);
		validateAndSetCommonEntries(k, r, n);
	}

	public BlackBox(int n) {
		_alphabet = new Alphabet();
		_ltGraph = new LTGraph(_alphabet);
		validateAndSetOrder(n);
	}

	public BlackBox() {
		_alphabet = new Alphabet();
		_ltGraph = new LTGraph(_alphabet);
	}

	private Alphabet checkAlphabet(Alphabet alphabet) {
		if (alphabet == null || alphabet.getSize() < 2) {
			System.err.println("Alphabet jest za mały by założenia co do miksów mogły być " + "spełnione. Używam domyślnego alfabetu.");
			return new Alphabet();
		} else {
			return alphabet;
		}
	}

	public void validateAndSetCommonEntries(List<Boolean> k, List<Boolean> r, int n) {
		if (k == null || k.isEmpty()) {
			System.err.println("Ustawiam domyślne słowo 'k'.");
		} else {
			_k = k.toArray(new Boolean[0]);
			_kSize = _k.length;
		}
		if (r == null || r.isEmpty()) {
			System.err.println("Ustawiam domyślne słowo 'r'.");
		} else {
			_r = r.toArray(new Boolean[0]);
			_rSize = _r.length;
		}
		validateAndSetOrder(n);
	}

	public void validateAndSetOrder(int n) {
		if (n < 0) {
			System.err.println("Ustawiam domyślną liczbę iteracji algorytmu 'n'.");
		} else {
			_n = n;
			_2n = n << 1;
		}
	}

	// public void invEnc() {
	// synchronized (encryptionLock) {
	// for (int i = 0; i < _encryptionCharsHalfLength; i++) {
	// char temp = _encryptionChars[i];
	// _encryptionChars[i] = _encryptionChars[_encryptionCharsLength - 1 - i];
	// _encryptionChars[_encryptionCharsLength - 1 - i] = temp;
	// }
	// }
	// }

	public void invEnc() {
		synchronized (encryptionLock) {
			// _encryptionBackward = !_encryptionBackward;
			_encryptionDirection = (byte) (1 - _encryptionDirection);
		}
	}

	// public void invDec() {
	// synchronized (decryptionLock) {
	// for (int i = 0; i < _decryptionCharsHalfLength; i++) {
	// char temp = _decryptionChars[i];
	// _decryptionChars[i] = _decryptionChars[_decryptionCharsLength - 1 - i];
	// _decryptionChars[_decryptionCharsLength - 1 - i] = temp;
	// }
	// }
	// }

	public void invDec() {
		synchronized (decryptionLock) {
			// _decryptionBackward = !_decryptionBackward;
			_decryptionDirection = (byte) (1 - _decryptionDirection);
		}
	}

	public static String inv_old(String string) {
		char[] s = string.toCharArray();
		int n = s.length;
		int halfLength = n / 2;
		for (int i = 0; i < halfLength; i++) {
			char temp = s[i];
			s[i] = s[n - 1 - i];
			s[n - 1 - i] = temp;
		}
		return new String(s);
	}

	private void enc() throws Exception {
		synchronized (encryptionLock) {
			int imodk = 0, imodr = 0;
			int[] direction = _encryptionDirections[_encryptionDirection];
			Character v = _alphabet.getLetter(0);
			for (int i = 0; i < _encryptionCharsLength; i++) {
				// int pos = nextEncPositionInCurrentDirection(i);
				int pos = direction[i];
				char c = _encryptionChars[pos];
				// char c = _encryptionChars[i];
				if (!_alphabet.hasLetter(c)) {
					throw new Exception("słowo zawiera literę: '" + c + "', która nie występuje w alfabecie.");
				}
				Character oldv = v;
				v = _ltGraph.N(v, c, _r[imodr]);
				// below must include also nextEncPositionInCurrentDirection if
				// not inversing _encryptionChars
				_encryptionChars[pos] = _ltGraph.L(oldv, v, _k[imodk]);
				// _encryptionChars[i] = _ltGraph.L(oldv, v, _k[imodk]);
				// in order to get rid of very costly modulo operation
				if (++imodk == _kSize) {
					imodk = 0;
				}
				if (++imodr == _rSize) {
					imodr = 0;
				}
			}
		}
	}

	// private int nextEncPositionInCurrentDirection(int i) throws Exception {
	// if (_encryptionBackward) {
	// i = _encryptionCharsLength - 1 - i;
	// }
	// if (i < 0 || i >= _encryptionCharsLength) {
	// throw new Exception("Algorytm szyfrujący dotarł poza granice szyfrowanego
	// słowa.");
	// }
	// return i;
	// }

	private String enc_old(String word) throws Exception {
		int kSize = _k.length, rSize = _r.length, wordSize = word.length(), imodk = 0, imodr = 0;
		char[] plaintext = word.toCharArray();
		char[] cryptotext = new char[wordSize];
		Character v = _alphabet.getLetter(0);
		for (int i = 0; i < wordSize; i++) {
			char c = plaintext[i];
			if (!_alphabet.hasLetter(c)) {
				throw new Exception("słowo zawiera literę: '" + c + "', która nie występuje w alfabecie.");
			}
			// cryptotext[i] = _ltGraph.L(v, c, _k[imodk], _r[imodr]);
			// v = _ltGraph.N(v, c, _r[imodr]);

			// above is slower than:

			// quicker_part_start
			Character oldv = v;
			v = _ltGraph.N(v, c, _r[imodr]);
			cryptotext[i] = _ltGraph.L(oldv, v, _k[imodk]);
			// quicker_part_end

			// in order to get rid of very costly modulo operation
			if (++imodk == kSize) {
				imodk = 0;
			}
			if (++imodr == rSize) {
				imodr = 0;
			}
		}
		return new String(cryptotext);
	}

	private String encAndInvert(String word) throws Exception {
		int kSize = _k.length, rSize = _r.length, wordSize = word.length(), imodk = 0, imodr = 0;
		char[] plaintext = word.toCharArray();
		char[] cryptotext = new char[wordSize];
		Character v = _alphabet.getLetter(0);
		for (int i = 0; i < wordSize; i++) {
			char c = plaintext[i];
			if (!_alphabet.hasLetter(c)) {
				throw new Exception("słowo zawiera literę: '" + c + "', która nie występuje w alfabecie.");
			}
			// cryptotext[i] = _ltGraph.L(v, c, _k[imodk], _r[imodr]);
			// v = _ltGraph.N(v, c, _r[imodr]);

			// above is slower than:

			// quicker_part_start
			Character oldv = v;
			v = _ltGraph.N(v, c, _r[imodr]);
			cryptotext[wordSize - i - 1] = _ltGraph.L(oldv, v, _k[imodk]);
			// quicker_part_end

			// in order to get rid of very costly modulo operation
			if (++imodk == kSize) {
				imodk = 0;
			}
			if (++imodr == rSize) {
				imodr = 0;
			}
		}
		return new String(cryptotext);
	}

	public String encrypt(String word) {
		if (word == null || word.isEmpty()) {
			System.err.println("Podałeś puste słowo do zaszyfrowania.");
			return null;
		}
		Character firstLetter = _alphabet.getLetter(0);
		Character start = _alphabet.getLetter(_generator.nextInt(_alphabet.getSize()));
		String startEncrypted = encryptStartingFrom(start.toString(), firstLetter);
		if (startEncrypted == null) {
			System.err.println("Wystąpił błąd podczas szyfrowania stanu początkowego.");
			return null;
		}
		String encryptedWord = encryptStartingFrom(word, start);
		if (encryptedWord == null) {
			System.err.println("Wystąpił błąd podczas szyfrowania wiadomości.");
			return null;
		}
		return startEncrypted + encryptedWord;
	}

	private String encryptStartingFrom(String word, Character start) {
		if (word == null || word.isEmpty()) {
			System.err.println("Nic do zaszyfrowania.");
			return null;
		}
		if (!_alphabet.hasLetter(start)) {
			System.err.println("Stan startowy dla zaszyfrowania nie jest literą z zadanego alfabetu.");
			return null;
		}
		synchronized (encryptionLock) {
			// _encryptionBackward = false;
			_encryptionDirection = 0;
			_encryptionChars = word.toCharArray();
			_encryptionCharsLength = _encryptionChars.length;
			int[] forward = new int[_encryptionCharsLength], backward = new int[_encryptionCharsLength];
			for (int i = 0; i < _encryptionCharsLength; i++) {
				forward[i] = i;
				backward[_encryptionCharsLength - i - 1] = i;
			}
			_encryptionDirections = new int[2][];
			_encryptionDirections[0] = forward;
			_encryptionDirections[1] = backward;
			// _encryptionCharsHalfLength = _encryptionCharsLength / 2;
			try {
				for (int i = 0; i < _n; i++) {
					enc();
					invEnc();
					enc();
					invEnc();
				}
				enc();
				String encoded = new String(_encryptionChars);
				// in order not to keep the word till another encryption process
				// will start
				_encryptionChars = null;
				// free memory
				_encryptionDirections = null;
				return encoded;
			} catch (Exception e) {
				System.err.println(e.getMessage() + " słowo nie zostanie zaszyfrowane.");
				return null;
			}
		}
	}

	private String encryptStartingFrom_old(String word) {
		if (word == null || word.isEmpty()) {
			System.err.println("Podałeś puste słowo do zaszyfrowania.");
			return null;
		}
		String cryptotext = word;
		try {
			for (int i = 0; i < _n; i++) {
				cryptotext = inv_old(enc_old(inv_old(enc_old(cryptotext))));
			}
			return enc_old(cryptotext);
		} catch (Exception e) {
			System.err.println(e.getMessage() + " słowo nie zostanie zaszyfrowane.");
			return null;
		}
	}

	private String encryptStartingFrom_old2(String word) {
		if (word == null || word.isEmpty()) {
			System.err.println("Podałeś puste słowo do zaszyfrowania.");
			return null;
		}
		String cryptotext = word;
		try {
			for (int i = 0; i < _2n; i++) {
				cryptotext = encAndInvert(cryptotext);
			}
			return enc_old(cryptotext);
		} catch (Exception e) {
			System.err.println(e.getMessage() + " słowo nie zostanie zaszyfrowane.");
			return null;
		}
	}

	private void dec() throws Exception {
		synchronized (decryptionLock) {
			int imodk = 0, imodr = 0;
			Character oldv = _alphabet.getLetter(0);
			int[] direction = _decryptionDirections[_decryptionDirection];
			for (int i = 0; i < _decryptionCharsLength; i++) {
				// int pos = nextDecPositionInCurrentDirection(i);
				int pos = direction[i];
				char c = _decryptionChars[pos];
				// char c = _decryptionChars[i];
				if (!_alphabet.hasLetter(c)) {
					throw new Exception("Kryptotekst zawiera literę: '" + c + "', która nie " + "występuje w alfabecie.");
				}
				Character v = _ltGraph.P(oldv, c, _k[imodk]);
				// below must include also nextDecPositionInCurrentDirection if
				// not inversing _decryptionChars
				_decryptionChars[pos] = _ltGraph.D(oldv, v, _r[imodr]);
				// _decryptionChars[i] = _ltGraph.D(oldv, v, _r[imodr]);
				oldv = v;

				// in order to get rid of very costly modulo operation
				if (++imodk == _kSize) {
					imodk = 0;
				}
				if (++imodr == _rSize) {
					imodr = 0;
				}
			}
		}
	}

	// private int nextDecPositionInCurrentDirection(int i) throws Exception {
	// if (_decryptionBackward) {
	// i = _decryptionCharsLength - 1 - i;
	// }
	// if (i < 0 || i >= _decryptionCharsLength) {
	// throw new Exception("Algorytm deszyfrujący dotarł poza granice
	// deszyfrowanego słowa.");
	// }
	// return i;
	// }

	private String dec_old(String word) throws Exception {
		int kSize = _k.length, rSize = _r.length, wordSize = word.length(), imodk = 0, imodr = 0;
		char[] cryptotext = word.toCharArray();
		char[] decrypted = new char[wordSize];
		Character oldv = _alphabet.getLetter(0);
		for (int i = 0; i < wordSize; i++) {
			char c = cryptotext[i];
			if (!_alphabet.hasLetter(c)) {
				throw new Exception("Kryptotekst zawiera literę: '" + c + "', która nie " + "występuje w alfabecie.");
			}
			Character v = _ltGraph.P(oldv, c, _k[imodk]);
			decrypted[i] = _ltGraph.D(oldv, v, _r[imodr]);
			oldv = v;

			// in order to get rid of very costly modulo operation
			if (++imodk == kSize) {
				imodk = 0;
			}
			if (++imodr == rSize) {
				imodr = 0;
			}
		}
		return new String(decrypted);
	}

	private String decAndInvert(String word) throws Exception {
		int kSize = _k.length, rSize = _r.length, wordSize = word.length(), imodk = 0, imodr = 0, position = wordSize;
		char[] cryptotext = word.toCharArray();
		char[] decrypted = new char[wordSize];
		Character oldv = _alphabet.getLetter(0);
		for (int i = 0; i < wordSize; i++) {
			char c = cryptotext[i];
			if (!_alphabet.hasLetter(c)) {
				throw new Exception("Kryptotekst zawiera literę: '" + c + "', która nie " + "występuje w alfabecie.");
			}
			Character v = _ltGraph.P(oldv, c, _k[imodk]);
			decrypted[--position] = _ltGraph.D(oldv, v, _r[imodr]);
			oldv = v;

			// in order to get rid of very costly modulo operation
			if (++imodk == kSize) {
				imodk = 0;
			}
			if (++imodr == rSize) {
				imodr = 0;
			}
		}
		return new String(decrypted);
	}

	public String decrypt(String cryptotext) {
		if (cryptotext == null || cryptotext.isEmpty()) {
			System.err.println("Podałeś puste słowo do odszyfrowania.");
			return null;
		}
		Character firstLetter = _alphabet.getLetter(0);
		String startDecrypted = decryptStartingFrom(cryptotext.substring(0, 1), firstLetter);
		if (startDecrypted == null || startDecrypted.toCharArray().length != 1) {
			System.err.println("Wystąpił błąd podczas deszyfrowania stanu początkowego.");
			return null;
		}
		String decryptedWord = decryptStartingFrom(cryptotext.substring(1), startDecrypted.charAt(0));
		if (decryptedWord == null) {
			System.err.println("Wystąpił błąd podczas deszyfrowania wiadomości.");
			return null;
		}
		return decryptedWord;
	}

	private String decryptStartingFrom(String cryptotext, Character start) {
		if (cryptotext == null || cryptotext.isEmpty()) {
			System.err.println("Nic do odszyfrowania.");
			return null;
		}
		if (!_alphabet.hasLetter(start)) {
			System.err.println("Stan startowy dla odszyfrowania nie jest literą z zadanego alfabetu.");
			return null;
		}
		synchronized (decryptionLock) {
			// _decryptionBackward = false;
			_decryptionDirection = 0;
			_decryptionChars = cryptotext.toCharArray();
			_decryptionCharsLength = _decryptionChars.length;
			int[] forward = new int[_decryptionCharsLength], backward = new int[_decryptionCharsLength];
			for (int i = 0; i < _decryptionCharsLength; i++) {
				forward[i] = i;
				backward[_decryptionCharsLength - i - 1] = i;
			}
			_decryptionDirections = new int[2][];
			_decryptionDirections[0] = forward;
			_decryptionDirections[1] = backward;
			// _decryptionCharsHalfLength = _decryptionCharsLength / 2;
			try {
				dec();
				for (int i = 0; i < _n; i++) {
					invDec();
					dec();
					invDec();
					dec();
				}
				String decoded = new String(_decryptionChars);
				// in order not to keep the word till another decryption process
				// will start
				_decryptionChars = null;
				// free memory
				_decryptionDirections = null;
				return decoded;
			} catch (Exception e) {
				System.err.println(e.getMessage() + " słowo nie zostanie odszyfrowane");
				return null;
			}
		}
	}

	public String decryptStartingFrom_old(String cryptotext) {
		if (cryptotext == null || cryptotext.isEmpty()) {
			System.err.println("Podałeś puste słowo do odszyfrowania.");
			return null;
		}
		String word = cryptotext;
		try {
			word = dec_old(cryptotext);
			for (int i = 0; i < _n; i++) {
				word = dec_old(inv_old(dec_old(inv_old(word))));
			}
			return word;
		} catch (Exception e) {
			System.err.println(e.getMessage() + " słowo nie zostanie odszyfrowane");
			return null;
		}
	}

	public String decryptStartingFrom_old2(String cryptotext) {
		if (cryptotext == null || cryptotext.isEmpty()) {
			System.err.println("Podałeś puste słowo do odszyfrowania.");
			return null;
		}
		String word = cryptotext;
		try {
			for (int i = 0; i < _2n; i++) {
				word = decAndInvert(word);
			}
			return dec_old(word);
		} catch (Exception e) {
			System.err.println(e.getMessage() + " słowo nie zostanie odszyfrowane");
			return null;
		}
	}

	public void changeKeysForTests(boolean changeRKey, int[] changePositions) {
		if (changeRKey) {
			for (int changePosition : changePositions) {
				if (changePosition < _r.length) {
					_r[changePosition] = !_r[changePosition];
				} else {
					System.err.println("Klucz r (długości " + _r.length + ") jest zbyt krótki, by zmienić jego pozycję nr " + changePosition + ".");
				}
			}
		} else {
			for (int changePosition : changePositions) {
				if (changePosition < _r.length) {
					_k[changePosition] = !_k[changePosition];
				} else {
					System.err.println("Klucz k (długości " + _k.length + ") jest zbyt krótki, by zmienić jego pozycję nr " + changePosition + ".");
				}
			}
		}
	}
	
	public Boolean[] getKey(boolean rKey) {
		Boolean[] keyToCopy = rKey ? _r : _k;
		return Arrays.copyOf(keyToCopy, keyToCopy.length);
	}

	public BlackBox[] getBlackBoxesWithChangedMixesQYForTests(int letterNumber, int[] swaps) {
		if (letterNumber < 0 || letterNumber > _alphabet.getSize() || swaps.length != _alphabet.getSize()) {
			return new BlackBox[0];
		}
		List<BlackBox> blackBoxes = new ArrayList<BlackBox>();
		Map<CharacterArray, Character> qMix, yMix = _ltGraph.getYMixLetterMap(), zMix = _ltGraph.getZMixLetterMap();
		for (int i = letterNumber + 1; i < _alphabet.getSize(); i++) {
			qMix = _ltGraph.getQMixLetterMap();
			// System.out.println("--------------------- wyjściowy q: ");
			// for (int w = 0; w < _alphabet.getSize(); w++)
			// for (int ww = 0; ww < _alphabet.getSize(); ww++)
			// System.out.println(_alphabet.getLetter(w)+"
			// "+_alphabet.getLetter(ww)+": "+qMix.get(new
			// CharacterArray(_alphabet.getLetter(w),
			// _alphabet.getLetter(ww))));
			for (int j = 0; j < _alphabet.getSize(); j++) {
				for (int k = 0; k < _alphabet.getSize(); k++) {
					CharacterArray currentMove = new CharacterArray(_alphabet.getLetter(j), _alphabet.getLetter(k));
					Character swapLetter1 = _alphabet.getLetter(letterNumber), swapLetter2 = _alphabet.getLetter(i);
					if (qMix.get(currentMove).equals(swapLetter1)) {
						qMix.put(currentMove, swapLetter2);
					} else if (qMix.get(currentMove).equals(swapLetter2)) {
						qMix.put(currentMove, swapLetter1);
					}
				}
			}
			// System.out.println("--------------------- zmieniony q: ");
			// for (int w = 0; w < _alphabet.getSize(); w++)
			// for (int ww = 0; ww < _alphabet.getSize(); ww++)
			// System.out.println(_alphabet.getLetter(w)+"
			// "+_alphabet.getLetter(ww)+": "+qMix.get(new
			// CharacterArray(_alphabet.getLetter(w),
			// _alphabet.getLetter(ww))));
			blackBoxes.add(new BlackBox(_alphabet, qMix, yMix, zMix, Arrays.asList(_k), Arrays.asList(_r), _n));
		}
		qMix = _ltGraph.getQMixLetterMap();
		for (int i = 0; i < _alphabet.getSize(); i++) {
			for (int j = i + 1; j < _alphabet.getSize(); j++) {
				if (swaps[i] == _alphabet.getSize() - 3) {
					break;
				}
				yMix = _ltGraph.getYMixLetterMap();
				zMix = _ltGraph.getZMixLetterMap();
				CharacterArray swap1 = new CharacterArray(_alphabet.getLetter(letterNumber), _alphabet.getLetter(i)),
						swap2 = new CharacterArray(_alphabet.getLetter(letterNumber), _alphabet.getLetter(j));
				if (!(yMix.get(swap1).equals(zMix.get(swap2)) || yMix.get(swap2).equals(zMix.get(swap1)))) {
					yMix.put(swap2, yMix.put(swap1, yMix.get(swap2)));
					blackBoxes.add(new BlackBox(_alphabet, qMix, yMix, zMix, Arrays.asList(_k), Arrays.asList(_r), _n));
					swaps[i]++;
					swaps[j]++;
				}
			}
		}
		return blackBoxes.toArray(new BlackBox[0]);
	}

	public BlackBox[] getBlackBoxesWithChangedMixesQYZForTests(int letterNumber, int[] swaps) {
		if (letterNumber < 0 || letterNumber > _alphabet.getSize() || swaps.length != _alphabet.getSize()) {
			return new BlackBox[0];
		}
		List<BlackBox> blackBoxes = new ArrayList<BlackBox>();
		Map<CharacterArray, Character> qMix, yMix = _ltGraph.getYMixLetterMap(), zMix = _ltGraph.getZMixLetterMap();
		for (int j = letterNumber + 1; j < _alphabet.getSize(); j++) {
			qMix = _ltGraph.getQMixLetterMap();
			// System.out.println("--------------------- wyjściowy q: ");
			// for (int w = 0; w < _alphabet.getSize(); w++)
			// for (int ww = 0; ww < _alphabet.getSize(); ww++)
			// System.out.println(_alphabet.getLetter(w)+"
			// "+_alphabet.getLetter(ww)+": "+qMix.get(new
			// CharacterArray(_alphabet.getLetter(w),
			// _alphabet.getLetter(ww))));
			for (int k = 0; k < _alphabet.getSize(); k++) {
				for (int l = 0; l < _alphabet.getSize(); l++) {
					CharacterArray currentMove = new CharacterArray(_alphabet.getLetter(k), _alphabet.getLetter(l));
					Character swapLetter1 = _alphabet.getLetter(letterNumber), swapLetter2 = _alphabet.getLetter(j);
					if (qMix.get(currentMove).equals(swapLetter1)) {
						qMix.put(currentMove, swapLetter2);
					} else if (qMix.get(currentMove).equals(swapLetter2)) {
						qMix.put(currentMove, swapLetter1);
					}
				}
			}
			// System.out.println("--------------------- zmieniony q: ");
			// for (int w = 0; w < _alphabet.getSize(); w++)
			// for (int ww = 0; ww < _alphabet.getSize(); ww++)
			// System.out.println(_alphabet.getLetter(w)+"
			// "+_alphabet.getLetter(ww)+": "+qMix.get(new
			// CharacterArray(_alphabet.getLetter(w),
			// _alphabet.getLetter(ww))));
			blackBoxes.add(new BlackBox(_alphabet, qMix, yMix, zMix, Arrays.asList(_k), Arrays.asList(_r), _n));
		}
		qMix = _ltGraph.getQMixLetterMap();
		for (int j = 0; j < _alphabet.getSize(); j++) {
			for (int k = j + 1; k < _alphabet.getSize(); k++) {
				if (swaps[j] == _alphabet.getSize() - 3) {
					break;
				}
				yMix = _ltGraph.getYMixLetterMap();
				zMix = _ltGraph.getZMixLetterMap();
				CharacterArray swap1 = new CharacterArray(_alphabet.getLetter(letterNumber), _alphabet.getLetter(j)),
						swap2 = new CharacterArray(_alphabet.getLetter(letterNumber), _alphabet.getLetter(k));
				if (!(yMix.get(swap1).equals(zMix.get(swap2)) || yMix.get(swap2).equals(zMix.get(swap1)))) {
					yMix.put(swap2, yMix.put(swap1, yMix.get(swap2)));
					blackBoxes.add(new BlackBox(_alphabet, qMix, yMix, zMix, Arrays.asList(_k), Arrays.asList(_r), _n));
					yMix.put(swap2, yMix.put(swap1, yMix.get(swap2)));
					zMix.put(swap2, zMix.put(swap1, zMix.get(swap2)));
					blackBoxes.add(new BlackBox(_alphabet, qMix, yMix, zMix, Arrays.asList(_k), Arrays.asList(_r), _n));
					swaps[j]++;
					swaps[k]++;
				}
			}
		}
		return blackBoxes.toArray(new BlackBox[0]);
	}

	public BlackBox[] getBlackBoxesWithChangedMixesQYForTests() {
		BlackBox[] blackBoxes = new BlackBox[_alphabet.getSize() * (_alphabet.getSize() - 1) / 2
				+ _alphabet.getSize() * (_alphabet.getSize() - 3) / 2 * _alphabet.getSize()];
		Map<CharacterArray, Character> qMix, yMix = _ltGraph.getYMixLetterMap(), zMix = _ltGraph.getZMixLetterMap();
		int position = 0;
		for (int i = 0; i < _alphabet.getSize(); i++) {
			for (int j = i + 1; j < _alphabet.getSize(); j++) {
				qMix = _ltGraph.getQMixLetterMap();
				// System.out.println("--------------------- wyjściowy q: ");
				// for (int w = 0; w < _alphabet.getSize(); w++)
				// for (int ww = 0; ww < _alphabet.getSize(); ww++)
				// System.out.println(_alphabet.getLetter(w)+"
				// "+_alphabet.getLetter(ww)+": "+qMix.get(new
				// CharacterArray(_alphabet.getLetter(w),
				// _alphabet.getLetter(ww))));
				for (int k = 0; k < _alphabet.getSize(); k++) {
					for (int l = 0; l < _alphabet.getSize(); l++) {
						CharacterArray currentMove = new CharacterArray(_alphabet.getLetter(k), _alphabet.getLetter(l));
						Character swapLetter1 = _alphabet.getLetter(i), swapLetter2 = _alphabet.getLetter(j);
						if (qMix.get(currentMove).equals(swapLetter1)) {
							qMix.put(currentMove, swapLetter2);
						} else if (qMix.get(currentMove).equals(swapLetter2)) {
							qMix.put(currentMove, swapLetter1);
						}
					}
				}
				// System.out.println("--------------------- zmieniony q: ");
				// for (int w = 0; w < _alphabet.getSize(); w++)
				// for (int ww = 0; ww < _alphabet.getSize(); ww++)
				// System.out.println(_alphabet.getLetter(w)+"
				// "+_alphabet.getLetter(ww)+": "+qMix.get(new
				// CharacterArray(_alphabet.getLetter(w),
				// _alphabet.getLetter(ww))));
				blackBoxes[position++] = new BlackBox(_alphabet, qMix, yMix, zMix, Arrays.asList(_k), Arrays.asList(_r), _n);
			}
		}
		qMix = _ltGraph.getQMixLetterMap();
		for (int i = 0; i < _alphabet.getSize(); i++) {
			int[] swaps = new int[_alphabet.getSize()];
			for (int j = 0; j < _alphabet.getSize(); j++) {
				for (int k = j + 1; k < _alphabet.getSize(); k++) {
					if (swaps[j] == _alphabet.getSize() - 3) {
						break;
					}
					yMix = _ltGraph.getYMixLetterMap();
					zMix = _ltGraph.getZMixLetterMap();
					CharacterArray swap1 = new CharacterArray(_alphabet.getLetter(i), _alphabet.getLetter(j)),
							swap2 = new CharacterArray(_alphabet.getLetter(i), _alphabet.getLetter(k));
					if (!(yMix.get(swap1).equals(zMix.get(swap2)) || yMix.get(swap2).equals(zMix.get(swap1)))) {
						yMix.put(swap2, yMix.put(swap1, yMix.get(swap2)));
						blackBoxes[position++] = new BlackBox(_alphabet, qMix, yMix, zMix, Arrays.asList(_k), Arrays.asList(_r), _n);
						swaps[j]++;
						swaps[k]++;
					}
				}
			}
		}
		return blackBoxes;
	}

	public BlackBox[] getBlackBoxesWithChangedMixesQYZForTests() {
		BlackBox[] blackBoxes = new BlackBox[_alphabet.getSize() * (_alphabet.getSize() - 1) / 2
				+ _alphabet.getSize() * (_alphabet.getSize() - 3) / 2 * _alphabet.getSize() * 2];
		Map<CharacterArray, Character> qMix, yMix = _ltGraph.getYMixLetterMap(), zMix = _ltGraph.getZMixLetterMap();
		int position = 0;
		for (int i = 0; i < _alphabet.getSize(); i++) {
			for (int j = i + 1; j < _alphabet.getSize(); j++) {
				qMix = _ltGraph.getQMixLetterMap();
				// System.out.println("--------------------- wyjściowy q: ");
				// for (int w = 0; w < _alphabet.getSize(); w++)
				// for (int ww = 0; ww < _alphabet.getSize(); ww++)
				// System.out.println(_alphabet.getLetter(w)+"
				// "+_alphabet.getLetter(ww)+": "+qMix.get(new
				// CharacterArray(_alphabet.getLetter(w),
				// _alphabet.getLetter(ww))));
				for (int k = 0; k < _alphabet.getSize(); k++) {
					for (int l = 0; l < _alphabet.getSize(); l++) {
						CharacterArray currentMove = new CharacterArray(_alphabet.getLetter(k), _alphabet.getLetter(l));
						Character swapLetter1 = _alphabet.getLetter(i), swapLetter2 = _alphabet.getLetter(j);
						if (qMix.get(currentMove).equals(swapLetter1)) {
							qMix.put(currentMove, swapLetter2);
						} else if (qMix.get(currentMove).equals(swapLetter2)) {
							qMix.put(currentMove, swapLetter1);
						}
					}
				}
				// System.out.println("--------------------- zmieniony q: ");
				// for (int w = 0; w < _alphabet.getSize(); w++)
				// for (int ww = 0; ww < _alphabet.getSize(); ww++)
				// System.out.println(_alphabet.getLetter(w)+"
				// "+_alphabet.getLetter(ww)+": "+qMix.get(new
				// CharacterArray(_alphabet.getLetter(w),
				// _alphabet.getLetter(ww))));
				blackBoxes[position++] = new BlackBox(_alphabet, qMix, yMix, zMix, Arrays.asList(_k), Arrays.asList(_r), _n);
			}
		}
		qMix = _ltGraph.getQMixLetterMap();
		for (int i = 0; i < _alphabet.getSize(); i++) {
			int[] swaps = new int[_alphabet.getSize()];
			for (int j = 0; j < _alphabet.getSize(); j++) {
				for (int k = j + 1; k < _alphabet.getSize(); k++) {
					if (swaps[j] == _alphabet.getSize() - 3) {
						break;
					}
					yMix = _ltGraph.getYMixLetterMap();
					zMix = _ltGraph.getZMixLetterMap();
					CharacterArray swap1 = new CharacterArray(_alphabet.getLetter(i), _alphabet.getLetter(j)),
							swap2 = new CharacterArray(_alphabet.getLetter(i), _alphabet.getLetter(k));
					if (!(yMix.get(swap1).equals(zMix.get(swap2)) || yMix.get(swap2).equals(zMix.get(swap1)))) {
						yMix.put(swap2, yMix.put(swap1, yMix.get(swap2)));
						blackBoxes[position++] = new BlackBox(_alphabet, qMix, yMix, zMix, Arrays.asList(_k), Arrays.asList(_r), _n);
						yMix.put(swap2, yMix.put(swap1, yMix.get(swap2)));
						zMix.put(swap2, zMix.put(swap1, zMix.get(swap2)));
						blackBoxes[position++] = new BlackBox(_alphabet, qMix, yMix, zMix, Arrays.asList(_k), Arrays.asList(_r), _n);
						swaps[j]++;
						swaps[k]++;
					}
				}
			}
		}
		return blackBoxes;
	}

	public static void main(String[] args) {
		Map<CharacterArray, Character> map = new HashMap<CharacterArray, Character>();
		map.put(new CharacterArray('a', 'a'), 'a');
		map.put(new CharacterArray('a', 'b'), 'b');
		map.put(new CharacterArray('a', 'c'), 'c');
		map.put(new CharacterArray('a', 'd'), 'd');
		map.put(new CharacterArray('b', 'a'), 'a');
		map.put(new CharacterArray('b', 'b'), 'b');
		map.put(new CharacterArray('b', 'c'), 'c');
		map.put(new CharacterArray('b', 'd'), 'd');
		map.put(new CharacterArray('c', 'a'), 'a');
		map.put(new CharacterArray('c', 'b'), 'b');
		map.put(new CharacterArray('c', 'c'), 'c');
		map.put(new CharacterArray('c', 'd'), 'd');
		map.put(new CharacterArray('d', 'a'), 'a');
		map.put(new CharacterArray('d', 'b'), 'b');
		map.put(new CharacterArray('d', 'c'), 'c');
		map.put(new CharacterArray('d', 'd'), 'd');
		Map<CharacterArray, Character> map2 = new HashMap<CharacterArray, Character>(map);
		map.put(new CharacterArray('a', 'a'), 'd');
		map.put(new CharacterArray('a', 'b'), 'c');
		map.put(new CharacterArray('a', 'c'), 'b');
		map.put(new CharacterArray('a', 'd'), 'a');
		map.put(new CharacterArray('b', 'a'), 'd');
		map.put(new CharacterArray('b', 'b'), 'c');
		map.put(new CharacterArray('b', 'c'), 'b');
		map.put(new CharacterArray('b', 'd'), 'a');
		map.put(new CharacterArray('c', 'a'), 'd');
		map.put(new CharacterArray('c', 'b'), 'c');
		map.put(new CharacterArray('c', 'c'), 'b');
		map.put(new CharacterArray('c', 'd'), 'a');
		map.put(new CharacterArray('d', 'a'), 'd');
		map.put(new CharacterArray('d', 'b'), 'c');
		map.put(new CharacterArray('d', 'c'), 'b');
		map.put(new CharacterArray('d', 'd'), 'a');
		map.getClass();
		map2.getClass();
		LinkedHashSet<Character> letterSet = new LinkedHashSet<Character>();
		for (Character letter : new Character[] { 'a', 'b', 'c', 'd' }){
			letterSet.add(letter);
		}
		Alphabet alphabet = new Alphabet(letterSet);
		alphabet.getClass();
		// BlackBox b = new BlackBox(alphabet, map, map, map2, null, null, 0);
		// BlackBox b2 = new BlackBox(alphabet, 0);
		// b2.getClass();
		// BlackBox b = new BlackBox(null, null, 0);
		BlackBox b = new BlackBox(7);
		b.getClass();
		try {
			// int ii = 1000000;
			int ii = 10;
			// Character[] chars = new Character[ii];
			char[] cc = new char[ii];
			for (int i = 0; i < ii; i++) {
				// chars[i] = 'a';
				cc[i] = 'a';
			}
			String word = new String(cc);
			String enccc = b.encrypt(word);
			System.out.println(word);
			System.out.println(enccc);
			System.out.println(b.decrypt(enccc));
			System.out.println("=================================================================================");
			b._encryptionChars = word.toCharArray();
			b._encryptionCharsLength = b._encryptionChars.length;
			// b._encryptionCharsHalfLength = b._encryptionCharsLength / 2;
			// System.out.println(word);
			// System.out.println(b._encryptionChars);
			System.out.println("Start");
			long start = System.nanoTime();
			String aa1 = b.encryptStartingFrom_old(word);
			long mid = System.nanoTime();
			String aa2 = b.encryptStartingFrom_old2(word);
			long end = System.nanoTime();
			String aa3 = b.encrypt(word);
			long end2 = System.nanoTime();
			System.out.println("1: " + (mid - start));
			System.out.println("2: " + (end - mid));
			System.out.println("3: " + (end2 - end));
			start = System.nanoTime();
			aa1 = b.encryptStartingFrom_old(word);
			mid = System.nanoTime();
			aa2 = b.encryptStartingFrom_old2(word);
			end = System.nanoTime();
			aa3 = b.encrypt(word);
			end2 = System.nanoTime();
			System.out.println(b.decryptStartingFrom_old(aa1).equals(word));
			System.out.println(b.decryptStartingFrom_old2(aa2).equals(word));
			System.out.println(b.decrypt(aa3).equals(word));
			// String aa2=new String(b._encryptionChars);
			// char[] aa = new char[aa1.length];
			// for (int i = 0; i < aa1.length; i++) {
			// aa[i]=aa1[i];
			// System.out.print(aa[i]);
			// }
			System.out.println();
			// System.out.println(aa1);
			// System.out.println(aa2);
			// System.out.println(b.decrypt(aa1));
			// System.out.println(b.decrypt_old(aa2));
			System.out.println(aa1.equals(aa2));
			System.out.println("1: " + (mid - start));
			System.out.println("2: " + (end - mid));
			System.out.println("3: " + (end2 - end));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// BlackBox[] bb = b.getBlackBoxesWithChangedMixesQYForTests();
		// BlackBox[] bb2 = b2.getBlackBoxesWithChangedMixesQYZForTests();
		// int[] swaps3 = new int[alphabet.getSize()];
		// for (int i = 0; i < alphabet.getSize(); i++) {
		// BlackBox[] bb3 = b.getBlackBoxesWithChangedMixesQYForTests(i,
		// swaps3);
		// bb3.getClass();
		// }
		// int[] swaps4 = new int[alphabet.getSize()];
		// for (int i = 0; i < alphabet.getSize(); i++) {
		// BlackBox[] bb4 = b2.getBlackBoxesWithChangedMixesQYZForTests(i,
		// swaps4);
		// bb4.getClass();
		// }
	}

	private Object decryptionLock = new Object();
	private Object encryptionLock = new Object();
	private int _n = 1;
	private int _2n = 2;
	private Boolean[] _k = new Boolean[] { false, true }, _r = new Boolean[] { false, true };
	private int _kSize = _k.length;
	private int _rSize = _r.length;
	private LTGraph _ltGraph;
	private Alphabet _alphabet;
	private char[] _encryptionChars;
	private char[] _decryptionChars;
	private int _encryptionCharsLength;
	// private int _encryptionCharsHalfLength;
	private int _decryptionCharsLength;
	// private int _decryptionCharsHalfLength;
	// private boolean _encryptionBackward;
	// private boolean _decryptionBackward;
	private byte _encryptionDirection;
	private byte _decryptionDirection;
	private int[][] _encryptionDirections;
	private int[][] _decryptionDirections;
	private Random _generator = RandomUtil.getRandom();
}
