package com.Dessertion.jth.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.Dessertion.jth.Game;
import com.Dessertion.jth.InputHandler;
import com.Dessertion.jth.sound.Sound;

public class Player extends DamageableEntity {

	Game game = null;
	InputHandler input = null;
	private boolean rightLeft = false, upDown = false;
	private boolean mvX = false, mvY = false;
	private int attackDelay = 7, attackFlag = 0;
	public static int lives;
	private BufferedImage img = null;
	private int w = 0, h=0, xoff,yoff;
	public double dmg;
	private int invulnWindow = 240;
	private int damageTime = -1;
	
	public Player(Game game, InputHandler input) {
		super(Game.WIDTH / 2, Game.HEIGHT - 50, 1);
		this.game = game;
		this.input = input;
		init();
	}
	
	private void init() {

		try {
			File file = new File("./res/player.png");
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		w = img.getWidth();
		h = img.getHeight();

		rw = 4;
		rh = 4;
		createHitBox();
		lives = 3;
	}

	@Override
	public void tick() {
		super.tick();
		if (!alive) {
			if (lives > 0) {
				// lol need a dying anim
				lives--;
				alive = true;
				hp = maxHp;
				teleport(Game.WIDTH / 2, Game.HEIGHT - 50);
				damageTime=tickTime;
			} else {
				Sound.shoot.stop();
				Game.hasLost = true;
			}
		}
		moveUtil();
		if (input.attack.isDown()) {
			if (!Sound.shoot.isPlaying()) {
					Sound.shoot.loop();
				}
			if (attackFlag <= 0) {
				attack();
				attackFlag = attackDelay;
				
			}
		} else {
			Sound.shoot.setPlaying(false);
			Sound.shoot.stop();
		}
		attackFlag--;
	}

	private void attack() {
		Bullet bullet = new Bullet(true, 3, x, y);
		bullet.setVy(-4);
		bullet.setDamage((int)dmg);
		Game.spawn(bullet);
	}
	
	@Override
	public boolean hurt(int damage) {
		if(tickTime-damageTime>invulnWindow) {
			return super.hurt(damage);
		}
		else return false;
	};

	private void moveUtil() {
		if (input.up.isDown() || input.down.isDown())
			mvY = true;
		else
			mvY = false;

		if (input.left.isDown() || input.right.isDown())
			mvX = true;
		else
			mvX = false;

		if (mvY) {
			// if both are held down, flip from previous orientation
			if (!(input.up.isDown() ^ input.down.isDown())) {
				// check if first time both are held down
				if (!upDown) {
					vy = vy > 0 ? -1.5f : 1.5f;
					upDown = true;
				}
			} else {
				// otherwise move in normal direction
				upDown = false;
				vy = input.up.isDown() ? -1.5f : 1.5f;
			}
		} else {
			upDown = false;
			vy = 0;
		}

		// same process for x direction
		if (mvX) {
			if (!(input.left.isDown() ^ input.right.isDown())) {
				if (!rightLeft) {
					vx = vx > 0 ? -1.5f : 1.5f;
					rightLeft = true;
				}
			} else {
				rightLeft = false;
				vx = input.left.isDown() ? -1.5f : 1.5f;
			}
		} else {
			rightLeft = false;
			vx = 0;
		}
		if ((vx < 0 && x + vx < 0) || vx > 0 && x  + vx > Game.WIDTH)
			vx = 0;
		if ((vy < 0 && y  + vy < 0) || vy > 0 && y  + vy > Game.HEIGHT)
			vy = 0;

		move(vx, vy);
	}

	@Override
	public void render(Graphics g) {
		xoff = (int) (x - w / 2);
		yoff = (int) (y - h / 2);
		if(tickTime-damageTime<=invulnWindow) {
			if(tickTime%2==0)g.drawImage(img, (int) xoff, (int) yoff, w, h, null);
		}
		else g.drawImage(img, (int) xoff, (int) yoff, w, h, null);
		
		// hitbox for clarity
//		g.setColor(Color.red);
//		g.fillRect(rect.x, rect.y, rw, rh);
	}
	
	public void render(Graphics g, int scale) {
		xoff = (int) (x - w / 2);
		yoff = (int) (y - h / 2);
		g.drawImage(img, (int) xoff, (int) yoff, w*scale, h*scale, null);
	}

}
