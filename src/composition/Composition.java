package composition;

import gui.Composer;

public class Composition {

    private CompType[][] terrainLayer;
    private CompType[][] activeLayer;

    private int width;
    private int height;

    private int[] playerLocation;

    private boolean isOpen;

    private Composer composer;

    //TO ADD:
    // - resizing

    /**
     * Constructor for a composition, which models the world.
     * Is made by running the entire script of the editor... atm...
     * Soon that will be changed to load the most recent version hardcoded,
     * Then keep a list of all commands across multiple instances
     * @param initWidth
     * @param initHeight
     */
    public Composition(int initWidth, int initHeight) {

        this.composer = null;

        this.width = initWidth;
        this.height = initHeight;

        initLayers();

    }

    public void initLayers() {
        terrainLayer = new CompType[width][height];
        activeLayer = new CompType[width][height];

        isOpen = false;

        for(int tx = 0; tx < width; tx++) {
            for(int ty = 0; ty < height; ty++) {
                terrainLayer[tx][ty] = CompType.NULL;
                activeLayer[tx][ty] = CompType.NULL;
            }
        }
    }

    /**
     * Sets the composer.
     * @param composer Mozart
     */
    public void setCompositor(Composer composer) {
        this.composer = composer;
    }

    /**
     * Removes composer.
     */
    public void removeCompositor() {
        this.composer = null;
    }

    /**
     * Opens the stream, enabling setting and disabling getting
     */
    public void open() {
        isOpen = true;
    }

    /**
     * Closes the stream, re-enabling getting and disallowing setting;
     */
    public void close() {
        isOpen = false;
    }

    /**
     * Sets at the layer and coordinate specified the value.
     * @param layer Which Layer to set at
     * @param x the x value
     * @param y the y value
     * @param value the CompType to place at value (i.e. NULL, FLOOR)
     */
    public void setAtLayer(Layer layer, int x, int y, CompType value) {

        boolean inBounds;

        if( x < 0 || x >= width || y < 0 || y >= height) {
            inBounds = false;
        } else {
            inBounds = true;
        }

        if(isOpen && inBounds) {
            switch(layer) {
                case ACTIVE:
                    activeLayer[x][y] = value;
                    break;
                case TERRAIN:
                    terrainLayer[x][y] = value;
                    break;
                default:
                    break;
            }
        } else {
            if (!isOpen) System.err.println("Tried to setAtLayer w/out Opening");
            if (!inBounds) System.err.println("Tried to setAtLayer out of bounds");
        }
    }

    /**
     * Returns the CompType at the specified point
     * @param layer which layer to select
     * @param x x value
     * @param y y value
     * @return CompType at specified point
     */
    public CompType getAtPoint(Layer layer, int x, int y) {

        if(x < 0 || x >= width || y < 0 || y >= height) {
            return CompType.NULL;
        }

        if(!isOpen) {
            switch(layer) {
                case ACTIVE:
                    return activeLayer[x][y];
                case TERRAIN:
                    return terrainLayer[x][y];
            }

        } else {
            System.err.println("Tried to getAtPoint w/out Closing");
        }
        return null;
    }

    /**
     * Returns the terrain layer (for Composer)
     * @return Layer that models terrain
     */
    public CompType[][] getTerrainLayer() {
        if(!isOpen) {
            return terrainLayer;
        } else {
            System.err.println("Tried to getTerrainLayer w/out Closing");
        }

        return null;
    }

    /**
     * Returns the active later (for Composer)
     * @return Layer that models "active" entities
     */
    public CompType[][] getActiveLayer() {
        if(!isOpen) {
            return activeLayer;
        } else {
            System.err.println("Tried to getActiveLayer w/out Closing");
        }

        return null;
    }

    public boolean isOpen() {
        return isOpen;
    }
}