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

import javax.swing.JPanel;

import com.Dessertion.jth.entity.Bullet;
import com.Dessertion.jth.entity.DamageableEntity;
import com.Dessertion.jth.entity.BasicEnemy;
import com.Dessertion.jth.entity.BasicEnemy.EnemyType;
import com.Dessertion.jth.sound.MPEGClip;

import javazoom.jl.decoder.JavaLayerException;

import com.Dessertion.jth.entity.Entity;
import com.Dessertion.jth.entity.Player;

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
	
	public static Player player;
	
	private InputHandler input;
	public static ArrayList<Entity> entities = new ArrayList<>();
	public static ArrayList<DamageableEntity> damageable = new ArrayList<>();
	public static ArrayList<Bullet> bullets = new ArrayList<>();
	
	BufferedImage canvas = new BufferedImage(Game.WIDTH,Game.HEIGHT,BufferedImage.TYPE_INT_ARGB);
	
	private MPEGClip clip;
	
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
		
		input = new InputHandler();
		addKeyListener(input);
		player = new Player(this, input);
		spawn(player);
		EnemyManager.init();
		BasicEnemy test = new BasicEnemy(50,50,10,EnemyType.ALIEN1);
		spawn(test);
		clip  = null;
		try {
			clip = new MPEGClip("./res/[07] Dancing Water Spray.mp3");
		} catch (FileNotFoundException | JavaLayerException e) {
			e.printStackTrace();
		}
		clip.loop();
		clip.play();
	
		
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
		tickTime++;
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
