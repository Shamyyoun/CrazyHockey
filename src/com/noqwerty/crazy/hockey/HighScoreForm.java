/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.noqwerty.crazy.hockey;

import com.nokia.mid.ui.gestures.GestureEvent;
import com.nokia.mid.ui.gestures.GestureInteractiveZone;
import com.nokia.mid.ui.gestures.GestureListener;
import com.nokia.mid.ui.gestures.GestureRegistrationManager;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Label;
import com.sun.lwuit.plaf.Style;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.midlet.MIDlet;

/**
 * 
 * @author Mahmoud Elshamy
 */
public class HighScoreForm extends GameCanvas implements Runnable,
		GestureListener {

	private MIDlet midlet;
	private GestureInteractiveZone gizone;
	private Thread threadHighScoreForm;
	private Image imageBackground;
	private Image imageUpload;
	private Image imageBack;
	private Image imagePlayer;
	private Image imageUploaded;
	private Image imageVirtual;
	private Sprite spriteUpload;
	private Sprite spriteBack;
	private Sprite spritePlayer;
	private Sprite spriteUploaded;
	private Sprite spriteUploadVirtual;
	private Sprite spriteBackVirtual;
	private int xUpload;
	private int yUpload;
	private int xBack;
	private int yBack;
	private int xPlayer;
	private int yPlayer;
	private int xStringHighScore;
	private int yStringHighScore;
	private boolean performUpload;
	private boolean performBack;
	private HighScoreRS highScore;
	private String stringHighScore;
	private int oldX;
	private int oldY;
	private SoundPlayer player;
	private int xUploaded;
	private int yUploaded;
	private boolean drawUploaded = false;

	public HighScoreForm(MIDlet midlet) {
		super(true);
		this.midlet = midlet;
		gizone = new GestureInteractiveZone(GestureInteractiveZone.GESTURE_ALL);
		GestureRegistrationManager.register(this, gizone);
		GestureRegistrationManager.setListener(this, this);
		start();
	}

	public void run() {
		updateScreenGraphics(getGraphics());
	}

	public void gestureAction(Object container,
			GestureInteractiveZone gestureInteractiveZone,
			GestureEvent gestureEvent) {
		switch (gestureEvent.getType()) {
		case GestureInteractiveZone.GESTURE_RECOGNITION_END: {
			doActions();
		}
			break;

		default:
			break;
		}
	}

	protected void pointerPressed(int x, int y) {
		super.pointerPressed(x, y);
		updateScreenGraphics(getGraphics());
		oldX = x;
		oldY = y;
	}

	protected void pointerDragged(int x, int y) {
		super.pointerDragged(x, y);

		moveButtonPlayer(x, y);
	}

	public void start() {
		this.setFullScreenMode(true);

		try {
			imageBackground = Image.createImage("/images/highscorebg.jpg");
			imageUpload = Image.createImage("/images/upload.png");
			imageBack = Image.createImage("/images/back.png");
			imagePlayer = Image.createImage("/images/menuplayer.PNG");
			imageUpload = Image.createImage("/images/uploaded.png");
			imageVirtual = Image.createImage("/images/virtual.png");

			spriteUpload = new Sprite(imageUpload);
			spriteBack = new Sprite(imageBack);
			spritePlayer = new Sprite(imagePlayer);
			spriteUploaded = new Sprite(imageUploaded);
			spriteUploadVirtual = new Sprite(imageVirtual);
			spriteBackVirtual = new Sprite(imageVirtual);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Initialize x,y
		xUpload = 30;
		yUpload = 260;
		xBack = imageBackground.getWidth() - xUpload - imageBack.getWidth();
		yBack = yUpload;
		xPlayer = (imageBackground.getWidth() / 2)
				- (imagePlayer.getWidth() / 2);
		yPlayer = imageBackground.getHeight() - imagePlayer.getHeight() - 15;
		xStringHighScore = 117;
		yStringHighScore = 180;
		xUploaded = imageBackground.getWidth()/2 - spriteUploaded.getWidth()/2;
		yUploaded = imageBackground.getHeight()/2 - spriteUploaded.getHeight()/2;

		// check sound in settings
		boolean sound = true;
		if (CrazyHockey.sound.equals("off")) {
			sound = false;
		}
		player = new SoundPlayer(sound);

		threadHighScoreForm = new Thread(this);
		threadHighScoreForm.start();

	}

	private void updateScreenGraphics(Graphics g) {
		drawBackground(g);
		drawStringHighScore(g);
		drawButtons(g);
		if (drawUploaded) {
			drawUploaded(g);
		}
		repaint();
	}

	private void drawBackground(Graphics g) {
		g.drawImage(imageBackground, 0, 0, 0);
	}
	
	private void drawButtons(Graphics g) {
		spriteUpload.setPosition(xUpload, yUpload);
		spriteUpload.paint(g);

		spriteBack.setPosition(xBack, yBack);
		spriteBack.paint(g);

		spritePlayer.setPosition(xPlayer, yPlayer);
		spritePlayer.paint(g);

		// draw virtual buttons
		spriteUploadVirtual.setPosition(xUpload + 16, yUpload + 7);
		spriteUploadVirtual.paint(g);

		spriteBackVirtual.setPosition(xBack + 16, yBack + 7);
		spriteBackVirtual.paint(g);
	}
	
	private void drawUploaded(Graphics g) {
		spriteUploaded.paint(g);
	}

	private void moveButtonPlayer(int x, int y) {
		xPlayer += (x - oldX);
		yPlayer += (y - oldY);
		if (xPlayer > imageBackground.getWidth() - spritePlayer.getWidth()) {
			xPlayer = imageBackground.getWidth() - spritePlayer.getWidth() - 1;
		}
		if (xPlayer < 0) {
			xPlayer = 1;
		}
		if (yPlayer > imageBackground.getHeight() - spritePlayer.getHeight()) {
			yPlayer = imageBackground.getHeight() - spritePlayer.getHeight()
					- 1;
		}
		if (yPlayer < 0) {
			yPlayer = 1;
		}

		int xSpace = 8;
		int ySpace = -3;
		if (spritePlayer.collidesWith(spriteUploadVirtual, false)) {
			xPlayer = xUpload + xSpace;
			yPlayer = yUpload + ySpace;

			performUpload = true;
		} else if (spritePlayer.collidesWith(spriteBackVirtual, false)) {
			xPlayer = xBack + xSpace;
			yPlayer = yBack + ySpace;

			performBack = true;
		}

		updateScreenGraphics(getGraphics());

		oldX = x;
		oldY = y;
	}

	private void doActions() {
		if (performUpload) {

			// play button sound in other way
			boolean sound = true;
			if (CrazyHockey.sound.equals("off")) {
				sound = false;
			}
			new SoundPlayer(sound).play("/sounds/but.wav", 400);

			xPlayer = (imageBackground.getWidth() / 2)
					- (imagePlayer.getWidth() / 2);
			yPlayer = imageBackground.getHeight() - imagePlayer.getHeight()
					- 15;
			updateScreenGraphics(getGraphics());
			try {
				String name = CrazyHockey.username;
				name = name.replace(' ', '+');
				String number = stringHighScore;
				String s = "http://noqwerty.com/high_core/index.php?game_name=crazy_hockey&&game_user="
						+ name + "&&high_score=" + number;
				sendPostRequest(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
			xPlayer = (imageBackground.getWidth() / 2)
					- (imagePlayer.getWidth() / 2);
			yPlayer = imageBackground.getHeight() - imagePlayer.getHeight()
					- 15;

			drawUploaded = true;
			updateScreenGraphics(getGraphics());
			performUpload = false;
		} else if (performBack) {

			// play button sound in other way
			boolean sound = true;
			if (CrazyHockey.sound.equals("off")) {
				sound = false;
			}
			new SoundPlayer(sound).play("/sounds/but.wav", 400);
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Display.getDisplay(midlet).setCurrent(new MainForm(midlet));
			performBack = false;
		}
	}

	private void drawStringHighScore(Graphics g) {
		// get high score
		highScore = new HighScoreRS();
		stringHighScore = highScore.getRecord();

		// set new x for stringHighScore
		xStringHighScore = 117;
		for (int i = 0; i < stringHighScore.length(); i++) {
			if (i != 0) {
				xStringHighScore -= 5;
			}
		}

		g.setColor(0xffffff);
		g.drawString(stringHighScore, xStringHighScore, yStringHighScore,
				Graphics.TOP | Graphics.LEFT);
	}

	public String sendPostRequest(String urlstring) throws IOException {
		HttpConnection hc = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;

		String message = "";

		// specifying the query string
		String requeststring = "request=gettimestamp";

		// openning up http connection with the web server
		// for both read and write access

		try {
			hc = (HttpConnection) Connector.open(urlstring);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Finally");
			if (hc != null) {
				int chk = hc.getResponseCode();

				// setting the request method to POST
				if (chk == HttpConnection.HTTP_OK) {
					// hc.setRequestMethod(HttpConnection.POST);

					// obtaining output stream for sending query string
					dos = hc.openDataOutputStream();
					byte[] request_body = requeststring.getBytes();

					// sending query string to web server
					for (int i = 0; i < request_body.length; i++) {
						dos.writeByte(request_body[i]);
					}
					// flush outdos.flush();

					// obtaining input stream for receiving HTTP response
					dis = new DataInputStream(hc.openInputStream());

					// reading the response from web server character by
					// character
					int ch;
					while ((ch = dis.read()) != -1) {
						message = message + (char) ch;
					}

				} else {

					Dialog validDialog = new Dialog("Error !!");
					validDialog.setScrollable(false);
					validDialog.setIsScrollVisible(false);
					validDialog.setTimeout(3000); // set timeout milliseconds
					Label l = new Label("no internet connection");
					Style s = new Style();
					s.setBgTransparency(0);
					s.setBgColor(255);
					l.setUnselectedStyle(s);
					l.setSelectedStyle(s);
					validDialog.addComponent(l);
					validDialog.show(0, 150, 0, 0, true);

				}
				hc.close();
				dis.close();
				dos.close();

				// return message;
			}

		}
		return "er";
	}
}
