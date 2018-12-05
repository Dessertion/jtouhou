package com.Dessertion.jth.sound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javazoom.jl.converter.WaveFileObuffer;
import javazoom.jl.converter.Converter;
import javazoom.jl.converter.WaveFile.WaveFileSample;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MPEGClip extends WavClip{
	
	private File file = null;
	
	public MPEGClip(String filename) throws FileNotFoundException, JavaLayerException {
		loadFile(filename);
		Converter converter = new Converter();
		File ret = null;
		try {
			ret = File.createTempFile("temp", ".wav");
		} catch (IOException e) {
			e.printStackTrace();
		}
		converter.convert(file.getPath(), ret.getPath());
		loadClip(ret);
	}
	
	private void loadFile(String filename) {
		file = new File(filename);
	}
	
}
