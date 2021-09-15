package command;

import composition.BrushType;
import composition.Composition;

public class PlaceCommand extends Command {

    private int x;
    private int y;

    private BrushType previousValue; //All paints/colours/enemies are model as enums
    private BrushType commandValue;

    /**
     * Places a cell at the specified location.
     * Also models deletion, as the "value" is set to default for Composition
     * background.
     * @param drawTo sets the composition to draw to.
     */
    public PlaceCommand(Composition drawTo, int x, int y, BrushType nextValue) {
        super(drawTo);

        this.x = x;
        this.y = y;

        this.previousValue = drawTo.getAtPoint(nextValue.layer, x, y);
        this.commandValue = nextValue;
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
        drawTo.setAtLayer(x, y, commandValue);
    }

    protected void reverseAction() {
        drawTo.setAtLayer(x, y, previousValue);
    }

}
