import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Observable;

public class Cell implements Serializable {
    private boolean alive;
    private int x;
    private int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.alive = false;
    }

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

    public Cell clone() {
        return new Cell(this.x, this.y, this.alive);
    }
}
