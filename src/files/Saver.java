package files;

import composition.BrushType;
import composition.Composition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;

import static composition.BrushType.*;

public class Saver {

    private static final String root = "source/core/assets/configs/rags/";
    private static final String fileType = ".rag";

    /**
     * Saves a rag to the specified filePath
     * @param toSave Composition to save
     * @param name filepath
     */
    public static void saveToFilePath(Composition toSave, String name) {

        String file = root + name + fileType;
        BufferedWriter writer;

        try {
            File writingTo = new File(file);

            // gets the title, width and height of the Composition
            String title = toSave.getTitle();
            int width = toSave.getWidth();
            int height = toSave.getHeight();
            String world = toSave.getWorld();

            // gets the playerX and Y
            int playerX = toSave.getPlayer()[0];
            int playerY = height - toSave.getPlayer()[1] - 1;

            // gets the Terrain Layer and the Active Layer of the Composition
            BrushType[][] terrainLayer = toSave.getTerrainLayer();
            Hashtable<String, BrushType> activeLayer = toSave.getActiveSet();
            Hashtable<String, BrushType> powerLayer = toSave.getPowerUpSet();

            // makes a string builder
            StringBuilder saveFile = new StringBuilder();

            // each of these is a call to a static funtion in the class that
            // returns a String
            saveFile.append(makeConfig(title, width, height, world));
            saveFile.append(makeTerrain(terrainLayer));
            saveFile.append(atPlayer(playerX, playerY));
            saveFile.append(listActiveEntities(activeLayer, width, height));
            saveFile.append(listPowerUps(powerLayer, width, height));
            saveFile.append(getLevelTrigger(width));

            // makes a writer to the filepath
            writer = new BufferedWriter(new FileWriter(writingTo));

            // appends the StringBuilder to the writer
            writer.append(saveFile.toString());

            // ice cold, like that, it closes
            writer.close();

        } catch (Exception e) {
            System.err.printf("Save File %s failed", name);
        }
    }

    /**
     * Makes the config String of the .rag
     * @param title title of rag
     * @param width width of rag
     * @param height height of rag
     * @param world world type of the rag
     * @return String of "_" arguments
     */
    private static String makeConfig(String title, int width, int height, String world) {

        String appendTitle = String.format("$_title %s\n", title);
        String appendWidth = String.format("$_width %d\n", width);
        String appendHeight = String.format("$_height %d\n", height);
        String appendWorld = String.format("$_world %s\n", world);

        return appendTitle + appendWidth + appendHeight + appendWorld + "\n";

    }

    /**
     * Makes the terrain String of the .rag
     * @param terrainLayer terrainLayer of rag
     * @return String of "#" arguments
     */
    private static String makeTerrain(BrushType[][] terrainLayer) {

        StringBuilder terrainArray = new StringBuilder();

        for (BrushType[] column : terrainLayer) {

            StringBuilder colString = new StringBuilder("$#");

            for (BrushType terrainAt : column) {
                switch (terrainAt) {

                    case FLOOR:
                        colString.append("F");
                        break;
                    case ROCKS:
                        colString.append("R");
                        break;
                    case PLATFORM:
                        colString.append("P");
                        break;
                    case SPIKES:
                        colString.append("S");
                        break;
                    case NULL:
                    default:
                        colString.append(".");
                        break;
                }
            }
            colString.append("\n");
            terrainArray.append(colString);
        }

        terrainArray.append("\n");

        return terrainArray.toString();

    }

    /**
     * Makes the atPlayer String of the .rag
     * @param x player x
     * @param y player y
     * @return String of "@player" argument
     */
    private static String atPlayer(int x, int y) {

        return String.format("$@player set [%d,%d]\n\n", x, y);

    }

    /**
     * Makes the "-spawn"s String of the .rag
     * @param entList List of entiies in the Composition
     * @param height Height to adjust ragEdit y value to RagnarokRacer y value
     * @return String of "-" arguments
     */
    private static String listActiveEntities(Hashtable<String, BrushType> entList, int width, int height) {

        StringBuilder activeEntityList = new StringBuilder();

        for (String coOrd : entList.keySet()) {

            String entityType = "";

            switch (entList.get(coOrd)) {
                case SKELETON:
                    entityType = "(skeleton)";
                    break;
                case WOLF:
                    entityType = "(wolf)";
                    break;
                case FIRE_SPIRIT:
                    entityType = "(fireSpirit)";
                    break;
            }

            //convert current indexing into what RagnarokRacer expects (y = height - y - 1)
            String coOrdTrim = coOrd.replace("[","").replace("]","");
            int x = Integer.parseInt(coOrdTrim.split(",")[0]);
            int y = height - Integer.parseInt(coOrdTrim.split(",")[1]) - 1;

            coOrd = String.format("[%d,%d]", x, y);

            String appendTo = String.format("$-spawn %s %s\n", coOrd, entityType);
            activeEntityList.append(appendTo);

        }

        activeEntityList.append("\n");

        return activeEntityList.toString();
    }

    /**
     * Makes the "-spawn"s String of the .rag
     * @param powerList List of entiies in the Composition
     * @param height Height to adjust ragEdit y value to RagnarokRacer y value
     * @return String of "-" arguments
     */
    private static String listPowerUps(Hashtable<String, BrushType> powerList,
                                int width, int height) {

        StringBuilder powerUpsList = new StringBuilder();

        for (String coOrd : powerList.keySet()) {

            String powerUp = "";

            switch (powerList.get(coOrd)) {
                case LIGHTNING:
                    powerUp = "(lightning)";
                    break;
                case SPEAR:
                    powerUp = "(spear)";
                    break;
                case SHIELD:
                    powerUp = "(shield)";
                    break;
            }

            //convert current indexing into what RagnarokRacer expects (y = height - y - 1)
            String coOrdTrim = coOrd.replace("[","").replace("]","");
            int x = Integer.parseInt(coOrdTrim.split(",")[0]);
            int y = height - Integer.parseInt(coOrdTrim.split(",")[1]) - 1;

            coOrd = String.format("[%d,%d]", x, y);

            String appendTo = String.format("$-spawn %s %s\n", coOrd, powerUp);
            powerUpsList.append(appendTo);

        }

        powerUpsList.append("\n");

        return powerUpsList.toString();
    }

    private static String getLevelTrigger(int width) {

        String coOrd = String.format("[%d,%d]", width - 5, 5);
        String appendTo = String.format("$-spawn %s %s\n", coOrd, "(levelTrigger)");

        return appendTo;

    }
}
