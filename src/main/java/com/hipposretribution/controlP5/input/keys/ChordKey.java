package com.hipposretribution.controlP5.input.keys;

import java.awt.event.KeyEvent;
import java.util.Arrays;

public class ChordKey {

	final int[] chordKeys;

	public ChordKey(int[] keys) {
		chordKeys = keys;
		Arrays.sort(chordKeys);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int code : chordKeys) {
			builder.append(KeyEvent.getKeyText(code) + "(" + (code) + ") ");
		}
		return builder.toString();
	}

	public int size() {
		return chordKeys.length;
	}

	public int[] getKeys() {
		return chordKeys;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ChordKey)) {
			return false;
		}

		ChordKey other = (ChordKey) obj;

		if (other.size() != size()) {
			return false;
		}

		for (int i = 0; i < size(); i++) {
			if (chordKeys[i] != other.chordKeys[i]) {
				return false;
			}
		}
		return true;
	}

	boolean contains(int nkey) {
		for (int key : chordKeys) {
			if (nkey == key) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		int n = 1;
		for (int key : chordKeys) {
			hashCode += key + Math.pow(key, n++);
		}
		return hashCode;
	}
}
