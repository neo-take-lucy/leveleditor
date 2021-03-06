package command;

import composition.BrushType;
import composition.Composition;

public class DeleteCommand extends Command {

    private String stringCoOrds;
    private BrushType previousValue; //All paints/colours/enemies are model as enums

    /**
     * Delete entity at specified location
     * @param drawTo sets the composition to draw to.
     */
    public DeleteCommand(Composition drawTo, String stringCoOrds) {
        super(drawTo);

        this.stringCoOrds = stringCoOrds;
        this.previousValue = drawTo.getActiveSet().get(stringCoOrds);

    }

    public void execute() {
        drawTo.open();
        action();
        drawTo.close();
    }

    public void unexecute() {
        drawTo.open();
        reverseAction();
        drawTo.close();
    }

    protected void action() {
        // Remove an entity from the canvas
        drawTo.deleteEntity(stringCoOrds);
    }

    protected void reverseAction() {
        // Re-add an entity that was removed from the canvas
        drawTo.replaceEntity(stringCoOrds, previousValue);
    }
}