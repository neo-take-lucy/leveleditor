package gui;

/**
 * A ridiculously monolithic list of subSpr coordinates.
 *
 * Each of these co-ordinates is the top left corner of the sub sprite on the sprite sheet.
 * Most of these values are used in the BrushType class,
 * but a select few (and only for config buttons) are used directly in the ToolBox class.
 */
public enum SubSpr {

    NULL(16, 24),
    FLOOR(0, 0),
    PLATFORM(8, 0),
    ROCK(0, 8),
    SPIKES(8, 8),

    WOLF(0, 16),
    SKELETON(8, 16),
    FIRE_SPIRIT(16, 16),

    PLAYER(0, 24),

    DELETE(40, 16),
    ERASE(48, 16),
    DELETE_POW(0, 40),

    SAVE(32, 24),
    LOAD(40, 24),
    NEW(48, 24),

    ARROW_LEFT(32, 0),
    ARROW_RIGHT(40, 0),
    ARROW_UP(32, 8),
    ARROW_DOWN(40, 8),

    ZOOM_IN(56, 0),
    ZOOM_OUT(48, 0),

    RESET_ZOOM(32, 16),

    UNDO(56, 8),
    REDO(48, 8),

    TOGGLE_TERM(56, 16);

    public int x;
    public int y;

    private SubSpr(int x, int y) {
        this.x = x;
        this.y = y;
    }


}
