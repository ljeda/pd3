package pd.web;

/**
 * @author �ukasz J�da
 * 
 */
public class PDNonnegativeNumberInputDialog extends PDInputDialog<Integer> {

	public PDNonnegativeNumberInputDialog(String buttonLabel) {
		super(buttonLabel);
	}

	@Override
	protected String validateEntry(String entry) {
		try {
			if ('-' != entry.charAt(0)) {
				_value = Integer.parseInt(entry);
				return null;
			}
		} catch (Exception e) {
			return "Nie poda�e� nieujemnej liczby ca�kowitej.";
		}
		return "Liczba " + _buttonLabel + " powinna by� nieujemn� liczb� ca�kowit�.";
	}

	@Override
	protected void setDefaultValue() {
		_value = 1;
	}

	@Override
	protected String getStringValue() {
		return _value.toString();
	}
}
