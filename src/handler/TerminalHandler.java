package handler;

import command.*;
import composition.BrushType;
import composition.Composition;
import files.Loader;
import files.Saver;
import gui.Composer;
import gui.TerminalTextBox;

import javax.swing.*;
import java.util.Hashtable;

/**
 * TerminalHandler is a stupidly messy and monolithic class used to handle commands that are written into
 * the terminal (and thus exists as the entire interface of the program)
 *
 * REM: if you're reading this, find John, grab him, shake him by the shoulders and scream in his ears
 * "FIX THE TERMINALHANDLER YOU HACK FRAUD"
 */
public class TerminalHandler {

    //String: command title, Integer: arguments
    private Hashtable<String, Integer> validCommands = new Hashtable<>();

    private Composition drawTo;
    private Composer graphicTo;
    private JFrame canvasTo;
    private TerminalTextBox terminalTo;

    private Hashtable<String, BrushType> stringToValueEnum = new Hashtable<String, BrushType>();

    private boolean macroFlag;
    private boolean isMacroMade;

    private MacroCommand flaggedMacro;

    public TerminalHandler() {
        makeValidCommands();
        makeValidStringToEnum();
    }

    /**
     * Creates a list of valid first arguments that the terminal will accept.
     * This is the first step to extending functionality, putting a new validCommand and
     * the amount of arguments it accepts.
     */
    private void makeValidCommands() {

        //commands without dashes are not "Commands", they reference other methods
        validCommands.put("help", 1);

        validCommands.put("-place", 3);     //-place [coords] (value)
        validCommands.put("-fill", 4);      //-fill [coords] [width/height] (value)
        validCommands.put("-delete", 2);    //-delete [coords]
        validCommands.put("-macro", 2);     //-macro ("open"/"close")
        validCommands.put("-resize", 3);    //-resize [width/height] (anchor)

        validCommands.put("u", 1);          //u
        validCommands.put("r", 1);          //r

        validCommands.put("con_s", 4);      //con_s pix_scale tileClms tileRows
        validCommands.put("con_o", 3);      //con_s off_hoz off_vert
        validCommands.put("con_z", 2);      //con_z zoom

        validCommands.put("print", 2);      //print "phrase"

        validCommands.put("b", 2);          //b colour          REM: brush
        validCommands.put("del", 1);        //delete            REM: sets the mouse to delete mode (activeEntites)

        validCommands.put("new", 3);        //new (name) [width/height]
        validCommands.put("save", 2);       //save filename     REM: more args?
        validCommands.put("load", 2);       //load filename
        validCommands.put("world", 2);      //world raw_world

        validCommands.put("clear", 1);      //clear

        validCommands.put("@player", 3);    //@player [command] [command argument]

        validCommands.put("toggleterm", 1); // terminal

    }

    /**
     * Another example of the unspingling of a bunch of data that could be stored in one place.
     * This list makes sure the correct CompType is sent to the Place.
     */
    private void makeValidStringToEnum() {  // put this and the one in compose together in an enum,
                                            // store whats needed in smaller version, or just use
                                            // algoriddim to reference
        for (BrushType b : BrushType.values()) {
            stringToValueEnum.put(b.type, b);
        }
    }

    public void setComposition(Composition composition) {
        this.drawTo = composition;
    }
    public void setComposer(Composer gui) {
        this.graphicTo = gui;
    }
    public void setCanvas(JFrame can) {
        this.canvasTo = can;
    }
    public void setTerminalTo(TerminalTextBox gui) {
        this.terminalTo = gui;
    }

    /**
     * Parses the string inputted into it. Currently just sends it to a huge switch statement,
     * which could hopefully be cleaned up by possibly having the validCommands table send
     * lambda functions? maybe? could be neat?
     * @param input
     */
    public void parseString(String input) {

        String[] split = input.split(" ");

        if(!validCommands.containsKey(split[0])) {
            System.err.printf("Invalid command %s, try 'help'\n :: rem: not in validCommands\n", split[0]);
            return;
        }

        if(validCommands.get(split[0]) != split.length) {
            System.err.println(String.format("Incorrect amount of arguments for command: %s", split[0]));
            return;
        }

        // if the macroFlag is set, then the program will be locked into this mode, which is pretty unclean, as really
        // commands should be added, and the macro consideration take place at end
        if(macroFlag) {
            if(!isMacroMade) {
                flaggedMacro = new MacroCommand(drawTo);
                isMacroMade = true;
            }

            // its assumed macrocommand is made when it parses the string
            // the undo/redo only affects macro commands AND only macrocommands created
            // through this cooked as fuck method. so yeh, have a think about it i guess...

            if (flaggedMacro != null) {

                //System.out.println("macro on, parsing " + split);

                if (split[0].equals("-place")) {
                    flaggedMacro.add(singlePlace(split));
                    return;
                } else if (split[0].equals("-macro")) {
                    if (split[1].equals("close")) {

                        //TODO: implement ability to click n drag "delete"

                        macroFlag = false;
                        input = String.format("-macro %d", MacroCommand.totalMacros);

                        CommandService.addCommand(flaggedMacro, input);
                        flaggedMacro = null;

                        //CommandService.gettem();
                        CommandService.executeNext();
                    }

                }
            }
            return;
        }

        // okay this needs to be made better, esp. if ur going to load macro commands into it
        if(split[0].equals("-place")) {
            //Place stuff

            //check if macro open, if not, then create MacroCommand,
            //if else, add to current macroCommand being made

            //Command placeCommand = parsePlace(split);

            MacroCommand macro = parsePlace(split);
            if (macro != null) {
                CommandService.addCommand(macro, input);
                CommandService.executeNext();
            }
        }
        // fill makes rectangles. the mouse functionality for this isn't in yet
        else if (split[0].equals("-fill")) {

            MacroCommand macro = parseFill(split);
            if (macro != null) {
                CommandService.addCommand(macro, input);
                CommandService.executeNext();
            }
        }
        // delete deletes entites.
        else if (split[0].equals("-delete")) {
            //Check if there is an entity at mouse click

            MacroCommand macro = parseDelete(split);

            if (macro != null) {
                CommandService.addCommand(macro, input);
                CommandService.executeNext();
            }
        }
        // undo
        else if(split[0].equals("u")) {
            CommandService.undo();
        }
        // redo
        else if(split[0].equals("r")) {
            CommandService.redo();
        }
        // configure settings
        else if(split[0].equals("con_s")) {
            parseConS(split);
        }
        // configure offset
        else if (split[0].equals("con_o")) {
            parseConO(split);
        }
        // configure zoom
        else if (split[0].equals("con_z")) {
            parseConZ(split);
        }
        // open macro
        else if (split[0].equals("-macro")) {
            if (split[1].equals("open")) {
                macroFlag = true;
                isMacroMade = false;
            }
            else System.err.println("Invalid Command");
        }
        // brush settings
        else if (split[0].charAt(0) == 'b') { // SO MAKE SURE NOTHING ELSE IS B
            parseBrush(input);
        }
        // save
        else if (split[0].equals("save")) {
            Saver.saveToFilePath(drawTo, split[1]);
        }
        // load
        else if (split[0].equals("load")) {
            Loader.loadOverride(split[1], this);
            graphicTo.requestRedraw();
        }
        // clear composition
        else if (split[0].equals("clear")) {
            parseClear();
        }
        // new
        else if (split[0].equals("new")) {
            parseNew(split);
        }
        // set player
        else if (split[0].equals("@player")) {
            parseAtPlayer(split);
        }
        // brush setting for delete
        else if (split[0].equals("del")) {
            parseBrush(split[0]);
        }
        // toggles termianl display on/off
        else if (split[0].equals("toggleterm")) {
            terminalTo.toggleTerminal();
        }

        else if (split[0].equals("world")) {
            drawTo.setWorldType(split[1]);
        }

        graphicTo.requestRedraw();

    }

    /**
     * Parses New requests
     * @param input String[] from parseString
     */
    private void parseNew(String[] input) {

        try {

            String title = input[1];

            String coOrds = input[2].replace("[", "").replace("]", "");
            String[] cSplit = coOrds.split(",");

            int width = Integer.parseInt(cSplit[0]);
            int height = Integer.parseInt(cSplit[1]);

            drawTo.makeNew(title, width, height);

        } catch (Exception e) {
            System.err.printf("parseNew could not parse. Check width and height commands are integers\n");
        }

        graphicTo.requestRedraw();
    }

    /**
     * Parses Clear requests
     */
    private void parseClear() {
        drawTo.initLayers();
        graphicTo.requestRedraw();
    }

    /**
     * Parses place requests
     * @param split String[] from parseString
     * @return MacroCommand with Strings in it
     */
    private MacroCommand parsePlace(String[] split) {
        MacroCommand macro = new MacroCommand(drawTo);
        macro.add(singlePlace(split));
        return macro;
    }

    /**
     * Returns a single place command
     * @param split String[] from parseString
     * @return PlaceCommand
     */
    private Command singlePlace(String[] split) {

        split[1] = split[1].replace("[", "");
        split[1] = split[1].replace("]", "");

        String[] coOrds = split[1].split(",");

        if(coOrds.length != 2) {
            System.err.println("Unexpected Amount of Coordinates in -place");
            return null;
        }

        // add a check for numbers, fuck it rn
        int x = Integer.parseInt(coOrds[0]);
        int y = Integer.parseInt(coOrds[1]);

        if (!stringToValueEnum.containsKey(split[2])) {
            System.err.printf("Unexpected Value %s, refer to manual for more :^)%n", split[2]);
            return null;
        };

        BrushType brushType = stringToValueEnum.get(split[2]);
        return new PlaceCommand(drawTo, x, y, brushType);
    }

    /**
     * Parses Delete requests
     * @param split String[] from thingy
     * @return MacroCommand of deletes
     */
    private MacroCommand parseDelete(String[] split) {
        MacroCommand macro = new MacroCommand(drawTo);

        Command del = delete(split);
        if (del != null) macro.add(delete(split));
        else return null;

        return macro;
    }

    /**
     * ewooeoweoweweowoewoeo
     * @param split
     * @return
     */
    private Command delete(String[] split) {

        if (drawTo.getActiveSet().containsKey(split[1])) {
            return new DeleteCommand(drawTo, split[1]);
        } else {
            return null;
        }
    }

    /**
     * Makes a giant list of PlaceCommands all contained in one MacroCommand.
     * @param split String[] from parseString
     * @return MacroCommand
     */
    private MacroCommand parseFill(String[] split) {

        split[1] = split[1].replace("[", "");
        split[1] = split[1].replace("]", "");

        split[2] = split[2].replace("[", "");
        split[2] = split[2].replace("]", "");

        String[] topLeft = split[1].split(",");
        String[] widthHeight = split[2].split(",");

        if(topLeft.length != 2) {
            System.err.println("Unexpected Amount of Coordinates in -fill [x,y]");
            return null;
        }

        if(widthHeight.length != 2) {
            System.err.println("Unexpected Amount of Coordinates in -fill [...] [width,height]");
            return null;
        }

        // add a check for numbers, fuck it rn
        int x = Integer.parseInt(topLeft[0]);
        int y = Integer.parseInt(topLeft[1]);

        int width = Integer.parseInt(widthHeight[0]);
        int height = Integer.parseInt(widthHeight[1]);

        if (!stringToValueEnum.containsKey(split[3])) {
            System.err.printf("Unexpected Value %s, refer to manual for more :^)%n", split[3]);
            return null;
        };

        BrushType brushType = stringToValueEnum.get(split[3]);

        MacroCommand macro = new MacroCommand(drawTo);

        int ex = x + width;
        int ey = y + height;

        int sx = x;
        for (; sx < ex; sx++) {
            int sy = y;
            for (; sy < ey; sy++) {
                macro.add(new PlaceCommand(drawTo, sx, sy, brushType));
            }
        }

        return macro;

    }

    private void parseConS(String[] split) {

        int px = Integer.parseInt(split[1]);
        int tw = Integer.parseInt(split[2]);
        int th = Integer.parseInt(split[3]);

        graphicTo.graphicalSettings(px, tw, th);
        graphicTo.requestRedraw();

    }

    private void parseConO(String[] split) {

        int o_h = Integer.parseInt(split[1]);
        int o_v = Integer.parseInt(split[2]);

        graphicTo.adjustHorizontalOffset(o_h);
        graphicTo.adjustVerticalOffset(o_v);

        //graphicTo.setHorizontalOffset(o_h);
        //graphicTo.setVerticalOffset(o_v);

        //int zoom = graphicTo.getZoom();
        //graphicTo.graphicalSettings(zoom, o_h, o_v);
        graphicTo.requestRedraw();

    }

    private void parseConZ(String[] split) {
        int zoomAdjust = Integer.parseInt(split[1]);
        int zoom = graphicTo.getZoom() + zoomAdjust;
        graphicTo.setZoom(zoom);
        graphicTo.requestRedraw();
    }

    private void parsePrint(String[] split) {

    }

    private void parseBrush(String brushSetting) {
        graphicTo.putBrushFromTerm(brushSetting);
    }

    private void parseAtPlayer(String[] split) {

        String command = split[1];

        int x = 5;
        int y = 5;

        String[] coOrds = split[2].replace("[", "").replace("]", "").split(",");
        try {

            x = Integer.parseInt(coOrds[0]);
            y = Integer.parseInt(coOrds[1]);

        } catch (Exception e) {

        }

        switch (command) {
            case "set":
                drawTo.setPlayerLocation(x, y);
                break;
        }

    }

}
