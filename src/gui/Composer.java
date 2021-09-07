package gui;

import composition.CompType;
import composition.Composition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

public class Composer extends JComponent {

    private int P_SCALE = 20; //pixel scale

    private int T_WIDTH; //amouunt of tiles to display, implement scrolling
    private int T_HEIGHT;

    private int EMBOSS = 40;
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;

    private int OFFSET_HOZ;
    private int OFFSET_VERT;

    private boolean isSpriteMode = true;

    private Composition painting;
    private Mouse mouse;

    private BufferedImage spriteSheet;
    private Hashtable<CompType, BufferedImage> subSprites;

    public Hashtable<String, String> brushSettings;   // this is consistent should should be more
                                                        // accessible
    public String currentBrushSetting;

    public Composer() {
        super();

        try {
            spriteSheet = ImageIO.read(new File("source/core/assets/configs/ragEditAssets/spritesheet.png"));
        } catch (Exception e) {
            System.err.print("Couldn't load spritesheet. Check: in configs/ragEditAssets/spritesheet.png\n");
        }

        initSubSprites();

        brushSettings = new Hashtable<>();

        // brush setting syntax: bi where i denotes a single character version
        brushSettings.put("b n", "(null)");        // but fuck it
        brushSettings.put("b f", "(floor)");
        brushSettings.put("b p", "(platform)");
        brushSettings.put("b a", "(player)"); // a for avatar
        brushSettings.put("b v", "(spikes)"); // v look like spike
        brushSettings.put("b r", "(rocks)");
        brushSettings.put("b w", "(wolf)");
        brushSettings.put("b s", "(skeleton)");

        currentBrushSetting = brushSettings.get("b n");


    }

    public void initSubSprites() {

        subSprites = new Hashtable<>();

        for (CompType cT : CompType.values()) {

            int x = cT.getSubSprite().x;
            int y = cT.getSubSprite().y;
            int ex = cT.getSubSprite().ex;
            int ey = cT.getSubSprite().ey;


            BufferedImage sprite = spriteSheet.getSubimage(x, y, 8, 8);

            subSprites.put(cT, sprite);
        }
    }

    public void setComposition(Composition grid) {
        painting = grid;
    }

    public void requestRedraw() {

        this.getParent().repaint();

    }

    @Override
    protected void paintComponent(Graphics g) {

        //g.fillRect(0, 0, 100, 100);
        int halfEmb = EMBOSS/2;

        if(!painting.isOpen()) {
            CompType[][] terrainLayer = painting.getTerrainLayer();
            Hashtable<String, CompType> activeSet = painting.getActiveSet();

            int x = 0;
            for (CompType[] column : terrainLayer) {
                int y = 0;
                for (CompType terrainAt : column) {

                    //System.out.printf("x: %d, y: %d, terrain: %s\n", x, y, terrainAt.toString());

                    int drawX = x * P_SCALE + halfEmb - OFFSET_HOZ;
                    int drawY = y * P_SCALE + halfEmb - OFFSET_VERT;

                    isSpriteMode = true;
                    if (isSpriteMode) {
                        g.drawImage(subSprites.get(terrainAt), drawX, drawY, P_SCALE, P_SCALE, null);
                    } else {
                        g.setColor(terrainAt.getColor());
                        g.fillRect( drawX,
                                drawY,
                                P_SCALE, P_SCALE);
                    }
                    y++;
                }
                x++;
            }

            for (String coOrd : activeSet.keySet()) {

                String trimmedCoOrd = coOrd.replace("[","").replace("]","");
                String[] split = trimmedCoOrd.split(",");
                int ax = Integer.parseInt(split[0]);
                int ay = Integer.parseInt(split[1]);

                int drawX = ax * P_SCALE + halfEmb - OFFSET_HOZ;
                int drawY = ay * P_SCALE + halfEmb - OFFSET_VERT;

                if (isSpriteMode) {
                    g.drawImage(subSprites.get(activeSet.get(coOrd)), drawX, drawY, P_SCALE, P_SCALE, null);
                } else {
                    g.setColor(activeSet.get(coOrd).getColor());
                    g.fillRect(drawX,
                            drawY,
                            P_SCALE, P_SCALE);
                }


            }

            // drawing the player
            int px = painting.getPlayer()[0];
            int py = painting.getPlayer()[1];

            int drawX = px * P_SCALE + halfEmb - OFFSET_HOZ;
            int drawY = py * P_SCALE + halfEmb - OFFSET_VERT;

            if (isSpriteMode) {
                g.drawImage(subSprites.get(CompType.PLAYER), drawX, drawY, P_SCALE, P_SCALE, null);
            } else {
                g.setColor(CompType.PLAYER.getColor());
                g.fillRect(drawX,
                        drawY,
                        P_SCALE, P_SCALE);
            }



        }
    }

    public void graphicalSettings(int ps, int tw, int th) {

        setZoom(ps);
        T_WIDTH = tw;
        T_HEIGHT = th;

        SCREEN_WIDTH = T_WIDTH * P_SCALE + EMBOSS;
        SCREEN_HEIGHT = T_HEIGHT * P_SCALE + EMBOSS;
    }

    public void setZoom(int ps) {
        P_SCALE = ps;
        //EMBOSS = P_SCALE*8;
    }

    public int[] getResolution() {
        return new int[]{SCREEN_WIDTH, SCREEN_HEIGHT};
    }

    public void adjustHorizontalOffset(int offset) {
        OFFSET_HOZ += offset;
    }
    public void setHorizontalOffset(int offset) {
        OFFSET_HOZ = offset;
    }

    public void adjustVerticalOffset(int offset) {
        OFFSET_VERT += offset;
    }
    public void setVerticalOffset(int offset) {
        OFFSET_VERT = offset;
    }

    public int[] getGridAt(int mouseX, int mouseY) {

        int gridX = (int) Math.floor((mouseX + OFFSET_HOZ - EMBOSS/2.0)/(double)P_SCALE);
        int gridY = (int) Math.floor((mouseY + OFFSET_VERT - EMBOSS/2.0)/(double)P_SCALE);

        return new int[]{gridX, gridY};
    }

    public void putBrushFromTerm(String fromTerm) {
        currentBrushSetting = brushSettings.get(fromTerm);
    }

    public int getZoom() {
        return P_SCALE;
    }

}

/*enum SpriteSheetEnum {

    // current spritesheet
    // eahc is 8, S in line 2 is spikes, in line 3 skeleton
    // . is blank
    // FP......
    // RS......
    // WS......
    // A


    FLOOR(0, 0, 8, 8),
    PLATFORM(8, 0, 16, 8),
    ROCK(0, 8, 8, 16),
    SPIKES(8, 8, 16, 16),
    WOLF(0, 16, 8, 24),
    SKELETON(8, 16, 16, 24),
    AVATAR(0, 24, 8, 32);

    public int x;
    public int y;
    public int ex;
    public int ey;

    private SpriteSheetEnum(int x, int y, int ex, int ey) {
        this.x = x;
        this.y = y;
        this.ex = ex;
        this.ey = ey;
    }
}*/
