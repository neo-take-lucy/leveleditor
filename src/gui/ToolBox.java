package gui;

import composition.CompType;
import files.FileManager;
import handler.TerminalHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

public class ToolBox extends JPanel {

    private final int BUTTON_SIZE = 64;

    private JPanel configPanel;
    private ActionListener configListener;

    private JPanel terrainPanel;
    private ActionListener brushListener;

    private JPanel activePanel;

    private TerminalHandler handler;

    private BufferedImage spriteSheet;
    private Hashtable<String, BufferedImage> icons;

    private GridLayout grid;
    private GridBagConstraints c;
    //this.setLayout(grid);

    public ToolBox(TerminalHandler handler) {
        super();

        this.handler = handler;

        //GridLayout grid = new GridLayout(10, 2);
        //grid.s
        //this.setPreferredSize();


        initMainPanel();
        initIcons();

        //JButton coolButt = new JButton();
        //this.add(coolButt);

    }

    private void initIcons() {

        try {
            spriteSheet = ImageIO.read(new File("source/core/assets/configs/ragEditAssets/spritesheet.png"));
        } catch (Exception e) {
            System.err.print("Couldn't load spritesheet. Check: in configs/ragEditAssets/spritesheet.png\n");
        }

        icons = new Hashtable<>();

        for (ConfigButtons cB : ConfigButtons.values()) {

            SubSpr sS = cB.getSubSprite();

            int x = sS.x;
            int y = sS.y;

            Image sprite = spriteSheet.getSubimage(x, y, 8, 8)
                    .getScaledInstance((int) (BUTTON_SIZE * 0.75), (int) (BUTTON_SIZE * 0.75), Image.SCALE_FAST);
            Icon buttonIcon = new ImageIcon(sprite);

            cB.getButton().setIcon(buttonIcon);
            cB.getButton().setBorderPainted(false);

            //subSprites.put(cT, sprite);
        }

        for (BrushButtons bB : BrushButtons.values()) {

            SubSpr sS = bB.getSubSprite();

            int x = sS.x;
            int y = sS.y;

            Image sprite = spriteSheet.getSubimage(x, y, 8, 8)
                    .getScaledInstance((int) (BUTTON_SIZE * 0.75), (int) (BUTTON_SIZE * 0.75), Image.SCALE_FAST);
            Icon buttonIcon = new ImageIcon(sprite);

            bB.getButton().setIcon(buttonIcon);
            bB.getButton().setBorderPainted(false);

            //subSprites.put(cT, sprite);
        }

        for (ActiveEntityButtons aB : ActiveEntityButtons.values()) {

            SubSpr sS = aB.getSubSprite();

            int x = sS.x;
            int y = sS.y;

            Image sprite = spriteSheet.getSubimage(x, y, 8, 8)
                    .getScaledInstance((int) (BUTTON_SIZE * 0.75), (int) (BUTTON_SIZE * 0.75), Image.SCALE_FAST);
            Icon buttonIcon = new ImageIcon(sprite);

            aB.getButton().setIcon(buttonIcon);
            aB.getButton().setBorderPainted(false);

            //subSprites.put(cT, sprite);
        }

    }

    private void initMainPanel() {

        grid = new GridLayout(4, 0);
        this.setLayout(grid);

        c = new GridBagConstraints();

        configPanel = initConfigPanel();
        terrainPanel = initTerrainPanel();
        activePanel = initActiveEntityPanel();

        c.fill = GridBagConstraints.NONE;

        this.add(configPanel, c);

        //c.gridy = 1;
        this.add(terrainPanel, c);

        //c.gridy = 2;
        this.add(activePanel, c);

        //grid.layoutContainer(this);

        //this.validate();

    }

    private JPanel initConfigPanel() {

        // buttons: (3 x 2)
        // new, save, load,
        // undo, redo

        configPanel = new JPanel(new GridBagLayout());
        configPanel.setPreferredSize(new Dimension(BUTTON_SIZE * 3, BUTTON_SIZE * 3));
        GridBagConstraints c = new GridBagConstraints();

        //Make the Label
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
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
                    case ">toggleTerm":
                }

                /*NEW(new JButton(), ">new"), // sets brush to null
                        SAVE(new JButton(), ">save"),
                        LOAD(new JButton(), ">load"),
                        UNDO(new JButton(), ">undo"),
                        REDO(new JButton(), ">redo"),
                        TOGGLETERM(new JButton(), ">toggleTerm");*/

            }
        };

        c.gridwidth = 1;
        c.gridheight = 1;

        //Make the Buttons and Put Them In
        int i = 3;
        for (ConfigButtons b : ConfigButtons.values()) {
            int y = i / 3;
            int x = i % 3;

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
        c.gridwidth = 3;
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
        int i = 3;
        for (BrushButtons b : BrushButtons.values()) {
            int y = i / 3;
            int x = i % 3;

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
        c.gridwidth = 3;
        c.gridheight = 1;

        activePanel.add(new JLabel("Enemies"), c);

        c.gridwidth = 1;
        c.gridheight = 1;

        //Make the Buttons and Put Them In
        int i = 3;
        for (ActiveEntityButtons b : ActiveEntityButtons.values()) {
            int y = i / 3;
            int x = i % 3;

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

enum ConfigButtons {

    // in the () can make icon

    // maybe also save values relating to the coordinates in this?
    NEW(new JButton(), ">new", SubSpr.NEW), // sets brush to null
    SAVE(new JButton(), ">save", SubSpr.SAVE),
    LOAD(new JButton(), ">load", SubSpr.LOAD),
    UNDO(new JButton(), ">undo", SubSpr.NULL),
    REDO(new JButton(), ">redo", SubSpr.NULL),
    TOGGLETERM(new JButton(), ">toggleTerm", SubSpr.NULL);

    private JButton butt;
    private String setting;
    private SubSpr subSprite;

    ConfigButtons(JButton button, String configSetting, SubSpr ss) {

        butt = button;
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

enum BrushButtons {

    // in the () can make icon

    // maybe also save values relating to the coordinates in this?
    ERASER(new JButton(), "b n", SubSpr.ERASE), // sets brush to null
    FLOOR(new JButton(), "b f", SubSpr.FLOOR),
    PLATFORM(new JButton(), "b p", SubSpr.PLATFORM),
    ROCKS(new JButton(), "b r", SubSpr.ROCK),
    SPIKES(new JButton(), "b v", SubSpr.SPIKES);

    private JButton butt;
    private String setting;
    private SubSpr subSprite;

    BrushButtons(JButton button, String brushSetting, SubSpr ss) {

        butt = button;
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

enum ActiveEntityButtons {

    // in the () can make icon

    // maybe also save values relating to the coordinates in this?
    DELETE(new JButton(), "delete", SubSpr.DELETE),
    SKELETON(new JButton(), "b s", SubSpr.SKELETON),
    WOLF(new JButton(), "b w", SubSpr.WOLF);

    private JButton butt;
    private String setting;
    private SubSpr subSprite;

    ActiveEntityButtons(JButton button, String brushSetting, SubSpr ss) {

        butt = button;
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

enum ZoomScrollButtons {

        // in the () can make icon

    // maybe also save values relating to the coordinates in this?
    DELETE(new JButton(), "delete", SubSpr.DELETE),
    SKELETON(new JButton(), "b s", SubSpr.SKELETON),
    WOLF(new JButton(), "b w", SubSpr.WOLF);

    private JButton butt;
    private String setting;
    private SubSpr subSprite;

    ZoomScrollButtons(JButton button, String brushSetting, SubSpr ss) {

        butt = button;
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
