package gui;

/**
 * A ridiculously monolithic list of subSpr coordinates
 */
public enum SubSpr {

    // current spritesheet
    // eahc is 8, S in line 2 is spikes, in line 3 skeleton
    // . is blank
    // FP......
    // RS......
    // WS...de. (d)elete, (e)rase
    // A.N.sln@ (s)ave, (l)oad, (n)ew, @ is palette

    NULL(16, 24, 32, 32),
    FLOOR(0, 0, 8, 8),
    PLATFORM(8, 0, 16, 8),
    ROCK(0, 8, 8, 16),
    SPIKES(8, 8, 16, 16),
    WOLF(0, 16, 8, 24),
    SKELETON(8, 16, 16, 24),

    PLAYER(0, 24, 8, 32),

    DELETE(40, 16, 48, 24),
    ERASE(48, 16, 56, 24),

    SAVE(32, 24, 40, 32),
    LOAD(40, 24, 48, 32),
    NEW(48, 24, 56, 32),

    ARROW_LEFT(32, 0, 0, 0),
    ARROW_RIGHT(40, 0, 0, 0),
    ARROW_UP(32, 8, 0, 0),
    ARROW_DOWN(40, 8, 0, 0),

    ZOOM_IN(56, 0, 0, 0),
    ZOOM_OUT(48, 0, 0, 0),

    RESET_ZOOM(32, 16, 0, 0),

    UNDO(56, 8, 0, 0),
    REDO(48, 8, 0, 0),

    TOGGLE_TERM(56, 16, 0, 0);

    public int x;
    public int y;
    public int ex;
    public int ey;

    private SubSpr(int x, int y, int ex, int ey) {
        this.x = x;
        this.y = y;
        this.ex = ex;
        this.ey = ey;
    }


}
