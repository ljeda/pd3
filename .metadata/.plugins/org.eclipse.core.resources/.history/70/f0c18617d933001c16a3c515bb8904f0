package pd.web;

/**
 * @author £ukasz Jêda
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
			return "Nie poda³eœ nieujemnej liczby ca³kowitej.";
		}
		return "Liczba " + _buttonLabel + " powinna byæ nieujemn¹ liczb¹ ca³kowit¹.";
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
