package com.Dessertion.jth;

public class Core implements Runnable {

	private final double UPDATE_FREQ = 30.0; // frequency in hertz
	private final double UPDATE_INTERVAL_TIME = 1e9 / UPDATE_FREQ; // calculate # of ns each frame should take for
																	// target freq
	private final int MAX_UPDATES_BEFORE_RENDER = 3; // update the game at most this many times before new render; lower
														// number for less visual hitches

	private final double TARGET_FPS = 60; // if we can get as high as this fps, don't rerender
	private final double TARGET_TIME_BETWEEN_RENDERS = 1e9 / TARGET_FPS;

	public boolean isRunning, isPaused;

	private double lastUpdateTime, lastRenderTime;
	private int frameCount, fps;

	private Game game;
	
	public Core() {
		game = new Game();
		game.setVisible(true);
		game.createBufferStrategy(2);
	}	

	public synchronized void stop() {
		isRunning = false;
	}
	
	public void run() {
		// get both last update time and render time
		lastRenderTime = lastUpdateTime = System.nanoTime();

		// get last second time; used to get fps
		int lastSecondTime = (int) (lastUpdateTime / 1e9);
		
		//start running
		isRunning = true;
		System.out.println("CURRENT TIME: " + System.nanoTime());
		
		while (isRunning) {
			// get current time
			double currentTime = System.nanoTime();
			
			int updateCount = 0;

			// check paused state
			if (!isPaused) {

				// do as many game updates as needed, potentially play catchup
				while ((currentTime - lastUpdateTime > UPDATE_INTERVAL_TIME)
						&& (updateCount < MAX_UPDATES_BEFORE_RENDER)) {
					// TODO: implement updateGame function

					lastUpdateTime += UPDATE_INTERVAL_TIME;
					updateCount++;
				}

				// if update is taking forever, we don't want to do a lot of catchup
				if (currentTime - lastUpdateTime > UPDATE_INTERVAL_TIME)
					lastUpdateTime = currentTime - UPDATE_INTERVAL_TIME;

				// Render
				// to do so, we need to calculate interpolation
				float interpolation = Math.min(1.0f, (float) ((currentTime - lastUpdateTime) / UPDATE_INTERVAL_TIME));
				// TODO: integrate game state
				lastRenderTime = currentTime;

				// update the fps
				int currentSecond = (int) (lastUpdateTime / 1e9);
				if (currentSecond > lastSecondTime) {
					System.out.println("Second: " + currentSecond + " " + frameCount);
					
					// set fps to current framecount after each second, reset framecount
					//TODO: update framecount with every render
					fps = frameCount;
					frameCount = 0;
					lastSecondTime = currentSecond;
					
				}
				
				//yield until it has been at least the target time between renders; saves cpu from hogging
				while(currentTime - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && currentTime - lastUpdateTime < UPDATE_INTERVAL_TIME) {
					Thread.yield();
					try {
						Thread.sleep(0,999999);
					}
					catch(Exception e) {e.printStackTrace();}
					currentTime = System.nanoTime();
				}
				
				

			}

		}
		game.dispose();
		return;
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
				while(true) {
					try {Thread.sleep(Long.MAX_VALUE);}catch(Exception e) {}
				}
			}
		};
		
		//run core 
		new Core() {
			{
				run();
			}
		};
		
		
	}

}
