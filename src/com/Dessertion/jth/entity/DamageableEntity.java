package com.Dessertion.jth.entity;

import com.Dessertion.jth.Game;

//entities with health
public abstract class DamageableEntity extends Entity{
	
	
	//health
	protected int hp, maxHp;
	
	public DamageableEntity(Game game, int health) {
		super(game);
		hp = maxHp = health;
		
	}

	
	
	
	
	public boolean hurt(int damage) {
		if (damage > 0) {
			hp -= damage;
			return true;
		}
		return false;
	}
	
	//potentially can use die for death anims?
	protected void die() {
		remove();
	}
	
	public void tick() {
		tickTime++;
		if(hp<=0)die();
	}
	
}
