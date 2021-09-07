package gui;

import handler.TerminalHandler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.LinkedList;

public class Mouse implements MouseListener, MouseMotionListener {

    Composer gui;
    TerminalHandler terminal;

    private HashSet<String> coDrag = new HashSet<>();

    public Mouse(Composer composer, TerminalHandler handler) {
        this.gui = composer;
        this.terminal = handler;
    }

    private boolean isActive = false;
    private boolean inClick = false;
    private static int x;
    private static int y;

    public static void setXY(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public void spindleCoOrds() {

        //before this, send "-macro open" command

        if (gui.currentBrushSetting.equals("(player)")) return;
        if (gui.currentBrushSetting.equals("(delete)")) return;

        terminal.parseString("-macro open");

        for (String c : coDrag) {

            //System.out.println(c);

            StringBuilder placeString = new StringBuilder("-place ");
            placeString.append(c);
            placeString.append(" ").append(gui.currentBrushSetting);

            //System.out.println(placeString.toString());

            terminal.parseString(placeString.toString());

        }
        //after this, send "-macro close" command

        //System.err.println("mouse click sent -macro close");
        terminal.parseString("-macro close");

        coDrag.clear();

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        inClick = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        inClick = true;

        //send single command
        //terminal.parseString("");
        int gridX = gui.getGridAt(e.getX(), e.getY())[0];
        int gridY = gui.getGridAt(e.getX(), e.getY())[1];

        //System.out.println(gui.currentBrushSetting);

        if (gui.currentBrushSetting.equals("(player)")) {
            terminal.parseString(String.format("@player set [%d,%d]", gridX, gridY));
        } else if (gui.currentBrushSetting.equals("(delete)")) {
            terminal.parseString(String.format("-delete [%d,%d]", gridX,
                    gridY));
        } else {
            terminal.parseString(String.format("-place [%d,%d] %s", gridX, gridY,
                    gui.currentBrushSetting));
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (inClick) spindleCoOrds();
        inClick = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isActive = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isActive = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        //this is also tool senstivie, dont forget it!
        //so maybe check state and delegate to other function for each tool :^)

        if(inClick) {
            int gridX = gui.getGridAt(e.getX(), e.getY())[0];
            int gridY = gui.getGridAt(e.getX(), e.getY())[1];

            String coD = String.format("[%d,%d]", gridX, gridY);
            coDrag.add(coD);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
