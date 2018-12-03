package com.Dessertion.jth;

import java.util.ArrayList;

public class Key {
	protected int presses, absorbs;
	protected boolean down, clicked;
	public static ArrayList<Key> keys = new ArrayList<Key>();

	public Key() {
		keys.add(this);
	}
	
	public void toggle(boolean pressed) {
		if(pressed != down) {
			down = pressed;
		}
		if(pressed)presses++;
	}
	
	public boolean isDown() {
		return down;
	}
	
	//copied from notch's ld22 entry
	//probably used to handle inputs that have been absorbed or smth for clicks
	public void tick() {
		if (absorbs < presses) {
			absorbs++;
			clicked = true;
		} else {
			clicked = false;
		}
	}

}
