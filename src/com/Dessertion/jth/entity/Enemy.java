package com.Dessertion.jth.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.Dessertion.jth.Game;

public class Enemy extends DamageableEntity{
	
	public static enum EnemyType{
		ALIEN("alien1.png");
		
		private final File file;
		EnemyType(String s) {
			file = new File("./res/" + s);
		}
		
		public File getFile() {return file;}
	}
	
	private EnemyType type;
	private BufferedImage img = null;
	
	public Enemy(float x, float y, int health, EnemyType type) {
		super(x,y,health);
		this.type=type;
		try {
			img = ImageIO.read(type.file);
		} catch (IOException e) {
			try {
				img = ImageIO.read(new File("./res/error.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		rw=img.getWidth();
		rh=img.getHeight();
		createHitBox();
	}
	
	public void tick() {
		super.tick();
		
		//lol probably shouldve used vector math but o well
		float dx = (Game.player.getX()-x);
		float dy = (Game.player.getY()-y-20);
		double ratio = dy/dx;
		
		double idir = Math.sqrt(1/(Math.pow(ratio, 2)+1));
		if(dx<0)idir=-idir;
		double jdir = ratio*idir;
		if(Double.isNaN(jdir))jdir=-1;
		attack(idir,jdir,0.25f);
		
	}
	
	private void attack(double idir, double jdir, float vTot) {
		Bullet bullet = new Bullet(false,1,x,y+20);
		bullet.setVTot(vTot);
		bullet.setVx((float) (vTot*idir));
		bullet.setVy((float) (vTot*jdir));
		Game.spawn(bullet);
	}

	@Override
	public void render(Graphics g) {
		int w = 0, h = 0;
		int xoff, yoff;
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
