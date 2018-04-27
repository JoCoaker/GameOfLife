import javax.swing.*;
import java.awt.*;

public class Cell {
    private boolean alive = false;
    private JPanel p;
    private int x;
    private int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        p = new JPanel();
        p.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        p.setBackground(Color.WHITE);
        p.setName(x + ";" + y);
    }

    public Cell(int x, int y, boolean alive) {
        this(x, y);
        this.alive = alive;
        p.setBackground(alive ? Color.BLACK : Color.WHITE);
    }

    /*
     Getter und Setter
     */

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
        p.setBackground(this.alive ? Color.BLACK : Color.WHITE);
    }

    public JPanel draw() {
        return p;
    }

    public Cell clone() {
        return new Cell(this.x, this.y, this.alive);
    }
}
