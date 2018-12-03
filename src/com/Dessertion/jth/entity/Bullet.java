package com.Dessertion.jth.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.Dessertion.jth.Game;

public class Bullet extends Entity{

	protected int dam;
	protected boolean friendly;
	
	protected BufferedImage bimg = null;
	
	public Bullet(Game game, boolean friendly, int bulletType) {
		super(game);
		this.friendly = friendly;
		setBulletType(bulletType);
		rw = bimg.getWidth();
		rh = bimg.getHeight();
	}
	
	private void setBulletType(int i) {
		File file;
		switch(i) {
		case 1:
			file = new File("./res/bullet1.png");
			break;
		default:
			file = null;
			break;
		}
		
		try {
			bimg = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void tick() {
		if(friendly) {
			for (DamageableEntity e : Game.damageable) {
				if(e instanceof Player)continue;
				if (intersects(e)) {
					e.hurt(dam);
				}
			}
		}
		else {
			if(intersects(Game.player)) {
				Game.player.hurt(dam);
			}
		}
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}

}
