public enum Ships_1 {
    AIRCRAFT_CARRIER(5, "Enter the start and end coordinate of the Aircraft Carrier (5 cells):\n" +
            "Example input: F2 J2 or f2 j2", "aircraft carrier"),
    BATTLESHIP(4, "Enter the start and end coordinate of the Battleship (4 cells):", "battleship"),
    SUBMARINE(3, "Enter the start and end coordinate of the Submarine (3 cells):", "submarine"),
    CRUISER(3, "Enter the start and end coordinate of the Cruiser (3 cells):", "cruiser"),
    DESTROYER(2, "Enter the start and end coordinate of the Destroyer Carrier (2 cells):",
            "destroyer");

    final int size;
    int cellsLeft;
    Coordinate start;
    Coordinate end;
    final String name;
    final String instruction;

    Ships_1(int size, String instruction, String name) {
        this.name = name;
        this.size = size;
        this.cellsLeft = size;
        this.instruction = instruction;
    }

    /**
     * Sets start and end coordinates of the ship. The coordinates must have been properly tested.
     * The smaller on (closer to A1 is the start)
     */
    public void setCoordinate(Coordinate a, Coordinate b) {
        if (a.column == b.column) {
            if (a.row < b.row) {
                this.start = a;
                this.end = b;
            } else {
                this.start = b;
                this.end = a;
            }
        } else {
            if (a.column < b.column) {
                this.start = a;
                this.end = b;
            } else {
                this.start = b;
                this.end = a;
            }
        }
    }

    public Coordinate getStart() {
        return start;
    }

    public Coordinate getEnd() {
        return end;
    }

    public int getCellsLeft() {
        return cellsLeft;
    }

    public int getSize() {
        return size;
    }

    public void printInstructions() {
        System.out.println(instruction);
    }

    /**
     * Checks whether the shot hit the ship. If it did the cell count will be reduced.
     * @return true if the ship was hit and false if not.
     */
    public boolean isHit(Coordinate shot) {
        if (start.column == end.column) {
            if (shot.column == start.column && shot.row >= start.row && shot.row <= end.row) {
                cellsLeft--;
                return true;
            } else {
                return false;
            }
        } else {
            if (shot.row == start.row && shot.column >= start.column && shot.column <= end.column) {
                cellsLeft--;
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Says if the every cell of the ship is destroyed
     * @return true: every cell was hit.
     *         false: at least on cell was not hit.
     */
    public boolean isDestroyed() {
        return cellsLeft == 0;
    }
}
