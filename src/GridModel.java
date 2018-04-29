import java.io.Serializable;
import java.util.Observable;

public class GridModel extends Observable implements Runnable, Serializable {
    private Cell[][] cells;
    private int width;
    private int height;
    private int speed;
    private transient boolean run = false;

    public GridModel(int width, int height, int speed) {
        this.width = width;
        this.height = height;
        this.speed = speed;

        this.cells = new Cell[width][height];

        fillCells();
    }

    @Override
    public void run() {
        try {
            while (this.run) {
                this.update();
                Thread.sleep(this.speed);
            }
        } catch (Exception e) {

        }
    }

    private void update() {
        boolean oneAlive = false;
        Cell[][] cellsClone = new Cell[width][height];
        for (int i = 0; i < cellsClone.length; i++) {
            for (int j = 0; j < cellsClone[i].length; j++) {
                cellsClone[i][j] = this.cells[i][j].clone();
            }
        }

        for (int x = 0; x < cellsClone.length; x++) {
            for (int y = 0; y < cellsClone[x].length; y++) {

                // Check the cell's current state, and count its living neighbors.
                boolean living = cellsClone[x][y].isAlive();
                int count = getLivingwidtheighbors(x, y, cellsClone);
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

                if (result != living) {
                    this.cells[x][y].setAlive(result);
                    setChanged();
                    notifyObservers(this.cells[x][y]);
                }

                if (result)
                    oneAlive = true;
            }
        }

        if (!oneAlive)
            this.run = false;

    }

    private int getLivingwidtheighbors(int x, int y, Cell[][] cells) {
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
            } else {
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
            } else {
                if (cells[width - 1][0].isAlive())
                    count++;
            }
        }

        // Check cell on the left.
        if (x != 0) {
            if (cells[x - 1][y].isAlive())
                count++;
        } else {
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
            } else {
                if (cells[width - 1][height - 1].isAlive())
                    count++;
            }
        }

        // Check cell on the top.
        if (y != 0) {
            if (cells[x][y - 1].isAlive())
                count++;
        } else {
            if (cells[x][height - 1].isAlive())
                count++;
        }

        // Check cell on the top right.
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

    private void fillCells() {
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                if (cells[x][y] == null)
                    cells[x][y] = new Cell(x, y);
            }
        }
    }

    public boolean updateAlive(int x, int y) {
        return cells[x][y].setAlive(!cells[x][y].isAlive());
    }
    public boolean updateAlive(int x, int y, boolean alive) {
        return cells[x][y].setAlive(alive);
    }

    public void start() {
        if (!this.run) {
            this.run = true;
            new Thread(this).start();
        }
    }

    public void stop() {
        this.run = false;
    }

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

    public void setSize(int width, int height) {
        this.height = height;
        this.width = width;
        Cell[][] cells = new Cell[width][height];
        for (int x = 0; x < this.cells.length; x++) {
            for (int y = 0; y < this.cells[x].length; y++) {
                if (x < this.width && y < this.height)
                    cells[x][y] = this.cells[x][y];
            }
        }

        this.cells = cells;

        fillCells();
        setChanged();
        notifyObservers(null);
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
//public enum Figure {
//    GLIDER(new boolean[][]{{false, true, false}, {false, false, true}, {true, true, true}}),
//    GLIDER_CANON(new boolean[][]{
//            {false, false, false, false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true, false,false,false,false,false,false,false,false,false,false,false},
//            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false},
//            {false,false,false,false,false,false,false,false,false,false,false,false,true,true,false,false,false,false,false,false,true,true,false,false,false,false,false,false,false,false,false,false,false,false,true,true},
//            {false,false,false,false,false,false,false,false,false,false,false,true,false,false,false,true,false,false,false,false,true,true,false,false,false,false,false,false,false,false,false,false,false,false,true,true},
//            {true,true,false,false,false,false,false,false,false,false,true,false,false,false,false,false,true,false,false,false,true,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
//            {true,true,false,false,false,false,false,false,false,false,true,false,false,false,true,false,true,true,false,false,true,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false},
//            {false,false,false,false,false,false,false,false,false,false,true,false,false,false,false,false,true,false,false,false,false,false,false,false,true,false,false,false,false,false,false,false,false,false,false,false},
//            {false,false,false,false,false,false,false,false,false,false,false,true,false,false,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
//            {false,false,false,false,false,false,false,false,false,false,false,false,true,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false}
//    });
//
//    private final boolean[][] mapping; // Instanzvariablen
//
//    Figure(boolean[][] mapping) { // Konstruktor
//        this.mapping = mapping;
//    }
//
//    public boolean[][] getMapping() { // Methode
//        return mapping;
//    }
//}
