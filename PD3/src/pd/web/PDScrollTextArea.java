package pd.web;

import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Łukasz Jęda
 * 
 */
public class PDScrollTextArea extends JTextArea {

	public PDScrollTextArea(String text, int rows, int columns) {
		super(rows, columns);
		setBorder(new TitledBorder(new EtchedBorder(), text));
		setLineWrap(true);
		_scroll = new JScrollPane(this);
		_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	}

	public JScrollPane getScroll() {
		return _scroll;
	}

	private final JScrollPane _scroll;
	private static final long serialVersionUID = 2801508141971576400L;
}
