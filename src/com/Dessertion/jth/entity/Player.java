package com.Dessertion.jth.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.Dessertion.jth.Game;
import com.Dessertion.jth.InputHandler;

public class Player extends DamageableEntity{
	
	Game game;
	InputHandler input;
	private boolean rightLeft = false, upDown = false;
	private boolean mvX = false, mvY = false;
	private int vx = 0, vy = 0;
	private int rw = 2, rh = 2;
	public Player(Game game, InputHandler input) {
		super(game, 1);
		this.game = game;
		this.input = input;
		x = Game.WIDTH/2;
		y = Game.HEIGHT-50;
		rect = new Rectangle((int)x-rw/2, (int)y-rh/2,rw,rh);
		
	}
	
	@Override
	public void tick() {
		super.tick();
		moveUtil();
		//TODO implement collision detection for player w/ walls
	}
	
	public void moveUtil() {
		if (input.up.isDown() || input.down.isDown())
			mvY = true;
		else
			mvY = false;
		
		if (input.left.isDown() || input.right.isDown())
			mvX = true;
		else
			mvX = false;

		if (mvY) {
			//if both are held down, flip from previous orientation
			if (!(input.up.isDown() ^ input.down.isDown())) {
				//check if first time both are held down
				if (!upDown) {
					vy = vy > 0 ? -1 : 1;
					upDown = true;
				}
			} else {
				//otherwise move in normal direction
				upDown = false;
				vy = input.up.isDown()? -1:1;
			}
		} else {
			upDown = false;
			vy = 0;
		}
		
		//same process for x direction
		if(mvX) {
			if(!(input.left.isDown()^input.right.isDown())) {
				if(!rightLeft) {
					vx = vx > 0? -1 : 1;
					rightLeft = true;
				}
			} else {
				rightLeft = false;
				vx = input.left.isDown()? -1:1;
			}
		} else {
			rightLeft = false;
			vx = 0;
		}
		
		
		
		move(vx,vy);
	}
	
	@Override
	public void render(Graphics g) {
		BufferedImage img = null;
		int w = 0, h = 0;
		int xoff, yoff;
		try {
			img = ImageIO.read(getClass().getResourceAsStream("/res/player.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		w = img.getWidth();
		h = img.getHeight();
		xoff = (int) (x-w/2);
		yoff = (int) (y-h/2);
		g.drawImage(img, (int) xoff, (int) yoff, w, h, null);
		
		//hitbox for clarity
		g.setColor(Color.red);
		g.fillRect(rect.x,rect.y,rw,rh);
	}

}
