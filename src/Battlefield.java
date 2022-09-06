import java.util.Scanner;
import java.util.Random;

/**
 * The battlefield of the game
 */
public class Battlefield {
    //array represents field: 0 = "~", 1 = "O", 2 = "X", 3 = "M"
    int[][] array;
    int[][] fogArray;
    Scanner scanner;
    int shipsCellsLeft;
    // player = 0: computer, player = 1: first player...
    int player;
    // gameMode = 0: Player vs. Computer, gameMode = 1: Player vs. Player
    int gameMode;
    ComputerPlayer computer;

    public Battlefield(int player, int gameMode) {
        if (player != 1 && player != 2 && player != 0) {
            throw new RuntimeException("Wrong player id.");
        }
        this.gameMode = gameMode;
        this.array = new int[10][10];
        this.fogArray = new int[10][10];
        scanner = new Scanner(System.in);
        this.player = player;
        if (gameMode == 0) {
            computer = new ComputerPlayer(this);
        }
    }

    /**
     * Prints battlefield on string
     * @return full field
     */
    public String toString() {
        StringBuilder field = new StringBuilder("  1 2 3 4 5 6 7 8 9 10 \n");
        for (int i = 0; i < 10; i++) {
            //A = 65
            char letter = (char) (65 + i);
            field.append(letter).append(" ");

            for (int j = 0; j < 10; j++) {
                switch (this.array[i][j]) {
                    case 0 -> field.append("~ ");
                    case 1 -> field.append("O ");
                    case 2 -> field.append("X ");
                    case 3 -> field.append("M ");
                    default -> throw new RuntimeException("Something went wrong");
                }
            }

            if (i != 9) {
                field.append("\n");
            }
        }

        return field.toString();
    }

    public String foggyField() {
        StringBuilder field = new StringBuilder("  1 2 3 4 5 6 7 8 9 10 \n");
        for (int i = 0; i < 10; i++) {
            //A = 65
            char letter = (char) (65 + i);
            field.append(letter).append(" ");

            for (int j = 0; j < 10; j++) {
                switch (this.fogArray[i][j]) {
                    case 0 -> field.append("~ ");
                    case 1 -> field.append("O ");
                    case 2 -> field.append("X ");
                    case 3 -> field.append("M ");
                    default -> throw new RuntimeException("Something went wrong");
                }
            }

            if (i != 9) {
                field.append("\n");
            }
        }

        return field.toString();
    }

    /**
     * prints the situation for a game. prints field of player on the left and foggy field of enemy on the right
     * @param player
     * @param enemy
     * @return
     */
    public static String printSituation(Battlefield player, Battlefield enemy) {
        StringBuilder situation = new StringBuilder("Your field:             |  Enemy field:\n" +
                                                    "  1 2 3 4 5 6 7 8 9 10  |    1 2 3 4 5 6 7 8 9 10\n");
        for (int i = 0; i < 10; i++) {
            //A = 65
            char letter = (char) (65 + i);
            situation.append(letter).append(" ");

            for (int j = 0; j < 10; j++) {
                switch (player.array[i][j]) {
                    case 0 -> situation.append("~ ");
                    case 1 -> situation.append("O ");
                    case 2 -> situation.append("X ");
                    case 3 -> situation.append("M ");
                    default -> throw new RuntimeException("Something went wrong");
                }
            }
            situation.append("  |  ");

            situation.append(letter).append(" ");
            for (int j = 0; j < 10; j++) {
                switch (enemy.fogArray[i][j]) {
                    case 0 -> situation.append("~ ");
                    case 1 -> situation.append("O ");
                    case 2 -> situation.append("X ");
                    case 3 -> situation.append("M ");
                    default -> throw new RuntimeException("Something went wrong");
                }
            }

            if (i != 9) {
                situation.append("\n");
            }
        }

        return situation.toString();
    }

    /**
     * Waits until the player presses ENTER
     */
    public void waitNextPlayer() {
        scanner.nextLine();
    }

    private void initiateFieldRandom() {
        Random random = new Random();
        for (Ships_2 ship : Ships_2.values()) {
            shipsCellsLeft += ship.getSize();
            Coordinate start;
            Coordinate end;
            do {
                // produce start coordinate
                int startRow = random.nextInt(10);
                int startColumn = random.nextInt(10);
                start = new Coordinate(startRow, startColumn);

                // produce end coordinate
                int endColumn = 0;
                int endRow = 0;
                switch (random.nextInt(2)) {
                    case 0 -> {
                        // end coordinate in same row
                        endRow = startRow;
                        if (startColumn + (ship.size - 1) > 9) {
                            endColumn = startColumn - (ship.size - 1);
                        } else if (startColumn - (ship.size - 1) < 0) {
                            endColumn = startColumn + (ship.size - 1);
                        } else endColumn = switch (random.nextInt(2)) {
                            // you can put the end coordinate on the left and the right
                            case 0 ->
                                // end to the right
                                    startColumn + (ship.size - 1);
                            case 1 ->
                                // end to the left
                                    startColumn - (ship.size - 1);
                            default -> endColumn;
                        };
                    }
                    case 1 -> {
                        // end coordinate in same column
                        endColumn = startColumn;
                        if (startRow + (ship.size - 1) > 9) {
                            endRow = startRow - (ship.size - 1);
                        } else if (startRow - (ship.size - 1) < 0) {
                            endRow = startRow + (ship.size - 1);
                        } else endRow = switch (random.nextInt(2)) {
                            // you can put the end coordinate under and over the start coordinate
                            case 0 ->
                                // under
                                    startRow + (ship.size - 1);
                            case 1 ->
                                // over
                                    startRow - (ship.size - 1);
                            default -> endRow;
                        };
                    }
                }
                end = new Coordinate(endRow, endColumn);
            } while (!doesNotTouchShip(start, end));

            // we now got new valid coordinates of the ship
            placeShipPlayer2OrComputer(start, end, ship);
        }
    }

    private void initiateFieldPlayer2() {
        boolean wentWell = false;
        for (Ships_2 ship : Ships_2.values()) {
            //Writing the instruction
            System.out.println();
            ship.printInstructions();

            String startStr = null;
            String endStr = null;
            Coordinate start;
            Coordinate end;

            //loop until the input is correct and the ship was placed correctly
            boolean firstTry = true;
            do {
                if (!firstTry) {
                    System.out.println("Try again:");
                }

                String inputLine = scanner.nextLine();
                Scanner input = new Scanner(inputLine);

                if (input.hasNext()) {
                    startStr = input.next();
                }
                if (!Coordinate.validCoordinateString(startStr)) {
                    wentWell = false;
                } else {
                    if (input.hasNext()) {
                        endStr = input.next();
                    }
                    wentWell = Coordinate.validCoordinateString(endStr);
                }

                if (wentWell) {
                    start = new Coordinate(startStr);
                    end = new Coordinate(endStr);

                    wentWell = placeShipPlayer2OrComputer(start, end, ship);
                }
                input.close();

                firstTry = false;
            } while (!wentWell);

            this.shipsCellsLeft += ship.size;
        }
    }

    private void initiateFieldPlayer1() {
        boolean wentWell = false;
        for (Ships_1 ship : Ships_1.values()) {
            //Writing the instruction
            System.out.println();
            ship.printInstructions();

            String startStr = null;
            String endStr = null;
            Coordinate start;
            Coordinate end;

            //loop until the input is correct and the ship was placed correctly
            boolean firstTry = true;
            do {
                if (!firstTry) {
                    System.out.println("Try again:");
                }

                String inputLine = scanner.nextLine();
                Scanner input = new Scanner(inputLine);

                if (input.hasNext()) {
                    startStr = input.next();
                }
                if (!Coordinate.validCoordinateString(startStr)) {
                    wentWell = false;
                } else {
                    if (input.hasNext()) {
                        endStr = input.next();
                    }
                    wentWell = Coordinate.validCoordinateString(endStr);
                }

                if (wentWell) {
                    start = new Coordinate(startStr);
                    end = new Coordinate(endStr);

                    wentWell = placeShipPlayer1(start, end, ship);
                }
                input.close();

                firstTry = false;
            } while (!wentWell);

            this.shipsCellsLeft += ship.size;
        }
    }

    /**
     * Gives Instruction on placing the ships
     * Places the ship with the given coordinates over the input
     */
    public void initiateField() {
        if (player == 0) {
            initiateFieldRandom();
            return;
        }
        //place ships
        System.out.println("Player " + player + ", place your ships on the game field.\n");
        System.out.println(this);

        if (player == 1) {
            initiateFieldPlayer1();
        } else {
            initiateFieldPlayer2();
        }

        //Waiting process for the next player
        System.out.println("\nAll of your ships have been placed on the field!");
        if (gameMode == 0) {
            System.out.println("Press ENTER to start the game.");
        } else {
            if (player == 1) {
                System.out.println("Press ENTER to hide your field and let the other player place his ships.");
            } else {
                System.out.println("Press ENTER to start the game and shoot your first shot.");
            }
        }
        waitNextPlayer();
        //command to hide the field / clear the console
        System.out.print("\033[H\033[2J");
        //System.out.flush();
    }

    /**
     * Checks whether the ship is not placed diagonally
     * @param start coordinate
     * @param end coordinate
     * @return true if the ship won't be placed diagonally
     */
    public static boolean notDiagonalCoordinates(Coordinate start, Coordinate end) {
        //coordinates are not diagonal if the rows or the columns are the same
        return start.row == end.row || start.column == end.column;
    }

    /**
     * tests whether the two coordinates represent a ship of the given size
     */
    public static boolean validShip(Coordinate start, Coordinate end, int length) {
        //test length
        if (length < 2 || length > 5) {
            throw new RuntimeException("Test method was given a invalid ship length.");
        }

        if (start.row == end.row) {
            return start.column - end.column == length - 1 || end.column - start.column == length - 1;
        }

        if (start.column == end.column) {
            return start.row - end.row == length - 1|| end.row - start.row == length - 1;
        }

        return false;
    }

    /**
     * checks whether there already is a ship in between the coordinates
     * checks whether it touches another ship
     * @return true: everything is fine
     * false: no space
     */
    public boolean doesNotTouchShip(Coordinate start, Coordinate end) {
        //check whether to iterate over row or column
        if (start.row == end.row) {
            //check in which direction to iterate
            if (start.column < end.column) {
                for (int i = start.column; i <= end.column; i++) {
                    //check on field
                    if (this.array[start.row][i] == 1) {
                        return false;
                    }
                    //check upper field
                    if (start.row - 1 >= 0 && this.array[start.row - 1][i] == 1) {
                        return false;
                    }
                    //check right field
                    if (i + 1 < 10 && this.array[start.row][i + 1] == 1) {
                        return false;
                    }
                    //check left field
                    if (i - 1 >= 0 && this.array[start.row][i - 1] == 1) {
                        return false;
                    }
                    //check lower field
                    if (start.row + 1 < 10 && this.array[start.row + 1][i] == 1) {
                        return false;
                    }
                }
            } else {
                for (int i = end.column; i <= start.column; i++) {
                    if (this.array[start.row][i] == 1) {
                        return false;
                    }
                    //check upper field
                    if (start.row - 1 >= 0 && this.array[start.row - 1][i] == 1) {
                        return false;
                    }
                    //check right field
                    if (i + 1 < 10 && this.array[start.row][i + 1] == 1) {
                        return false;
                    }
                    //check left field
                    if (i - 1 >= 0 && this.array[start.row][i - 1] == 1) {
                        return false;
                    }
                    //check lower field
                    if (start.row + 1 < 10 && this.array[start.row + 1][i] == 1) {
                        return false;
                    }
                }
            }
            return true;
        } else if (start.column == end.column) {
            //check in which direction to iterate
            if (start.row < end.row) {
                for (int i = start.row; i <= end.row; i++) {
                    //check on field
                    if (this.array[i][start.column] == 1) {
                        return false;
                    }
                    //check upper field
                    if (i - 1 >= 0 && this.array[i - 1][start.column] == 1) {
                        return false;
                    }
                    //check right field
                    if (start.column + 1 < 10 && this.array[i][start.column + 1] == 1) {
                        return false;
                    }
                    //check left field
                    if (start.column - 1 >= 0 && this.array[i][start.column - 1] == 1) {
                        return false;
                    }
                    //check lower field
                    if (i + 1 < 10 && this.array[i + 1][start.column] == 1) {
                        return false;
                    }
                }
            } else {
                for (int i = end.row; i <= start.row; i++) {
                    //check on field
                    if (this.array[i][start.column] == 1) {
                        return false;
                    }
                    //check upper field
                    if (i - 1 >= 0 && this.array[i - 1][start.column] == 1) {
                        return false;
                    }
                    //check right field
                    if (start.column + 1 < 10 && this.array[i][start.column + 1] == 1) {
                        return false;
                    }
                    //check left field
                    if (start.column - 1 >= 0 && this.array[i][start.column - 1] == 1) {
                        return false;
                    }
                    //check lower field
                    if (i + 1 < 10 && this.array[i + 1][start.column] == 1) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            throw new RuntimeException("Invalid coordinates given.");
        }
    }

    public boolean validCoordinates(Coordinate start, Coordinate end, int length) {
        if (!notDiagonalCoordinates(start, end)) {
            System.out.println("Error! The coordinates are diagonal.");
            return false;
        }
        if (!validShip(start, end, length)) {
            System.out.println("Error! The coordinates represent a ship of the wrong length.");
            return false;
        }
        if (!this.doesNotTouchShip(start, end)) {
            System.out.println("Error! The ship touches another ship.");
            return false;
        }
        return true;
    }

    /**
     * Places the ship in the array after everything has been checked and it is safe to do so
     */
    public void fillField(Coordinate start, Coordinate end) {
        if (start.row == end.row) {
            if (start.column < end.column) {
                for (int i = start.column; i <= end.column; i++) {
                    this.array[start.row][i] = 1;
                }
            } else {
                for (int i = end.column; i <= start.column; i++) {
                    this.array[start.row][i] = 1;
                }
            }
        } else if (start.column == end.column) {
            if (start.row < end.row) {
                for (int i = start.row; i <= end.row; i++) {
                    this.array[i][start.column] = 1;
                }
            } else {
                for (int i = end.row; i <= start.row; i++) {
                    this.array[i][start.column] = 1;
                }
            }
        } else {
            throw new RuntimeException("Something went wrong. fillField was given invalid coordinates.");
        }
    }


    /**
     * Checks whether the given coordinates are a valid placement of the ship
     * Then places the ship
     * @param start start coordinate of the ship
     * @param end end coordinate of the ship
     * @param ship the ship to be placed
     * @return false: null coordinates or invalid placement
     * true: ship has been successfully placed
     */
    public boolean placeShipPlayer1(Coordinate start, Coordinate end, Ships_1 ship) {
        if (start == null || end == null) {
            throw new RuntimeException("placeShip was given null coordinates");
        }

        if (!validCoordinates(start, end, ship.size)) {
            return false;
        }

        fillField(start, end);
        ship.setCoordinate(start, end);
        System.out.println("\n" + this);
        return true;
    }

    public boolean placeShipPlayer2OrComputer(Coordinate start, Coordinate end, Ships_2 ship) {
        if (start == null || end == null) {
            throw new RuntimeException("placeShip was given null coordinates");
        }

        if (!validCoordinates(start, end, ship.size)) {
            return false;
        }

        fillField(start, end);
        ship.setCoordinate(start, end);
        if (player == 2) {
            System.out.println("\n" + this);
        }
        return true;
    }

    /**
     * Reads the field of the coordinate
     * @return returns value of the field
     */
    public int fieldAt(Coordinate coordinate) {
        return this.array[coordinate.row][coordinate.column];
    }

    /**
     * sets the field of the given coordinate to the given value
     */
    public void setField(Coordinate coordinate, int value) {
        this.array[coordinate.row][coordinate.column] = value;
    }

    public void setFoggyField(Coordinate coordinate, int value) {
        this.fogArray[coordinate.row][coordinate.column] = value;
    }
    /**
     * Method to actually shoot the shot
     * @return for the computer to recognize what has been hit
     * 0: game is over, 3: Miss, 2: Hit and ship was not destroyed, 4: Hit and ship was destroyed
     */
    private int shootShot(Coordinate coordinate) {
        switch (fieldAt(coordinate)) {
            case 0 -> {
                setField(coordinate, 3);
                setFoggyField(coordinate, 3);
                if (gameMode == 0 && player == 1) {
                    System.out.println("\nYour enemy missed!");
                } else {
                    System.out.println("\nYou missed!");
                }
                return 3;
            }
            case 1 -> {
                shipsCellsLeft--;
                setField(coordinate, 2);
                setFoggyField(coordinate, 2);
                if (shipsCellsLeft == 0) {
                    //check whether the game ends
                    if (gameMode == 0 && player == 1) {
                        System.out.println("\nI'm sorry captain. But our last ship was destroyed.");
                    } else {
                        System.out.println("\nYou sank the last ship. You won. Congratulations!");
                    }
                    return 0;
                }
                //search which ship this was
                boolean shipDestroyed = false;
                if (player == 1) {
                    for (Ships_1 ship : Ships_1.values()) {
                        if (ship.isHit(coordinate)) {
                            if (ship.isDestroyed()) {
                                if (gameMode == 0) {
                                    System.out.println("\nCaptain! Our " + ship.name + " was destroyed.");
                                } else {
                                    System.out.println("\nYou sank the " + ship.name + "!");
                                }
                                shipDestroyed = true;
                            } else {
                                if (gameMode == 0) {
                                    System.out.println("\nOne of our ships was hit!");
                                } else {
                                    System.out.println("\nYou hit a ship!");
                                }
                            }
                        }
                    }
                    if (shipDestroyed) return 4;
                    else return 2;
                } else {
                    for (Ships_2 ship : Ships_2.values()) {
                        if (ship.isHit(coordinate)) {
                            if (ship.isDestroyed()) {
                                System.out.println("\nYou sank the " + ship.name + "!");
                                shipDestroyed = true;
                            } else {
                                System.out.println("\nYou hit a ship!");
                            }
                        }
                    }
                    if (shipDestroyed) return 4;
                    else return 2;
                }
            }
            default -> throw new RuntimeException("Something went wrong.");
        }
    }

    /**
     * Method that is for shooting
     * reads input and shoots the shot
     * only shoots one shot
     */
    public void shooting() {
        //Loop until we have a valid Coordinate object
        Coordinate coordinate = null;
        boolean wentWell;
        do {
            String coordinateString = scanner.nextLine();
            wentWell = Coordinate.validCoordinateString(coordinateString);
            if (wentWell) {
                coordinate = new Coordinate(coordinateString);
                if (fieldAt(coordinate) == 2 || fieldAt(coordinate) == 3) {
                    wentWell = false;
                    System.out.println("Captain. You have already shot at that field. Try again:");
                }
            }
        } while (!wentWell);

        shootShot(coordinate);
        if (this.hasNotLost() && gameMode == 1) {
            System.out.println("Press ENTER and pass the move to another player and hide your field.");
            waitNextPlayer();
            //command to hide the field / clear the console
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    public void computerShooting() {
        Coordinate coordinate = computer.nextTarget();

        int value = shootShot(coordinate);

        if (value == 2) {
            computer.shipHit(coordinate);
        } else if (value == 4) {
            computer.shipSunk();
        }
    }

    public boolean hasNotLost() {
        return shipsCellsLeft > 0;
    }

    public static void main(String[] args) {
        Battlefield battlefield = new Battlefield(1, 1);
        //System.out.println(battlefield);
        battlefield.initiateField();
    }
}
