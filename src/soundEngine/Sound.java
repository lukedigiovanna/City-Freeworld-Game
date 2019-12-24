package soundEngine;

import javax.sound.sampled.*;
import java.io.*;

public class Sound {
	
	//any sound lower than MIN_VOLUME should completely mute the sound.
	public static final float MIN_VOLUME = -50.0f, MAX_VOLUME = 0.0f;
	
	private Clip clip;
	private float volume;
	
	private boolean playing = false;
	
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
		playing = true;
	}
	
	public void pause() {
		clip.stop();
		playing = false;
	}
	
	public void toggle() {
		if (playing)
			pause();
		else
			play();
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void setVolume(float value) {
		if (value < MIN_VOLUME)
			value = -1000.0f;
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(value); 
		this.volume = value;
	}
}
