import java.io.Serializable;

/**
 * Cell Klasse
 *
 * Beinhaltet die Position und status einer Zelle.
 *
 * @author Felix Ruess (199261)
 * @author Lukas Reichert (199034)
 * @author Peter Tim Oliver Nauroth (198322)
 * @version 1.0.0
 */
public class Cell implements Serializable {
    private boolean alive;
    private int x;
    private int y;

    /**
     * Konstruktor.
     *
     * @param x {int}
     * @param y {int}
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.alive = false;
    }

    /**
     * Konstruktor.
     *
     * @param x {int}
     * @param y {int}
     * @param alive {boolean}
     */
    public Cell(int x, int y, boolean alive) {
        this(x, y);
        this.alive = alive;
    }

    /*
     Getter und Setter
     */

    public boolean isAlive() {
        return alive;
    }

    public boolean setAlive(boolean alive) {
        this.alive = alive;
        return alive;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Deep clone der Zelle.
     *
     * @return {Cell}
     */
    public Cell clone() {
        return new Cell(this.x, this.y, this.alive);
    }
}
