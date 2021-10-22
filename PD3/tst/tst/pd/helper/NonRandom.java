package tst.pd.helper;

import java.util.*;

public class NonRandom extends Random {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 142315341955689074L;

	@Override
	public int nextInt() {
		return 0;
	}
	
	@Override
	public int nextInt(int bound) {
		return nextInt();
	}
	
	@Override
	public boolean nextBoolean() {
		return false;
	}

}
