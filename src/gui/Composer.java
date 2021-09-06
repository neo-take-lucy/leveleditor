package gui;

import composition.CompType;
import composition.Composition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Hashtable;

public class Composer extends JComponent {

    private int P_SCALE = 20; //pixel scale

    private int T_WIDTH; //amouunt of tiles to display, implement scrolling
    private int T_HEIGHT;

    private int EMBOSS = 40;
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;

    private int OFFSET_HOZ;
    private int OFFSET_VERT;

    private Composition painting;
    private Mouse mouse;

    public Hashtable<String, String> brushSettings;   // this is consistent should should be more
                                                        // accessible
    public String currentBrushSetting;

    public Composer() {
        super();


        brushSettings = new Hashtable<>();

        // brush setting syntax: bi where i denotes a single character version
        brushSettings.put("b n", "(null)");        // but fuck it
        brushSettings.put("b f", "(floor)");
        brushSettings.put("b p", "(platform)");
        brushSettings.put("b a", "(player)"); // a for avatar
        brushSettings.put("b v", "(spikes)"); // v look like spike
        brushSettings.put("b r", "(rocks)");
        brushSettings.put("b w", "(wall)");
        brushSettings.put("b s", "(skeleton)");

        currentBrushSetting = brushSettings.get("b n");


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

            int x = 0;
            for (CompType[] column : terrainLayer) {
                int y = 0;
                for (CompType terrainAt : column) {

                    //System.out.printf("x: %d, y: %d, terrain: %s\n", x, y, terrainAt.toString());

                    g.setColor(terrainAt.getColor());
                    g.fillRect( x * P_SCALE + halfEmb - OFFSET_HOZ,
                                y * P_SCALE + halfEmb - OFFSET_VERT,
                                    P_SCALE, P_SCALE);

                    y++;
                }
                x++;
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

    public void adjustVerticalOffset(int offset) {
        OFFSET_VERT += offset;
    }

    public int[] getGridAt(int mouseX, int mouseY) {

        int gridX = (int) Math.floor((mouseX + OFFSET_HOZ - EMBOSS/2.0)/(double)P_SCALE);
        int gridY = (int) Math.floor((mouseY + OFFSET_VERT - EMBOSS/2.0)/(double)P_SCALE);

        return new int[]{gridX, gridY};
    }

    public void putBrushFromTerm(String fromTerm) {
        currentBrushSetting = brushSettings.get(fromTerm);
    }

}
