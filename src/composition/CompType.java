package composition;

import gui.SubSpr;

import java.awt.*;

public enum CompType {
    NULL(Color.PINK, SubSpr.NULL),
    WALL(Color.BLACK, SubSpr.NULL),
    FLOOR(Color.BLUE, SubSpr.FLOOR),
    PLATFORM(Color.cyan, SubSpr.PLATFORM),
    ROCKS(Color.red, SubSpr.ROCK),
    PLAYER(Color.green, SubSpr.PLAYER),
    SPIKES(Color.gray, SubSpr.SPIKES),
    SKELETON(Color.white, SubSpr.SKELETON),
    WOLF(Color.darkGray, SubSpr.WOLF);

    private Color color;
    private SubSpr subSprite;

    CompType(Color set, SubSpr sub) {
        this.color = set;
        this.subSprite = sub;
    }

    public Color getColor() {
        return color;
    }
    public SubSpr getSubSprite() {
        return subSprite;
    }
}
