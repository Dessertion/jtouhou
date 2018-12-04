package com.Dessertion.jth.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
