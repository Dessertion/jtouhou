package com.Dessertion.jth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.Dessertion.jth.entity.Bullet;
import com.Dessertion.jth.entity.DamageableEntity;
import com.Dessertion.jth.entity.Entity;
import com.Dessertion.jth.entity.Player;
import com.Dessertion.jth.sound.Sound;

import javazoom.jl.decoder.JavaLayerException;

public class Game extends JPanel {

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
	public static int winTime = -1;
	
	private Random random = new Random();
	private int countDown = 180;
	private int launchTick = -1;

	public static Player player;

	private InputHandler input;
	private boolean musicChanged = false;
	private boolean startCountDown = false;

	public static ArrayList<Entity> entities = new ArrayList<>();
	public static ArrayList<DamageableEntity> damageable = new ArrayList<>();
	public static ArrayList<Bullet> bullets = new ArrayList<>();

	private BufferedImage canvas = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);
	private BufferedImage gensokyobg = null;
	private BufferedImage cornfield = null;

	public Game() {
		setSize(new Dimension((WIDTH * SCALE), HEIGHT * SCALE));
		setMaximumSize(new Dimension((WIDTH * SCALE), HEIGHT * SCALE));
		setMinimumSize(new Dimension((WIDTH * SCALE), HEIGHT * SCALE));
		setFocusable(true);

		init();
	}

	public static void spawn(Entity e) {
		entities.add(e);
		if (e instanceof Bullet)
			bullets.add((Bullet) e);
	}

	private void init() {
		try {
			Sound.init();
		} catch (FileNotFoundException | JavaLayerException e) {
			e.printStackTrace();
		}

		input = new InputHandler();
		addKeyListener(input);

		player = new Player(this, input);
		player.teleport(Game.WIDTH / 2, Game.HEIGHT - 75);
		spawn(player);

		EnemyManager.init();

		try {
			gensokyobg = ImageIO.read(new File("./res/gensokyo.jpg"));
			cornfield = ImageIO.read(new File("./res/cornfield.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void tick() {
		if (!Core.gameStart) {
			if (input.attack.down) {
				startCountDown = true;
			}
			if (startCountDown) {

				if (countDown % 60 == 0)
					Sound.beep.play();
				countDown--;
			}
			if (countDown <= 0) {
				if (launchTick == -1) {
					launchTick = tickTime;
					Sound.launch.play();
				}
				startCountDown = false;
				player.move(0, -2.5f * (tickTime - launchTick) / 10);
				if (tickTime - launchTick >= 120) {
					Core.gameStart = true;
					Sound.bgm.loop();
					player.teleport(Game.WIDTH / 2, Game.HEIGHT - 50);
				}
			}

		} else if (hasWon&&tickTime-winTime>=180) {
			if(tickTime-winTime==180) {
				Game.player.teleport(Game.WIDTH/2, -20);
				Sound.bosstheme.stop();
				Sound.shoot.stop();
				Sound.landing.play();
			}
			
			if(7*2f/(tickTime-winTime-179)>0.1)player.move(0, 15*2f/(tickTime-winTime-179));
			if(tickTime-winTime>=720)System.exit(0);
		} else {
			if (!hasFocus())
				input.releaseAll();
			else {
				// remove dead entities
				Iterator<Entity> itr = entities.iterator();
				while (itr.hasNext())
					if (!itr.next().isAlive())
						itr.remove();

				for (int i = 0; i < entities.size(); i++)
					entities.get(i).tick();
				input.tick();

				EnemyManager.tick();
			}
			if (hasLost) {
				if (input.attack.clicked)
					System.exit(0);
			}
			if (bossFight && !musicChanged) {
				Sound.bgm.stop();
				Sound.bosstheme.loop();
				musicChanged = true;
			}

		}
		tickTime++;
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(canvas, 0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE, null);
	}

	private void renderBackground(Graphics g) {
		// TODO: actual background rendering lol
		g.setColor(new Color(18, 6, 63));
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.WHITE);
		for (int i = 0; i < 50; i++) {
			g.drawOval(random.nextInt(Game.WIDTH), random.nextInt(Game.HEIGHT), 1, 1);
		}
	}

	public void render() {
		// TODO: handle interpolation
		Graphics g = (Graphics2D) getGraphics();
		Graphics g2 = canvas.getGraphics();
		if (!Core.gameStart) {
			renderOpening(g2);
		} else if (hasWon&&tickTime-winTime>=180) {
			renderClosing(g2);
		} else {
			renderBackground(g2);
			renderSprites(g2);
			if (hasLost)
				displayGameOver(g2);
		}
		repaint();
		g.dispose();
		g2.dispose();
	}

	public void renderOpening(Graphics g) {
		g.drawImage(gensokyobg, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		if (startCountDown || launchTick != -1) {
			g.setFont(new Font("Trebuchet MS", Font.BOLD, 24));
			g.setColor(Color.black);
			g.drawString(Integer.toString(countDown / 60 + 1), 5, Game.HEIGHT / 2);
			g.setFont(new Font("Trebuchet MS", Font.PLAIN, 22));
			g.setColor(Color.white);
			g.drawString(Integer.toString(countDown / 60 + 1), 7, Game.HEIGHT / 2);
		} else {
			g.setFont(new Font("Trebuchet MS", Font.BOLD, 10));
			g.setColor(Color.magenta);
			g.drawString("Press <Z> to start countdown", 5, Game.HEIGHT / 2);
		}
		player.render(g, 2);
	}

	public void renderClosing(Graphics g) {
		g.drawImage(cornfield, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		player.render(g, 2);
		if(tickTime-winTime>600) {
			g.setFont(new Font("Trebuchet MS", Font.BOLD, 10));
			g.setColor(Color.magenta);
			g.drawString("YOU WIN!", Game.WIDTH/2-50, Game.HEIGHT / 2);
		}
	}

	private void displayGameOver(Graphics g) {
		g.setColor(Color.MAGENTA);
		g.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		g.drawString("GAME OVER", 0, Game.HEIGHT / 2);
		g.setFont(new Font("Trebuchet MS", Font.PLAIN, 10));
		g.drawString("Press <Z> to exit", 0, Game.HEIGHT / 2 + 18);
	}

	private void renderSprites(Graphics g) {
		for (Entity e : entities)
			e.render(g);
	}

}
