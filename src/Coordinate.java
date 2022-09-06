import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Class for coordinates on the field
 */
public class Coordinate {
    protected String coordinateString;
    protected int row;
    protected int column;

    /**
     * Tests the coordinate string from the player input.
     * @param coordinateString String from the input
     * @return false if String is null, is too long or short or if it is not a letter or digit
     * on the field
     */
    public static boolean validCoordinateString(String coordinateString) {
        if (coordinateString == null) {
            System.out.println("Error! Invalid coordinates.");
            return false;
        }

        //remove white space
        coordinateString = coordinateString.trim();

        //check length
        if (coordinateString.length() < 2 || coordinateString.length() > 3) {
            System.out.println("Error! Invalid coordinates.");
            return false;
        }

        try (Reader reader = new StringReader(coordinateString.toUpperCase())) {
            //get and test letter
            int numberLetter = reader.read();

            //test
            if (numberLetter == - 1) {
                throw new IOException();
            }

            //working with ASCII code
            // 'A' = 65, 'J' = 74
            if (numberLetter < 65 || numberLetter > 74) {
                System.out.println("Error! Invalid coordinates.");
                return false;
            }

            //get and test digit
            int numberDigit = reader.read();

            //test
            if (numberDigit == - 1) {
                throw new IOException();
            }

            //working with ASCII code
            // '1' = 49, '9' = 57
            if (numberDigit < 49 || numberDigit > 57) {
                System.out.println("Error! Invalid coordinates.");
                return false;
            }

            //Check if it is 10 -> '1' = 49, '0' = 48
            int numberDigit2 = reader.read();
            if (numberDigit2 != - 1) {
                if (numberDigit != 49 || numberDigit2 != 48) {
                    System.out.println("Error! Invalid coordinates.");
                    return false;
                }
            }

            //every case was checked
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Something is wrong with the coordinate string or reader.");
        }
    }

    /**
     * Converts coordinateString to a real coordinate
     * The String has already been tested. It is guaranteed that it is correct
     */
    void convertString() {
        try (Reader reader = new StringReader(this.coordinateString.toUpperCase())) {
            //get letter
            int numberLetter = reader.read();

            //working with ASCII code
            // 'A' = 65, 'J' = 74
            this.row = numberLetter - 65; //->A: row = 65-64 = 1

            //get digit
            int numberDigit = reader.read();

            //working with ASCII code
            // '1' = 49, '9' = 57
            this.column = numberDigit - 49;

            //check if the digit coordinate is 10
            if (reader.read() != -1) {
                this.column = 9;
            }
        } catch (IOException e) {
            throw new RuntimeException("Something wrong with string reader.");
        }
    }

    public Coordinate(String coordinateString) {
        if (coordinateString == null) {
            throw new RuntimeException("Coordinate string is null");
        }

        this.coordinateString = coordinateString.trim();
        convertString();
    }

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
        coordinateString = "";
    }
}

