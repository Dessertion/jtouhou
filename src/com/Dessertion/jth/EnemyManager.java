package com.Dessertion.jth;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

import com.Dessertion.jth.entity.BasicEnemy;
import com.Dessertion.jth.entity.BasicEnemy.EnemyType;
import com.Dessertion.jth.entity.BigBoi;

public final class EnemyManager {
	private static int tickTimer = 0;
	public static PriorityQueue<Wave> waves = new PriorityQueue<Wave>(1,new Wave.WaveComparator());
	private static Wave cur = null;
	private static Random random = new Random();
	
	public static class Wave{
		private BasicEnemy[] arr;
		private int tickTime;
		public Wave() {
			
		}
		public Wave(int n, int tickTime) {
			arr = new BasicEnemy[n];
			this.tickTime=tickTime;
		}
		
		public Wave(BasicEnemy[] arr, int tickTime) {
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
		BasicEnemy[] arr;
		for(int i = 0; i<=10; i++) {
			arr = new BasicEnemy[Math.min(i+1, 4)];
			for(int j = 0 ; j < arr.length; j++) {
				arr[j]= new BasicEnemy(random.nextInt(Game.WIDTH*2)-50, random.nextInt(30)*-1-20, (int)(i*1.5)+1, EnemyType.ALIEN1);
			}
			waves.add(new Wave(arr,i*300+random.nextInt(100)));
		}
		waves.add(new Wave(new BasicEnemy[] {new BigBoi(50,-5)},3600));
		tickTimer=0;
	}
	
	//lol poorly written
	public static void tick() {
		if (!waves.isEmpty() && cur == null)
			cur = waves.poll();
		if (cur != null) {
			if (tickTimer >= cur.getTickTime()) {
				Game.player.dmg+=0.5;
				for (BasicEnemy e : cur.arr) {
					if(e instanceof BigBoi) {
						Game.bossFight=true;
					}
					Game.spawn(e);
				}
				cur = null;

			}
		}
		tickTimer++;
	}
}
