package com.hipposretribution.controlP5.input.keys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import processing.event.KeyEvent;

/**
 * Helper Class to Store Key Presses
 * 
 * @author zakski
 * 
 */
public class KeyStorage {

	protected HashMap<Integer, Long> keys; // storage of the key and their held time
	protected HashMap<ChordKey, ArrayList<KeyShortcut>> chords; // simple shortcut map

	protected KeyEvent lastPressedEvent; // last key Pressed
	protected KeyEvent lastReleasedEvent; // last key Released

	protected ArrayList<KeyEventHandler> handlers; // the handlers to inform of key events

	protected long holdTime; // time in millis before the key is considered held
	protected boolean shortcutsEnabled; // whether we should be trying to process shortcuts

	/**
	 * Simple Constructor to initialise the key storage
	 * 
	 * @param timeBeforeConsideredHeld - time in millis before the key is considered held
	 */
	public KeyStorage(long timeBeforeConsideredHeld) {
		holdTime = timeBeforeConsideredHeld;
		
		keys = new HashMap<>();
		chords = new HashMap<>();
		handlers = new ArrayList<>();
		
		lastPressedEvent = null;
		lastReleasedEvent = null;
	}

	/**
	 * Method to register a key press
	 * 
	 * @param code - code of the key to be added
	 * @return true if the key has been added, false otherwise
	 */
	public boolean keyPressed(int code) {
		if (!keys.containsKey(code)) {
			keys.put(code, System.currentTimeMillis());
			return true;
		}

		return false;
	}

	/**
	 * Method to release a key
	 * 
	 * @param code - code of the key to be added
	 * @return true if the key has been removed, false otherwise
	 */
	public boolean keyReleased(int code) {
		if (keys.containsKey(code)) {
			keys.remove(code);
			return true;
		}

		return false;
	}

	/**
	 * Method to check for a key press
	 * 
	 * @param code - code of the key to be checked
	 * @return true if the key has been pressed, false otherwise
	 */
	public boolean isKeyPressed(int code) {
		return keys.containsKey(code);
	}

	/**
	 * Method to check if a key is held
	 * 
	 * @param code - code of the key to be checked
	 * @return true if the key is held, false otherwise
	 */
	public boolean isKeyHeld(int code) {
		long time = System.currentTimeMillis();
		return keys.containsKey(code) && ((time - keys.get(code)) >= holdTime);
	}

	/**
	 * Method to get the hold time of a key
	 * 
	 * @param code - code of the key to be checked
	 * @return time the key has been held for, -1 if the key is currently not held
	 */
	public long keyHeldTime(int code) {
		return (keys.containsKey(code)) ? keys.get(code) : -1L;
	}

	/**
	 * Method to get the pressed keys
	 * 
	 * @return a list of keys currently held down
	 */
	public Set<Integer> keysDown() {
		return keys.keySet();
	}

	/**
	 * Method to get the pressed keys
	 * 
	 * @return an array of keys currently held down
	 */
	public int[] keysArray() {
		int[] keyArray = new int[keys.size()];
		int i = 0;
		for (Integer k : keys.keySet())
			keyArray[i++] = k;
		return keyArray;
	}

	/**
	 * Method to process KeyEvents as they happen
	 * 
	 * @param event - the latest event to process
	 */
	public void keyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.PRESS) {

			boolean chordCheck = shortcutsEnabled && isKeyPressed(event.getKeyCode());
			lastPressedEvent = event;
			keyPressed(event.getKeyCode());

			if (chordCheck) {
				ChordKey chord = new ChordKey(keysArray());

				if (chords.containsKey(chord)) {
					for (KeyShortcut shortcut : chords.get(chord)) {
						shortcut.keyEvent();
					}
				}

			}
		} else if (event.getAction() == KeyEvent.RELEASE) {
			lastReleasedEvent = event;
			keyReleased(event.getKeyCode());
		}
		for (KeyEventHandler handler : handlers) {
			handler.handleKeyEvent(event);
		}
	}

	public boolean attachHandler(KeyEventHandler handle) {
		return handlers.add(handle);
	}

	public boolean detachHandler(KeyEventHandler handle) {
		return handlers.remove(handle);
	}

	/**
	 * Method to add a keyboard shortcut
	 * 
	 * @param shortcut - the logic to call when the shortcut is used
	 * @param keys - the key combination that will trigger the shortcut
	 */
	public void mapShortcut(KeyShortcut shortcut, int... keys) {

		ChordKey kc = new ChordKey(keys);
		if (!chords.containsKey(kc)) {
			chords.put(kc, new ArrayList<KeyShortcut>());
		}
		chords.get(kc).add(shortcut);
	}
	
	/**
	 * Method to remove a keyboard shortcut
	 * 
	 * @param shortcut - the logic called when the shortcut is used
	 * @param keys - the key combination that was used to trigger the shortcut
	 */
	public void removeShortcut(KeyShortcut shortcut, int... keys) {
		ArrayList<KeyShortcut> shortcuts = chords.get(new ChordKey(keys));
		if (shortcuts != null) {
			shortcuts.remove(shortcut);
		}
	}

	/**
	 * Method to remove all shortcuts for a set of keys
	 * 
	 * @param keys - the key combination to remove
	 */
	public void removeShortcutsFor(int... keys) {
		chords.remove(new ChordKey(keys));
	}

	/**
	 * Method to clear key records
	 */
	public void clear() {
		keys.clear();
	}

	/**
	 * Method to disable shortcuts
	 * 
	 */
	public void disableShortcuts() {
		shortcutsEnabled = false;
	}

	/**
	 * Method to check if shortcuts are enabled
	 * 
	 * @return true if enabled, false otherwise
	 */
	public boolean shortcutsOn() {
		return shortcutsEnabled;
	}

	/**
	 * Method to enable shortcuts
	 */
	public void enableShortcuts() {
		shortcutsEnabled = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Set<Integer> keySet = keys.keySet();

		builder.append('[');

		for (Iterator<Integer> index = keySet.iterator(); index.hasNext();) {
			int code = index.next();
			builder.append(java.awt.event.KeyEvent.getKeyText(code));

			if (index.hasNext())
				builder.append(", ");
		}

		builder.append(']');

		return builder.toString();
	}

	public KeyEvent lastPressedEvent() {
		return lastPressedEvent;
	}
}
