package com.Dessertion.jth;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Game extends JFrame{
	
	public final static int WIDTH = 800, HEIGHT = 600;
	
	public Game() {
		super("Rocket Landing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(WIDTH,HEIGHT));
		setResizable(false);
		setLocationRelativeTo(null);
		init(); 
	}
	
	private void init() {
		
	}
	
	
	
	
}
