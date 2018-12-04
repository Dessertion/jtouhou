package com.Dessertion.jth;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.Dessertion.jth.EnemyManager.Wave;
import com.Dessertion.jth.entity.Enemy;
import com.Dessertion.jth.entity.Enemy.EnemyType;

public final class EnemyManager {
	private static int tickTimer = 0;
	public static PriorityQueue<Wave> waves = new PriorityQueue<Wave>(1,new Wave.WaveComparator());
	private static Wave cur = null;
	
	public static class Wave{
		private Enemy[] arr;
		private int tickTime;
		public Wave() {
			
		}
		public Wave(int n, int tickTime) {
			arr = new Enemy[n];
			this.tickTime=tickTime;
		}
		
		public Wave(Enemy[] arr, int tickTime) {
			this.arr=arr;
			this.tickTime = tickTime;
		}
		
		public int getTickTime() {
			return tickTime;
		}
		
		static class WaveComparator implements Comparator<Wave>{
			@Override
			public int compare(Wave a, Wave b) {
				return a.tickTime<b.tickTime? -1:1;
			}
		}
		
	}
	
	
	
	public static void init() { 
		waves.add(new Wave(new Enemy[] {new Enemy(10,10,10,EnemyType.ALIEN)},200));
		tickTimer=0;
	}
	
	//lol poorly written
	public static void tick() {
		if (!waves.isEmpty() && cur == null)
			cur = waves.poll();
		if (cur != null) {
			if (tickTimer >= cur.getTickTime()) {
				for (Enemy e : cur.arr) {
					Game.spawn(e);
				}
				cur = null;

			}
		}
		tickTimer++;
	}
}
