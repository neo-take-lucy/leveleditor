import command.CommandService;
import files.Loader;
import gui.Composer;
import composition.Composition;
import gui.Mouse;
import gui.ToolBox;
import handler.TerminalHandler;
import gui.TerminalTextBox;

import javax.swing.*;
import java.awt.*;

public class Launcher {

    private JFrame mainWindow;

    private JPanel mainPanel;

    private Composer mainCanvas;
    private Composition mainPaint;
    private TerminalHandler terminalHandler;
    private ToolBox mainToolBox;
    private TerminalTextBox mainTextBox;

    public static void main(String args[]){

        Launcher main = new Launcher();
        main.launch();

    }

    /**
     * Launches the program. Makes calls to other init methods.
     */
    private void launch() {

        mainWindow = new JFrame("ragEdit - Ghostface Killah !!!, my rhymes like garlic");
        mainWindow.setLayout(new BorderLayout());
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        terminalHandler = new TerminalHandler();

        mainCanvas = new Composer();

        mainPaint = new Composition(1, 1);
        mainCanvas.setComposition(mainPaint);

        initCommandService();
        initTerminalHandler();
        initMouse();
        initToolBox();

        initMainPanel();

        terminalHandler.parseString("new default [10,10]");

        Loader.loadOverride("ragnorok", terminalHandler);

    }

    /**
     * Sets the mainCanvas (a Composer) of the commandService
     */
    private void initCommandService() {
        CommandService.setMain(mainCanvas);
    }

    /**
     * Sets all the interfaces up for the TerminalHandler.... . . . . . . .
     */
    private void initTerminalHandler() {

        terminalHandler = new TerminalHandler();
        terminalHandler.setComposition(mainPaint);
        terminalHandler.setComposer(mainCanvas);
        terminalHandler.setCanvas(mainWindow);

        mainTextBox = new TerminalTextBox();
        mainTextBox.setHandler(terminalHandler);
        mainTextBox.addActionListener(event -> mainTextBox.handleInput());
        terminalHandler.setTerminalTo(mainTextBox);

    }

    /**
     * The mainPanel is the tilemap of the level.
     */
    private void initMainPanel() {

        mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(mainCanvas, BorderLayout.CENTER);
        mainPanel.add(mainTextBox, BorderLayout.PAGE_END);
        mainPanel.add(mainToolBox, BorderLayout.EAST);

        mainCanvas.graphicalSettings(35, 24, 20);
        mainWindow.setSize(mainCanvas.getResolution()[0], mainCanvas.getResolution()[1]);
        mainWindow.getContentPane().add(mainPanel);

        mainPanel.setBackground(new Color(5260080));

        mainTextBox.setVisible(false);
        mainWindow.setVisible(true);

    }

    /**
     * Initialises the mouse.
     */
    private void initMouse() {
        Mouse mouse = new Mouse(mainCanvas, terminalHandler);

        mainCanvas.addMouseListener(mouse);
        mainCanvas.addMouseMotionListener(mouse);
    }

    /**
     * Initialises the ToolBox (the display of buttons to interface with the editor)
     */
    private void initToolBox() {
        mainToolBox = new ToolBox(terminalHandler);
    }

}