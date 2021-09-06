package composition;

import java.awt.*;

public enum CompType {
    NULL(Color.PINK),
    WALL(Color.BLACK),
    FLOOR(Color.BLUE),
    PLATFORM(Color.cyan),
    ROCKS(Color.red),
    PLAYER(Color.green),
    SPIKES(Color.gray),
    SKELETON(Color.white),
    WOLF(Color.darkGray);

    private Color color;

    CompType(Color set) {
        this.color = set;
    }

    public Color getColor() {
        return color;
    }
}
