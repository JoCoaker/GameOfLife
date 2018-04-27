import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GridView extends JInternalFrame implements MouseListener, Runnable {

    private Cell[][] cells;

    private int height = 10;
    private int width = 10;

    public GridView() {
        getContentPane().setLayout(new GridLayout(height, width));
        setClosable(true);
        setResizable(true);
        setIconifiable(true);
        setMaximizable(true);

        cells = new Cell[width][height];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell(i, j);
                JPanel p = cells[i][j].draw();
                p.addMouseListener(this);
                getContentPane().add(p);
            }
        }

        new Thread(this).start();
    }

    private void update() {
        Cell[][] cellsClone = new Cell[width][height];
        for (int i = 0; i < cellsClone.length; i++) {
            for (int j = 0; j < cellsClone[i].length; j++) {
                cellsClone[i][j] = cells[i][j].clone();
            }
        }

        for (int x = 0; x < cellsClone.length; x++) {
            for (int y = 0; y < cellsClone[x].length; y++) {

                // Check the cell's current state, and count its living neighbors.
                boolean living = cellsClone[x][y].isAlive();
                int count = getLivingNeighbors(x, y, cellsClone);
                boolean result = false;

                // Apply the rules and set the next state.
                if (living && count < 2)
                    result = false;
                if (living && (count == 2 || count == 3))
                    result = true;
                if (living && count > 3)
                    result = false;
                if (!living && count == 3)
                    result = true;

                cells[x][y].setAlive(result);
            }
        }

    }

    private int getLivingNeighbors(int x, int y, Cell[][] cells) {
        int count = 0;

        // Check cell on the right.
        if (x != width - 1) {
            if (cells[x + 1][y].isAlive())
                count++;
        } else {
            if (cells[0][y].isAlive())
                count++;
        }

        // Check cell on the bottom right.
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
            }else {
                if (cells[0][0].isAlive())
                    count++;
            }
        }

        // Check cell on the bottom.
        if (y != height - 1) {
            if (cells[x][y + 1].isAlive())
                count++;
        } else {
            if (cells[x][0].isAlive())
                count++;
        }

        // Check cell on the bottom left.
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
            }else {
                if (cells[width - 1][0].isAlive())
                    count++;
            }
        }

        // Check cell on the left.
        if (x != 0) {
            if (cells[x - 1][y].isAlive())
                count++;
        }else {
            if (cells[width - 1][y].isAlive())
                count++;
        }

        // Check cell on the top left.
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
            }else {
                if (cells[width - 1][height - 1].isAlive())
                    count++;
            }
        }

        // Check cell on the top.
        if (y != 0) {
            if (cells[x][y - 1].isAlive())
                count++;
        }else {
            if (cells[x][height - 1].isAlive())
                count++;
        }

        // Check cell on the top right.
        if (x != width - 1 && y != 0) {
            if (cells[x + 1][y - 1].isAlive())
                count++;
        }else {
            if (x != width - 1) {
                if (cells[x + 1][height - 1].isAlive())
                    count++;
            } else if (y != 0) {
                if (cells[0][y - 1].isAlive())
                    count++;
            }else {
                if (cells[0][height - 1].isAlive())
                    count++;
            }
        }

        return count;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
            System.out.println("Beginning...");
            while (true) {
                update();
                Thread.sleep(200);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        JPanel p = (JPanel) e.getSource();
        System.out.println("Click " + p.getName());
        String[] s = p.getName().split(";");
        if (s.length > 1) {
            int x = Integer.valueOf(s[0]);
            int y = Integer.valueOf(s[1]);

            cells[x][y].setAlive(!cells[x][y].isAlive());
            // p.setBackground(cells[x][y].isAlive() ? java.awt.Color.BLACK : java.awt.Color.WHITE);
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
}
