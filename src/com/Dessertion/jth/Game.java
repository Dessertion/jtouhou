package com.Dessertion.jth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JPanel;

import com.Dessertion.jth.entity.Bullet;
import com.Dessertion.jth.entity.DamageableEntity;
import com.Dessertion.jth.entity.Entity;
import com.Dessertion.jth.entity.Player;
import com.Dessertion.jth.sound.Sound;

import javazoom.jl.decoder.JavaLayerException;

public class Game extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static int WIDTH = 200, HEIGHT = 200;
	public static final int SCALE = 3;
	public static boolean hasWon = false;
	public static boolean hasLost = false;
	public static int tickTime = 0;
	public static boolean bossFight = false;
	private Random random = new Random();
	
	public static Player player;
	
	private InputHandler input;
	private boolean musicChanged = false;
	
	
	public static ArrayList<Entity> entities = new ArrayList<>();
	public static ArrayList<DamageableEntity> damageable = new ArrayList<>();
	public static ArrayList<Bullet> bullets = new ArrayList<>();
	
	BufferedImage canvas = new BufferedImage(Game.WIDTH,Game.HEIGHT,BufferedImage.TYPE_INT_ARGB);
	
	
	public Game() {
		setSize(new Dimension((WIDTH*SCALE),HEIGHT*SCALE));
		setMaximumSize(new Dimension((WIDTH*SCALE),HEIGHT*SCALE));
		setMinimumSize(new Dimension((WIDTH*SCALE),HEIGHT*SCALE));
		setFocusable(true);
		
		init(); 
	}
	
	public static void spawn(Entity e) {
		entities.add(e);
		if(e instanceof Bullet)bullets.add((Bullet) e);
	}
	
	private void init() {
		try {
			Sound.init();
		} catch (FileNotFoundException | JavaLayerException e) {
			e.printStackTrace();
		}
		Sound.bgm.loop();
		
		input = new InputHandler();
		addKeyListener(input);
		
		player = new Player(this, input);
		spawn(player);
		
		EnemyManager.init();
	}
	
	public void tick() {
		if(!hasFocus())input.releaseAll();
		else {
			//remove dead entities
			Iterator<Entity> itr = entities.iterator();
			while(itr.hasNext())if(!itr.next().isAlive())itr.remove();
			
			for(int i = 0 ; i<entities.size(); i++)entities.get(i).tick();
			input.tick();
			
			EnemyManager.tick();
			}
		if(hasLost) {
		    if(input.attack.clicked)System.exit(0);
		}
		if(bossFight&&!musicChanged) {
			Sound.bgm.stop();
			Sound.bosstheme.loop();
			musicChanged=true;
		}
		tickTime++;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(canvas, 0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE, null);
	}
	
	private void renderBackground(Graphics g) {
		//TODO: actual background rendering lol
		g.setColor(new Color(18,6,63));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.WHITE);
		for(int i = 0 ; i < 50; i++) {
			g.drawOval(random.nextInt(Game.WIDTH), random.nextInt(Game.HEIGHT), 1, 1);
		}
	}

	public void render() {
		//TODO: handle interpolation
		Graphics g = (Graphics2D) getGraphics();
		Graphics g2 = canvas.getGraphics();
		renderBackground(g2);
		renderSprites(g2);
		if(hasLost)displayGameOver(g2);
		repaint();
		g.dispose();
		g2.dispose();
	}

	private void displayGameOver(Graphics g) {
	    g.setColor(Color.MAGENTA);
	    g.setFont(new Font("Trebuchet MS",Font.BOLD,18));
	    g.drawString("GAME OVER", 0, Game.HEIGHT/2);
	    g.setFont(new Font("Trebuchet MS",Font.PLAIN,10));
	    g.drawString("Press <Z> to exit", 0, Game.HEIGHT/2+18);
	}

	private void renderSprites(Graphics g) {
		for(Entity e : entities)e.render(g);
	}
	
	
	
	
}
