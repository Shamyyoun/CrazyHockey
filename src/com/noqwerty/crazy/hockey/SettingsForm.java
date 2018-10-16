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
public class SettingsForm extends GameCanvas implements Runnable, GestureListener {

    private MIDlet midlet;
    private GestureInteractiveZone gizone;
    private Thread threadHighScoreForm;
    private Image imageBackground;
    private Image imageBack;
    private Image imageSound;
    private Image imageRadioButton;
    private Image imagePlayer;
    private Image imageVirtual;
    private Sprite spriteBack;
    private Sprite spriteSound;
    private Sprite spriteRadioButtonEasy;
    private Sprite spriteRadioButtonMedium;
    private Sprite spriteRadioButtonHard;
    private Sprite spriteRadioButtonTime;
    private Sprite spriteRadioButtonScore;
    private Sprite spritePlayer;
    private Sprite spriteBackVirtual;
    private int xBack;
    private int yBack;
    private int xSound;
    private int ySound;
    private int xRadioButtonEasy;
    private int yRadioButtonEasy;
    private int xRadioButtonMedium;
    private int yRadioButtonMedium;
    private int xRadioButtonHard;
    private int yRadioButtonHard;
    private int xRadioButtonTime;
    private int yRadioButtonTime;
    private int xRadioButtonScore;
    private int yRadioButtonScore;
    private int xPlayer;
    private int yPlayer;
    private boolean performBack;
    private int oldX;
    private int oldY;
    private int spaceBetweenRadios = 25;
    private int buttonMargin = 8;
    private SoundPlayer player;

    public SettingsForm(MIDlet midlet) {
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
            case GestureInteractiveZone.GESTURE_TAP: {
                if (gestureEvent.getStartX() >= xSound - 10
                        && gestureEvent.getStartX() <= spriteSound.getWidth() + xSound + 10
                        && gestureEvent.getStartY() >= ySound - 10
                        && gestureEvent.getStartY() <= spriteSound.getHeight() + ySound + 10) {
                    onSound();
                } else if (gestureEvent.getStartX() >= xRadioButtonEasy - buttonMargin
                        && gestureEvent.getStartX() <= spriteRadioButtonEasy.getWidth() + xRadioButtonEasy + buttonMargin + 40
                        && gestureEvent.getStartY() >= yRadioButtonEasy - buttonMargin
                        && gestureEvent.getStartY() <= spriteRadioButtonEasy.getHeight() + yRadioButtonEasy + buttonMargin) {
                    onRadioButtonDiff(spriteRadioButtonEasy);
                } else if (gestureEvent.getStartX() >= xRadioButtonMedium - buttonMargin
                        && gestureEvent.getStartX() <= spriteRadioButtonMedium.getWidth() + xRadioButtonMedium + buttonMargin + 40
                        && gestureEvent.getStartY() >= yRadioButtonMedium - buttonMargin
                        && gestureEvent.getStartY() <= spriteRadioButtonMedium.getHeight() + yRadioButtonMedium + buttonMargin) {
                    onRadioButtonDiff(spriteRadioButtonMedium);
                } else if (gestureEvent.getStartX() >= xRadioButtonHard - buttonMargin
                        && gestureEvent.getStartX() <= spriteRadioButtonHard.getWidth() + xRadioButtonHard + buttonMargin + 40
                        && gestureEvent.getStartY() >= yRadioButtonHard - buttonMargin
                        && gestureEvent.getStartY() <= spriteRadioButtonHard.getHeight() + yRadioButtonHard + buttonMargin) {
                    onRadioButtonDiff(spriteRadioButtonHard);
                } else if (gestureEvent.getStartX() >= xRadioButtonTime - buttonMargin
                        && gestureEvent.getStartX() <= spriteRadioButtonTime.getWidth() + xRadioButtonTime + buttonMargin + 40
                        && gestureEvent.getStartY() >= yRadioButtonTime - buttonMargin
                        && gestureEvent.getStartY() <= spriteRadioButtonTime.getHeight() + yRadioButtonTime + buttonMargin) {
                    onRadioButtonMode(spriteRadioButtonTime);
                } else if (gestureEvent.getStartX() >= xRadioButtonScore - buttonMargin
                        && gestureEvent.getStartX() <= spriteRadioButtonScore.getWidth() + xRadioButtonScore + buttonMargin + 40
                        && gestureEvent.getStartY() >= yRadioButtonScore - buttonMargin
                        && gestureEvent.getStartY() <= spriteRadioButtonScore.getHeight() + yRadioButtonScore + buttonMargin) {
                    onRadioButtonMode(spriteRadioButtonScore);
                }
            }
            break;

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
            imageBackground = Image.createImage("/images/settingsbg.jpg");
            imageBack = Image.createImage("/images/back.png");
            imageSound = Image.createImage("/images/sound.png");
            imageRadioButton = Image.createImage("/images/rb.png");
            imagePlayer = Image.createImage("/images/menuplayer.PNG");
            imageVirtual = Image.createImage("/images/virtual.png");

            spriteBack = new Sprite(imageBack);
            spriteSound = new Sprite(imageSound, imageSound.getWidth() / 2, imageSound.getHeight());
            spriteRadioButtonEasy = new Sprite(imageRadioButton, imageRadioButton.getWidth() / 2, imageRadioButton.getHeight());
            spriteRadioButtonMedium = new Sprite(imageRadioButton, imageRadioButton.getWidth() / 2, imageRadioButton.getHeight());
            spriteRadioButtonHard = new Sprite(imageRadioButton, imageRadioButton.getWidth() / 2, imageRadioButton.getHeight());
            spriteRadioButtonTime = new Sprite(imageRadioButton, imageRadioButton.getWidth() / 2, imageRadioButton.getHeight());
            spriteRadioButtonScore = new Sprite(imageRadioButton, imageRadioButton.getWidth() / 2, imageRadioButton.getHeight());
            spritePlayer = new Sprite(imagePlayer);
            spriteBackVirtual = new Sprite(imageVirtual);

            spriteRadioButtonEasy.setFrame(1);
            spriteRadioButtonScore.setFrame(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // intialize x,y
        xBack = imageBackground.getWidth() - spriteBack.getWidth() - 30;
        yBack = 320;
        xSound = 130;
        ySound = 85;
        xRadioButtonEasy = 125;
        yRadioButtonEasy = 142;
        xRadioButtonMedium = xRadioButtonEasy;
        yRadioButtonMedium = yRadioButtonEasy + spaceBetweenRadios - 1;
        xRadioButtonHard = xRadioButtonEasy;
        yRadioButtonHard = yRadioButtonEasy + (spaceBetweenRadios * 2) - 2;
        xRadioButtonTime = xRadioButtonEasy;
        yRadioButtonTime = yRadioButtonEasy + (spaceBetweenRadios * 2) + 41;
        xRadioButtonScore = xRadioButtonEasy;
        yRadioButtonScore = yRadioButtonTime + spaceBetweenRadios;
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

        setOptionButtons();
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
        spriteBack.setPosition(xBack, yBack);
        spriteBack.paint(g);

        spriteSound.setPosition(xSound, ySound);
        spriteSound.paint(g);

        spriteRadioButtonEasy.setPosition(xRadioButtonEasy, yRadioButtonEasy);
        spriteRadioButtonEasy.paint(g);

        spriteRadioButtonMedium.setPosition(xRadioButtonMedium, yRadioButtonMedium);
        spriteRadioButtonMedium.paint(g);

        spriteRadioButtonHard.setPosition(xRadioButtonHard, yRadioButtonHard);
        spriteRadioButtonHard.paint(g);

        spriteRadioButtonTime.setPosition(xRadioButtonTime, yRadioButtonTime);
        spriteRadioButtonTime.paint(g);

        spriteRadioButtonScore.setPosition(xRadioButtonScore, yRadioButtonScore);
        spriteRadioButtonScore.paint(g);

        spritePlayer.setPosition(xPlayer, yPlayer);
        spritePlayer.paint(g);

        // draw virtual buttons
        spriteBackVirtual.setPosition(xBack + 16, yBack + 7);
        spriteBackVirtual.paint(g);
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
            yPlayer = imageBackground.getHeight() - spritePlayer.getHeight() - 1;
        }
        if (yPlayer < 0) {
            yPlayer = 1;
        }

        int xSpace = 8;
        int ySpace = -3;
        if (spritePlayer.collidesWith(spriteBackVirtual, false)) {
            xPlayer = xBack + xSpace;
            yPlayer = yBack + ySpace;

            performBack = true;
        }

        updateScreenGraphics(getGraphics());

        oldX = x;
        oldY = y;
    }

    private void doActions() {
        if (performBack) {
            player.play("/sounds/but.wav", 400);
            try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
            Display.getDisplay(midlet).setCurrent(new MainForm(midlet));
        }
    }

    private void onSound() {
        spriteSound.nextFrame();
        if (CrazyHockey.sound.equals("on")) {
            // make it off
            CrazyHockey.sound = "off";
            player.setSound(false);
        } else {
            // make it on
            CrazyHockey.sound = "on";
            player.setSound(true);
        }
        updateScreenGraphics(getGraphics());
    }

    private void onRadioButtonDiff(Sprite sprite) {
        // make all unselected first
        spriteRadioButtonEasy.setFrame(0);
        spriteRadioButtonMedium.setFrame(0);
        spriteRadioButtonHard.setFrame(0);
        sprite.setFrame(1);
        if (sprite == spriteRadioButtonEasy) {
            CrazyHockey.diff = "easy";
        } else if (sprite == spriteRadioButtonMedium) {
            CrazyHockey.diff = "medium";
        } else if (sprite == spriteRadioButtonHard) {
            CrazyHockey.diff = "hard";
        }
        updateScreenGraphics(getGraphics());
    }

    private void onRadioButtonMode(Sprite sprite) {
        // make all unselected first
        spriteRadioButtonTime.setFrame(0);
        spriteRadioButtonScore.setFrame(0);
        sprite.setFrame(1);
        updateScreenGraphics(getGraphics());
        if (sprite == spriteRadioButtonTime) {
            CrazyHockey.mode = "time";
        } else if (sprite == spriteRadioButtonScore) {
            CrazyHockey.mode = "score";
        }
    }

    private void setOptionButtons() {
        if (CrazyHockey.sound.equals("off")) {
            spriteSound.setFrame(1);
        }

        if (CrazyHockey.diff.equals("medium")) {
            spriteRadioButtonEasy.setFrame(0);
            spriteRadioButtonMedium.setFrame(1);
        } else if (CrazyHockey.diff.equals("hard")) {
            spriteRadioButtonEasy.setFrame(0);
            spriteRadioButtonHard.setFrame(1);
        }

        if (CrazyHockey.mode.equals("time")) {
            spriteRadioButtonScore.setFrame(0);
            spriteRadioButtonTime.setFrame(1);
        }
    }
}
