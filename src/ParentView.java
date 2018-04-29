import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ParentView extends JFrame implements ActionListener {

    private JDesktopPane desk;
    public static final String DIR = "./saves";
    public static JFileChooser c;

    private int height = 10;
    private int width = 10;
    private int speed = 250;

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
        JMenu jMenuGame = new JMenu("Spiel");

        JMenuItem jMenuItemGameNew = new JMenuItem("Neues Spiel");
        JMenuItem jMenuItemGameExit = new JMenuItem("Schließen");

        jMenuItemGameNew.setActionCommand("NEW");
        jMenuItemGameExit.setActionCommand("EXIT");

        jMenuItemGameNew.addActionListener(this);
        jMenuItemGameExit.addActionListener(this);

        jMenuGame.add(jMenuItemGameNew);
        jMenuGame.add(jMenuItemGameExit);

        mb1.add(jMenuGame);
        setJMenuBar(mb1);

        // Ordner erstellen falls noetig
        File dir = new File(DIR);
        if (!dir.exists()) {
            try {
                dir.mkdir();
            } catch (Exception e) {
                System.err.println("Failed to create dir:\n\r" + e);
            }
        }
        // JFileChooser erstellen
        c = new JFileChooser(dir);
        c.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Save Files", "save");
        c.setFileFilter(filter);

        // Zeige das Fenster.
        setVisible(true);
    }

    public void addGrid() {
        addGrid(new GridModel(width, height, speed));
    }

    public void addGrid(GridModel gridModel) {
        GridView child = new GridView(gridModel, this);
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
        }

    }
}
