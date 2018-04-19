package main.java.ui;

import main.java.resources.FontResource;
import main.java.speech.SpeechUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CommandPanel extends FrostedPane {

    JTextField command_box;

    public CommandPanel() {
        super(25, 25, 500, 100, "COMMAND PANEL");

        command_box = new JTextField();
        command_box.setBounds(10, 40, 300, 20);
        command_box.getCaret().setBlinkRate(0);
        command_box.setForeground(Color.gray);
        command_box.setFont(FontResource.command_hint);
        command_box.setText("Waiting for Commands...");

        command_box.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (command_box.getFont() == FontResource.command_hint) {
                    command_box.setForeground(Color.black);
                    command_box.setFont(FontResource.command_content);
                    command_box.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (command_box.getText().replaceAll("\\s", "").equals("")) {
                    command_box.setForeground(Color.gray);
                    command_box.setFont(FontResource.command_hint);
                    command_box.setText("Waiting for Commands...");
                }
            }
        });

        add(command_box);

        SpeechUtils.command_display = command_box;

    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);


    }

}
