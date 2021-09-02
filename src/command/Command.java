package command;

import composition.Composition;

public abstract class Command {

    //Composition to affect. This is stored by the TerminalHandler
    protected final Composition drawTo;

    /**
     * Default constructor, all commands instantiate.
     * @param drawTo sets the composition to draw to.
     */
    public Command(Composition drawTo) {
        this.drawTo = drawTo;
    }

    /**
     * Executes the given command on the composition
     */
    public void execute() { }

    /**
     * Akin to undo. Command stores state relevant to before the execute, and if "undone"
     * in correct order, should maintain consistency.
     */
    public void unexecute() { }

    /**
     * Called in the execute function, not called directly.
     * Commands must open a stream and close it for safety,
     * Which is done in execute().
     */
    protected void action() { }

    /**
     * Called in the unexecute function, not called directly.
     * Commands must open a stream and close it for safety,
     * Which is done in unexecute().
     */
    protected void reverseAction() {

    }
}
