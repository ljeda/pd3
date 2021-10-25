package pd.web;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * @author Łukasz Jęda
 * 
 */
public class PDButton extends JButton {

	public PDButton(String text, final PDApplet applet, final boolean encrypt) {
		super(text);
		setAlignmentX(Component.CENTER_ALIGNMENT);
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (encrypt) {
					applet.encrypt();
				} else {
					applet.decrypt();
				}
			}

		});
	}

	private static final long serialVersionUID = 5136450535908387953L;
}
