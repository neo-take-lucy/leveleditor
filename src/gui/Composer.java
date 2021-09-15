package gui;

import composition.CompType;
import composition.Composition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

/**
 * GUI Representation of the Composition.
 * Extends the JComponent class, and overrides the draw method to display the current map
 */
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

        // gets the Spritesheet
        try {
            spriteSheet = ImageIO.read(new File("source/core/assets/configs/ragEditAssets/spritesheet.png"));
        } catch (Exception e) {
            System.err.print("Couldn't load spritesheet. Check: in configs/ragEditAssets/spritesheet.png\n");
        }

        initSubSprites();
        initBrushSettings();

        currentBrushSetting = brushSettings.get("b n");


    }

    /**
     * Initialises the Sprites to represent the map etc.
     * To add more sprites, you would need to extend the CompType Enum, and
     * assign the subSprite coordinates of the sprite.
     */
    private void initSubSprites() {

        subSprites = new Hashtable<>();

        for (CompType cT : CompType.values()) {

            int x = cT.getSubSprite().x;
            int y = cT.getSubSprite().y;

            BufferedImage sprite = spriteSheet.getSubimage(x, y, 8, 8);

            subSprites.put(cT, sprite);
        }
    }

    /**
     * Initialises the brush settings, which are the (type) arguments that
     * the mouse sends to the TerminalHandler.
     * To extend the (type)s that TerminalHandler accepts from the mouse,
     * you would need to add to this list.
     */
    private void initBrushSettings() {
        brushSettings = new Hashtable<>();

        // brush setting syntax: bi where i denotes a single character version
        brushSettings.put("b n", "(null)");        // but fuck it
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
    }

    public void setComposition(Composition grid) {
        painting = grid;
    }

    /**
     * This function is used to make public calls to the repaint() method
     * of the JComponent class
     */
    public void requestRedraw() {

        this.getParent().repaint();

    }

    @Override
    protected void paintComponent(Graphics g) {

        int halfEmb = EMBOSS/2;
        isSpriteMode = true;    // if this is set to false, will display squares of stuff...
                                // could be a useful mode if you want to extend and check functionality
                                // without drawing new sprites

        // won't paint if the painting stream is open... this is like some silly double-buffering
        // inspired data protection... i doubt it does anything
        if(!painting.isOpen()) {
            CompType[][] terrainLayer = painting.getTerrainLayer();
            Hashtable<String, CompType> activeSet = painting.getActiveSet();

            // draws the terrain
            int x = 0;
            for (CompType[] column : terrainLayer) {
                int y = 0;
                for (CompType terrainAt : column) {

                    int drawX = x * P_SCALE + halfEmb - OFFSET_HOZ;
                    int drawY = y * P_SCALE + halfEmb - OFFSET_VERT;


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

            // draws the entities
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

    /**
     * Sets the graphical settings of the Composer.
     * This can be set via the con_s [ps] [tw] [th] command,
     * but calls to "con_o" and "con_z" are preferred
     * @param ps Pixel Size (the size of each Tile)
     * @param tw Tile Width (the amount of tiles horizontal)
     * @param th Tile Height (the amount of tiles vertically)
     */
    public void graphicalSettings(int ps, int tw, int th) {

        setZoom(ps);
        T_WIDTH = tw;
        T_HEIGHT = th;

        SCREEN_WIDTH = T_WIDTH * P_SCALE + EMBOSS;
        SCREEN_HEIGHT = T_HEIGHT * P_SCALE + EMBOSS;
    }

    /**
     * Sets the size of each tile (effectively zooming in/out).
     * This can be set via the con_z [ps] command
     * @param ps pixel size.
     */
    public void setZoom(int ps) {
        P_SCALE = ps;
        //EMBOSS = P_SCALE*8;
    }

    public int[] getResolution() {
        return new int[]{SCREEN_WIDTH, SCREEN_HEIGHT};
    }

    // this is a whole family of functions to deal with the offset etc...
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

    /**
     * Returns the grid-coordinate under the mouse, and uses math to do so!
     * I'm so happy
     * @param mouseX mouse X position
     * @param mouseY mouse Y position
     * @return int[] of gird coordinates.
     */
    public int[] getGridAt(int mouseX, int mouseY) {

        int gridX = (int) Math.floor((mouseX + OFFSET_HOZ - EMBOSS/2.0)/(double)P_SCALE);
        int gridY = (int) Math.floor((mouseY + OFFSET_VERT - EMBOSS/2.0)/(double)P_SCALE);

        return new int[]{gridX, gridY};
    }

    /**
     * This is called by the terminalHandler to set the brushSetting via
     * the b [setting] command.
     * @param fromTerm single char brush argument
     */
    public void putBrushFromTerm(String fromTerm) {
        currentBrushSetting = brushSettings.get(fromTerm);
    }

    /**
     * Returns the zooooooooooooooom.
     * This is used to adjust zoom gradually.
     * @return the zoooooooom
     */
    public int getZoom() {
        return P_SCALE;
    }

}