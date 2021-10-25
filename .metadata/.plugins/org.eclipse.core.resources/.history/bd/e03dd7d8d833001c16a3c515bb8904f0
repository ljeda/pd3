package tst;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Tst {
	private Random random = new Random(0);
	Character[] tab;
	Map<Integer, Character> map;

	public void test(int size, int tests) {
		System.out.println("test started");
		
		long start1, start2, start3, start4, end1, end2, end3, end4;
		Character a;
		
		tab = new Character[size];
		map = new HashMap<Integer, Character>();
		for (int i = 0; i < size; i++) {
			Character c = (char) i;
			tab[i] = c;
			map.put(i, c);
		}
		
		for (int i = 0; i < size; i++) {
			if (tab[i] != map.get(i))
				System.out.println("fuck!!!");
		}
		
		int[] gets = new int[tests];
		for (int i = 0; i < tests; i++) {
			gets[i] = random.nextInt(size);
		}
		
		a = 'a';
		
		start2 = System.nanoTime();
		for (int i = 0; i < tests; i++) {
			a = getMap(gets[i]);
		}
		end2 = System.nanoTime();
		
		a = 'a';
		
		start1 = System.nanoTime();
		for (int i = 0; i < tests; i++) {
			a = getTab(gets[i]);
		}
		end1 = System.nanoTime();
		
		a = 'a';
		
		start3 = System.nanoTime();
		for (int i = 0; i < tests; i++) {
			a = getMap(gets[i]);
		}
		end3 = System.nanoTime();
		
		a = 'a';
		
		start4 = System.nanoTime();
		for (int i = 0; i < tests; i++) {
			a = getTab(gets[i]);
		}
		end4 = System.nanoTime();
		
		System.out.println("test finished");
		System.out.println(end1-start1);
		System.out.println(end2-start2);
		System.out.println(end3-start3);
		System.out.println(end4-start4);
	}
	
	private Character getTab(int i) {
		if (i < 0 || i > tab.length) {
			System.err.println("Brak litery o indeksie " + i + " w alfabecie.");
			return null;
		}
		return tab[i];
	}
	
	private Character getMap(int i) {
		Character c = map.get(i);
		if (c == null) {
			System.err.println("Brak litery o indeksie " + i + " w alfabecie.");
		}
		return c;
	}
	
	public static void main(String[] args) {
		Tst tst = new Tst();
		tst.test(200, 100000000);
	}
}
