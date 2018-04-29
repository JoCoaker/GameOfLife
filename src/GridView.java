import javafx.scene.control.ColorPicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.Observable;
import java.util.Observer;

public class GridView extends JInternalFrame implements MouseListener, ActionListener, Observer {

    private GridModel gridModel;
    private ParentView parentView;

    private JCheckBoxMenuItem[] speedMenuItems;
    private JCheckBoxMenuItem[] sizeMenuItems;
    private JPanel[][] panels;

    private Color dead = Color.WHITE;
    private Color alive = Color.BLACK;

    private boolean drawing = false;
    private boolean drawingMode = false;
    private Figure addFigure = null;

    public GridView(GridModel gridModel, ParentView parentView) {
        this.gridModel = gridModel;
        this.parentView = parentView;

        this.gridModel.addObserver(this);

        setClosable(true);
        setResizable(true);
        setIconifiable(true);
        setMaximizable(true);

        // Erstelle ein Menubar und füge es dem Fenster hinzu.
        JMenuBar mb1 = new JMenuBar();
        JMenu jMenuMode = new JMenu("Modus");
        JMenu jMenuFile = new JMenu("Datei");
        JMenu jMenuWindow = new JMenu("Fenster");
        JMenu jMenuFigures = new JMenu("Figuren");

        JMenuItem jMenuItemStart = new JMenuItem("Start");
        JMenuItem jMenuItemStop = new JMenuItem("Stop");
        JMenuItem jMenuItemSpeed = new JMenu("Geschwindigkeit");
        JMenuItem jMenuItemSize = new JMenu("Groeße");
        JMenuItem jMenuItemDrawingMode = new JMenuItem("Malen");
        JMenuItem jMenuItemCopy = new JMenuItem("Kopieren");
        JMenuItem jMenuItemDuplicate = new JMenuItem("Duplizieren");
        JMenuItem jMenuItemRotate = new JMenuItem("Rotieren");
        JMenuItem jMenuItemColorAlive = new JMenuItem("Farbe lebende");
        JMenuItem jMenuItemColorDead = new JMenuItem("Farbe tote");
        JMenuItem jMenuItemFiguresGlider = new JMenuItem("Gleiter");
        JMenuItem jMenuItemFiguresGliderCanon = new JMenuItem("Gleiter Kanone");


        JMenuItem jMenuItemFileSave = new JMenuItem("Speichern...");
        JMenuItem jMenuItemFileOpen = new JMenuItem("Oeffnen");

        jMenuItemStart.setActionCommand("START");
        jMenuItemStop.setActionCommand("STOP");
        jMenuItemFileSave.setActionCommand("SAVE");
        jMenuItemFileOpen.setActionCommand("OPEN");
        jMenuItemDrawingMode.setActionCommand("DRAWING_MODE");
        jMenuItemCopy.setActionCommand("COPY");
        jMenuItemDuplicate.setActionCommand("DUPLICATE");
        jMenuItemRotate.setActionCommand("ROTATE");
        jMenuItemColorAlive.setActionCommand("COLOR_ALIVE");
        jMenuItemColorDead.setActionCommand("COLOR_DEAD");
        jMenuItemFiguresGlider.setActionCommand("FIGURES_GLIDER");
        jMenuItemFiguresGliderCanon.setActionCommand("FIGURES_GLIDER_CANON");

        speedMenuItems = new JCheckBoxMenuItem[]{
                new JCheckBoxMenuItem("1x"),
                new JCheckBoxMenuItem("0.5x"),
                new JCheckBoxMenuItem("0.25x"),
                new JCheckBoxMenuItem("0.1x"),
                new JCheckBoxMenuItem("0.05x"),
                new JCheckBoxMenuItem("0.01x")
        };

        sizeMenuItems = new JCheckBoxMenuItem[]{
                new JCheckBoxMenuItem("10x10"),
                new JCheckBoxMenuItem("25x25"),
                new JCheckBoxMenuItem("50x50"),
                new JCheckBoxMenuItem("100x100")
        };
        for (JCheckBoxMenuItem ch :
                speedMenuItems) {
            if (ch.getText().equals("0.25x"))
                ch.setState(true);
            ch.setActionCommand("SPEED");
            ch.addActionListener(this);
            jMenuItemSpeed.add(ch);
        }
        for (JCheckBoxMenuItem ch :
                sizeMenuItems) {
            if (ch.getText().equals("10x10"))
                ch.setState(true);
            ch.setActionCommand("SIZE");
            ch.addActionListener(this);
            jMenuItemSize.add(ch);
        }

        jMenuItemStart.addActionListener(this);
        jMenuItemStop.addActionListener(this);
        jMenuItemFileSave.addActionListener(this);
        jMenuItemFileOpen.addActionListener(this);
        jMenuItemDrawingMode.addActionListener(this);
        jMenuItemCopy.addActionListener(this);
        jMenuItemDuplicate.addActionListener(this);
        jMenuItemRotate.addActionListener(this);
        jMenuItemColorAlive.addActionListener(this);
        jMenuItemColorDead.addActionListener(this);
        jMenuItemFiguresGlider.addActionListener(this);
        jMenuItemFiguresGliderCanon.addActionListener(this);

        jMenuMode.add(jMenuItemStart);
        jMenuMode.add(jMenuItemStop);
        jMenuMode.add(jMenuItemSpeed);
        jMenuMode.add(jMenuItemSize);
        jMenuMode.add(jMenuItemDrawingMode);
        jMenuFile.add(jMenuItemFileSave);
        jMenuFile.add(jMenuItemFileOpen);
        jMenuWindow.add(jMenuItemCopy);
        jMenuWindow.add(jMenuItemDuplicate);
        jMenuWindow.add(jMenuItemRotate);
        jMenuWindow.add(jMenuItemColorAlive);
        jMenuWindow.add(jMenuItemColorDead);
        jMenuFigures.add(jMenuItemFiguresGlider);
        jMenuFigures.add(jMenuItemFiguresGliderCanon);

        mb1.add(jMenuFile);
        mb1.add(jMenuMode);
        mb1.add(jMenuWindow);
        mb1.add(jMenuFigures);
        setJMenuBar(mb1);

        this.draw();
        this.gridModel.start();
    }

    private void draw() {
        this.drawing = true;
        getContentPane().removeAll();

        getContentPane().setLayout(new GridLayout(gridModel.getWidth(), gridModel.getHeight()));
        panels = new JPanel[gridModel.getWidth()][gridModel.getHeight()];

        Cell[][] cells = gridModel.getCells();
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                panels[x][y] = new JPanel();
                panels[x][y].setBackground(cells[x][y].isAlive() ? alive : dead);
                panels[x][y].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.25f), Color.BLACK));
                panels[x][y].setName(x + ";" + y);
                panels[x][y].addMouseListener(this);
                getContentPane().add(panels[x][y]);
            }
        }
        repaint();
        revalidate();
        this.drawing = false;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
            draw();
            return;
        }
        if (arg instanceof Cell && !drawing) {
            Cell c = (Cell) arg;

            for (int x = 0; x < panels.length; x++) {
                for (int y = 0; y < panels[x].length; y++) {
                    if (panels[x][y].getName().equals(c.getX() + ";" + c.getY())) {
                        panels[x][y].setBackground(c.isAlive() ? alive : dead);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (addFigure == null)
            changeCellStatus((JPanel) e.getSource());
        else {
            JPanel p = (JPanel) e.getSource();
            String[] s = p.getName().split(";");
            if (s.length > 1) {
                int offsetX = Integer.valueOf(s[0]);
                int offsetY = Integer.valueOf(s[1]);
                boolean[][] mapping = addFigure.getMapping();
                for (int x = 0; x < mapping.length; x++) {
                    int tmpOffsetX = 0;
                    int tmpOffsetY = 0;
                    for (int y = 0; y < mapping[x].length; y++) {
                        if (tmpOffsetX + offsetX + x >= gridModel.getWidth()) {
                            tmpOffsetX = -1 * (offsetX + x);
                        }
                        if (tmpOffsetY + offsetY + y >= gridModel.getHeight()) {
                            tmpOffsetY = -1 * (offsetY + y);
                        }
                        panels[tmpOffsetX + offsetX + x][tmpOffsetY + offsetY + y].setBackground(gridModel.updateAlive(tmpOffsetX + offsetX + x, tmpOffsetY + offsetY + y, mapping[x][y]) ? alive : dead);
                    }
                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (drawingMode) {
            changeCellStatus((JPanel) e.getSource());
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void changeCellStatus(JPanel p) {
        String[] s = p.getName().split(";");
        if (s.length > 1) {
            int x = Integer.valueOf(s[0]);
            int y = Integer.valueOf(s[1]);

            p.setBackground(gridModel.updateAlive(x, y) ? alive : dead);
        }
    }

    public void rotate() {
        Cell[][] cells = gridModel.getCells();

        for (int x = 0; x < gridModel.getWidth() / 2; x++) {
            for (int y = x; y < gridModel.getHeight() - x - 1; y++) {
                JPanel tmp = panels[x][y];

                panels[x][y].setName(panels[y][gridModel.getWidth() - 1 - x].getName());
                panels[x][y].setBackground(Color.WHITE);

                panels[y][gridModel.getWidth() - 1 - x].setName(panels[gridModel.getWidth() - 1 - x][gridModel.getWidth() - 1 - y].getName());
                panels[y][gridModel.getWidth() - 1 - x].setBackground(Color.WHITE);

                panels[gridModel.getWidth() - 1 - x][gridModel.getWidth() - 1 - y].setName(panels[gridModel.getWidth() - 1 - y][x].getName());
                panels[gridModel.getWidth() - 1 - x][gridModel.getWidth() - 1 - y].setBackground(Color.WHITE);

                panels[gridModel.getWidth() - 1 - y][x].setName(tmp.getName());
                panels[gridModel.getWidth() - 1 - y][x].setBackground(Color.WHITE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "START":
                this.gridModel.start();
                break;
            case "STOP":
                this.gridModel.stop();
                break;
            case "DRAWING_MODE":
                this.drawingMode = !this.drawingMode;
                break;
            case "FIGURES_GLIDER":
                this.addFigure = Figure.GLIDER;
                break;
            case "FIGURES_GLIDER_CANON":
                this.addFigure = Figure.GLIDER_CANON;
                break;
            case "COPY":
                GridModel gm = new GridModel(this.gridModel.getWidth(), this.gridModel.getHeight(), this.gridModel.getSpeed());
                Cell[][] tmp = this.gridModel.getCells();

                Cell[][] cells = new Cell[this.gridModel.getWidth()][this.gridModel.getHeight()];
                for (int x = 0; x < tmp.length; x++) {
                    for (int y = 0; y < tmp[x].length; y++) {
                        cells[x][y] = tmp[x][y].clone();
                    }
                }

                gm.setCells(cells);
                this.parentView.addGrid(gm);
                break;
            case "DUPLICATE":
                this.parentView.addGrid(this.gridModel);
                break;
            case "ROTATE":
//                boolean wasRunning = this.gridModel.isRun();
//                if (wasRunning)
//                    this.gridModel.stop();

                rotate();

//                if (wasRunning)
//                    this.gridModel.start();
                break;
            case "COLOR_ALIVE":
                openColorPicker(alive, true);
                break;
            case "COLOR_DEAD":
                openColorPicker(dead, false);
                break;
            case "SPEED":
                for (int i = 0; i < this.speedMenuItems.length; i++) {
                    this.speedMenuItems[i].setState(false);
                }

                switch (((JCheckBoxMenuItem) e.getSource()).getText()) {
                    case "1x":
                        this.gridModel.setSpeed(1000);
                        break;
                    case "0.5x":
                        this.gridModel.setSpeed(500);
                        break;
                    case "0.25x":
                        this.gridModel.setSpeed(250);
                        break;
                    case "0.1x":
                        this.gridModel.setSpeed(100);
                        break;
                    case "0.05x":
                        this.gridModel.setSpeed(50);
                        break;
                    case "0.01x":
                        this.gridModel.setSpeed(10);
                        break;
                }
                break;
            case "SIZE":
                boolean wasRunning2 = this.gridModel.isRun();
                if (wasRunning2)
                    this.gridModel.stop();
                for (int i = 0; i < this.sizeMenuItems.length; i++) {
                    this.sizeMenuItems[i].setState(false);
                }

                int height = 10;
                int width = 10;

                switch (((JCheckBoxMenuItem) e.getSource()).getText()) {
                    case "10x10":
                        height = 10;
                        width = 10;
                        break;
                    case "25x25":
                        height = 25;
                        width = 25;
                        break;
                    case "50x50":
                        height = 50;
                        width = 50;
                        break;
                    case "100x100":
                        height = 100;
                        width = 100;
                        break;
                }
                if (height != 0)
                    this.gridModel.setSize(width, height);
                draw();
                if (wasRunning2)
                    this.gridModel.start();
                break;
            case "SAVE":
                this.gridModel.stop();
                // Spiel Speichern.
                // Dialog Fenster optionen setzten und oeffnen.
                ParentView.c.setDialogTitle("Spielstand Speichern unter...");
                ParentView.c.setSelectedFile(new File("gameOfLife.save"));
                ParentView.c.setApproveButtonText("speichern");
                int rVal = ParentView.c.showOpenDialog(this);
                // Schauen ob es erfolgreich war.
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        // Ueberpruefen ob es ein Pfad ist.
                        if (ParentView.c.getSelectedFile().isDirectory()) {
                            JOptionPane.showMessageDialog(this, "Kann Datei nicht als Verzeichnis Speichern.", "Fehler beim Speichern", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        String endPrefix = "";
                        // Ueberpruefen, ob es die Richtige Extension hat.
                        if (!ParentView.c.getSelectedFile().getName().substring(ParentView.c.getSelectedFile().getName().length() - 5).equals(".save"))
                            endPrefix = ".save";

                        // Datei speichern.
                        FileOutputStream fs = new FileOutputStream(ParentView.c.getSelectedFile().getAbsolutePath() + endPrefix);
                        ObjectOutputStream os = new ObjectOutputStream(fs);
                        os.writeObject(this.gridModel);
                        os.close();

                        fs.flush();
                        fs.close();

                        // Erfolgsnachricht anzeigen.
                        JOptionPane.showMessageDialog(this, "Datei erfolgreich gespeichert.", "Speichern erfolgreich", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // Fehlermeldung ausgeben.
                        JOptionPane.showMessageDialog(this, "Datei konnte nicht gespeichert werden.", "Fehler beim Speichern", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case "OPEN":
                this.gridModel.stop();
                // Spielstand oeffnen.
                // Dialog Fenster optionen setzten und oeffnen.
                ParentView.c.setDialogTitle("Spielstand Oeffnen");
                ParentView.c.setApproveButtonText("oeffnen");
                int reVal = ParentView.c.showOpenDialog(this);
                // Schauen ob es erfolgreich war.
                if (reVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        // Ueberpruefen ob es ein Pfad ist.
                        if (ParentView.c.getSelectedFile().isDirectory()) {
                            JOptionPane.showMessageDialog(this, "Verzeichnis kann nicht geoeffnet werden.", "Fehler beim oeffnen", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Datei oeffnen.
                        FileInputStream fs = new FileInputStream(ParentView.c.getSelectedFile().getAbsolutePath());
                        ObjectInputStream is = new ObjectInputStream(fs);
                        GridModel gridModel = (GridModel) is.readObject();
                        is.close();
                        fs.close();
                        // Instanzvariablen Setzten
                        this.gridModel = gridModel;

                        this.gridModel.addObserver(this);

                        for (int i = 0; i < this.speedMenuItems.length; i++) {
                            if (speedMenuItems[i].getText().equals((double) this.gridModel.getSpeed() / 1000D + "x")) {
                                this.speedMenuItems[i].setState(true);
                                i = this.speedMenuItems.length;
                            } else
                                this.speedMenuItems[i].setState(false);
                        }
                        for (int i = 0; i < this.sizeMenuItems.length; i++) {
                            if (sizeMenuItems[i].getText().equals(this.gridModel.getWidth() + "x" + this.gridModel.getHeight())) {
                                this.sizeMenuItems[i].setState(true);
                                i = this.sizeMenuItems.length;
                            } else
                                this.sizeMenuItems[i].setState(false);
                        }

                        setTitle(ParentView.c.getSelectedFile().getName().substring(0, ParentView.c.getSelectedFile().getName().length() - 5));
                        // Fenster neu Malen.
                        draw();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // Fehlermeldung ausgeben.
                        JOptionPane.showMessageDialog(this, "Datei konnte nicht geoeffnet werden.", "Fehler beim Oeffnen", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
        }

        if (e.getSource() instanceof JCheckBoxMenuItem)
            ((JCheckBoxMenuItem) e.getSource()).setState(true);
    }

    private void openColorPicker(Color color, final boolean alive) {
        Color c = JColorChooser.showDialog(this, alive ? "Lebende Farbe auswaehlen" : "Toten Farbe auswaehlen", color);
        if (c != color) {
            if (alive)
                this.alive = c;
            else
                this.dead = c;
            draw();
        }
    }
}
