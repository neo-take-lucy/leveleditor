package gui;

import handler.TerminalHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class ToolBox extends JComponent {

    private ActionListener brushListener;

    private ActionListener configListener;

    private TerminalHandler handler;

    public ToolBox(TerminalHandler handler) {
        super();

        this.handler = handler;

        this.setLayout(new GridLayout(5, 1));
        //this.setPreferredSize();

        init();

        //JButton coolButt = new JButton();
        //this.add(coolButt);

    }

    private void init() {

        brushListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                handler.parseString(e.getActionCommand());

            }
        };

        int x = 0;

        for (BrushButtons b : BrushButtons.values()) {

            b.getButton().setText(b.toString());

            b.getButton().addActionListener(brushListener);
            b.getButton().setActionCommand(b.getSetting());
            b.getButton().setPreferredSize(new Dimension(64, 64));

            this.add(b.getButton());

            x++;
        }

    }


}

enum BrushButtons {

    // in the () can make icon

    // maybe also save values relating to the coordinates in this?
    ERASER(new JButton(), "b n"), // sets brush to null
    FLOOR(new JButton(), "b f"),
    PLATFORM(new JButton(), "b p"),
    ROCKS(new JButton(), "b r"),
    SPIKES(new JButton(), "b v"),
    SKELETON(new JButton(), "b s"),
    WOLF(new JButton(), "b w"),
    DELETE(new JButton(), "delete");

    private JButton butt;
    private String setting;

    BrushButtons(JButton button, String brushSetting) {

        butt = button;
        setting = brushSetting;

    }

    public JButton getButton() {
        return butt;
    }

    public String getSetting() {
        return setting;
    }


}
