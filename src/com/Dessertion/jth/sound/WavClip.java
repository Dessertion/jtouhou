package com.Dessertion.jth.sound;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WavClip {
	private Clip clip;
	private boolean playing=false;
	
	public WavClip() {

	}

	public WavClip(String filename) {
		loadClip(filename);
	}
	
	public void loadClip(File file) {
		try {
			if (file.exists()) {
				AudioInputStream sound = AudioSystem.getAudioInputStream(file);
				clip = AudioSystem.getClip();
				clip.open(sound);
			}
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	private void loadClip(String filename) {
		File file = new File(filename);
		loadClip(file);
	}

	public void setPlaying(boolean playing) {
		this.playing=playing;
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.start();
				}
			}.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		clip.stop();
	}
}
