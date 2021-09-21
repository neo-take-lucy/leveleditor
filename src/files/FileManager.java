package files;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

public class FileManager {

    private static final String root = "source/core/assets/configs/rags/";
    private static final String fileType = ".rag";

    private static final String[] rawWorlds = {
            "earth_1",
            "earth_2",
            "earth_3",
            "earth_4",
            "asgard_1",
            "asgard_2",
            "hel_1",
            "hel_2",
            "jotunheimr_1",
            "jotunheimr_2"
    };

    private static final String[] worlds = {
            "earth",
            "asgard",
            "hel",
            "jotunheimr"
    };

    private static LinkedList<String> validRags;

    /**
     * Makes a valid list of all the .rags in the rag configs/rags folder.
     * This is called before any .rags are gotten or set.
     */
    private static void makeValidList() {
        String[] pathNames;
        File f = new File(root);

        pathNames = f.list();

        assert pathNames != null;
        for (String path : pathNames) {
            if (path.endsWith(fileType)) {
                validRags.add(path.replace(fileType, ""));
            }
        }

    }

    /**
     * Returns a list of rags in the current folder.
     * @return LinkedList<String></String> of yeh.
     */
    public static LinkedList<String> getRags() {
        validRags = new LinkedList<>();
        makeValidList();
        return validRags;

    }

    /**
     * Returns whether or not the rag is already in the directoy.
     * @param rag rag trying to save.
     * @return boolean value if the .rag trying to save is already in the .rag directoy.
     */
    public static boolean isSaveFileInRags(String rag) {

        for(String fileInFolder : getRags()) {
            if (rag.equals(fileInFolder)) return true;
        }
        return false;
    }

    /**
     * Returns a list of the world types, in their raw format, for now...
     * Maybe will be expanded to not be the raw numbers,
     * and add some variance to the game over time. Doing so requires some time
     * to figure a bunch of stuff out, though.
     * @return LinkedList<String></String> of "raw" world names
     */
    public static LinkedList<String> getRawWorlds() {
        LinkedList<String> worldList = new LinkedList<>(Arrays.asList(rawWorlds));
        return worldList;
    }

}
