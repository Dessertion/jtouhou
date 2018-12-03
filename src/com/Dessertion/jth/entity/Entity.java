package com.Dessertion.jth.entity;

import java.awt.Graphics;
import java.awt.Rectangle; 

import com.Dessertion.jth.Game;

public abstract class Entity {
	
	// bounding box
	protected Rectangle rect;

	//coords
	protected float x,y;
	
	//render if entity still alive
	protected boolean alive = true;	
	
	//tick time
	protected int tickTime = 0;
	
	protected boolean stopped = false;
	
	public Entity(Game game) {
		game.entities.add(this);
	}
	
	public Rectangle getRect() {
		return rect;
	}
	
	public boolean intersects(Entity e) {
		return rect.intersects(e.rect);
	}
	
	public float getX() {
		return x;
	}


	public void setX(float x) {
		this.x = x;
	}


	public float getY() {
		return y;
	}


	public void setY(float y) {
		this.y = y;
	}


	public boolean isAlive() {
		return alive;
	}
	
	public void remove() {
		alive = false;
	}

	public abstract void tick();

	public boolean move(int vx, int vy) {
		if(vx!=0||vy!=0) {
			x+=vx;
			y+=vy;
			rect.translate(vx, vy);
			return !(stopped = false);
		}
		
		return !(stopped=true);
	}
	

	public abstract void render(Graphics g);
	
	public boolean damage(DamageableEntity e, int damage) {
		if(damage>0) {
			e.hurt(damage);
		}
		return false;
	}
	

	
}	
