/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.noqwerty.crazy.hockey;

import com.sun.lwuit.Button;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.CoordinateLayout;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author Mahmoud Elshamy
 */
public class EnterUsernameForm extends Form{
    MIDlet midlet;
    Label labelEnterUsername;
    TextField textUsername;
    Button buttonOk;
    Button buttonExit;
    
    public EnterUsernameForm(MIDlet midlet) {
        this.midlet = midlet;
        initComponents();
    }
    
    private void initComponents() {
        this.setLayout(new CoordinateLayout());
        this.setScrollable(false);
        
        //define componnents
        labelEnterUsername = new Label("Enter your name:");
        textUsername = new TextField();
        buttonOk = new Button("Ok");
        buttonExit = new Button("Exit");
        
        // set x,y for each component
        labelEnterUsername.setWidth(136);
        labelEnterUsername.setHeight(30);
        labelEnterUsername.setX(this.getWidth()/2 - labelEnterUsername.getWidth()/2);
        labelEnterUsername.setY(50);
        this.addComponent(labelEnterUsername);
        
        textUsername.setWidth(200);
        textUsername.setHeight(40);
        textUsername.setX(this.getWidth()/2 - textUsername.getWidth()/2);
        textUsername.setY(labelEnterUsername.getY() + 30);
        textUsername.getStyle().setPadding(10, 10, 10, 10);
        this.addComponent(textUsername);
        
        buttonExit.setWidth(90);
        buttonExit.setHeight(40);
        buttonExit.setX(this.getWidth() - buttonExit.getWidth() - 20);
        buttonExit.setY(this.getHeight() - buttonExit.getHeight() - 70);
        this.addComponent(buttonExit);
        buttonExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                midlet.notifyDestroyed();
            }
        });
        
        buttonOk.setWidth(90);
        buttonOk.setHeight(40);
        buttonOk.setX(20);
        buttonOk.setY(this.getHeight() - buttonOk.getHeight() - 70);
        this.addComponent(buttonOk);
        buttonOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                onOk();
            }
        });
    }
    
    private void onOk(){
        // check username
        if (textUsername.getText().equals("")) {
            labelEnterUsername.getStyle().setFgColor(0xFF0000);
            repaint();
            return;
        } else {
            // set username
            UsernameRS usernameRS = new UsernameRS();
            usernameRS.storeRecord(textUsername.getText());
            CrazyHockey.username = textUsername.getText();
            Display display = Display.getDisplay(midlet);
            display.setCurrent(new SplashForm(midlet));
        }
    }
    
}
