package com.Dessertion.jth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.Dessertion.jth.entity.Entity;
import com.Dessertion.jth.entity.Player;

public class Game extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static int WIDTH = 160, HEIGHT = 160;
	public static final int SCALE = 4;
	public boolean hasWon = false;
	
	public Player player;
	
	private InputHandler input;
	public ArrayList<Entity> entities = new ArrayList<>();
	
	BufferedImage canvas = new BufferedImage(Game.WIDTH,Game.HEIGHT,BufferedImage.TYPE_INT_ARGB);
	
	public Game() {
		setSize(new Dimension((WIDTH*SCALE),HEIGHT*SCALE));
		setMaximumSize(new Dimension((WIDTH*SCALE),HEIGHT*SCALE));
		setMinimumSize(new Dimension((WIDTH*SCALE),HEIGHT*SCALE));
		setFocusable(true);
		
		init(); 
	}
	
	private void init() {
		
		input = new InputHandler();
		addKeyListener(input);
		player = new Player(this, input);
	}
	
	public void tick() {
		if(!hasFocus())input.releaseAll();
		else {
			input.tick();
			for(Entity e : entities)e.tick();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(canvas, 0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE, null);
	}
	
	private void renderBackground(Graphics g) {
		//TODO: actual background rendering lol
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
	}

	public void render() {
		//TODO: handle interpolation
		Graphics g = (Graphics2D) getGraphics();
		Graphics g2 = canvas.getGraphics();
		renderBackground(g2);
		renderSprites(g2);
		repaint();
		g.dispose();
		g2.dispose();
	}

	private void renderSprites(Graphics g) {
		for(Entity e : entities)e.render(g);
	}
	
	
	
	
}
