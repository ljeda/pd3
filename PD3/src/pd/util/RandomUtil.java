package pd.util;

import java.util.*;

public class RandomUtil {

	private static Random _random = new Random("Cauchy i Norka".length());

	public static Random getRandom() {
		return _random;
	}

}
