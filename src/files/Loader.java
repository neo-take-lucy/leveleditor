package files;

import command.CommandService;
import composition.CompType;
import composition.Composition;
import handler.TerminalHandler;

import java.io.*;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Hashtable;

public class Loader {

    private Composition toWrite;
    //private

    public Loader() {

    }

    public static void loadTest() {

        BufferedReader br = null;
        boolean loadSuccessful = false;
        String pathName = "saves/LVL1.rag";

        try {
            File levelFile = new File(pathName);
            br = new BufferedReader(new FileReader(levelFile));
            loadSuccessful = true;
        } catch (FileNotFoundException e) {
            System.err.printf("%s || File Not Found\n", pathName);
        }

        if(loadSuccessful) {
            System.err.printf("it worked... cool\n");

            try {
                br.close();
            } catch (IOException e) {
                System.err.printf("Some error w/ IO in filepath %s", pathName);
            }
        }
    }

    public static void loadOverrideNew(String name, TerminalHandler writer) {
        Hashtable<String, String> entities = new Hashtable<>();
        entities.put(".", "(null)");
        entities.put("F", "(floor)");
        entities.put("P", "(platform)");
        entities.put("S", "(spikes)");
        entities.put("R", "(rocks)");

        writer.parseString("clear");

        String filepath = "source/core/assets/configs/rags/" + name + ".rag";

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

            String line;
            String title = null;

            int width = 0;
            int height = 0;

            ArrayList<String> argsToRun = new ArrayList<>();
            int x = 0;

            while ((line = br.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }

                if (line.charAt(0) != '$') {
                    throw new IOException();
                }

                char specifier = line.charAt(1);
                String delim = " ";

                if (specifier == '#') {
                    delim = "";
                }

                String[] args = line.substring(2).split(delim);

                switch (specifier) {
                    case '_':
                        String config = args[0];
                        String value = args[1];

                        if (config.equals("title")) {
                            title = value;
                        }

                        if (config.equals("width")) {
                            try {
                                width = Integer.parseInt(value);
                            } catch (NumberFormatException exception) {
                                System.err.print("Could not parse width/height value");
                            }
                        } else if (config.equals("height")) {
                            try {
                                height = Integer.parseInt(value);
                            } catch (NumberFormatException exception) {
                                System.err.print("Could not parse width/height value");
                            }
                        }

                        break;

                    case '#':
                        int y = 0;

                        for (String key: args) {
                            argsToRun.add(String.format("-place [%d,%d] %s"
                            , x
                            , y
                            , entities.get(key)));

                            y++;
                        }

                        x++;

                        break;

                    case '@':
                        argsToRun.add(String.format("@%s %s %s", args[0], args[1], args[2]));
                        break;

                    case '-':

                        //TO DOLAN:
                        // the entites are saved from the game which indexes y going upscreen
                        // so the whole height - y transformation has to happen forgot about that

                        // fuck my liiiiiiife

                        String[] coOrdArgs = args[1].replace("[","").replace("]","")
                                                    .split(",");

                        int sy = height - 1 - Integer.parseInt(coOrdArgs[1]);
                        args[1] = String.format("[%s,%d]", coOrdArgs[0], sy);
                        argsToRun.add(String.format("-place %s %s", args[1], args[2]));
                        break;

                    default:
                        throw new IOException();
                }
            }

            argsToRun.add(0, String.format("new %s [%d,%d]", title, width, height));

            for (String arg: argsToRun) {
                writer.parseString(arg);
            }

        } catch (IOException e) {
            System.err.print("Error loading file");
        }
    }

    public static void loadOverride(String name, TerminalHandler writer) {

        writer.parseString("clear");

        String filepath = "source/core/assets/configs/rags/" + name + ".rag";

        BufferedReader br = null;
        boolean loadSuccessful = false;

        try {
            File levelFile = new File(filepath);
            br = new BufferedReader(new FileReader(levelFile));
            loadSuccessful = true;
        } catch (FileNotFoundException e) {
            System.err.printf("%s || File Not Found\n", filepath);
        }
        try {
            if (loadSuccessful) {

                String line;
                float lane = 6.8f;

                int i = 0;
                while((line = br.readLine()) != null) {

                    for (int j = 0; j < line.length(); j++) {

                        int y = i;
                        int x = j;
                        String whatToPlace = null;

                        switch(line.charAt(j)) {

                            case ' ': //Empty
                                whatToPlace = "(null)";
                                break;
                            case 'P': //Platform
                                whatToPlace = "(platform)";
                                break;
                            case 'F': //Floor
                                whatToPlace = "(floor)";
                                break;
                            /*case 'A': //Avatar (player)
                                whatToPlace = "(player)";
                                break;*/
                            case 'S': //Spikes
                                whatToPlace = "(spikes)";
                                break;
                            case 'R': //Rocks
                                whatToPlace = "(rocks)";
                                break;
                            default:
                                break;
                        }

                        if (whatToPlace != null) {
                            String toParse = String.format("-place [%d,%d] %s", x, y, whatToPlace);
                            writer.parseString(toParse);
                        }
                    }
                    i++;
                }

                CommandService.clearHistory();


                try {
                    br.close();
                } catch (IOException e) {
                    System.err.printf("Some error w/ IO in filepath %s", filepath);
                }
            }
        } catch (IOException e) {
            System.err.printf("problem loading file, but u gotta deal with that");
        }

    }

}
