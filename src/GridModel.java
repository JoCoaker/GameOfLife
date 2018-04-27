import java.io.Serializable;

public class GridModel implements Runnable, Serializable {
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

                this.cells[x][y].setAlive(result);

                if (result)
                    oneAlive = true;
            }
        }

        if (!oneAlive)
            this.run = false;

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
                cells[x][y] = new Cell(x, y);
            }
        }
    }

    public void updateAlive(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height)
            cells[x][y].setAlive(!cells[x][y].isAlive());
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

    public void setSize(int width, int height) {
        if (run)
            run = false;

        this.height = height;
        this.width = width;
        this.cells = new Cell[width][height];

        fillCells();
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
