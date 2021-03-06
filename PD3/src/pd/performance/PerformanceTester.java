package pd.performance;

import java.io.*;
import java.text.*;
import java.util.*;

import pd.core.*;
import pd.test.*;
import pd.util.*;

/**
 * @author Łukasz Jęda
 *
 */
public class PerformanceTester {

	public PerformanceTester(int blackBoxesNumber, int n, int alphabetSize, int keysLength, int sampleSize) {
		Alphabet alphabet;
		try {
			alphabet = AlphabetUtil.generateAlphabet(alphabetSize);
			validateInput(blackBoxesNumber, n, keysLength, sampleSize);
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
			_blackBoxesGenerator = null;
			return;
		}
		_blackBoxesGenerator = new BlackBoxesGenerator(_generator, blackBoxesNumber, alphabet, n, keysLength, true);
		_n = n;
		_alphabetSize = alphabetSize;
		_keysLength = keysLength;
		_sample = TextUtil.generateRandomPlaintext(_generator, alphabetSize, sampleSize);
		_letterBitLength = AlphabetUtil.calculateLetterBitLength(alphabetSize);
		_encryptionTimes = new long[_blackBoxesGenerator.getSize()];
		_decryptionTimes = new long[_blackBoxesGenerator.getSize()];
		_encryptionSizes = new int[_blackBoxesGenerator.getSize()];
		_decryptionSizes = new int[_blackBoxesGenerator.getSize()];
	}

	private void validateInput(int blackBoxesNumber, int n, int keysLength, int sampleSize) throws Exception {
		if (blackBoxesNumber < 1) {
			throw new Exception("Liczba powtórzeń testu powinna być większa od 0.");
		}
		if (n < 0) {
			throw new Exception("Liczba iteracji algorytmu powinna być nieujemna.");
		}
		if (keysLength < 1) {
			throw new Exception("Długość kluczy powinna być większa od 0.");
		}
		if (sampleSize < 1) {
			throw new Exception("Długość próbki powinna być większa od 0.");
		}
	}

	public void performTest(String subfolder) {
		if (_blackBoxesGenerator == null) {
			System.err.println("Instancje do testów zostały zainicjalizowane niepoprawnie.");
			return;
		}
		if (_sample == null) {
			System.err.println("Próbka do testów została zainicjalizowana niepoprawnie.");
			return;
		}
		for (int i = 0; i < _blackBoxesGenerator.getSize(); i++) {
			try {
				BlackBox currentBlackBox = _blackBoxesGenerator.getBlackBox(i);
				long startTime = System.nanoTime();
				String encryptedSample = currentBlackBox.encrypt(_sample);
				long midTime = System.nanoTime();
				String decryptedSample = currentBlackBox.decrypt(encryptedSample);
				long endTime = System.nanoTime();
				// just to check
				for (char ch : _sample.toCharArray()) {
					int in = ch;
					System.out.println("1:" + in);
				}
				for (char ch : decryptedSample.toCharArray()) {
					int in = ch;
					System.out.println("2:" + in);
				}
				System.out.println("test".equals("test"));
				System.out.println("1:" + _sample);
				System.out.println("2:" + decryptedSample);
				if (!_sample.equals(decryptedSample)) {
					System.err.println("Algorytm został zaburzony, " + "zdekodowana wiadomość jest inna niż wyjściowa.");
					break;
				}
				_encryptionTimes[i] = midTime - startTime;
				_decryptionTimes[i] = endTime - midTime;
				_encryptionSizes[i] = _sample.length() * _letterBitLength;
				_decryptionSizes[i] = encryptedSample.length() * _letterBitLength;
			} catch (Exception e) {
				System.err.println("Wystąpił błąd przy próbie pobrania instancji " + "do testów wydajnościowych: " + e.getLocalizedMessage());
				continue;
			}
		}
		writeResults(subfolder);
	}

	private void writeResults(String subfolder) {
		if (subfolder == null) {
			subfolder = "";
		}
		String timestamp = new SimpleDateFormat("yyyy-MM-dd__HH_mm_ss").format(new Date());
		String rootFolder = "D:\\studia\\III stopień\\doktorat\\workspace\\PD3\\performance_tests\\";
		String testFolder = rootFolder;
		try {
			File directory = new File(rootFolder + subfolder);
			if (directory.exists()) {
				if (directory.isDirectory()) {
					testFolder = directory.getAbsolutePath();
				}
			} else {
				if (directory.mkdir()) {
					testFolder = directory.getAbsolutePath();
				}
			}
		} catch (Exception e) {
			System.err.println("Wystąpił błąd przy próbie stworzenia folderu do zapisu wyników testów wydajnościowych: " + e.getLocalizedMessage());
		}
		String filepath = testFolder + "\\performance_test_" + timestamp + ".txt";
		PrintWriter writer = null;
		try {
			try {
				writer = new PrintWriter(filepath, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				writer = new PrintWriter(filepath);
			}
			writer.println(prepareCenteredString("Performance Test: " + timestamp));
			writer.println(prepareCenteredString("Encryption/Decryption Order: " + _n));
			writer.println(prepareCenteredString("Test Instances Number: " + _blackBoxesGenerator.getSize()));
			writer.println(prepareCenteredString("Alphabet Size: " + _alphabetSize));
			writer.println(prepareCenteredString("Keys Length: " + _keysLength));
			writer.println();
			writer.println();
			writer.println(_bar);
			writer.println(prepareTableRow("Encryption Time [ns]", "Decryption Time [ns]", "Sample Bit Size [b]", "Encrypted Sample Bit Size [b]",
					"Encryption Bit Rate [Kb/s]", "Decryption Bit Rate [Kb/s]"));
			writer.println(_bar);
			for (int i = 0; i < _blackBoxesGenerator.getSize(); i++) {
				writer.println(prepareTableRow(_encryptionTimes[i], _decryptionTimes[i], _encryptionSizes[i], _decryptionSizes[i]));
			}
			writer.println(_bar);
			writer.println(_bar);
			writer.println(prepareCenteredString("Overall Statistics"));
			writer.println(_bar);
			writer.println(_bar);
			writer.println(prepareTableRow("Sum", "Sum", "Sum", "Sum", "Mean Value", "Mean Value"));
			writer.println(_bar);
			writer.println(prepareMeanValuesRow());
			writer.println(_bar);
		} catch (FileNotFoundException e) {
			System.err.println("Zapisywanie wyników do pliku nie powiodło się: " + e.getLocalizedMessage());
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	private static final String prepareCenteredString(String sentence) {
		if (sentence == null) {
			sentence = "";
		}
		int repetitions = (_lineLength + 1 - sentence.length()) / 2;
		for (int i = 0; i < repetitions; i++) {
			sentence = " " + sentence;
		}
		return sentence;
	}

	private final String prepareMeanValuesRow() {
		long encryptionTime = 0, decryptionTime = 0, encryptionSize = 0, decryptionSize = 0;
		for (int i = 0; i < _blackBoxesGenerator.getSize(); i++) {
			encryptionTime += _encryptionTimes[i];
			decryptionTime += _decryptionTimes[i];
			encryptionSize += _encryptionSizes[i];
			decryptionSize += _decryptionSizes[i];
		}
		return prepareTableRow(encryptionTime, decryptionTime, encryptionSize, decryptionSize);
	}

	private static final String prepareTableRow(long encryptionTime, long decryptionTime, long encryptionSize, long decryptionSize) {
		return prepareTableRow(String.valueOf(encryptionTime), String.valueOf(decryptionTime), String.valueOf(encryptionSize), String.valueOf(decryptionSize),
				String.valueOf(calculateBitRate(encryptionTime, encryptionSize)), String.valueOf(calculateBitRate(decryptionTime, decryptionSize)));
	}

	private static final String prepareTableRow(String encryptionTime, String decryptionTime, String encryptionSize, String decryptionSize,
			String encryptionBitRate, String decryptionBitRate) {
		return padCell(encryptionTime) + padCell(decryptionTime) + padCell(encryptionSize) + padCell(decryptionSize) + padCell(encryptionBitRate)
				+ padCell(decryptionBitRate) + "|";
	}

	private static final String padCell(String sentence) {
		if (sentence == null) {
			sentence = "";
		}
		sentence = "| " + sentence;
		int repetitions = _lineLength / _ColumnNumber - sentence.length();
		for (int i = 0; i < repetitions; i++) {
			sentence += " ";
		}
		return sentence;
	}

	private static final double calculateBitRate(long time, long size) {
		if (time == 0) {
			return 0;
		}
		return (double) size / (double) time * 1000000;
	}

	private static final String horizontalBar() {
		String bar = "-";
		for (int i = 0; i < _lineLength; i++) {
			bar += "-";
		}
		return bar;
	}

	public static void firstSuite() {
		PerformanceTester tester1 = new PerformanceTester(100, 128, 32, 128, 1024);
		tester1.performTest("first_suite");
		PerformanceTester tester2 = new PerformanceTester(100, 10, 32, 128, 1024);
		tester2.performTest("first_suite");
		PerformanceTester tester3 = new PerformanceTester(100, 0, 32, 128, 1024);
		tester3.performTest("first_suite");
		// niewykonane
		// PerformanceTester tester4 = new PerformanceTester(100, 128, 32, 128,
		// 1024000);
		// tester4.performTest("first_suite");
		// PerformanceTester tester5 = new PerformanceTester(100, 10, 32, 128,
		// 1024000);
		// tester5.performTest("first_suite");
		// PerformanceTester tester6 = new PerformanceTester(100, 0, 32, 128,
		// 1024000);
		// tester6.performTest("first_suite");
	}

	public static void secondSuite() {
		PerformanceTester tester1 = new PerformanceTester(100, 128, 32, 128, 1024);
		tester1.performTest("second_suite");
		PerformanceTester tester2 = new PerformanceTester(100, 10, 32, 128, 1024);
		tester2.performTest("second_suite");
		PerformanceTester tester3 = new PerformanceTester(100, 0, 32, 128, 1024);
		tester3.performTest("second_suite");
	}

	public static void thirdSuite() {
		String folder = "third_suite";
		PerformanceTester tester1;
		tester1 = new PerformanceTester(100, 128, 32, 128, 10240);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 10, 32, 128, 10240);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 0, 32, 128, 10240);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 128, 32, 128, 102400);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 10, 32, 128, 102400);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 0, 32, 128, 102400);
		tester1.performTest(folder);
	}

	public static void fourthSuite() {
		String folder = "";
		PerformanceTester tester1;
		tester1 = new PerformanceTester(100, 0, 32, 128, 1024);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 3, 32, 128, 1024);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 0, 32, 128, 10240);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 3, 32, 128, 10240);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 0, 32, 128, 102400);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 3, 32, 128, 102400);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 0, 32, 128, 1024000);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 3, 32, 128, 1024000);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 0, 32, 128, 10240000);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 3, 32, 128, 10240000);
		tester1.performTest(folder);
		folder = "";
		tester1 = new PerformanceTester(100, 10, 32, 128, 1024);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 10, 32, 128, 10240);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 10, 32, 128, 102400);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 10, 32, 128, 1024000);
		tester1.performTest(folder);
		// niewykonane
		// tester1 = new PerformanceTester(100, 10, 32, 128, 10240000);
		// tester1.performTest(folder);
	}

	public static void fifthSuite() {
		String folder = "fifth_suite";
		PerformanceTester tester1;
		tester1 = new PerformanceTester(100, 0, 32, 128, 102400000);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 0, 32, 128, 1024000000);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 3, 32, 128, 102400000);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 3, 32, 128, 1024000000);
		tester1.performTest(folder);
	}

	public static void sixthSuite() {
		String folder = "sixth_suite";
		PerformanceTester tester1;
		tester1 = new PerformanceTester(100, 1, 32, 128, 1024);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 1, 32, 128, 10240);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 1, 32, 128, 102400);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 1, 32, 128, 1024000);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 1, 32, 128, 10240000);
		tester1.performTest(folder);
		tester1 = new PerformanceTester(100, 10, 32, 128, 10240000);
		tester1.performTest(folder);
	}

	public static void debugSuite() {
		String folder = "debug_suite";
		PerformanceTester tester1;
		tester1 = new PerformanceTester(10, 1, 32, 128, 10);
		tester1.performTest(folder);
	}

	public static void main(String[] args) {
		debugSuite();
		// PerformanceTester tester1 = new PerformanceTester(2, 128, 32, 128,
		// 1024);
		// tester1.performTest("first_suite");
	}

	private BlackBoxesGenerator _blackBoxesGenerator;
	private int _n;
	private int _alphabetSize;
	private int _keysLength;
	private String _sample;
	private int _letterBitLength;
	private long[] _encryptionTimes, _decryptionTimes;
	private int[] _encryptionSizes, _decryptionSizes;
	// private final Random _generator = new Random(12 + 07 + 2014);
	private final Random _generator = RandomUtil.getRandom();
	// _lineLength must be divisible by _ColumnNumber by convention
	private static final int _ColumnNumber = 6;
	// mind the fact that actual line length is one letter bigger than below
	// value
	private static final int _lineLength = 186;
	private static final String _bar = horizontalBar();

}
