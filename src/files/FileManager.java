package files;

import java.io.File;
import java.util.LinkedList;

public class FileManager {

    private static final String root = "source/core/assets/configs/rags/";
    private static final String fileType = ".rag";

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



}
