package soundEngine;

import javax.sound.sampled.*;
import java.io.*;

public class Sound {
	
	private Clip clip;
	
	/**
	 * Creates a sound from a WAVE file
	 * @param path File path to the .wav file
	 */
	public Sound(String path) {
		try {
			AudioInputStream audioInputStream = 
					AudioSystem.getAudioInputStream(
							new File(path)
							);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (Exception e) {
			
		}
	}
	
	public void play() {
		clip.start();
	}
	
	public void pause() {
		clip.stop();
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void setVolume(float value) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(value); // Reduce volume by 10 decibels.
	}
}
