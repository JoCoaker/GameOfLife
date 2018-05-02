import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * ParentView Klasse
 * <p>
 * Das Hauptfenster welches die Spielfelder in sich hat.
 *
 * @author Felix Ruess (199261)
 * @author Lukas Reichert (199034)
 * @author Peter Tim Oliver Nauroth (198322)
 * @version 1.0.0
 */
public class ParentView extends JFrame implements ActionListener {

    private JDesktopPane desk;
    public static final String DIR = "./saves";
    public static JFileChooser c;
    // Standardeinstellung fuer ein neues Spielfeld.
    private int height = 10;
    private int width = 10;
    private int speed = 250;

    /**
     * Konstruktor.
     */
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

    /**
     * Erstellt ein neues Spielfeld-Fenster mit einem neuen Model.
     */
    public void addGrid() {
        addGrid(new GridModel(width, height, speed));
    }

    /**
     * Erstellt ein neues Spielfeld-Fenster mit dem uebergebenem Model.
     *
     * @param gridModel {GridModel}
     */
    public void addGrid(GridModel gridModel) {
        GridView child = new GridView(gridModel, this);
        child.setLocation(getContentPane().getWidth() / 2 - 200, getContentPane().getHeight() / 2 - 200);
        child.setSize(400, 400);
        child.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        // Fenster in sich selbst einfuegen.
        desk.add(child);
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
