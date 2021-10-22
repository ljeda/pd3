package tst.pd.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pd.core.BlackBox;

public class BlackBoxTester {

	@Test
	public void changeKeysForTestsTest() {
		boolean changeRKey;
		
		List<Boolean> k = new ArrayList<Boolean>();
		List<Boolean> r = new ArrayList<Boolean>();
		for (int i = 0; i < 10; i++) {
			k.add(false);
			r.add(false);
		}
		
		changeRKey = true;
		testKey(r, k, changeRKey);

		changeRKey = false;
		testKey(r, k, changeRKey);

		r.set(5, true);
		r.set(8, true);
		
		changeRKey = true;
		testKey(r, k, changeRKey);

		changeRKey = false;
		testKey(r, k, changeRKey);

		k.set(5, true);
		k.set(8, true);
		
		changeRKey = true;
		testKey(r, k, changeRKey);

		changeRKey = false;
		testKey(r, k, changeRKey);
	}
	
	private void testKey(List<Boolean> r, List<Boolean> k, boolean changeRKey) {
		Boolean[] actualKey;
		Boolean[] expectedKey = (changeRKey ? r : k).toArray(new Boolean[] {});
		BlackBox blackbox = new BlackBox(k, r, 0);
		for (int i = 0; i < 10; i++) {
			blackbox.changeKeysForTests(changeRKey, new int[] {i});
			actualKey = blackbox.getKey(changeRKey);
			expectedKey[i] = !expectedKey[i];
			assertArrayEquals(expectedKey, actualKey);
			blackbox.changeKeysForTests(changeRKey, new int[] {i});
			actualKey = blackbox.getKey(changeRKey);
			expectedKey[i] = !expectedKey[i];
			assertArrayEquals(expectedKey, actualKey);
		}
	}
}
