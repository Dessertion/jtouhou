package com.Dessertion.jth.sound;

import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;

public final class Sound {
	public static MPEGClip shoot = null;
	public static MPEGClip bgm = null;
	public static MPEGClip bosstheme = null;
	public static MPEGClip ko = null;
	
	public static void init() throws FileNotFoundException, JavaLayerException {
		shoot = new MPEGClip("./res/shoot.mp3");
		bgm = new MPEGClip("./res/[07] Dancing Water Spray.mp3");
		bosstheme = new MPEGClip("./res/[05] Dark Side of Fate.mp3");
		ko = new MPEGClip("./res/ko1.mp3");
	}
}
