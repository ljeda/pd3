package pd.web;

import java.util.*;

/**
 * @author Łukasz Jęda
 * 
 */
public class PDBinaryWordInputDialog extends PDInputDialog<List<Boolean>> {

	public PDBinaryWordInputDialog(String buttonLabel) {
		super(buttonLabel);
	}

	@Override
	protected String validateEntry(String entry) {
		_value = new ArrayList<Boolean>();
		for (int i = 0; i < entry.length(); i++) {
			char c = entry.charAt(i);
			if (c == '0') {
				_value.add(false);
			} else if (c == '1') {
				_value.add(true);
			} else {
				return "słowo powinno składać się wyłącznie z zer i jedynek.";
			}
		}
		_stringValue = entry;
		return null;
	}

	@Override
	protected String getStringValue() {
		return _stringValue;
	}

	@Override
	protected void setDefaultValue() {
		_value = new ArrayList<Boolean>();
		_value.add(false);
		_value.add(true);
		_stringValue = "01";
	}

	private String _stringValue;
}
