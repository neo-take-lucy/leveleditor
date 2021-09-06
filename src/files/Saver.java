package files;

import composition.CompType;
import composition.Composition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

public class Saver {

    private static final String root = "source/core/assets/configs/rags/";
    private static final String fileType = ".rag";

    public static void saveToFilePath(Composition toSave, String name) {

        String file = root + name + fileType;

        BufferedWriter writer;

        try {
            File writingTo = new File(file);
            if (writingTo.createNewFile()) {

                //System.err.printf("Couldn't create new file %s", file);

            } else {
                // pause program until conflict is resolved
            }

            CompType[][] terrainLayer = toSave.getTerrainLayer();
            // loop thru all

            CompType[][] activeLayer = toSave.getActiveLayer();
            // then for this one, when it's a list of entities, place em

            writer = new BufferedWriter(new FileWriter(writingTo));

            LinkedList<StringBuilder> fileAsList = new LinkedList<>();

            int x = 0;
            for (CompType[] column : terrainLayer) {

                int y = 0;
                for (CompType toPlace : column) {

                    //System.out.printf("x:%dy:%d%s$",x,y,toPlace.toString());

                    if (y == fileAsList.size()) {
                        fileAsList.addLast(new StringBuilder(""));
                    }

                    Hashtable<CompType, String> toWrite = new Hashtable<>();
                    toWrite.put(CompType.NULL, " ");
                    toWrite.put(CompType.WALL, " ");
                    toWrite.put(CompType.FLOOR, "F");
                    toWrite.put(CompType.PLATFORM, "P");
                    toWrite.put(CompType.ROCKS, "R");
                    toWrite.put(CompType.PLAYER, "A");
                    toWrite.put(CompType.SPIKES, "S");
                    toWrite.put(CompType.SKELETON, " ");

                    String append = toWrite.get(toPlace);
                    fileAsList.get(y).append(append);

                    y++;
                }
                //System.out.printf("\n");
                //fileAsList.get(y).append("\n");
                x++;
            }

            for(StringBuilder row : fileAsList) {
                //System.out.println(row.toString());
                row.append("\n");
                writer.write(row.toString());
            }

            writer.close();

            //writer = new BufferedWriter(new FileWriter(writingTo));
            //writer.write("c c c c c");
            //writer.close();
        } catch (IOException e) {
            System.err.printf("Problem in writingTo.createNewFile()");
        }
    }

}
