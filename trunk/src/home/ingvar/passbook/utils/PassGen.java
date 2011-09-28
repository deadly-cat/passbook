package home.ingvar.passbook.utils;

import java.security.SecureRandom;
import java.util.BitSet;

public abstract class PassGen {
	
	private static final String SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+:;~";
	private static final int[] GROUPS = {0, 26, 52, 62}; //start position of groups of symbols (upper case, lower case, numbers, special chars)
	
	public static void main(String[] args) {
		for(int i = 0; i < 10; i++) {
			System.out.println(generate(20));
		}
	}
	
	public static String generate(int length) {
		SecureRandom rand = new SecureRandom();
		
		//compute count symbols for groups
		int sum = 0;
		int pos = 0;
		int[] counts = new int[4];
		while(sum != length) {
			if(pos > 3) {
				pos = rand.nextInt(4);
			}
			int max = Math.round(length / 4);
			int min = (int) Math.round(max * 0.4);
			int count = rand.nextInt(max+1);
			if(counts[pos] + count < min) {
				count = min;
			}
			if(sum + count > length) {
				count = length - sum;
			}
			sum += count;
			counts[pos++] += count;
		}
		
		pos = 0;
		BitSet bitSet = new BitSet();
		char[] values = new char[length];
		for(int g = 0; g < GROUPS.length; g++) {
			int len = ((g == GROUPS.length-1) ? SYMBOLS.length() : GROUPS[g+1]) - GROUPS[g]; //end group + 1
			for(int i = 0; i < counts[g]; i++) {
				char sym = 124;
				sym = SYMBOLS.charAt(GROUPS[g] + rand.nextInt(len));
				if(i > 0 && counts[g] < len) {
					while(bitSet.get(sym)) {
						sym = SYMBOLS.charAt(GROUPS[g] + rand.nextInt(len));
					}
				}
				bitSet.set(sym);
				values[pos++] = sym;
			}
		}
		
		//mixing
		int min = values.length * 2;
		int mixing = rand.nextInt(1000);
		if(mixing < min) {
			mixing = min;
		}
		for(int i = 0; i < mixing; i++) {
			int _1 = rand.nextInt(values.length);
			int _2 = rand.nextInt(values.length);
			swap(values, _1, _2);
		}
		//first and end symbols must be not belongs special char
		swapSpecialChar(values, 0);
		swapSpecialChar(values, values.length-1);
		
		return new String(values);
	}
	
	private static void swap(char[] array, int i, int j) {
		char tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}
	
	private static void swapSpecialChar(char[] array, int pos) {
		if(SYMBOLS.indexOf(array[pos]) >= GROUPS[3]) {
			for(int i = 1; i < array.length-1; i++) {
				if(SYMBOLS.indexOf(array[i]) < GROUPS[3]) {
					swap(array, pos, i);
				}
			}
		}
	}
	
}
