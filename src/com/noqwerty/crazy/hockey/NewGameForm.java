/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.noqwerty.crazy.hockey;

import com.nokia.mid.ui.gestures.GestureEvent;
import com.nokia.mid.ui.gestures.GestureInteractiveZone;
import com.nokia.mid.ui.gestures.GestureListener;
import com.nokia.mid.ui.gestures.GestureRegistrationManager;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author Mahmoud Elshamy
 */
public class NewGameForm extends GameCanvas implements Runnable, GestureListener {

    private MIDlet midlet;
    private GestureInteractiveZone gizone;
    private Thread threadHighScoreForm;
    private Image imageBackground;
    private Image imageOnePlayer;
    private Image imageTwoPlayers;
    private Image imageBluetooth;
    private Image imageBack;
    private Image imagePlayer;
    private Image imageVirtual;
    private Sprite spriteOnePlayer;
    private Sprite spriteTwoPlayers;
    private Sprite spriteBluetooth;
    private Sprite spriteBack;
    private Sprite spritePlayer;
    private Sprite spriteOnePlayerVirtual;
    private Sprite spriteTwoPlayersVirtual;
    private Sprite spriteBluetoothVirtual;
    private Sprite spriteBackVirtual;
    private int xOnePlayer;
    private int yOnePlayer;
    private int xTwoPlayers;
    private int yTwoPlayers;
    private int xBluetooth;
    private int yBluetooth;
    private int xBack;
    private int yBack;
    private int xPlayer;
    private int yPlayer;
    private boolean performOnePlayer;
    private boolean performTwoPlayers;
    private boolean performBluetooth;
    private boolean performBack;
    private int oldX;
    private int oldY;
    private int buttonSpace = 90;
    private SoundPlayer player;

    public NewGameForm(MIDlet midlet) {
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

    public void gestureAction(Object container, GestureInteractiveZone gestureInteractiveZone, GestureEvent gestureEvent) {
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
            imageBackground = Image.createImage("/images/mainbg.jpg");
            imageOnePlayer = Image.createImage("/images/oneplayer.png");
            imageTwoPlayers = Image.createImage("/images/twoplayers.png");
            imageBluetooth = Image.createImage("/images/bluetooth.png");
            imageBack = Image.createImage("/images/back.png");
            imagePlayer = Image.createImage("/images/menuplayer.PNG");
            imageVirtual = Image.createImage("/images/virtual.png");

            spriteOnePlayer = new Sprite(imageOnePlayer);
            spriteTwoPlayers = new Sprite(imageTwoPlayers);
            spriteBluetooth = new Sprite(imageBluetooth);
            spriteBack = new Sprite(imageBack);
            spritePlayer = new Sprite(imagePlayer);
            spriteOnePlayerVirtual = new Sprite(imageVirtual);
            spriteTwoPlayersVirtual = new Sprite(imageVirtual);
            spriteBluetoothVirtual = new Sprite(imageVirtual);
            spriteBackVirtual = new Sprite(imageVirtual);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // intialize x,y
        xOnePlayer = 20;
        yOnePlayer = 160;
        xTwoPlayers = xOnePlayer;
        yTwoPlayers = yOnePlayer + buttonSpace;
        xBluetooth = imageBackground.getWidth() - spriteBluetooth.getWidth() - xOnePlayer;
        yBluetooth = yOnePlayer;
        xBack = xBluetooth;
        yBack = yTwoPlayers;
        xPlayer = (imageBackground.getWidth() / 2) - (imagePlayer.getWidth() / 2);
        yPlayer = imageBackground.getHeight() - imagePlayer.getHeight() - 15;

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
        drawButtons(g);
        repaint();
    }

    private void drawBackground(Graphics g) {
        g.drawImage(imageBackground, 0, 0, 0);
    }

    private void drawButtons(Graphics g) {
        spriteOnePlayer.setPosition(xOnePlayer, yOnePlayer);
        spriteOnePlayer.paint(g);

        spriteTwoPlayers.setPosition(xTwoPlayers, yTwoPlayers);
        spriteTwoPlayers.paint(g);
        
        spriteBluetooth.setPosition(xBluetooth, yBluetooth);
        spriteBluetooth.paint(g);

        spriteBack.setPosition(xBack, yBack);
        spriteBack.paint(g);
        
        spritePlayer.setPosition(xPlayer, yPlayer);
        spritePlayer.paint(g);

        // draw virtual buttons
        spriteOnePlayerVirtual.setPosition(xOnePlayer + 16, yOnePlayer + 7);
        spriteOnePlayerVirtual.paint(g);

        spriteTwoPlayersVirtual.setPosition(xTwoPlayers + 16, yTwoPlayers + 7);
        spriteTwoPlayersVirtual.paint(g);
        
        spriteBluetoothVirtual.setPosition(xBluetooth + 16, yBluetooth + 7);
        spriteBluetoothVirtual.paint(g);
        
        spriteBackVirtual.setPosition(xBack + 16, yBack + 7);
        spriteBackVirtual.paint(g);
    }
    
    private void moveButtonPlayer(int x, int y) {
        xPlayer += (x - oldX);
        yPlayer += (y - oldY);
        if (xPlayer > imageBackground.getWidth()-spritePlayer.getWidth()) {
            xPlayer = imageBackground.getWidth()-spritePlayer.getWidth() - 1;
        }
        if (xPlayer < 0) {
            xPlayer = 1;
        }
        if (yPlayer > imageBackground.getHeight()-spritePlayer.getHeight()) {
            yPlayer = imageBackground.getHeight()-spritePlayer.getHeight() - 1;
        }
        if (yPlayer < 0) {
            yPlayer = 1;
        }

        int xSpace = 8;
        int ySpace = -3;
        if (spritePlayer.collidesWith(spriteOnePlayerVirtual, false)) {
            xPlayer = xOnePlayer + xSpace;
            yPlayer = yOnePlayer + ySpace;

            performOnePlayer = true;
        } else if (spritePlayer.collidesWith(spriteTwoPlayersVirtual, false)) {
            xPlayer = xTwoPlayers + xSpace;
            yPlayer = yTwoPlayers + ySpace;

            performTwoPlayers = true;
        } else if (spritePlayer.collidesWith(spriteBluetoothVirtual, false)) {
            xPlayer = xBluetooth + xSpace;
            yPlayer = yBluetooth + ySpace;

            performBluetooth = true;
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
        if (performOnePlayer) {
            player.play("/sounds/but.wav", 400);
            try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
            /*----write code------*/
        } else if (performTwoPlayers) {
            player.play("/sounds/but.wav", 400);
            try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
            /*----write code------*/
        } else if (performBluetooth) {
            player.play("/sounds/but.wav", 400);
            try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
            /*----write code------*/
        } else if (performBack) {
            player.play("/sounds/but.wav", 400);
            try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
            Display.getDisplay(midlet).setCurrent(new MainForm(midlet));
        }
    }
}
