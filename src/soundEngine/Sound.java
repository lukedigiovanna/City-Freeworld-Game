package soundEngine;

import javax.sound.sampled.*;
import java.io.*;

public class Sound {
	
	public static final Sound 
			GUN_SHOT = new Sound("assets/sounds/gunfire.wav"),
			GUN_CLICK = new Sound("assets/sounds/gunclick.wav");
	
	//any sound lower than MIN_VOLUME should completely mute the sound.
	public static final float MIN_VOLUME = -50.0f, MAX_VOLUME = 0.0f;
	
	private File audioFile;
	private Clip clip;
	private float volume;
	
	private boolean playing = false;
	
	/**
	 * Creates a sound from a WAVE file
	 * @param path File path to the .wav file
	 */
	public Sound(String path) {
		this(new File(path));
	}
	
	public Sound(File audioFile) {
		this.audioFile = audioFile;
		try {
			AudioInputStream audioInputStream = 
					AudioSystem.getAudioInputStream(
							audioFile
							);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (Exception e) {
			
		}
	}
	
	public Sound copy() {
		return new Sound(this.audioFile);
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
	
	public boolean dead() {
		return clip.getMicrosecondPosition() >= clip.getMicrosecondLength();
	}
}
