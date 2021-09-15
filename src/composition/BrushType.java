package composition;

import gui.SubSpr;

import java.awt.*;

/*        brushSettings.put("b n", "(null)");        // but fuck it
                brushSettings.put("b d", "(delete)");
                brushSettings.put("b f", "(floor)");
                brushSettings.put("b p", "(platform)");
                brushSettings.put("b a", "(player)"); // a for avatar
                brushSettings.put("b v", "(spikes)"); // v look like spike
                brushSettings.put("b r", "(rocks)");
                brushSettings.put("b w", "(wolf)");
                brushSettings.put("b s", "(skeleton)");
                brushSettings.put("del", "(delete)");
                brushSettings.put("b z", "(fireSpirit)");
                brushSettings.put("b l", "(levelTrigger)");
*/

/**
 * BrushType contains all the entities that can be placed with the -place command in the
 * Editor. If you wish to add another type, add it to this list, and fill out the requiried
 * arguments of the Enum. The color isn't particularly important. Use a temporary SubSpr value
 * if you wish to just test functionality.
 *
 * If you wish to add a button that corresponds, you need to go to the ToolBox.java class and
 * add it in the Enum there.
 */
public enum BrushType {

    PLAYER(Layer.ACTIVE, "", "", SubSpr.PLAYER, Color.yellow),

    ERASER(Layer.TERRAIN, "", "b n", SubSpr.ERASE, Color.cyan),
    NULL(Layer.TERRAIN, "(null)", "b n", SubSpr.NULL, Color.PINK),
    FLOOR(Layer.TERRAIN, "(floor)", "b f", SubSpr.FLOOR, Color.BLUE),
    PLATFORM(Layer.TERRAIN, "(platform)", "b p", SubSpr.PLATFORM, Color.cyan),
    SPIKES(Layer.TERRAIN, "(spikes)", "b v", SubSpr.SPIKES, Color.white),
    ROCKS(Layer.TERRAIN, "(rocks)", "b r", SubSpr.ROCK, Color.red),

    DELETE(Layer.ACTIVE, "(delete)", "del", SubSpr.DELETE, Color.PINK),
    SKELETON(Layer.ACTIVE, "(skeleton)", "b s", SubSpr.SKELETON, Color.white),
    WOLF(Layer.ACTIVE, "(wolf)", "b w", SubSpr.WOLF, Color.darkGray),
    FIRE_SPIRIT(Layer.ACTIVE, "(fireSpirit)", "b z", SubSpr.FIRE_SPIRIT, Color.BLUE),

    DELETE_POW(Layer.POWER_UP, "(delPow)", "b pdel", SubSpr.DELETE_POW, Color.PINK),
    LIGHTNING(Layer.POWER_UP, "(lightning)", "b p1", SubSpr.ARROW_DOWN,
            Color.gray),
    SPEAR(Layer.POWER_UP, "(spear)", "b p2", SubSpr.ARROW_RIGHT, Color.YELLOW),
    SHIELD(Layer.POWER_UP, "(shield)", "b p3", SubSpr.ARROW_UP, Color.orange);

    public Layer layer;
    public String type;
    public String brush;
    public SubSpr subSpr;
    public Color color;

    BrushType(Layer layer, String type, String brush, SubSpr subSpr, Color color) {

        this.layer = layer;
        this.type = type;
        this.brush = brush;
        this.subSpr = subSpr;
        this.color = color;

    }

}
