package command;

import gui.Composer;

import java.util.LinkedList;

public class CommandService {

    private static Composer window;

    static LinkedList<Command> commands = new LinkedList<>();
    static LinkedList<String> stringCommands = new LinkedList<>();

    static LinkedList<CmdnStr> commandList = new LinkedList<>();
    static int totalCommands = 0;
    static int commandIndex = 0;

    static boolean isInHistory = false;

    //make sure to do this separate from makin em... if that makes sense... :^)
    public static void addCommand(Command toAdd, String parsed) {

        if (isInHistory) {

            //tempList = commandList.subList(0, commandIndex)

            commandList.subList(commandIndex, commandList.size()).clear();
            totalCommands = commandList.size();
            isInHistory = false;
        }

        commandList.addLast(new CmdnStr(toAdd, parsed));

        totalCommands++;

        /*if(isInHistory) {
            commands.subList(commandIndex, commands.size()).clear();
            totalCommands = commands.size();
            isInHistory = false;
        }

        commands.addLast(toAdd);
        stringCommands.addLast()
        totalCommands++;*/
    }

    public static int getCommandIndex() {
        return commandIndex;
    }

    public static void setMain(Composer mainComp) {
        window = mainComp;
    }

    public static void executeNext() {

        /*for (CmdnStr c : commandList) {
            System.out.println(c.getTerminalLine());
        }*/

        /*if (isInHistory) {
            CmdnStr addback = commandList.peekLast();
            commandList.subList(commandIndex, commandList.size()).clear();
            commandList.addLast(addback);
            isInHistory = false;
        }*/

        stepForward();
    }

    public static void executeAll() {
        while(totalCommands > commandIndex) {
            executeNext();
        }
    }

    public static void undo() {

        isInHistory = true;

        /*if (commandList.get(commandIndex).getTerminalLine().equals("-macro close")) {

        }*/

        if(commandIndex == 0) {
            System.err.println("At earliest reversible command");
        } else if (commandIndex < 0) {
            System.err.println("whaaaaat");
            commandIndex = 0;
        } else {
            commandIndex--;
            commandList.get(commandIndex).unexecute();
        }

        window.requestRedraw();
    }

    private static void stepForward() {
        if (commandIndex == totalCommands) {
            System.err.println("At Latest Command, cannot execute further");
        } else if (commandIndex > totalCommands) {
            commandIndex = totalCommands - 1;
            System.err.println("Dunno how you did this, but you did something whack");
        } else {
            commandList.get(commandIndex).execute();
            commandIndex++;
        }

        if (commandIndex == totalCommands) {
            isInHistory = false;
        }

        window.requestRedraw();
    }

    public static void redo() {
        stepForward();
    }

    public static void gettem() {
        System.out.println("List of Commands");
        int no = -1;
        for (CmdnStr c : commandList) {
            no++;
            System.out.println(no + ": " + c.getTerminalLine());
        }
    }

    public static void clearHistory() {
        commandList.clear();
        commandIndex = 0;
        totalCommands = 0;
    }
}

class CmdnStr {

    private Command toExecute;
    private String terminalLine;

    public CmdnStr(Command execute, String parsed) {
        toExecute = execute;
        terminalLine = parsed;
    }

    public Command getToExecute() {
        return toExecute;
    }

    public String getTerminalLine() {
        return terminalLine;
    }

    public void execute() {
        toExecute.execute();
    }

    public void unexecute() {
        toExecute.unexecute();
    }
}
