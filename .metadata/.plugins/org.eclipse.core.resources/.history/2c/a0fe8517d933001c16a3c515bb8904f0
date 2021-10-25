package pd.web;

import javax.swing.*;
import java.awt.event.*;

/**
 * @author £ukasz Jêda
 * 
 */
public abstract class PDInputDialog<T> {

	public PDInputDialog(String buttonLabel) {
		_buttonLabel = buttonLabel;
		setDefaultValue();
	}

	public JPanel getDialogPanel() {
		JButton button = new JButton(_buttonLabel);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				String entry = JOptionPane.showInputDialog(null, "Wpisz " + _buttonLabel + " (lub pozostaw to pole puste by ustawiæ wartoœæ domyœln¹): ",
						_buttonLabel, JOptionPane.QUESTION_MESSAGE);
				if (entry != null) {
					if (entry.equals("")) {
						setDefaultValue();
						JOptionPane.showMessageDialog(null, _buttonLabel + " - ustawiona wartoœæ domyœlna (" + getStringValue() + ")", _buttonLabel,
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						String errorMessage = validateEntry(entry);
						if (errorMessage == null) {
							JOptionPane.showMessageDialog(null, _buttonLabel + " : " + getStringValue(), _buttonLabel, JOptionPane.INFORMATION_MESSAGE);
						} else {
							setDefaultValue();
							JOptionPane.showMessageDialog(null,
									errorMessage + " " + _buttonLabel + " - ustawiona wartoœæ " + "domyœlna (" + getStringValue() + ")", _buttonLabel,
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Klikn¹³eœ anuluj. " + _buttonLabel + " : " + getStringValue(), _buttonLabel,
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		JPanel dialogPanel = new JPanel();
		dialogPanel.add(button);
		return dialogPanel;
	}

	public T getValue() {
		return _value;
	}

	protected abstract String validateEntry(String entry);

	protected abstract void setDefaultValue();

	protected abstract String getStringValue();

	protected final String _buttonLabel;
	protected T _value;
}