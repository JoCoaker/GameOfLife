import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Cell implements Serializable {
    private boolean alive;
    private transient JPanel p;
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

    public void setAlive(boolean alive) {
        this.alive = alive;
        p.setBackground(this.alive ? Color.BLACK : Color.WHITE);
    }

    public JPanel draw() {
        if (p == null) {
            p = new JPanel();
            p.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.25f), Color.BLACK));
            p.setBackground(alive ? Color.BLACK : Color.WHITE);
            p.setName(x + ";" + y);
        }
        return p;
    }

    public Cell clone() {
        return new Cell(this.x, this.y, this.alive);
    }
}
