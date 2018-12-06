package com.Dessertion.jth.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.imageio.ImageIO;

import com.Dessertion.jth.Game;
import com.Dessertion.jth.Pair;

public class BasicEnemy extends DamageableEntity {

	public static final int BOUNDINGYMAX = 75, BOUNDINGYMIN = 10;

	public static enum EnemyType {
		ALIEN1("alien1.png"), ALIEN2("alien2.png"), BOSS("boss.png");

		private final File file;

		EnemyType(String s) {
			file = new File("./res/" + s);
			System.out.println(file.exists());
		}

		public File getFile() {
			return file;
		}
	}

	public static class AttackPattern {

		public static enum AttackType {
			LINE, CIRCLE, SPREAD;
		}

		public final static int CIRCLE_DELAY = 2;
		public final static int DEFAULT_DELAY = 20;
		private int attackWaves;
		private int tickTimer;
		private int delay;
		private AttackType type;
		private boolean finished;
		private Queue<Pair<Bullet, Integer>> queue = new LinkedList<>();
		private Entity source;

		public AttackPattern(int attackWaves, int delay, AttackType type, Entity source) {
			this.attackWaves = attackWaves;
			this.type = type;
			this.source = source;
			this.delay = delay;
			init();
		}
		
		public AttackPattern() {
			finished=true;
		}

		private void init() {
			finished = false;
			tickTimer = 0;
			switch (type) {
			case LINE:
				attackLine();
				break;
			case SPREAD:
				attackSpread();
				break;
			case CIRCLE:
				attackCircle();
				break;
			}
		}

		public void tick() {
			tickTimer++;
			// if delay passed
			if (queue.isEmpty()) {
				finished = true;
				return;
			}
			while (!queue.isEmpty()&&tickTimer >= queue.peek().second) {
				Bullet b = queue.poll().first;
				Game.spawn(b);
			}
		}

		private void attackLine() {
			double[] centerDir = getDir(Game.player.x - source.x,Game.player.y-source.y);
			double angle;
			if(centerDir[0]==0) {
				if(centerDir[1]>0)angle = -Math.PI/2;
				else angle = Math.PI/2;
			}
			else angle = Math.atan(centerDir[1] / centerDir[0]);
			if(centerDir[0]<0)angle=Math.PI+angle;
			System.out.println(angle + " " + centerDir[0] + " " + centerDir[1]);
			double angleLeft = angle - 0.2, angleRight = angle + 0.2;
			Pair<Double, Double> leftDir = new Pair<>(Math.cos(angleLeft), Math.sin(angleLeft));
			Pair<Double, Double> rightDir = new Pair<>(Math.cos(angleRight), Math.sin(angleRight));
			for (int i = 0; i < attackWaves; i++) {
				Bullet bcenter = new Bullet(false, 1, source.x, source.y);
				Bullet bleft = new Bullet(false, 1, source.x, source.y);
				Bullet bright = new Bullet(false, 1, source.x, source.y);
				bcenter.setVx((float)Math.cos(angle));
				bcenter.setVy((float)Math.sin(angle));
				bleft.setVx(leftDir.first.floatValue());
				bleft.setVy(leftDir.second.floatValue());
				bright.setVx(rightDir.first.floatValue());
				bright.setVy(rightDir.second.floatValue());
				queue.add(new Pair<>(bcenter, i * delay));
				queue.add(new Pair<>(bright, i * delay));
				queue.add(new Pair<>(bleft, i * delay));
			}
		}

		private void attackSpread() {
			for (int i = 0; i < attackWaves; i++) {
				for (int j = 0; j < 12; j++) {
					double angle = j * Math.PI * 2 / 12;
					Bullet b = new Bullet(false, 1, source.x, source.y);
					b.setVx((float)Math.cos(angle)*(0.5f));
					b.setVy((float)Math.sin(angle)*(0.5f));
					queue.add(new Pair<>(b, i * delay));
				}
			}
		}

		private void attackCircle() {
			for (int i = 0; i < attackWaves; i++) {
				for (int j = 0; j < 36; j++) {
					double angle = j * Math.PI * 2 / 36;
					Bullet b = new Bullet(false, 2, source.x, source.y);
					b.setVx((float)Math.cos(angle)*0.5f);
					b.setVy((float)Math.sin(angle)*0.5f);
					queue.add(new Pair<>(b, i * delay * 36 + j * delay));
				}
			}
		}

		public boolean isFinished() {
			return finished;
		}

	}

	protected EnemyType type;
	protected BufferedImage img = null;
	protected Random random = new Random();
	protected int attackTimer = 240, attackTimerMin = 60, attackTimerMax = 200;
	protected float vbullet = 0.25f;
	protected int stoppedTimer = 100;
	protected int moveTimer = 0;
	protected int minStoppedTime = 120;
	protected int maxStoppedTime = 240;

	public BasicEnemy(float x, float y, int health, EnemyType type) {
		super(x, y, health);
		this.type = type;
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
		rw = img.getWidth();
		rh = img.getHeight();
		createHitBox();
	}

	public void setAttackTimer(int min, int max) {
		attackTimerMin = min;
		attackTimerMax = max;
	}

	public void setBulletVelocity(float v) {
		vbullet = v;
	}

	protected static double[] getDir(double dx, double dy) {
		double ratio = dy / dx;

		double idir = Math.sqrt(1 / (Math.pow(ratio, 2) + 1));
		if (dx < 0)
			idir = -idir;
		double jdir = ratio * idir;
		if (Double.isNaN(jdir))
			jdir = -1;
		return new double[] { idir, jdir };
	}

	public void tick() {
		super.tick();
		attack();
		tryMove();
	}

	protected void attack() {
		// lol probably shouldve used vector math but o well
		float dx = (Game.player.getX() - x);
		float dy = (Game.player.getY() - y);

		double[] toPlayerDir = getDir(dx, dy);
		if (attackTimer <= 0) {
			shoot(toPlayerDir[0], toPlayerDir[1], vbullet);
			attackTimer = random.nextInt(attackTimerMax) + attackTimerMin;
		}
		attackTimer--;
	}

	protected void tryMove() {
		// check if within bounds
		if (x >= 0 && x <= Game.WIDTH && y >= 0 && y <= BOUNDINGYMAX)
			inBounds = true;
		else
			inBounds = false;

		// randomly move if movement flag checked
		if (stopped) {
			stoppedTimer--;
			if (stoppedTimer <= 0) {
				// pick some random x and y within bounding area, move towards it
				int randx = random.nextInt(Game.WIDTH);
				int randy = random.nextInt(BOUNDINGYMAX-BOUNDINGYMIN)+BOUNDINGYMIN;
				float dx2 = randx - x;
				float dy2 = randy - y;
				float dis = (float) Math.sqrt((Math.pow(dx2, 2) + Math.pow(dy2, 2)));
				double[] moveDir = getDir(dx2, dy2);
				vx = (float) moveDir[0];
				vy = (float) moveDir[1];
				stopped = false;
				moveTimer = (int) dis;
				stoppedTimer = random.nextInt(maxStoppedTime) + minStoppedTime;
			}
		} else {
			if (inBounds) {
				vx *= 0.98;
				vy *= 0.98;
			}
			if (moveTimer <= 0) {
				vx = 0;
				vy = 0;
				stopped = true;
			}
			moveTimer--;
		}
		move(vx, vy);

	}

	private void shoot(double idir, double jdir, float vTot) {
		Bullet bullet = new Bullet(false, 1, x, y);
		bullet.setVTot(vTot);
		bullet.setVx((float) (vTot * idir));
		bullet.setVy((float) (vTot * jdir));
		Game.spawn(bullet);
	}

	@Override
	public void render(Graphics g) {
		int w = 0, h = 0;
		int xoff, yoff;
		w = img.getWidth();
		h = img.getHeight();
		xoff = (int) (x - w / 2);
		yoff = (int) (y - h / 2);
		g.drawImage(img, (int) xoff, (int) yoff, w, h, null);

		// hitbox for clarity
//		g.setColor(Color.red);
//		g.drawRect(rect.x, rect.y, rw, rh);
	}

}
