package com.Dessertion.jth;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Core implements Runnable {

	private final double tickFreq = 60.0; // frequency in hertz
	private final double nsPerTick = 1e9 / tickFreq; // calculate # of ns each frame should take for
																	// target freq
	private final int MAX_UPDATES_BEFORE_RENDER = 2; // update the game at most this many times before new render; lower
														// number for less visual hitches

	private final double TARGET_FPS = 60; // if we can get as high as this fps, don't rerender
	private final double TARGET_TIME_BETWEEN_RENDERS = 1e9 / TARGET_FPS;

	public boolean isRunning, isPaused;

	private double lastTickTime, lastRenderTime;
	private int frames;

	private Game game;
	
	public Core() {
		game = new Game();
		
		JFrame frame = new JFrame("Rocket Landing");
		frame.setSize(new Dimension(Game.WIDTH*Game.SCALE,Game.HEIGHT*Game.SCALE));
		frame.setMinimumSize(new Dimension(Game.WIDTH*Game.SCALE,Game.HEIGHT*Game.SCALE));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game,BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}	

	public synchronized void stop() {
		isRunning = false;
	}
	
	public void start() {
		isRunning = true;
		run();
	}
	
	public void run() {
		// get both last update time and render time
		lastRenderTime = lastTickTime = System.nanoTime();
		int lastSecondTime = (int) (lastTickTime / 1e9);
		
		double currentTime = System.nanoTime();
		int ticks=0;
		int frames=0;
		
		System.out.println("CURRENT TIME: " + System.nanoTime());
		
		
		while (isRunning&&!game.hasWon) {
			
			int updateCount = 0;
			
			// check paused state
			if (!isPaused) {

				// do as many game updates as needed, potentially play catchup
				double unprocessed = (currentTime - lastTickTime)/nsPerTick;
				while (unprocessed>=1&&updateCount<MAX_UPDATES_BEFORE_RENDER){
					ticks++;
					tick();
					lastTickTime += nsPerTick;
					updateCount++;
					unprocessed--;
				}
				
				
				// Render
				// for smooth rendering we need to calculate interpolation
				float interpolation = Math.min(1.0f, (float) ((currentTime - lastTickTime) / nsPerTick));
				game.render();
				lastRenderTime = currentTime;
				frames++;

				//yield until it has been at least the target time between renders; saves cpu from hogging
				while(currentTime - lastRenderTime<TARGET_TIME_BETWEEN_RENDERS&&currentTime - lastTickTime < nsPerTick) {
					Thread.yield();
					try {Thread.sleep(1);}
					catch(Exception e) {e.printStackTrace();}
					currentTime = System.nanoTime();
				}
				
				// check if a second has elapsed
				int currentSecond = (int) (lastTickTime / 1e9);
				if (currentSecond > lastSecondTime) {
					System.out.println("Second: " + currentSecond + " Frames: " + frames + " Ticks: " + ticks);
					// reset framecount, ticks, update second
					frames = 0;
					ticks = 0;
					lastSecondTime = currentSecond;
				}
				
				
				

			}

		}

		return;
	}

	private void tick() {
		game.tick();
	}

	public static void main(String[] args) {
		//create a daemon thread that sleeps for long.max_value
		//supposed to be a shoddy workaround to get high resolution sleep
		new Thread() {
			{
				setDaemon(true);
				start();
			}
			public void run() {
				System.out.println("yay");
				while(true) {
					try {Thread.sleep(Long.MAX_VALUE);}catch(Exception e) {}
				}
			}
		};
		
		//run core 
		Core core = new Core();
		core.start();
	}

}
