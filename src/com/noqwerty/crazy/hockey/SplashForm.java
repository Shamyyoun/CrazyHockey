/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.noqwerty.crazy.hockey;

import java.io.IOException;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author gazr
 */
public class SplashForm extends GameCanvas implements Runnable {

    MIDlet midlet;
    private Image imageBackground;
    private Image imageCrazy;
    private Image imageHockey;
    private Sprite spriteCrazy;
    private Sprite spriteHockey;
    private Thread threadSplash;
    private int xCrazy;
    private int yCrazy;
    private int xHockey;
    private int yHockey;
    private int xCrazyEnd;
    private int xHockeyEnd;
    private int sleepTime = 1;
    private boolean draw = true;

    public SplashForm(MIDlet midlet) {
        super(true);
        this.midlet = midlet;
        start();
    }

    public void run() {
        while (draw) {
            updateScreenGraphics(getGraphics());
            
            try {
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // sleep and open Mainform
        try {
            Thread.sleep(1000 * 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Display.getDisplay(midlet).setCurrent(new MainForm(midlet));
    }

    private void start() {
        this.setFullScreenMode(true);

        try {
            imageBackground = Image.createImage("/images/splashbg.jpg");
            imageCrazy = Image.createImage("/images/crazy.png");
            imageHockey = Image.createImage("/images/hockey.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        spriteCrazy = new Sprite(imageCrazy);
        spriteHockey = new Sprite(imageHockey);
        
        xCrazy = imageBackground.getWidth();
        yCrazy = 150;
        xHockey = 0 - spriteHockey.getWidth();
        yHockey = yCrazy + 48;
        xCrazyEnd = imageBackground.getWidth()/2 - spriteCrazy.getWidth()/2;
        xHockeyEnd = imageBackground.getWidth()/2 - spriteHockey.getWidth()/2;
        
        threadSplash = new Thread(this);
        threadSplash.start();
    }

    private void updateScreenGraphics(Graphics g) {
        drawBackground(g);
        moveLogo();
        drawLogo(g);
        repaint();
        
        // check locations to stop drawing
        if (xCrazy == xCrazyEnd && xHockey == xHockeyEnd) {
            draw = false;
        }
    }

    private void drawBackground(Graphics g) {
        g.drawImage(imageBackground, 0, 0, 0);
    }
    
    private void moveLogo() {
        int step = 6;
        if (xCrazy > xCrazyEnd) {
            xCrazy -= step;
            if (xCrazy < xCrazyEnd) {
                xCrazy = xCrazyEnd;
            }
        } else {
            xCrazy = xCrazyEnd;
        }
        
        if (xHockey < xHockeyEnd) {
            xHockey += step;
            if (xHockey > xHockeyEnd) {
                xHockey = xHockeyEnd;
            }
        } else {
            xHockey = xHockeyEnd;
        }
    }
    
    private void drawLogo(Graphics g) {
        spriteCrazy.setPosition(xCrazy, yCrazy);
        spriteCrazy.paint(g);
        
        spriteHockey.setPosition(xHockey, yHockey);
        spriteHockey.paint(g);
    }
}
