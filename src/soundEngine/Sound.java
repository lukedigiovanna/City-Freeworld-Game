package soundEngine;

import javax.sound.sampled.*;
import java.io.*;

public class Sound {
	
	//any sound lower than MIN_VOLUME should completely mute the sound.
	public static final float MIN_VOLUME = -50.0f, MAX_VOLUME = 0.0f;
	
	private File audioFile;
	private Clip clip;
	private float volume;
	private float relativeVolume; //how loudly the sound should be played relative to other sounds
	// this is useful for determining the strength of a sound based on distance
	// is on a scale from 0 to 50, where 50 is a very loud sound
	
	private boolean playing = false;
	
	private boolean mute = false;
	
	/**
	 * Creates a sound from a WAVE file
	 * @param path File path to the .wav file
	 */
	public Sound(String path, float relativeVolume) {
		this(new File(path), relativeVolume);
	}
	
	public Sound(File audioFile, float relativeVolume) {
		this.audioFile = audioFile;
		this.relativeVolume = relativeVolume;
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
		return new Sound(this.audioFile,this.relativeVolume);
	}
	
	public void play() {
		if (mute)
			return;
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
	
	public void mute() {
		this.mute = true;
	}
	
	public void unmute() {
		this.mute = false;
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	/**
	 * Sets the actual volume that this sound will be played at.
	 * @param value
	 */
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
