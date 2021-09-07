package command;

import composition.CompType;
import composition.Composition;
import composition.Layer;

import java.util.Hashtable;

public class DeleteCommand extends Command {
    private int x;
    private int y;

    private Layer layer;
    private CompType previousValue; //All paints/colours/enemies are model as enums
    private CompType commandValue;
    private String entity;
    Hashtable<String, CompType> entities;

    /**
     * Delete entity at specified location
     * @param drawTo sets the composition to draw to.
     */
    public DeleteCommand(Composition drawTo, int x, int y, String entity,
                         Layer layer, CompType previousValue) {
        super(drawTo);

        this.x = x;
        this.y = y;

        this.entity = entity;
        entities = drawTo.getActiveSet();

        this.layer = layer;
        this.previousValue = previousValue;
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
        entities.remove(entity);
        drawTo.setActiveSet(entities);
    }

    protected void reverseAction() {
        // Re-add an entity that was removed from the canvas
        drawTo.setAtLayer(layer, x, y, previousValue);
    }
}