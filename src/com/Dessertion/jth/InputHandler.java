package com.Dessertion.jth;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener{

	
	public InputHandler() {
		
	}
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key attack = new Key();
	public Key special = new Key();
	public Key menu = new Key();
	
	@Override
	public void keyTyped(KeyEvent paramKeyEvent) {		
	}

	@Override
	public void keyPressed(KeyEvent paramKeyEvent) {
		toggle(paramKeyEvent, true);
	}

	@Override
	public void keyReleased(KeyEvent paramKeyEvent) {
		toggle(paramKeyEvent, false);
		
	}
	
	//toggle movement based on arrow keys
	//toggle shoot,special with z,x
	//toggle menu with esc
	private void toggle(KeyEvent e, boolean pressed) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			up.toggle(pressed);
			
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT)left.toggle(pressed);
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			
			down.toggle(pressed);
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)right.toggle(pressed);
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)menu.toggle(pressed);
		
		if(e.getKeyCode() == KeyEvent.VK_Z)attack.toggle(pressed);
		if(e.getKeyCode() == KeyEvent.VK_X)special.toggle(pressed);
	}
	
	public void releaseAll() {
		for(Key key : Key.keys) {
			key.down = false;
		}
	}
	
	//tick each key
	public void tick() {
		for(Key key : Key.keys)key.tick();
	}
}
