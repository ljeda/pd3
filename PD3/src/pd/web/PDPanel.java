package pd.web;

import java.io.*;
import java.util.*;

import javax.swing.*;

/**
 * @author £ukasz Jêda
 * 
 */
public class PDPanel extends JPanel {

	public PDPanel(PDApplet applet) {

		JPanel panel1 = new JPanel(), panel2 = new JPanel(), panel3 = new JPanel(), panel4 = new JPanel(), panel5 = new JPanel(), panel6 = new JPanel();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel label1 = new JLabel("Ustawienia:");
		panel1.add(label1);
		add(panel1);

		panel2.add(_alphabetDialog.getDialogPanel());
		panel2.add(_kWordDialog.getDialogPanel());
		panel2.add(_rWordDialog.getDialogPanel());
		panel2.add(_nIterationNumberDialog.getDialogPanel());
		add(panel2);

		panel3.add(_qMixArea.getScroll());
		panel3.add(_yMixArea.getScroll());
		panel3.add(_zMixArea.getScroll());
		add(panel3);

		JLabel label2 = new JLabel("Szyfrowanie/Odszyfrowywanie:");
		panel4.add(label2);
		add(panel4);

		panel5.add(_plaintextArea.getScroll());

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		PDButton encryptButton = new PDButton("Zaszyfruj", applet, true);
		buttonPanel.add(encryptButton);
		PDButton decryptButton = new PDButton("Odszyfruj", applet, false);
		buttonPanel.add(decryptButton);
		panel5.add(buttonPanel);

		panel5.add(_cryptotextArea.getScroll());
		add(panel5);

		PrintStream printStream = new PrintStream(_outputStream);
		System.setOut(printStream);
		System.setErr(printStream);
		panel6.add(_outputStream.getScroll());
		add(panel6);
	}

	public String getPlaintext() {
		return _plaintextArea.getText();
	}

	public void setPlaintext(String plaintext) {
		_plaintextArea.setText(plaintext);
	}

	public String getCryptotext() {
		return _cryptotextArea.getText();
	}

	public void setCryptotext(String cryptotext) {
		_cryptotextArea.setText(cryptotext);
	}

	public LinkedHashSet<Character> getAlphabet() {
		return _alphabetDialog.getValue();
	}

	public List<Boolean> getKWord() {
		return _kWordDialog.getValue();
	}

	public List<Boolean> getRWord() {
		return _rWordDialog.getValue();
	}

	public Integer getNIterationNumber() {
		return _nIterationNumberDialog.getValue();
	}

	public String getQMix() {
		return _qMixArea.getText();
	}

	public String getYMix() {
		return _yMixArea.getText();
	}

	public String getZMix() {
		return _zMixArea.getText();
	}

	public void clearOutputTextArea() {
		_outputStream.clearTextArea();
	}

	private PDAlphabetInputDialog _alphabetDialog = new PDAlphabetInputDialog("alfabet");
	private PDBinaryWordInputDialog _kWordDialog = new PDBinaryWordInputDialog("s³owo k");
	private PDBinaryWordInputDialog _rWordDialog = new PDBinaryWordInputDialog("s³owo r");
	private PDNonnegativeNumberInputDialog _nIterationNumberDialog = new PDNonnegativeNumberInputDialog("n");
	private PDScrollTextArea _qMixArea = new PDScrollTextArea("tQ (puste = domyœlny)", 10, 12);
	private PDScrollTextArea _yMixArea = new PDScrollTextArea("tY (puste = domyœlny)", 10, 12);
	private PDScrollTextArea _zMixArea = new PDScrollTextArea("tZ (puste = domyœlny)", 10, 12);
	private PDScrollTextArea _plaintextArea = new PDScrollTextArea("Tekst", 10, 30);
	private PDScrollTextArea _cryptotextArea = new PDScrollTextArea("Kryptogram", 10, 30);
	private PDOutputStream _outputStream = new PDOutputStream("Komunikaty", 3, 70);
	private static final long serialVersionUID = -7742889749874038510L;
}