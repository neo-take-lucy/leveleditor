package gui;

import command.CommandService;
import handler.TerminalHandler;

import javax.swing.*;

public class TerminalTextBox extends JTextField {

    private TerminalHandler handler;

    public TerminalTextBox() {
        super();
    }

    public void setHandler(TerminalHandler handler) {
        this.handler = handler;
    }

    //so after a line is typed and enter is hit, pass input string into terminal handler
    // and clear the textbox
    // later add the string to a list of commands

    public void handleInput() {
        //make sure to add this string to a list of global commands
        //but that is the point of TEXT AREA, not textfield
        String text = this.getText();
        handler.parseString(text);
        //CommandService.executeNext();
        this.setText("");
    }

}
