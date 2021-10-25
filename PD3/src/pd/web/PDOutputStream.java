package pd.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.*;

/**
 * @author Łukasz Jęda
 * 
 */
public class PDOutputStream extends OutputStream {

	public JScrollPane getScroll() {
		return _outputTextArea.getScroll();
	}

	public void clearTextArea() {
		_outputTextArea.setText(null);
	}

	public PDOutputStream(String text, int rows, int columns) {
		_outputTextArea = new PDScrollTextArea(text, rows, columns);
	}

	@Override
	public void write(int b) throws IOException {
		_outputTextArea.append(new String(new byte[] { (byte) b }, "Cp1250"));
		_outputTextArea.setCaretPosition(_outputTextArea.getDocument().getLength());
	}

	private PDScrollTextArea _outputTextArea;
}
