package command;

import composition.Composition;

import java.util.Collections;
import java.util.LinkedList;

public class MacroCommand extends Command {

    private LinkedList<Command> compositeList;
    private LinkedList<Command> reverseList;
    public static int totalMacros;

    /**
     * A collection of commands which are iterated through.
     * @param drawTo sets the Composition to draw to
     */
    public MacroCommand(Composition drawTo) {
        super(drawTo);
        totalMacros++;
        this.compositeList = new LinkedList<>();
    }

    @Override
    public void execute() {

        drawTo.open();
        int number = 0;
        for(Command c : this.compositeList) {
            //System.out.println("Command in Macro: " + number + c.getClass());
            c.action();
            number++;
        }

        drawTo.close();

    }

    @Override
    public void unexecute() {

        this.reverseList = new LinkedList<>(this.compositeList);
        Collections.reverse(this.reverseList);
        //Collections.reverse(compositeList);

        drawTo.open();
        for(Command c : this.reverseList) {
            c.reverseAction();
        }
        drawTo.close();

    }

    public void add(Command toAdd) {
        this.compositeList.addLast(toAdd);
    }

}
