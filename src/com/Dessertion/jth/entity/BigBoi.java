package com.Dessertion.jth.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.Dessertion.jth.Game;
import com.Dessertion.jth.entity.BasicEnemy.AttackPattern.AttackType;
import com.Dessertion.jth.sound.Sound;

public class BigBoi extends BasicEnemy {

	protected AttackType[] types = AttackType.values();
	private AttackPattern currentAttack = new AttackPattern(); // empty constructor, auto sets it to finished lol

	public BigBoi(float x, float y) {
		super(x, y, 1200, EnemyType.BOSS);
		init();
	}

	private void init() {
		attackTimerMin = 120;
		attackTimerMax = 120;
		minStoppedTime = 240;
		maxStoppedTime = 120;
	}

	@Override
	public void tick() {
		tickTime++;
		if (hp < 0)
			die();
		if (!alive) {
			Game.hasWon = true;
			Sound.ko.play();
		}
			
		if(currentAttack.isFinished())super.tryMove();
		attack();
	}

	@Override
	protected void attack() {
		if (!currentAttack.isFinished()) {
			currentAttack.tick();
		} else {
			if (attackTimer <= 0) {
				if (stopped) {
					AttackType nextType = randomAttackType();
					System.out.println(nextType);
					
					if (nextType != AttackType.CIRCLE) {
						currentAttack = new AttackPattern(5, AttackPattern.DEFAULT_DELAY, nextType, this);
					} else {
						currentAttack = new AttackPattern(1, AttackPattern.CIRCLE_DELAY, nextType, this);
					}
					attackTimer = random.nextInt(attackTimerMax) + attackTimerMin;
				}
			}
			attackTimer--;
		}
		
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		g.setColor(Color.red);
		g.setFont(new Font("Trebuchet MS",Font.PLAIN,8));
		g.drawString("BOSS HP", 5, Game.HEIGHT-15);
		g.drawLine(5,Game.HEIGHT-5,5+(Game.WIDTH-10)*hp/maxHp,Game.HEIGHT-5);
	}

	protected AttackType randomAttackType() {
		return types[random.nextInt(types.length)];
	}

}
