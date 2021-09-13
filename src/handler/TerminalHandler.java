package handler;

import command.*;
import composition.CompType;
import composition.Composition;
import composition.Layer;
import files.Loader;
import files.Saver;
import gui.Composer;
import gui.TerminalTextBox;

import javax.swing.*;
import java.util.Hashtable;

public class TerminalHandler {

    private String[] split;

    //String: command title, Integer: arguments
    private Hashtable<String, Integer> validCommands = new Hashtable<>();

    private Composition drawTo;
    private Composer graphicTo;
    private JFrame canvasTo;
    private TerminalTextBox terminalTo;

    private Hashtable<String, CompType> stringToValueEnum = new Hashtable<String, CompType>();

    private boolean macroFlag;
    private boolean isMacroMade;

    private MacroCommand flaggedMacro;

    public TerminalHandler() {
        makeValidCommands();
        makeValidStringToEnum();
    }

    private void makeValidCommands() {

        //commands without dashes are not "Commands", they reference other methods

        validCommands.put("help", 1);

        validCommands.put("-place", 3);     //-place [coords] (value)
        validCommands.put("-fill", 4);      //-fill [coords] [width/height] (value)
        validCommands.put("-delete", 2);    //-delete [coords]
        validCommands.put("-macro", 2);     //-macro ("open"/"close")
        validCommands.put("-resize", 3);    //-resize [width/height] (anchor)

        validCommands.put("new", 3);        //new (name) [width/height]
        validCommands.put("u", 1);          //u
        validCommands.put("r", 1);          //r

        validCommands.put("con_s", 4);      //con_s pix_scale tileClms tileRows
        validCommands.put("con_o", 3);      //con_s off_hoz off_vert
        validCommands.put("con_z", 2);      //con_z zoom

        validCommands.put("print", 2);      //print "phrase"

        validCommands.put("b", 2);          //b colour          REM: brush
        validCommands.put("del", 1);        //delete            REM: sets the mouse to delete mode (activeEntites)

        validCommands.put("save", 2);       //save filename     REM: more args?
        validCommands.put("load", 2);       //load filename

        validCommands.put("clear", 1);      //clear

        validCommands.put("@player", 3);    //@player [command] [command argument]

        validCommands.put("toggleterm", 1); // terminal

        //TODO: save filename : with restrictions, saves to working dir of ragnarok racer
        //TODO: load filename : loads file from filename
        //TODO: new : with parameters -> make new file return false if
        //TODO: getdir : expose the names of the files

    }

    private void makeValidStringToEnum() {  // put this and the one in compose together in an enum,
                                            // store whats needed in smaller version, or just use
                                            // algoriddim to reference
        stringToValueEnum.put("(null)", CompType.NULL);
        stringToValueEnum.put("(delete)", CompType.DELETE);
        stringToValueEnum.put("(floor)", CompType.FLOOR);
        stringToValueEnum.put("(platform)", CompType.PLATFORM);
        //stringToValueEnum.put("(player)", CompType.PLAYER);
        stringToValueEnum.put("(spikes)", CompType.SPIKES);
        stringToValueEnum.put("(rocks)", CompType.ROCKS);
        stringToValueEnum.put("(wall)", CompType.WALL);
        stringToValueEnum.put("(skeleton)", CompType.SKELETON);
        stringToValueEnum.put("(wolf)", CompType.WOLF);
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

        if(macroFlag) {
            //System.out.println("in Macro Open:");

            //MacroCommand flaggedMacro = null;
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
        } else if (split[0].equals("-fill")) {

            MacroCommand macro = parseFill(split);
            if (macro != null) {
                CommandService.addCommand(macro, input);
                CommandService.executeNext();
            }
        } else if (split[0].equals("-delete")) {
            //Check if there is an entity at mouse click

            MacroCommand macro = parseDelete(split);

            if (macro != null) {
                CommandService.addCommand(macro, input);
                CommandService.executeNext();
            }
        } else if(split[0].equals("u")) {
            //Undo
            CommandService.undo();
        } else if(split[0].equals("r")) {
            //Redo
            CommandService.redo();
        } else if(split[0].equals("con_s")) {
            parseConS(split);
        } else if (split[0].equals("con_o")) {
            parseConO(split);
        } else if (split[0].equals("con_z")) {
            parseConZ(split);
        } else if (split[0].equals("-macro")) {
            if (split[1].equals("open")) {
                macroFlag = true;
                isMacroMade = false;
                //flaggedMacro = new MacroCommand(drawTo);
            }
            else System.err.println("Invalid Command");
        } else if (split[0].charAt(0) == 'b') { // SO MAKE SURE NOTHING ELSE IS B
            parseBrush(input);
        } else if (split[0].equals("save")) {
            Saver.saveToFilePath(drawTo, split[1]);
        } else if (split[0].equals("load")) {
            Loader.loadOverrideNew(split[1], this);
            graphicTo.requestRedraw();
        } else if (split[0].equals("clear")) {
            parseClear();
        } else if (split[0].equals("new")) {
            parseNew(split);
        } else if (split[0].equals("@player")) {
            parseAtPlayer(split);
        } else if (split[0].equals("del")) {
            parseBrush(split[0]);
        } else if (split[0].equals("toggleterm")) {
            terminalTo.toggleTerminal();
        }

        graphicTo.requestRedraw();

    }

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

    private void parseClear() {
        drawTo.initLayers();
        graphicTo.requestRedraw();
    }


    private MacroCommand parsePlace(String[] split) {
        MacroCommand macro = new MacroCommand(drawTo);
        macro.add(singlePlace(split));
        return macro;
    }

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

        CompType compType = stringToValueEnum.get(split[2]);
        Layer layer;
        switch (compType) {
            case SKELETON:
            case WOLF:
                layer = Layer.ACTIVE;
                break;
            default:
                layer = Layer.TERRAIN;
        }

        return new PlaceCommand(drawTo, layer, x, y, compType);
    }

    private MacroCommand parseDelete(String[] split) {
        MacroCommand macro = new MacroCommand(drawTo);

        Command del = delete(split);
        if (del != null) macro.add(delete(split));
        else return null;

        return macro;
    }

    private Command delete(String[] split) {

        if (drawTo.getActiveSet().containsKey(split[1])) {
            return new DeleteCommand(drawTo, split[1]);
        } else {
            return null;
        }
    }

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

        CompType compType = stringToValueEnum.get(split[3]);
        Layer layer;


        /* TODO: fix this to make sense !!!!!!!!! */
        switch (compType) {
            default:
                layer = Layer.TERRAIN;
        }

        MacroCommand macro = new MacroCommand(drawTo);

        int ex = x + width;
        int ey = y + height;

        int sx = x;
        for (; sx < ex; sx++) {
            int sy = y;
            for (; sy < ey; sy++) {
                macro.add(new PlaceCommand(drawTo, layer, sx, sy, compType));
            }
        }

        //      -fill [2,2] [10,10] (wall)

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
