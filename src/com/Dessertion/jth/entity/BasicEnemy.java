package com.Dessertion.jth.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.Dessertion.jth.Game;

public class BasicEnemy extends DamageableEntity{
	
	public static final int BOUNDINGY = 75;
	
	public static enum EnemyType{
		ALIEN1("alien1.png"),
		ALIEN2("alien2.png"),
		BOSS("boss.png");
	    
		private final File file;
		EnemyType(String s) {
			file = new File("./res/" + s);
		}
		
		public File getFile() {return file;}
	} 
	
	private EnemyType type;
	private BufferedImage img = null;
	private Random random = new Random();
	private int attackTimer = 240, attackTimerMin = 60, attackTimerMax = 200;
	private float vbullet = 0.25f;
	private int stoppedTimer = 100;
	private int moveTimer = 0;
	
	public BasicEnemy(float x, float y, int health, EnemyType type) {
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
	
	public void setAttackTimer(int min, int max) {
		attackTimerMin=min;
		attackTimerMax=max;
	}
	
	public void setBulletVelocity(float v) {
		vbullet=v;
	}
	
	private double[] getDir(double dx, double dy) {
		double ratio = dy/dx;
		
		double idir = Math.sqrt(1/(Math.pow(ratio, 2)+1));
		if(dx<0)idir=-idir;
		double jdir = ratio*idir;
		if(Double.isNaN(jdir))jdir=-1;
		return new double[] {idir,jdir};
	}
	
	public void tick() {
		super.tick();
		
		//lol probably shouldve used vector math but o well
		float dx = (Game.player.getX()-x);
		float dy = (Game.player.getY()-y);
		
		double[] toPlayerDir = getDir(dx,dy);
		if (attackTimer <= 0) {
				attack(toPlayerDir[0], toPlayerDir[1], vbullet);
				attackTimer = random.nextInt(attackTimerMax) + attackTimerMin;
			}
			attackTimer--;
			
		//check if within bounds
		if(x>=0&&x<=Game.WIDTH&&y>=0&&y<=BOUNDINGY)inBounds=true;
		else inBounds=false;
			
			//randomly move if movement flag checked
			if(stopped) {
				stoppedTimer--;
				if (stoppedTimer <= 0) {
					// pick some random x and y within bounding area, move towards it
					int randx = random.nextInt(Game.WIDTH);
					int randy = random.nextInt(BOUNDINGY);
					float dx2 = randx - x;
					float dy2 = randy - y;
					float dis = (float) Math.sqrt((Math.pow(dx2, 2)+Math.pow(dy2, 2)));
					double[] moveDir = getDir(dx2, dy2);
					vx=(float) moveDir[0];
					vy=(float) moveDir[1];
					stopped=false;
					moveTimer = (int) dis;
					stoppedTimer=random.nextInt(240)+120;
				}
			}
			else {
				if(inBounds) {
				vx*=0.98;
				vy*=0.98;
				}
				if(moveTimer<=0) {
					vx=0;
					vy=0;
					stopped=true;
				}
				moveTimer--;
			}
			move(vx,vy);
			
		
		
		
		
		
	}
	
	private void attack(double idir, double jdir, float vTot) {
		Bullet bullet = new Bullet(false,1,x,y);
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
		g.drawRect(rect.x,rect.y,rw,rh);
	}
	
}
