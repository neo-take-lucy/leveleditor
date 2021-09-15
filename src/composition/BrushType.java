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
public enum BrushType {

    PLAYER(Layer.ACTIVE, "(player)", "b a", SubSpr.PLAYER, Color.yellow),

    NULL(Layer.TERRAIN, "(null)", "b n", SubSpr.NULL, Color.PINK),
    DELETE(Layer.ACTIVE, "(delete)", "del", SubSpr.DELETE, Color.PINK),
    FLOOR(Layer.TERRAIN, "(floor)", "b f", SubSpr.FLOOR, Color.BLUE),
    PLATFORM(Layer.TERRAIN, "(platform)", "b p", SubSpr.PLATFORM, Color.cyan),
    SPIKES(Layer.TERRAIN, "(spikes)", "b v", SubSpr.SPIKES, Color.white),
    ROCKS(Layer.TERRAIN, "(rocks)", "b r", SubSpr.ROCK, Color.red),
    WOLF(Layer.ACTIVE, "(wolf)", "b w", SubSpr.WOLF, Color.darkGray),
    SKELETON(Layer.ACTIVE, "(skeleton)", "b s", SubSpr.SKELETON, Color.white),
    FIRE_SPIRIT(Layer.ACTIVE, "(fireSpirit)", "b z", SubSpr.ARROW_DOWN, Color.BLUE);

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
