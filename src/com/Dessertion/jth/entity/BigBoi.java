package com.Dessertion.jth.entity;
public class BigBoi extends BasicEnemy{

    public BigBoi(float x, float y, int health, EnemyType type) {
	super(x, y, health, type);
    }
    
    @Override
    public void tick() {
	tickTime++;
    }
    
    static class AttackPattern{
	
	public AttackPattern() {
	    
	}
	
    }
    
    
    

}



