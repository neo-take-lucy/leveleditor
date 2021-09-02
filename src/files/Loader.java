package files;

import command.CommandService;
import composition.CompType;
import composition.Composition;
import handler.TerminalHandler;

import java.io.*;

public class Loader {

    private Composition toWrite;
    //private

    public Loader() {

    }

    public static void loadTest() {

        BufferedReader br = null;
        boolean loadSuccessful = false;
        String pathName = "saves/LVL1.map";

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

    public static void loadOverride(String filepath, TerminalHandler writer) {

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
                            case 'A': //Avatar (player)
                                whatToPlace = "(player)";
                                break;
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

    /*public void loadWorld() throws IOException {
        // URL url = getClass().getResource("com/deco2800/game/entities/configs/LVL1.map");
        // spawnPlatform(1, LANE_0, 3);
        // spawnPlatform(1, LANE_1, 4);
        // spawnPlatform(1, LANE_2, 5);


        File levelFile = new File("saves/LVL1.map");
        FileReader fr = new FileReader(levelFile);
        BufferedReader br = new BufferedReader(fr);
        String line;
        float lane = 6.8f; // why do u have this number JACKSON!!!!!
        while ((line = br.readLine()) != null) {
            // TODO: PERFORM INPUT CHECKING ON LVL FILE
            // also: encapsulate loader in Loader() class.
            // can pass reference to GameArea instance if spawn functions wished to be called
            // directly, but perhaps more elegant to introduce an intermediary
            for (int i = 0; i < line.length(); i++) {
                switch ((line.charAt(i))) {
                    case 'P':
                        // PLATFORM

                        break;
                    case 'F':
                        // FLOOR
                        spawnFloor(1, LANES[Math.round(lane/2)], (i*3));
                        spawnFloor(1, LANES[Math.round(lane/2)], (i*3)+1);
                        spawnFloor(1, LANES[Math.round(lane/2)], (i*3)+2);
                        break;
                    case 'A':
                        //A for Avatar :) the last airbender
                        player = spawnPlayer(LANES[Math.round(lane/2)]+1, (i*3)+1);
                        break;
                    case 'S':
                        //SPIKE
                        spawnSpike(LANES[Math.round(lane/2)]+1, (i*3)+1);
                        break;
                    case 'R':
                        //ROCK
                        spawnRock(LANES[Math.round(lane/2)]+1, (i*3)+1);
                    default:
                        break;
                }
            }
            lane = lane -1;
        }
        br.close();
        fr.close();
    }*/

}
