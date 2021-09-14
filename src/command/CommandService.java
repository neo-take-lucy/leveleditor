package command;

import gui.Composer;

import java.util.LinkedList;

/**
 * CommandService facilitates the recording of previous commands, undo and redo.
 */
public class CommandService {

    private static Composer window;

    static LinkedList<Command> commands = new LinkedList<>();
    static LinkedList<String> stringCommands = new LinkedList<>();

    static LinkedList<CmdnStr> commandList = new LinkedList<>();
    static int totalCommands = 0;
    static int commandIndex = 0;

    static boolean isInHistory = false;

    //make sure to do this separate from makin em... if that makes sense... :^)

    /**
     * Adds a Command to the commandList. Keep in mind this doesn't execute it immediately,
     * that is typically called *after* the command is added in the TerminalHandler.
     * @param toAdd Command to add
     * @param parsed a String to record
     */
    public static void addCommand(Command toAdd, String parsed) {

        // if the command is in history (i.e. mid-undo) it will clear the list,
        // and add this command as the latest in the new list.
        if (isInHistory) {
            commandList.subList(commandIndex, commandList.size()).clear();
            totalCommands = commandList.size();
            isInHistory = false;
        }

        commandList.addLast(new CmdnStr(toAdd, parsed));
        totalCommands++;

    }

    public static int getCommandIndex() {
        return commandIndex;
    }

    public static void setMain(Composer mainComp) {
        window = mainComp;
    }

    /**
     * executeNext should be called statically by classes outside of the CommandService.
     * It in tern only calls stepForward.
     */
    public static void executeNext() {
        stepForward();
    }

    public static void executeAll() {
        while(totalCommands > commandIndex) {
            executeNext();
        }
    }

    /**
     * Undoes the previous command.
     */
    public static void undo() {

        isInHistory = true; // now in history, as undo has been called

        if(commandIndex == 0) {
            System.err.println("At earliest reversible command");
        } else if (commandIndex < 0) {
            System.err.println("whaaaaat");
            commandIndex = 0;
        } else {
            commandIndex--;
            commandList.get(commandIndex).unexecute();
        }

        window.requestRedraw(); // this is required whenever the window updates.
    }

    /**
     * Steps the command list forward. Private method, public calls should be done to
     * executeNext().
     */
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

        // if was inhistoy, then not anymore :^)
        if (commandIndex == totalCommands) {
            isInHistory = false;
        }

        window.requestRedraw();
    }

    /**
     * Redoes the previous undo.
     */
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

    /**
     * Clears the undo history.
     */
    public static void clearHistory() {
        commandList.clear();
        commandIndex = 0;
        totalCommands = 0;
    }
}

/**
 * This is a strange class used to model the command and the string...
 */
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
