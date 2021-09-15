package gui;

import composition.BrushType;
import files.FileManager;
import handler.TerminalHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Toolbox is used to create and model the selection of entities on the side of the screen. Yeah, this
 * class is pretty poorly written. Probably a better method would be to store the buttons as lists of my own
 * object class and that way they can be passed to a single constructor to make each pane but... this thing
 * is due in like a week.
 */
public class ToolBox extends JPanel {

    private final int BUTTON_SIZE = 64;
    private final float BUTTON_SCALE = 0.63f;
    private final int GRID_WIDTH = 4;

    private JPanel configPanel;
    private ActionListener configListener;

    private JPanel terrainPanel;
    private ActionListener brushListener;

    private JPanel activePanel;
    private JPanel zoomPanel;

    private TerminalHandler handler;

    private BufferedImage spriteSheet;

    private GridLayout grid;
    private GridBagConstraints c;
    //this.setLayout(grid);

    public ToolBox(TerminalHandler handler) {
        super();

        this.handler = handler;

        initMainPanel();
        initIcons();

    }

    private void initIcons() {

        try {
            spriteSheet = ImageIO.read(new File("source/core/assets/configs/ragEditAssets/spritesheet.png"));
        } catch (Exception e) {
            System.err.print("Couldn't load spritesheet. Check: in configs/ragEditAssets/spritesheet.png\n");
        }

        // Initialses the SubSpr icons of the ConfigButtons
        for (ConfigButtons cB : ConfigButtons.values()) {

            SubSpr sS = cB.getSubSprite();

            int x = sS.x;
            int y = sS.y;

            Image sprite = spriteSheet.getSubimage(x, y, 8, 8)
                    .getScaledInstance((int) (BUTTON_SIZE * BUTTON_SCALE),
                            (int) (BUTTON_SIZE * BUTTON_SCALE),
                            Image.SCALE_FAST);
            Icon buttonIcon = new ImageIcon(sprite);

            cB.getButton().setIcon(buttonIcon);
            cB.getButton().setBorderPainted(false);

        }

        for (BrushButtons bB : BrushButtons.values()) {

            SubSpr sS = bB.getSubSprite();

            int x = sS.x;
            int y = sS.y;

            Image sprite = spriteSheet.getSubimage(x, y, 8, 8)
                    .getScaledInstance((int) (BUTTON_SIZE * BUTTON_SCALE),
                            (int) (BUTTON_SIZE * BUTTON_SCALE),
                            Image.SCALE_FAST);
            Icon buttonIcon = new ImageIcon(sprite);

            bB.getButton().setIcon(buttonIcon);
            bB.getButton().setBorderPainted(false);
        }

        for (ActiveEntityButtons aB : ActiveEntityButtons.values()) {

            SubSpr sS = aB.getSubSprite();

            int x = sS.x;
            int y = sS.y;

            Image sprite = spriteSheet.getSubimage(x, y, 8, 8)
                    .getScaledInstance((int) (BUTTON_SIZE * BUTTON_SCALE),
                            (int) (BUTTON_SIZE * BUTTON_SCALE),
                            Image.SCALE_FAST);
            Icon buttonIcon = new ImageIcon(sprite);

            aB.getButton().setIcon(buttonIcon);
            aB.getButton().setBorderPainted(false);

            //subSprites.put(cT, sprite);
        }

        for (ZoomScrollButtons zB : ZoomScrollButtons.values()) {

            SubSpr sS = zB.getSubSprite();

            int x = sS.x;
            int y = sS.y;

            Image sprite = spriteSheet.getSubimage(x, y, 8, 8)
                    .getScaledInstance((int) (BUTTON_SIZE * BUTTON_SCALE),
                            (int) (BUTTON_SIZE * BUTTON_SCALE),
                            Image.SCALE_FAST);
            Icon buttonIcon = new ImageIcon(sprite);

            zB.getButton().setIcon(buttonIcon);
            zB.getButton().setBorderPainted(false);

        }

    }

    private void initMainPanel() {

        grid = new GridLayout(4, 0);
        this.setLayout(grid);

        c = new GridBagConstraints();

        configPanel = initConfigPanel();
        terrainPanel = initTerrainPanel();
        activePanel = initActiveEntityPanel();
        zoomPanel = initZoomPanel();

        Color bg = new Color(16774076);
        configPanel.setBackground(bg);
        terrainPanel.setBackground(bg);
        activePanel.setBackground(bg);
        zoomPanel.setBackground(bg);

        c.fill = GridBagConstraints.NONE;

        this.add(configPanel, c);
        this.add(terrainPanel, c);
        this.add(activePanel, c);
        this.add(zoomPanel, c);

    }

    private JPanel initConfigPanel() {

        // buttons: (3 x 2)
        // new, save, load,
        // undo, redo

        configPanel = new JPanel(new GridBagLayout());
        configPanel.setPreferredSize(new Dimension(BUTTON_SIZE * 2, BUTTON_SIZE * 3));
        GridBagConstraints c = new GridBagConstraints();

        //Make the Label
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GRID_WIDTH;
        c.gridheight = 1;

        configPanel.add(new JLabel("Configure"), c);

        // The config panel gets the action command and then switches to determine which dialog to
        // open
        configListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //handler.parseString(e.getActionCommand());

                switch (e.getActionCommand()) {
                    case ">new":
                        String newArguments = getNewDialog();
                        handler.parseString("new " + newArguments);
                        break;
                    case ">save":
                        //open save dialog
                        String file = getSaveDialog();

                        if (file.equals("")) {
                            break;
                        } else {
                            handler.parseString("save " + file);
                        }
                        break;
                    case ">load":
                        // open load dialog
                        file = getLoadDialog();
                        if (file.equals("")) break;
                        handler.parseString("load " + file);
                        break;
                    case ">undo":
                        // parse "u"
                        handler.parseString("u");
                        break;
                    case ">redo":
                        handler.parseString("r");
                        break;
                        // parse "r"
                    case ">toggleterm":
                        handler.parseString("toggleterm");
                        break;
                }
            }
        };

        c.gridwidth = 1;
        c.gridheight = 1;

        //Make the Buttons and Put Them In
        int i = GRID_WIDTH;
        for (ConfigButtons b : ConfigButtons.values()) {
            int y = i / GRID_WIDTH;
            int x = i % GRID_WIDTH;

            //System.out.printf("%s: grid x %d grid y %d\n", b.toString(), x, y);

            //b.getButton().setText(b.toString());
            b.getButton().addActionListener(configListener);
            b.getButton().setActionCommand(b.getSetting());
            b.getButton().setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

            //c.fill = GridBagConstraints.CENTER
            c.gridx = x;
            c.gridy = y;

            configPanel.add(b.getButton(), c);

            i++;
        }

        return configPanel;

    }

    private JPanel initTerrainPanel() {

        // buttons:
        // eraser, floor, platform
        // rocks, spikes

        terrainPanel = new JPanel(new GridBagLayout());
        terrainPanel.setMaximumSize(new Dimension(BUTTON_SIZE * 3,
                                                BUTTON_SIZE * 3));
        GridBagConstraints c = new GridBagConstraints();

        //Make the Label
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GRID_WIDTH;
        c.gridheight = 1;

        terrainPanel.add(new JLabel("Terrain Brushes"), c);

        // Makes the action listener, the actionCommand is set in the Loop below
        // actionCommand takes the Enum "brushSetting" and then parses it to handler
        brushListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handler.parseString(e.getActionCommand());
            }
        };

        c.gridwidth = 1;
        c.gridheight = 1;

        //Make the Buttons and Put Them In
        int i = GRID_WIDTH;
        for (BrushButtons b : BrushButtons.values()) {
            int y = i / GRID_WIDTH;
            int x = i % GRID_WIDTH;

            //System.out.printf("%s: grid x %d grid y %d\n", b.toString(), x, y);

            //b.getButton().setText(b.toString());
            b.getButton().addActionListener(brushListener);
            b.getButton().setActionCommand(b.getSetting());
            b.getButton().setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

            //c.fill = GridBagConstraints.CENTER
            c.gridx = x;
            c.gridy = y;

            terrainPanel.add(b.getButton(), c);

            i++;
        }

        return terrainPanel;

    }

    private JPanel initActiveEntityPanel() {

        // buttons:
        // delete, wolf, skeleton

        activePanel = new JPanel(new GridBagLayout());
        activePanel.setPreferredSize(new Dimension(BUTTON_SIZE * 3, BUTTON_SIZE * 3));
        GridBagConstraints c = new GridBagConstraints();

        //Make the Label
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GRID_WIDTH;
        c.gridheight = 1;

        activePanel.add(new JLabel("Enemies and Power Ups"), c);

        c.gridwidth = 1;
        c.gridheight = 1;

        //Make the Buttons and Put Them In
        int i = GRID_WIDTH;
        for (ActiveEntityButtons b : ActiveEntityButtons.values()) {
            int y = i / GRID_WIDTH;
            int x = i % GRID_WIDTH;

            //b.getButton().setText(b.toString());
            b.getButton().addActionListener(brushListener);
            b.getButton().setActionCommand(b.getSetting());
            b.getButton().setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

            //c.fill = GridBagConstraints.CENTER
            c.gridx = x;
            c.gridy = y;

            activePanel.add(b.getButton(), c);

            i++;
        }

        return activePanel;

    }

    private JPanel initZoomPanel() {
        zoomPanel = new JPanel(new GridBagLayout());
        zoomPanel.setPreferredSize(new Dimension(BUTTON_SIZE * 3, BUTTON_SIZE * 2));
        GridBagConstraints c = new GridBagConstraints();

        //Make the Label
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 1;

        zoomPanel.add(new JLabel("Zoom / Scroll"), c);

        c.gridwidth = 1;
        c.gridheight = 1;

        //Make the Buttons and Put Them In
        int i = GRID_WIDTH;
        for (ZoomScrollButtons b : ZoomScrollButtons.values()) {
            int y = i / GRID_WIDTH;
            int x = i % GRID_WIDTH;

            //b.getButton().setText(b.toString());
            b.getButton().addActionListener(brushListener);
            b.getButton().setActionCommand(b.getSetting());
            b.getButton().setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

            //c.fill = GridBagConstraints.CENTER
            c.gridx = x;
            c.gridy = y;

            zoomPanel.add(b.getButton(), c);

            i++;
        }

        return zoomPanel;
    }

    private String getLoadDialog() {

        int x = SubSpr.LOAD.x;
        int y = SubSpr.LOAD.y;

        Image sprite = spriteSheet.getSubimage(x, y, 8, 8)
                .getScaledInstance((int) (BUTTON_SIZE), (int) (BUTTON_SIZE), Image.SCALE_FAST);

        Icon icon = new ImageIcon(sprite);
        Object[] files = FileManager.getRags().toArray();
        String file = (String) JOptionPane.showInputDialog(this,
                "Select .rag to Load", "Load .rag",
                JOptionPane.PLAIN_MESSAGE, icon, files, "");

        return file;

    }

    private String getSaveDialog() {

        int x = SubSpr.SAVE.x;
        int y = SubSpr.SAVE.y;

        String file = "";

        Image sprite = spriteSheet.getSubimage(x, y, 8, 8)
                .getScaledInstance((int) (BUTTON_SIZE), (int) (BUTTON_SIZE), Image.SCALE_FAST);

        Icon icon = new ImageIcon(sprite);
        //Object[] files = FileManager.getRags().toArray();
        String firstFile = (String) JOptionPane.showInputDialog(this,
                "Save .rag as", "Save .rag",
                JOptionPane.PLAIN_MESSAGE, icon, null, "");

        if (firstFile.equals("")) return "";

        if (FileManager.isSaveFileInRags(firstFile)) {

            String s = String.format("%s.rag already in rag directory, would you like to override?\n" , firstFile);

            int confirm = (int) JOptionPane.showConfirmDialog(this,
                    s, "Confirm save Override",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);

            // confirm = 0 , YES
            // confirm = 1, NO

            if (confirm == 1) return "";

        }

        file = firstFile;

        return file;

    }

    private String getNewDialog() {

        JTextField title = new JTextField();
        JTextField width = new JTextField();
        JTextField height = new JTextField();

        JComponent[] inputs = {
                new JLabel("Title:"),
                title,
                new JLabel("Width:"),
                width,
                new JLabel("Height:"),
                height,
        };

        int x = SubSpr.NEW.x;
        int y = SubSpr.NEW.y;
        Image sprite = spriteSheet.getSubimage(x, y, 8, 8)
                .getScaledInstance((int) (BUTTON_SIZE), (int) (BUTTON_SIZE), Image.SCALE_FAST);
        Icon icon = new ImageIcon(sprite);

        JOptionPane.showMessageDialog(this, inputs,
                "Make New .rag", JOptionPane.INFORMATION_MESSAGE, icon);

        try {

            Integer.parseInt(width.getText());
            Integer.parseInt(height.getText());

        } catch (NumberFormatException e) {
            System.err.printf("\nProvided invalid numbers to width or height while making New in Gui\n");
            JOptionPane.showMessageDialog(this,
                    "Width or Height is not an Integer", "Error making New",
                    JOptionPane.INFORMATION_MESSAGE, icon);
        }

        return String.format("%s [%s,%s]", title.getText(), width.getText(), height.getText());
    }

}

/**
 * Enum representing the Configuration Buttons
 */
enum ConfigButtons {

    // in the () can make icon

    // maybe also save values relating to the coordinates in this?
    NEW(">new", SubSpr.NEW), // sets brush to null
    SAVE(">save", SubSpr.SAVE),
    LOAD(">load", SubSpr.LOAD),
    TOGGLE_TERM(">toggleterm", SubSpr.TOGGLE_TERM),
    UNDO(">undo", SubSpr.UNDO),
    REDO(">redo", SubSpr.REDO);

    private JButton butt;
    private String setting;
    private SubSpr subSprite;

    ConfigButtons(String configSetting, SubSpr ss) {

        butt = new JButton();
        setting = configSetting;
        subSprite = ss;

    }

    public JButton getButton() {
        return butt;
    }

    public String getSetting() {
        return setting;
    }

    public SubSpr getSubSprite() {
        return subSprite;
    }
}

/**
 * Enum representing the Brush (Terrain Tile) Buttons
 */
enum BrushButtons {

    // in the () can make icon

    // maybe also save values relating to the coordinates in this?
    ERASER(BrushType.ERASER), // sets brush to null
    FLOOR(BrushType.FLOOR),
    PLATFORM(BrushType.PLATFORM),
    ROCKS(BrushType.ROCKS),
    SPIKES(BrushType.SPIKES);

    private JButton butt;
    private String setting;
    private SubSpr subSprite;

    BrushButtons(BrushType brushType) {

        butt = new JButton();
        setting = brushType.brush;
        subSprite = brushType.subSpr;

    }

    public JButton getButton() {
        return butt;
    }

    public String getSetting() {
        return setting;
    }

    public SubSpr getSubSprite() {
        return subSprite;
    }
}

/**
 * Enum representing the ActiveEntity (Active/PowerUp) Buttons
 */
enum ActiveEntityButtons {

    // in the () can make icon

    // maybe also save values relating to the coordinates in this?
    DELETE(BrushType.DELETE),
    DELETE_POW(BrushType.DELETE_POW),
    SKELETON(BrushType.SKELETON),
    WOLF(BrushType.WOLF),
    FIRE_SPIRIT(BrushType.FIRE_SPIRIT),
    POWER_UP_1(BrushType.POWER_UP_1);

    private JButton butt;
    private String setting;
    private SubSpr subSprite;

    ActiveEntityButtons(BrushType brushType) {

        butt = new JButton();
        setting = brushType.brush;
        subSprite = brushType.subSpr;

    }

    public JButton getButton() {
        return butt;
    }

    public String getSetting() {
        return setting;
    }

    public SubSpr getSubSprite() {
        return subSprite;
    }
}

/**
 * Enum representing the Zoom/Scroll Buttons
 */
enum ZoomScrollButtons {

        // in the () can make icon

    // maybe also save values relating to the coordinates in this?
    ARROW_LEFT("con_o -100 0", SubSpr.ARROW_LEFT),
    ARROW_RIGHT("con_o 100 0", SubSpr.ARROW_RIGHT),
    ARROW_UP("con_o 0 -100", SubSpr.ARROW_UP),
    ARROW_DOWN("con_o 0 100", SubSpr.ARROW_DOWN),
    ZOOM_IN("con_z 10", SubSpr.ZOOM_IN),
    ZOOM_OUT("con_z -10", SubSpr.ZOOM_OUT);

    private JButton butt;
    private String setting;
    private SubSpr subSprite;

    ZoomScrollButtons(String brushSetting, SubSpr ss) {

        butt = new JButton();
        setting = brushSetting;
        subSprite = ss;

    }

    public JButton getButton() {
        return butt;
    }

    public String getSetting() {
        return setting;
    }

    public SubSpr getSubSprite() {
        return subSprite;
    }
}
