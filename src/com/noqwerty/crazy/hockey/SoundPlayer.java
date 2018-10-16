/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.noqwerty.crazy.hockey;

import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;

/**
 * 
 * @author Mahmoud Elshamy
 */
public class SoundPlayer implements Runnable {
	private boolean sound;
	private Thread threadPlayer;
	private int soundLength;
	private String path;

	public SoundPlayer(boolean sound) {
		this.sound = sound;
		threadPlayer = new Thread(this);
	}

	public void play(String path, int soundLength) {
		this.path = path;
		this.soundLength = soundLength;
		threadPlayer.start();
	}

	public void setSound(boolean sound) {
		this.sound = sound;
	}

	public void run() {
		if (sound) {
			InputStream is;
			Player p;
			is = getClass().getResourceAsStream(path);

			try {
				p = Manager.createPlayer(is, "audio/X-wav");
				p.start();
				threadPlayer.sleep(soundLength);
				p.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
