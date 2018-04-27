import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class GridView extends JInternalFrame implements MouseListener, ActionListener {

    private GridModel gridModel;

    private int height = 10;
    private int width = 10;
    private int speed = 250;
    private JCheckBoxMenuItem[] speedMenuItems;
    private JCheckBoxMenuItem[] sizeMenuItems;


    public GridView() {
        setClosable(true);
        setResizable(true);
        setIconifiable(true);
        setMaximizable(true);

        // Erstelle ein Menubar und füge es dem Fenster hinzu.
        JMenuBar mb1 = new JMenuBar();
        JMenu jMenuMode = new JMenu("Modus");
        JMenu jMenuFile = new JMenu("Datei");

        JMenuItem jMenuItemStart = new JMenuItem("Start");
        JMenuItem jMenuItemStop = new JMenuItem("Stop");
        JMenuItem jMenuItemSpeed = new JMenu("Geschwindigkeit");
        JMenuItem jMenuItemSize = new JMenu("Groeße");

        JMenuItem jMenuItemFileSave = new JMenuItem("Speichern...");
        JMenuItem jMenuItemFileOpen = new JMenuItem("Oeffnen");

        jMenuItemStart.setActionCommand("START");
        jMenuItemStop.setActionCommand("STOP");
        jMenuItemFileSave.setActionCommand("SAVE");
        jMenuItemFileOpen.setActionCommand("OPEN");

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

        jMenuMode.add(jMenuItemStart);
        jMenuMode.add(jMenuItemStop);
        jMenuMode.add(jMenuItemSpeed);
        jMenuMode.add(jMenuItemSize);
        jMenuFile.add(jMenuItemFileSave);
        jMenuFile.add(jMenuItemFileOpen);

        mb1.add(jMenuMode);
        mb1.add(jMenuFile);
        setJMenuBar(mb1);

        // Create Model and add to screen
        gridModel = new GridModel(width, height, speed);
        this.draw();
        gridModel.start();
    }

    private void draw() {
        getContentPane().removeAll();
        getContentPane().setLayout(new GridLayout(height, width));

        Cell[][] cells = gridModel.getCells();
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                JPanel p = cells[x][y].draw();
                p.addMouseListener(this);
                getContentPane().add(p);
            }
        }
        repaint();
        revalidate();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        JPanel p = (JPanel) e.getSource();
        String[] s = p.getName().split(";");
        if (s.length > 1) {
            int x = Integer.valueOf(s[0]);
            int y = Integer.valueOf(s[1]);

            gridModel.updateAlive(x, y);
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

    }

    @Override
    public void mouseExited(MouseEvent e) {

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
                for (int i = 0; i < this.sizeMenuItems.length; i++) {
                    this.sizeMenuItems[i].setState(false);
                }

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
                this.gridModel.setSize(width, height);
                draw();
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
                        this.height = this.gridModel.getHeight();
                        this.width = this.gridModel.getWidth();

                        for (int i = 0; i < this.speedMenuItems.length; i++) {
                            if (speedMenuItems[i].getText().equals((double)this.gridModel.getSpeed() / 1000D + "x"))
                                this.speedMenuItems[i].setState(true);
                                else
                            this.speedMenuItems[i].setState(false);
                        }
                        for (int i = 0; i < this.sizeMenuItems.length; i++) {
                            if (sizeMenuItems[i].getText().equals(this.gridModel.getWidth() + "x" + this.gridModel.getHeight()))
                                this.sizeMenuItems[i].setState(true);
                            else
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
}
