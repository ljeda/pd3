package pd.web;

import java.nio.*;
import java.nio.charset.*;
import java.util.*;

/**
 * @author £ukasz Jêda
 * 
 */
public class PDAlphabetInputDialog extends PDInputDialog<LinkedHashSet<Character>> {

	public PDAlphabetInputDialog(String buttonLabel) {
		super(buttonLabel);
	}

	@Override
	protected String validateEntry(String entry) {
		LinkedHashSet<Character> letterSet = new LinkedHashSet<Character>();
		for (int i = 0; i < entry.length(); i++) {
			letterSet.add(entry.charAt(i));
		}
		_value = letterSet;
		return null;
	}

	@Override
	protected String getStringValue() {
		return _value.toString();
	}

	@Override
	protected void setDefaultValue() {
		LinkedHashSet<Character> letterSet = new LinkedHashSet<Character>();
		final Charset charset = Charset.forName("CP1250");
		for (int i = Character.MIN_VALUE; i < Character.MAX_VALUE; i++) {
			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.putInt(i);
			String trimmedString = new String(bb.array(), charset).trim();
			if (!trimmedString.isEmpty()) {
				letterSet.add(trimmedString.charAt(0));
			}
		}
		letterSet.add(' ');
		letterSet.add('\n');
		letterSet.add('\r');
		letterSet.add('\t');

		_value = letterSet;

	}
}
