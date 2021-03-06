package pd.test;

import java.io.*;

public class ResultsSaver {

	private static final int[] sequenceLengthForTestMode = new int[TestPerformer.highestTestMode];

	static {
		// for (int i = 0; i < TestPerformer.highestTestMode; i++) {
		// sequenceLengthForTestMode[i] = 1024;
		// }

		// without initial state randomization
		// sequenceLengthForTestMode[0] = 10158080;
		// sequenceLengthForTestMode[1] = 10076160;
		// sequenceLengthForTestMode[2] = 10000000;
		// sequenceLengthForTestMode[3] = 10000000;
		// sequenceLengthForTestMode[4] = 10000000;
		// sequenceLengthForTestMode[5] = 10568960;
		// sequenceLengthForTestMode[6] = 10568960;
		// sequenceLengthForTestMode[7] = 10568960;
		// sequenceLengthForTestMode[8] = 10568960;
		// sequenceLengthForTestMode[9] = 19640320;
		// sequenceLengthForTestMode[10] = 19322880;

		sequenceLengthForTestMode[0] = 10072320;
		sequenceLengthForTestMode[1] = 10072320;
		sequenceLengthForTestMode[2] = 10000080;
		sequenceLengthForTestMode[3] = 10000080;
		sequenceLengthForTestMode[4] = 10000080;
		sequenceLengthForTestMode[5] = 10651530;
		sequenceLengthForTestMode[6] = 10651530;
		sequenceLengthForTestMode[7] = 10651530;
		sequenceLengthForTestMode[8] = 10651530;
		sequenceLengthForTestMode[9] = 19793760;
		sequenceLengthForTestMode[10] = 19473840;
	}

	public ResultsSaver(int blackBoxesNumber, String resultsDir) throws Exception {
		if (blackBoxesNumber < 1) {
			throw new Exception("Liczba powtórzeń testu powinna być dodatnia.");
		}
		_blackBoxesNumber = blackBoxesNumber;
		_resultsDir = validateResultsDir(resultsDir);
	}

	private File validateResultsDir(String resultsDir) throws Exception {
		File dir = new File(resultsDir, "enhanced_algorithm_tests");
		if (dir.exists() && dir.isDirectory()) {
			return dir;
		} else {
			throw new Exception("Podano błędną ścieżkę do katalogu z wynikami testów.");
		}
	}

	private void runTestAndSaveResults(int testMode, boolean quick) throws Exception {
		File testModeDir = prepareResultsDir(testMode);
		int desiredBitArraySize = quick ? 1024 : sequenceLengthForTestMode[testMode - 1];
		TestPerformer testPerformer = new TestPerformer(_blackBoxesNumber, testMode, desiredBitArraySize, _encryptionOrder);
		testPerformer.setDebugModeDisabled(true);
		for (int blackbox = 0; blackbox < _blackBoxesNumber; blackbox++) {
			boolean[] bits = (boolean[]) testPerformer.execute(new int[] { blackbox });
			FileWriter bitsOutput = new FileWriter(new File(testModeDir, getResultFileName(blackbox, _blackBoxesNumber)));
			for (boolean bit : bits) {
				bitsOutput.write(bit ? "1" : "0");
			}
			bitsOutput.close();
		}
	}

	private File prepareResultsDir(int testMode) {
		File testModeDir = new File(_resultsDir, "" + testMode);
		if (testModeDir.exists()) {
			for (File file : testModeDir.listFiles()) {
				file.delete();
			}
		} else {
			testModeDir.mkdir();
		}
		return testModeDir;
	}

	private static String getResultFileName(int blackbox, int blackBoxesNumber) {
		String leadingZeros = "";
		if (blackBoxesNumber > 0) {
			int rank = (int) Math.log10(blackBoxesNumber);
			for (int i = 0; i < rank; i++) {
				leadingZeros += blackbox < Math.pow(10, i + 1) ? "0" : "";
			}
		}
		return "bits-blackbox_" + leadingZeros + blackbox + ".txt";
	}

	private static void startAllTests(int blackBoxesNumber, boolean quick) {
		String workspaceRootDir = System.getProperty("user.dir", "C:\\Users\\Jedi\\Dropbox\\studia_doktoranckie\\doktorat\\workspace_new_eclipse_mars\\PD3\\");

		ResultsSaver resultsSaver = null;
		try {
			resultsSaver = new ResultsSaver(blackBoxesNumber, workspaceRootDir);
		} catch (Exception e) {
			System.err.println("błąd inicjalizacji testów:  " + e.getLocalizedMessage());
			return;
		}

		for (int testMode = TestPerformer.highestTestMode; testMode > 0; testMode--) {
			try {
				resultsSaver.runTestAndSaveResults(testMode, quick);
			} catch (Exception e) {
				System.err.println("Zapisywanie wyników dla testu: " + testMode + " nie udało się: " + e.getLocalizedMessage());
			}
		}
	}

	private static void printUsage() {
		System.out.println("Użycie: java -jar PD_ResultsSaver [ <liczba_powtórzeń_testu> [ --quick ] ]");
	}

	public static void main(String[] args) {
		if (args.length > 2) {
			printUsage();
			return;
		}
		int blackBoxesNumber = 500;
		if (args.length > 0) {
			try {
				blackBoxesNumber = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("Podano złą liczbę powtórzeń testu.");
				printUsage();
				return;
			}
		}

		boolean quick = false;
		if (args.length > 1 && "--quick".equals(args[1])) {
			quick = true;
		}

		startAllTests(blackBoxesNumber, quick);
	}

	private int _blackBoxesNumber;
	private final File _resultsDir;
	private static final int _encryptionOrder = 1;
}
