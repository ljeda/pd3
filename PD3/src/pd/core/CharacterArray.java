package pd.core;

/**
 * @author £ukasz Jêda
 * 
 */
public class CharacterArray {

	public CharacterArray(Character state, Character letter) {
		if (state == null || letter == null) {
			System.out.println("oups");
		}
		_array[0] = state;
		_array[1] = letter;
	}

	public Character getState() {
		return _array[0];
	}

	public Character getLetter() {
		return _array[1];
	}

	public int hashCode() {
		return _array[0] + (_array[1] << 8);
	}

	public boolean equals(Object o) {
		if ((o instanceof CharacterArray)) {
			CharacterArray obj = (CharacterArray) o;
			if (obj.getState().charValue() == getState().charValue() 
					&& obj.getLetter().charValue() == getLetter().charValue()) {
				return true;
			}
		}
		return false;
	}

	Character[] _array = new Character[2];
}
