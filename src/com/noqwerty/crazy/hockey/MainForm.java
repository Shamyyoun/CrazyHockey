/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.noqwerty.crazy.hockey;

import com.nokia.mid.ui.gestures.GestureEvent;
import com.nokia.mid.ui.gestures.GestureInteractiveZone;
import com.nokia.mid.ui.gestures.GestureListener;
import com.nokia.mid.ui.gestures.GestureRegistrationManager;
import java.io.IOException;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author Mahmoud
 */
public class MainForm extends GameCanvas implements Runnable, GestureListener {

    private MIDlet midlet;
    private GestureInteractiveZone gizone;
    private Thread threadMainForm;
    private Image imageBackground;
    private Image imageNewGame;
    private Image imageHighScore;
    private Image imageSettings;
    private Image imageHelp;
    private Image imageAbout;
    private Image imageExit;
    private Image imagePlayer;
    private Image imageHand;
    private Image imageVirtual;
    private Sprite spriteNewGame;
    private Sprite spriteHighScore;
    private Sprite spriteSettings;
    private Sprite spriteHelp;
    private Sprite spriteAbout;
    private Sprite spriteExit;
    private Sprite spritePlayer;
    private Sprite spriteHand;
    private Sprite spriteNewGameVirtual;
    private Sprite spriteHighScoreVirtual;
    private Sprite spriteSettingsVirtual;
    private Sprite spriteHelpVirtual;
    private Sprite spriteAboutVirtual;
    private Sprite spriteExitVirtual;
    private int xNewGame;
    private int yNewGame;
    private int xHighScore;
    private int yHighScore;
    private int xSettings;
    private int ySettings;
    private int xHelp;
    private int yHelp;
    private int xAbout;
    private int yAbout;
    private int xExit;
    private int yExit;
    private int xPlayer;
    private int yPlayer;
    private int xHand;
    private int yHand;
    private int buttonsSpace = 70;
    private boolean performNewGame;
    private boolean performHighScore;
    private boolean performSettings;
    private boolean performHelp;
    private boolean performAbout;
    private boolean performExit;
    private int oldX;
    private int oldY;
    private SoundPlayer player;
    private int handDrawingTimes = 0;
    private boolean drawHand = true;

    public MainForm(MIDlet midlet) {
        super(true);
        this.midlet = midlet;
        gizone = new GestureInteractiveZone(GestureInteractiveZone.GESTURE_ALL);
        GestureRegistrationManager.register(this, gizone);
        GestureRegistrationManager.setListener(this, this);
        start();
    }

    public void run() {
        while (drawHand) {
            updateScreenGraphics(getGraphics());
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
    }

    public void gestureAction(Object container, GestureInteractiveZone gestureInteractiveZone, GestureEvent gestureEvent) {
        switch (gestureEvent.getType()) {
            /* --Old Technique to move player--
             case GestureInteractiveZone.GESTURE_DRAG: {
             if (gestureEvent.getStartX() >= xPlayer + buttonMargin
             && gestureEvent.getStartX() <= spritePlayer.getWidth() + xPlayer + buttonMargin
             && gestureEvent.getStartY() >= yPlayer + buttonMargin
             && gestureEvent.getStartY() <= spritePlayer.getHeight() + yPlayer + buttonMargin) {

             moveButtonPlayer(gestureEvent);
             }
             }
             break;*/

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
        CrazyHockey.drawHand = false;
        drawHand = false;
        updateScreenGraphics(getGraphics());
        oldX = x;
        oldY = y;
    }

    protected void pointerDragged(int x, int y) {
        super.pointerDragged(x, y);

        moveButtonPlayer(x, y);
    }

    private void start() {
        this.setFullScreenMode(true);

        try {
            imageBackground = Image.createImage("/images/mainbg.jpg");
            imageNewGame = Image.createImage("/images/newGame.png");
            imageHighScore = Image.createImage("/images/highScore.png");
            imageSettings = Image.createImage("/images/settings.png");
            imageHelp = Image.createImage("/images/help.png");
            imageAbout = Image.createImage("/images/about.png");
            imageExit = Image.createImage("/images/exit.png");
            imagePlayer = Image.createImage("/images/menuplayer.PNG");
            imageVirtual = Image.createImage("/images/virtual.png");
            imageHand = Image.createImage("/images/hand.png");

            spriteNewGame = new Sprite(imageNewGame);
            spriteHighScore = new Sprite(imageHighScore);
            spriteSettings = new Sprite(imageSettings);
            spriteHelp = new Sprite(imageHelp);
            spriteAbout = new Sprite(imageAbout);
            spriteExit = new Sprite(imageExit);
            spritePlayer = new Sprite(imagePlayer);
            spriteHand = new Sprite(imageHand);

            spriteNewGameVirtual = new Sprite(imageVirtual);
            spriteHighScoreVirtual = new Sprite(imageVirtual);
            spriteSettingsVirtual = new Sprite(imageVirtual);
            spriteHelpVirtual = new Sprite(imageVirtual);
            spriteAboutVirtual = new Sprite(imageVirtual);
            spriteExitVirtual = new Sprite(imageVirtual);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // intialize x,y
        xNewGame = 20;
        yNewGame = 155;
        xHighScore = xNewGame;
        yHighScore = yNewGame + buttonsSpace;
        xSettings = xNewGame;
        ySettings = yHighScore + buttonsSpace;
        xHelp = imageBackground.getWidth() - xNewGame - imageHelp.getWidth();
        yHelp = yNewGame;
        xAbout = xHelp;
        yAbout = yHighScore;
        xExit = xHelp;
        yExit = ySettings;
        xPlayer = (imageBackground.getWidth() / 2) - (imagePlayer.getWidth() / 2);
        yPlayer = imageBackground.getHeight() - imagePlayer.getHeight() - 10;
        oldX = xPlayer;
        oldY = yPlayer;
        xHand = 79;
        yHand = 331;

        // check sound in settings
        boolean sound = true;
        if (CrazyHockey.sound.equals("off")) {
            sound = false;
        }
        player = new SoundPlayer(sound);

        threadMainForm = new Thread(this);
        threadMainForm.start();
    }

    private void updateScreenGraphics(Graphics g) {
        drawBackground(g);
        drawButtons(g);
        if (CrazyHockey.drawHand) {
            moveHand();
            drawHand(g);
        } else {
            drawHand = false;
        }
        repaint();
    }

    private void drawBackground(Graphics g) {
        g.drawImage(imageBackground, 0, 0, 0);
    }

    private void drawButtons(Graphics g) {

        // draw original buttons 
        spriteNewGame.setPosition(xNewGame, yNewGame);
        spriteNewGame.paint(g);

        spriteHighScore.setPosition(xHighScore, yHighScore);
        spriteHighScore.paint(g);

        spriteSettings.setPosition(xSettings, ySettings);
        spriteSettings.paint(g);

        spriteHelp.setPosition(xHelp, yHelp);
        spriteHelp.paint(g);

        spriteAbout.setPosition(xAbout, yAbout);
        spriteAbout.paint(g);

        spriteExit.setPosition(xExit, yExit);
        spriteExit.paint(g);

        spritePlayer.setPosition(xPlayer, yPlayer);
        spritePlayer.paint(g);

        // draw virtual buttons
        spriteNewGameVirtual.setPosition(xNewGame + 16, yNewGame + 7);
        spriteNewGameVirtual.paint(g);

        spriteHighScoreVirtual.setPosition(xHighScore + 16, yHighScore + 7);
        spriteHighScoreVirtual.paint(g);

        spriteSettingsVirtual.setPosition(xSettings + 16, ySettings + 7);
        spriteSettingsVirtual.paint(g);

        spriteHelpVirtual.setPosition(xHelp + 16, yHelp + 7);
        spriteHelpVirtual.paint(g);

        spriteAboutVirtual.setPosition(xAbout + 16, yAbout + 7);
        spriteAboutVirtual.paint(g);

        spriteExitVirtual.setPosition(xExit + 16, yExit + 7);
        spriteExitVirtual.paint(g);
    }

    private void moveHand() {
        if (yHand < 331 - 30) {
            yHand = 331;
            handDrawingTimes += 1;
            if (handDrawingTimes == 3) {
                CrazyHockey.drawHand = false;
                drawHand = false;
                updateScreenGraphics(getGraphics());
                System.out.println("DONE!");
            }
        } else {
            yHand -= 3;
        }
    }

    private void drawHand(Graphics g) {
        spriteHand.setPosition(xHand, yHand);
        spriteHand.paint(g);
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
        if (spritePlayer.collidesWith(spriteNewGameVirtual, false)) {
            xPlayer = xNewGame + xSpace;
            yPlayer = yNewGame + ySpace;

            performNewGame = true;
        } else if (spritePlayer.collidesWith(spriteHighScoreVirtual, false)) {
            xPlayer = xHighScore + xSpace;
            yPlayer = yHighScore + ySpace;

            performHighScore = true;
        } else if (spritePlayer.collidesWith(spriteSettingsVirtual, false)) {
            xPlayer = xSettings + xSpace;
            yPlayer = ySettings + ySpace;

            performSettings = true;
        } else if (spritePlayer.collidesWith(spriteHelpVirtual, false)) {
            xPlayer = xHelp + xSpace;
            yPlayer = yHelp + ySpace;

            performHelp = true;
        } else if (spritePlayer.collidesWith(spriteAboutVirtual, false)) {
            xPlayer = xAbout + xSpace;
            yPlayer = yAbout + ySpace;

            performAbout = true;
        } else if (spritePlayer.collidesWith(spriteExitVirtual, false)) {
            xPlayer = xExit + xSpace;
            yPlayer = yExit + ySpace;

            performExit = true;
        }

        updateScreenGraphics(getGraphics());

        oldX = x;
        oldY = y;
    }

    private void doActions() {
        if (performNewGame) {
            player.play("/sounds/but.wav", 400);
            try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
            Display.getDisplay(midlet).setCurrent(new NewGameForm(midlet));
        } else if (performHighScore) {
            player.play("/sounds/but.wav", 400);
            try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
            Display.getDisplay(midlet).setCurrent(new HighScoreForm(midlet));
        } else if (performSettings) {
            player.play("/sounds/but.wav", 400);
            try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
            Display.getDisplay(midlet).setCurrent(new SettingsForm(midlet));
        } else if (performHelp) {
            player.play("/sounds/but.wav", 400);
            try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
            Display.getDisplay(midlet).setCurrent(new HelpForm(midlet));
        } else if (performAbout) {
            player.play("/sounds/but.wav", 400);
            try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
            Display.getDisplay(midlet).setCurrent(new AboutForm(midlet));
        } else if (performExit) {
            player.play("/sounds/but.wav", 400);
            try {
				Thread.sleep(400);
			} catch (Exception e) {
				e.printStackTrace();
			}
            midlet.notifyDestroyed();
        }
    }
}