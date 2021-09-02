package command;

import composition.CompType;
import composition.Composition;
import composition.Layer;

public class PlaceCommand extends Command {

    private int x;
    private int y;

    private Layer layer;
    private CompType previousValue; //All paints/colours/enemies are model as enums
    private CompType commandValue;

    /**
     * Places a cell at the specified location.
     * Also models deletion, as the "value" is set to default for Composition
     * background.
     * @param drawTo sets the composition to draw to.
     */
    public PlaceCommand(Composition drawTo, Layer layer, int x, int y, CompType nextValue) {
        super(drawTo);

        this.x = x;
        this.y = y;
        this.layer = layer;

        this.previousValue = drawTo.getAtPoint(layer, x, y);
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
        drawTo.setAtLayer(layer, x, y, commandValue);
    }

    protected void reverseAction() {
        drawTo.setAtLayer(layer, x, y, previousValue);
    }

}
