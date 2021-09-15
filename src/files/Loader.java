package files;

import handler.TerminalHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class Loader {

    private static Hashtable<String, String> terrainChars;

    public Loader() {

    }

    /**
     * Initialises the loader.
     */
    private static void initLoader() {
        initTerrainChars();
    }

    /**
     * Assigns the characters in the terrainArray to meaningful (name)s the
     * ragCmd will accept.
     *
     * If you wish to extend the types of entities that are loaded to the editor,
     * you should add to this Hashtable.
     *
     * REM: remind john to make a single data structure for the saver and loader.
     */
    private static void initTerrainChars() {
        terrainChars = new Hashtable<>();

        terrainChars.put(".", "(null)");
        terrainChars.put("F", "(floor)");
        terrainChars.put("P", "(platform)");
        terrainChars.put("S", "(spikes)");
        terrainChars.put("R", "(rocks)");
    }

    /**
     * Sends requests to the terminal handler to clear the current Composition,
     * send config commands to title, set width and height, then fills out the composition
     * with "-place" commands.
     *
     * REM: if you're reading this, remind John to split up "-place" and "-spawn"
     * @param name name of the file to load
     * @param writer TerminalHandler to send terminal commands to
     */
    public static void loadOverride(String name, TerminalHandler writer) {

        initLoader();

        // clears the composition
        writer.parseString("clear");

        String filepath = "source/core/assets/configs/rags/" + name + ".rag";

        // encased in a try block in case the file cannot be loaded (this should
        // be stopped by the Interface before this happens, tho... lets hope
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

            String line;
            String title = null;

            int width = 0;
            int height = 0;

            ArrayList<String> argsToRun = new ArrayList<>();
            int x = 0;

            // this will read all the lines in the file until it reaches the end
            while ((line = br.readLine()) != null) {
                //System.err.println("parsing line " + line);
                // Ignores Empty Lines
                if (line.length() == 0) {
                    continue;
                }

                // Ignores non-$ lines
                if (line.charAt(0) != '$') {
                    continue; //to Dolan: accept non-$ lines for comments maybe?
                    //throw new IOException();
                }

                // If both tests are passed, gets the specifier char (2nd char of line)
                char specifier = line.charAt(1);
                String delim = " ";

                // yo Dolan thats clean
                if (specifier == '#') {
                    delim = "";
                }

                String[] args = line.substring(2).split(delim);

                switch (specifier) {
                    // '_' is a config line
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
                                System.err.print("Could not parse width value");
                            }
                        } else if (config.equals("height")) {
                            try {
                                height = Integer.parseInt(value);
                            } catch (NumberFormatException exception) {
                                System.err.print("Could not parse height value");
                            }
                        }

                        break;
                    // '#' is a terrainLayer line
                    case '#':
                        int y = 0;

                        for (String key: args) {
                            argsToRun.add(String.format("-place [%d,%d] %s"
                            , x
                            , y
                            , terrainChars.get(key)));

                            y++;
                        }

                        x++;

                        break;

                    // '@' is an "atEntity" line to set traits about specific ent
                    case '@':
                        argsToRun.add(String.format("@%s %s %s", args[0], args[1], args[2]));
                        break;

                    // '-' is a "-place" for an active entity
                    case '-':

                        if (args[2].equals("(levelTrigger)")) break;

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

            // yoooo thats so clean
            argsToRun.add(0, String.format("new %s [%d,%d]", title, width, height));

            // then just killem all
            for (String arg: argsToRun) {
                writer.parseString(arg);
            }

        } catch (IOException e) {
            System.err.print("Error loading file in Loader");
        }
    }
}
