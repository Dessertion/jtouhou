package com.Dessertion.jth;
public class Core implements Runnable{
    
	private final double UPDATE_FREQ = 30.0; //frequency in hertz 
	private final double UPDATE_INTERVAL_TIME= 1e9/UPDATE_FREQ; //calculate # of ns each frame should take for target freq
	private final int MAX_UPDATES_BEFORE_RENDER = 3; //update the game at most this many times before new render; lower number for less visual hitches
	
	private final double TARGET_FPS = 60; //if we can get as high as this fps, don't rerender 
	private final double TARGET_UPDATE_TIME = 1e9/TARGET_FPS;
	
	public boolean isRunning;
	
	public Core() {
		
	}
	
	public void run() {
		//get both last update time and render time
		long lastUpdateTime, lastRenderTime = lastUpdateTime = System.nanoTime(); 
		
		//get fps
		int lastSecondTime = (int)(lastUpdateTime/1e9);
		
		while(isRunning) {
			
		}
		
		
		
	}
	
	public static void main(String[] args) {
    	
    }
    
    
}



