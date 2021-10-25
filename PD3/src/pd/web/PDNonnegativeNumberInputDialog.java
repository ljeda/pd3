package pd.web;

/**
 * @author Łukasz Jęda
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
			return "Nie Podałeś nieujemnej liczby całkowitej.";
		}
		return "Liczba " + _buttonLabel + " powinna być nieujemną liczbę całkowitą.";
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
