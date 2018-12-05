package com.Dessertion.jth.entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.Dessertion.jth.Game;

public class Bullet extends Entity{

	protected int dmg=1;
	protected boolean friendly = false;
	
	protected BufferedImage bimg = null;
	
	public Bullet(boolean friendly, int bulletType, float x, float y) {
		super(x,y);
		this.friendly = friendly;
		setBulletType(bulletType);
		rw = bimg.getWidth();
		rh = bimg.getHeight();
		rx = x;
		ry=y;
		rect = new Rectangle((int)(rx),(int)(ry),rw,rh);
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
	
	public void setDamage(int dmg) {
		this.dmg = dmg;
	}
	
	@Override
	public void tick() {
		if(x<-10||x>Game.WIDTH+10)remove();
		if(y<-10||y>Game.WIDTH+10)remove();

		for (Entity e : Game.entities) {
			if (e instanceof DamageableEntity) {
				// check if player
				if (intersects(e)) {
					if (e instanceof Player) {
						// if friendly bullet do nothing
						if (friendly)
							continue;
						else {
							((DamageableEntity) e).hurt(dmg);
							remove();
							break;
						}
					}
					if (friendly) {
						((DamageableEntity) e).hurt(dmg);
						remove();
					}
				}
			}
		}
		
		
		move(vx,vy);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(bimg,(int) x, (int)y, null);
		g.drawRect(rect.x, rect.y, rw, rh);
	}

}
