import java.io.Serializable;
import java.util.Observable;

/**
 * GridModel Klasse
 * <p>
 * Das ist das Model welches die Zellen beinhaltet. Größe und Geschwindigkeit
 * des Spielfeldes wird hier auch gespeichert.
 *
 * @author Felix Ruess (199261)
 * @author Lukas Reichert (199034)
 * @author Peter Tim Oliver Nauroth (198322)
 * @version 1.0.0
 */
public class GridModel extends Observable implements Runnable, Serializable {
    private Cell[][] cells;
    private int width;
    private int height;
    private int speed;
    private transient boolean run = false;

    /**
     * Konstruktor.
     *
     * @param width  {int}
     * @param height {int}
     * @param speed  {int}
     */
    public GridModel(int width, int height, int speed) {
        this.width = width;
        this.height = height;
        this.speed = speed;

        this.cells = new Cell[width][height];

        fillCells();
    }

    /**
     * Thread-Methode, die das updaten (Generationen) in einer Schleife ausfuehrt.
     */
    @Override
    public void run() {
        try {
            while (this.run) {
                this.update();
                // Warten.
                Thread.sleep(this.speed);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Updated die Zellen.
     */
    private void update() {
        boolean oneAlive = false;
        // Clone erstellen, damit das andere Array verändert werden kann.
        Cell[][] cellsClone = new Cell[width][height];
        for (int i = 0; i < cellsClone.length; i++) {
            for (int j = 0; j < cellsClone[i].length; j++) {
                cellsClone[i][j] = this.cells[i][j].clone();
            }
        }

        // Durch alle Zellen durch gehen und überpruefen, ob die Nachbarn Leben.
        for (int x = 0; x < cellsClone.length; x++) {
            for (int y = 0; y < cellsClone[x].length; y++) {

                // Anzahl lebender Nachbarzellen ermitteln.
                int count = getLivingNeighbors(x, y, cellsClone);
                boolean result = false;

                // Spielregeln anwenden.
                switch (count) {
                    case 2:
                        if (cellsClone[x][y].isAlive())
                            result = true;
                        break;
                    case 3:
                        result = true;
                        break;
                }

                // Ueberpruefen ob sich der Zustand der Zelle sich geaendert hat.
                if (result != cellsClone[x][y].isAlive()) {
                    // Aendern und Oberserver bescheid geben.
                    this.cells[x][y].setAlive(result);
                    setChanged();
                    notifyObservers(this.cells[x][y]);
                }
                // Ueberpruefen ob ueberhaupt noch Zellen leben.
                if (result)
                    oneAlive = true;
            }
        }
        // Wenn keine Zelle mehr lebt gehe aus dem Thread Loop heraus.
        if (!oneAlive)
            this.run = false;

    }

    /**
     * Ueberprueft wie viele Nachbarzellen noch leben.
     * Gibt die Anzahl der lebende Nachbarn zurueck.
     *
     * @param x     {int} X-Kordinate der Zelle
     * @param y     {int} Y-Kordinate der Zelle
     * @param cells {Cell[][]}
     * @return {int}
     */
    private int getLivingNeighbors(int x, int y, Cell[][] cells) {
        int count = 0;

        // Rechter Nachbar.
        if (x != width - 1) {
            if (cells[x + 1][y].isAlive())
                count++;
        } else {
            if (cells[0][y].isAlive())
                count++;
        }

        // Unten-Rechter Nachbar.
        if (x != width - 1 && y != height - 1) {
            if (cells[x + 1][y + 1].isAlive())
                count++;
        } else {
            if (x != width - 1) {
                if (cells[x + 1][0].isAlive())
                    count++;
            } else if (y != height - 1) {
                if (cells[0][y + 1].isAlive())
                    count++;
            } else {
                if (cells[0][0].isAlive())
                    count++;
            }
        }

        // Unterer Nachbar.
        if (y != height - 1) {
            if (cells[x][y + 1].isAlive())
                count++;
        } else {
            if (cells[x][0].isAlive())
                count++;
        }

        // Unten-Linker Nachbar.
        if (x != 0 && y != height - 1) {
            if (cells[x - 1][y + 1].isAlive())
                count++;
        } else {
            if (x != 0) {
                if (cells[x - 1][0].isAlive())
                    count++;
            } else if (y != height - 1) {
                if (cells[width - 1][y + 1].isAlive())
                    count++;
            } else {
                if (cells[width - 1][0].isAlive())
                    count++;
            }
        }

        // Linker Nachbar.
        if (x != 0) {
            if (cells[x - 1][y].isAlive())
                count++;
        } else {
            if (cells[width - 1][y].isAlive())
                count++;
        }

        // Oben-Linker Nachbar.
        if (x != 0 && y != 0) {
            if (cells[x - 1][y - 1].isAlive())
                count++;
        } else {
            if (x != 0) {
                if (cells[x - 1][height - 1].isAlive())
                    count++;
            } else if (y != 0) {
                if (cells[width - 1][y - 1].isAlive())
                    count++;
            } else {
                if (cells[width - 1][height - 1].isAlive())
                    count++;
            }
        }

        // Oberer Nachbar.
        if (y != 0) {
            if (cells[x][y - 1].isAlive())
                count++;
        } else {
            if (cells[x][height - 1].isAlive())
                count++;
        }

        // Oben-Rechter Nachbar.
        if (x != width - 1 && y != 0) {
            if (cells[x + 1][y - 1].isAlive())
                count++;
        } else {
            if (x != width - 1) {
                if (cells[x + 1][height - 1].isAlive())
                    count++;
            } else if (y != 0) {
                if (cells[0][y - 1].isAlive())
                    count++;
            } else {
                if (cells[0][height - 1].isAlive())
                    count++;
            }
        }

        return count;
    }

    /**
     * Fuellt das Zellen Array mit neuen Zellen.
     */
    private void fillCells() {
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                if (cells[x][y] == null)
                    cells[x][y] = new Cell(x, y);
            }
        }
    }

    /**
     * Updated den Status einer Zelle auf sein gegenteil.
     *
     * @param x {int}
     * @param y {int}
     * @return {boolean}
     */
    public boolean updateAlive(int x, int y) {
        return cells[x][y].setAlive(!cells[x][y].isAlive());
    }

    /**
     * Updated den Status einer Zelle.
     *
     * @param x     {int}
     * @param y     {int}
     * @param alive {boolean}
     * @return {boolean}
     */
    public boolean updateAlive(int x, int y, boolean alive) {
        return cells[x][y].setAlive(alive);
    }

    /**
     * Startet den Thread dieser Klasse.
     */
    public void start() {
        if (!this.run) {
            this.run = true;
            new Thread(this).start();
        }
    }

    /**
     * Endet den Thread dieser Klasse.
     */
    public void stop() {
        this.run = false;
    }

    /**
     * Aendert die groesse desSpielfeldes.
     *
     * @param width  {int}
     * @param height {int}
     */
    public void setSize(int width, int height) {
        this.height = height;
        this.width = width;
        Cell[][] cells = new Cell[width][height];
        // Alte Zellen uebernehmen.
        for (int x = 0; x < this.cells.length; x++) {
            for (int y = 0; y < this.cells[x].length; y++) {
                // Sicherstellen, dass die Zellen ueberhaupt noch angezeigt werden.
                if (x < this.width && y < this.height)
                    cells[x][y] = this.cells[x][y];
            }
        }
        // Zellen Array setzten.
        this.cells = cells;

        // Leere Zellen auffuellen
        fillCells();
        // Oberservers benachrichtigen.
        setChanged();
        notifyObservers(null);
    }

    /*
     Getter und Setter
     */

    public Cell[][] getCells() {
        return cells;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isRun() {
        return run;
    }
}
