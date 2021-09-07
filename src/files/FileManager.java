package files;

import java.io.File;
import java.util.LinkedList;

public class FileManager {

    private static final String root = "source/core/assets/configs/rags/";
    private static final String fileType = ".rag";

    private static LinkedList<String> validRags;

    private static void makeValidList() {
        String[] pathNames;
        File f = new File(root);

        pathNames = f.list();

        for (String path : pathNames) {
            if (path.endsWith(fileType)) {

                validRags.add(path.replace(fileType, ""));

            }
        }

        for (String s : validRags) {
            System.out.println(s);
        }

    }

    public static LinkedList<String> getRags() {
        validRags = new LinkedList<>();
        makeValidList();
        return validRags;

    }

    public static boolean isSaveFileInRags(String rag) {
        makeValidList();
        for(String fileInFolder : validRags) {
            if (rag.equals(fileInFolder)) return true;
        }
        return false;
    }



}
