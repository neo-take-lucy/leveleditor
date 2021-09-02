import command.CommandService;
import files.Loader;
import gui.Composer;
import composition.Composition;
import gui.Mouse;
import handler.TerminalHandler;
import gui.TerminalTextBox;

import javax.swing.*;
import java.awt.*;

public class Launcher {

    private JFrame mainWindow;
    private Composer mainCanvas;
    private Composition mainPaint;
    private TerminalHandler terminalHandler;

    private TerminalTextBox mainTextBox;

    public static void main(String args[]){

        Launcher main = new Launcher();
        main.launch();
        //main.loadTest();

        //main.launch();
    }

    private void launch() {

        mainWindow = new JFrame("Lightweight Level Editor");
        mainWindow.setLayout(new BorderLayout());
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //mainWindow.setSize(32*32 + 64, 32*16 + 64 + 32);

        terminalHandler = new TerminalHandler();

        mainCanvas = new Composer();

        mainPaint = new Composition(46, 8);
        mainCanvas.setComposition(mainPaint);

        initCommandService();
        initTerminalHandler();

        Mouse mouse = new Mouse(mainCanvas, terminalHandler);

        mainCanvas.addMouseListener(mouse);
        mainCanvas.addMouseMotionListener(mouse);

        mainWindow.getContentPane().add(mainCanvas, BorderLayout.CENTER);
        mainWindow.getContentPane().add(mainTextBox, BorderLayout.PAGE_END);

        mainCanvas.graphicalSettings(35, 24, 14);
        mainWindow.setSize(mainCanvas.getResolution()[0], mainCanvas.getResolution()[1]);
        mainWindow.setVisible(true);

        Loader.loadOverride("source/core/assets/configs/rags/LVL1.map", terminalHandler);

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

    private void loadTest() {
        Loader.loadTest();
    }

    public void oldlaunch() {
                /*Composition comp = new Composition(32, 16);
        CompType whatHere;

        TerminalHandler termHand = new TerminalHandler();
        termHand.setComposition(comp);

        termHand.parseString("-place [21,10] (wall)");
        termHand.parseString("-place [11,2] (floor)");
        termHand.parseString("-place [20,6] (wall)");

        termHand.parseString("r");
        whatHere = comp.getAtPoint(Layer.TERRAIN, 21, 10);
        System.out.println(whatHere);

        CommandService.executeAll();
        whatHere = comp.getAtPoint(Layer.TERRAIN, 11, 2);
        System.out.println(whatHere);
        whatHere = comp.getAtPoint(Layer.TERRAIN, 20, 6);
        System.out.println(whatHere);
        termHand.parseString("r");

        termHand.parseString("u");
        whatHere = comp.getAtPoint(Layer.TERRAIN, 20, 6);
        System.out.println(whatHere);

        termHand.parseString("u");
        termHand.parseString("u");
        termHand.parseString("u");
        whatHere = comp.getAtPoint(Layer.TERRAIN, 21, 10);
        System.out.println(whatHere);

        comp.open();
        //nextCommand.execute();
        comp.close();

        comp.open();
        //nextCommand.unexecute();
        comp.close();

        whatHere = comp.getAtPoint(Layer.TERRAIN, 21, 10);


        int x = 1 + 1; */
    }


}