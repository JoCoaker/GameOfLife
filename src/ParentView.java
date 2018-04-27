import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ParentView extends JFrame implements ActionListener {

    private JDesktopPane desk;

    public ParentView() {
        // Standard Einstellungen einstellen.
        setTitle("Game Of Life");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension minimumSize = new Dimension(680, 500);
        setMinimumSize(minimumSize);
        setSize(minimumSize);

        // Desktop einstellen.
        desk = new JDesktopPane();
        desk.setDesktopManager(new DefaultDesktopManager());
        setContentPane(desk);
        setVisible(true);

        // Erstelle ein Menubar und füge es dem Fenster hinzu.
        JMenuBar mb1 = new JMenuBar();
        JMenu jMenuFile = new JMenu("Datei");
        JMenu jMenuGame = new JMenu("Spiel");

        JMenuItem jMenuItemGameNew = new JMenuItem("Neues Spiel");
        JMenuItem jMenuItemGameExit = new JMenuItem("Schließen");
        JMenuItem jMenuItemFileSave = new JMenuItem("Speichern...");
        JMenuItem jMenuItemFileOpen = new JMenuItem("Öffnen...");

        jMenuItemGameNew.setActionCommand("NEW");
        jMenuItemGameExit.setActionCommand("EXIT");
        jMenuItemFileSave.setActionCommand("SAVE");
        jMenuItemFileOpen.setActionCommand("OPEN");

        jMenuItemGameNew.addActionListener(this);
        jMenuItemGameExit.addActionListener(this);
        jMenuItemFileSave.addActionListener(this);
        jMenuItemFileOpen.addActionListener(this);

        jMenuGame.add(jMenuItemGameNew);
        jMenuGame.add(jMenuItemGameExit);
        jMenuFile.add(jMenuItemFileSave);
        jMenuFile.add(jMenuItemFileOpen);

        mb1.add(jMenuGame);
        mb1.add(jMenuFile);
        setJMenuBar(mb1);

        // Zeige das Fenster.
        setVisible(true);
    }

    private void addGrid() {
        GridView child = new GridView();
        child.setLocation(getContentPane().getWidth() / 2 - 200, getContentPane().getHeight() / 2 - 200); // Ort und
        child.setSize(400, 400); // Groesse setzen
        child.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE); // Schiessoperation
        desk.add(child); // Kindfenster einfuegen
        child.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Schauen welches Menü Item geklickt wurde.
        switch (e.getActionCommand()) {
            case "NEW":
                addGrid();
                break;
            case "EXIT":
                // Spiel schliessen.
                setVisible(false);
                dispose();
                break;
            case "SAVE":
//                // Spiel Speichern.
//                // Dialog Fenster optionen setzten und oeffnen.
//                c.setDialogTitle("Spielstand Speichern unter...");
//                c.setSelectedFile(new File("game.save"));
//                c.setApproveButtonText("speichern");
//                int rVal = c.showOpenDialog(Game.this);
//                // Schauen ob es erfolgreich war.
//                if (rVal == JFileChooser.APPROVE_OPTION) {
//                    try {
//                        // Ueberpruefen ob es ein Pfad ist.
//                        if (c.getSelectedFile().isDirectory()) {
//                            JOptionPane.showMessageDialog(Game.this, "Kann Datei nicht als Verzeichnis Speichern.", "Fehler beim Speichern", JOptionPane.ERROR_MESSAGE);
//                            return;
//                        }
//
//                        String endPrefix = "";
//                        // Ueberpruefen, ob es die Richtige Extension hat.
//                        if (!c.getSelectedFile().getName().substring(c.getSelectedFile().getName().length() - 5).equals(".save"))
//                            endPrefix = ".save";
//
//                        // Datei speichern.
//                        FileOutputStream fs = new FileOutputStream(c.getSelectedFile().getAbsolutePath() + endPrefix);
//                        ObjectOutputStream os = new ObjectOutputStream(fs);
//                        os.writeObject(Game.this.board);
//                        os.writeInt(Game.this.playerIndex);
//                        os.close();
//
//                        fs.flush();
//                        fs.close();
//
//                        // Erfolgsnachricht anzeigen.
//                        JOptionPane.showMessageDialog(Game.this, "Datei erfolgreich gespeichert.", "Speichern erfolgreich", JOptionPane.INFORMATION_MESSAGE);
//                        return;
//                    } catch (Exception ex) {
//                        // Fehlermeldung ausgeben.
//                        JOptionPane.showMessageDialog(Game.this, "Datei konnte nicht gespeichert werden.", "Fehler beim Speichern", JOptionPane.ERROR_MESSAGE);
//                        return;
//                    }
//                }

                break;
            case "OPEN":
//                // Spielstand oeffnen.
//                // Dialog Fenster optionen setzten und oeffnen.
//                c.setDialogTitle("Spielstand Öffnen");
//                c.setApproveButtonText("öffnen");
//                int reVal = c.showOpenDialog(Game.this);
//                // Schauen ob es erfolgreich war.
//                if (reVal == JFileChooser.APPROVE_OPTION) {
//                    try {
//                        // Ueberpruefen ob es ein Pfad ist.
//                        if (c.getSelectedFile().isDirectory()) {
//                            JOptionPane.showMessageDialog(Game.this, "Kann Datei nicht als Verzeichnis Speichern.", "Fehler beim Speichern", JOptionPane.ERROR_MESSAGE);
//                            return;
//                        }
//
//                        // Datei oeffnen.
//                        FileInputStream fs = new FileInputStream(c.getSelectedFile().getAbsolutePath());
//                        ObjectInputStream is = new ObjectInputStream(fs);
//                        Board board = (Board) is.readObject();
//                        int playerIndex = is.readInt();
//                        is.close();
//                        fs.close();
//                        // Derzeitiges Fenster schliessen.
//                        Game.this.setVisible(false);
//                        Game.this.dispose();
//                        // Neues Fenster oeffnen.
//                        new Game(board, playerIndex);
//                    } catch (Exception ex) {
//                        // Fehlermeldung ausgeben.
//                        JOptionPane.showMessageDialog(Game.this, "Datei konnte nicht geöffnet werden.", "Fehler beim Öffnen", JOptionPane.ERROR_MESSAGE);
//                    }
//                }
                break;
        }

    }
}
