package com.noqwerty.crazy.hockey;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class CrazyHockey extends MIDlet {
	// sound values: "on", "off"
    public static String sound = "on";
    // diff values: "easy", "medium", "hard"
    public static String diff = "easy";
    // diff values: "time", "score"
    public static String mode = "score";
    public static String username;
    
    public static boolean drawHand = true;

	public CrazyHockey() {
		// TODO Auto-generated constructor stub
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		checkHandTutorial();
        username = getUsername();
	}
	
	private String getUsername() {
        UsernameRS usernameRS = new UsernameRS();
        String username = usernameRS.getRecord();

        // check user name exists or not
        if (username.equals("User")) {
            // user opens game for the first time
            // should enter his name
            com.sun.lwuit.Display.init(this);
            new EnterUsernameForm(this).show();
        } else {
            Display display = Display.getDisplay(this);
            HighScoreForm form = new HighScoreForm(this);
            display.setCurrent(form);
        }
        return username;
    }
    
    private void checkHandTutorial() {
        HandTutorialRS rs = new HandTutorialRS();
        int times = Integer.parseInt(rs.getRecord());
        if (times >= 5) {
            CrazyHockey.drawHand = false;
        }
        times++;
        rs.storeRecord("" + times);
    }

}
