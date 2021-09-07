import command.CommandService;
import files.FileManager;
import files.Loader;
import files.Saver;
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

        //FileManager.getRags();

        //main.loadTest();

        //main.launch();
    }

    private void launch() {

        mainWindow = new JFrame("ragEdit - Ghostface Killah !!!, my rhymes like garlic");
        mainWindow.setLayout(new BorderLayout());
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //mainWindow.setSize(32*32 + 64, 32*16 + 64 + 32);

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

        //Loader.loadOverride("LVL1", terminalHandler);

        //Saver.saveToFilePath(mainPaint, "test");

        //terminalHandler.parseString("-place [10,10] (wall)");
        //CommandService.executeNext();

    }

    public void initCommandService() {
        CommandService.setMain(mainCanvas);
    }

    public void initTerminalHandler() {

        terminalHandler = new TerminalHandler();
        terminalHandler.setComposition(mainPaint);
        terminalHandler.setComposer(mainCanvas);
        terminalHandler.setCanvas(mainWindow);

        mainTextBox = new TerminalTextBox();
        mainTextBox.setHandler(terminalHandler);
        mainTextBox.addActionListener(event -> mainTextBox.handleInput());

    }

    private void initMainPanel() {

        mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(mainCanvas, BorderLayout.CENTER);
        mainPanel.add(mainTextBox, BorderLayout.PAGE_END);
        mainPanel.add(mainToolBox, BorderLayout.EAST);

        mainCanvas.graphicalSettings(35, 24, 20);
        mainWindow.setSize(mainCanvas.getResolution()[0], mainCanvas.getResolution()[1]);
        mainWindow.getContentPane().add(mainPanel);

        mainWindow.setVisible(true);

    }

    private void initMouse() {
        Mouse mouse = new Mouse(mainCanvas, terminalHandler);

        mainCanvas.addMouseListener(mouse);
        mainCanvas.addMouseMotionListener(mouse);
    }

    private void initToolBox() {
        mainToolBox = new ToolBox(terminalHandler);
    }

    private void loadTest() {
        Loader.loadTest();
    }


}