import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Random;

public class ComputerPlayer {
    // 0 - fog, 2 - hit, 3 - miss
    Battlefield field;
    Coordinate aShip;
    Coordinate secondShipField;
    boolean foundShip;
    boolean haveSecondField;


    public ComputerPlayer(Battlefield field) {
        this.field = field;
        foundShip = false;
        haveSecondField = false;
    }

    /**
     * should be called when a ship is hit at (@coordinate) and we already have two coordinates of the ship
     * @param coordinate
     */
    private void updateCoordinates(Coordinate coordinate) {
        if (aShip.row == secondShipField.row) {
            if (coordinate.row != aShip.row) {
                throw new RuntimeException("The new coordinate does not belong to the same ship.");
            }
            if (aShip.column < secondShipField.column) {
                if (coordinate.column < aShip.column) {
                    aShip = coordinate;
                } else if (secondShipField.column < coordinate.column) {
                    secondShipField = coordinate;
                }
            } else {
                if (coordinate.column < secondShipField.column) {
                    secondShipField = coordinate;
                } else if (aShip.column < coordinate.column) {
                    aShip = coordinate;
                }
            }
        } else if (aShip.column == secondShipField.column) {
            if (coordinate.column != aShip.column) {
                throw new RuntimeException("The new coordinate does not belong to the same ship.");
            }
            if (aShip.row < secondShipField.row) {
                if (coordinate.row < aShip.row) {
                    aShip = coordinate;
                } else if (secondShipField.row < coordinate.row) {
                    secondShipField = coordinate;
                }
            } else {
                if (coordinate.row < secondShipField.row) {
                    secondShipField = coordinate;
                } else if (aShip.row < coordinate.row) {
                    aShip = coordinate;
                }
            }
        } else {
            throw new RuntimeException("The coordinates of ComputerPlayer do not belong to the same ship.");
        }
    }

    /**
     * must be called when a ship was hit and the ship was not sunk
     * @param coordinate coordinate that was shot
     */
    public void shipHit(Coordinate coordinate) {
        // check if this is the second coordinated
        if (foundShip && !haveSecondField) {
            haveSecondField = true;
            secondShipField = coordinate;
        } else if (!foundShip) {
            foundShip = true;
            aShip = coordinate;
        } else if (foundShip && haveSecondField) {
            // we already have 2 coordinates
            // the coordinates must be updated so that they represent the end of the ship (that is known)
            updateCoordinates(coordinate);
        }
    }

    /**
     * must be called when the current target ship was destroyed. That way the search for a new target begins
     */
    public void shipSunk() {
        foundShip = false;
        haveSecondField = false;
    }

    private Coordinate convertNumberToCoordinate(int number) {
        if (number < 0 || number > 99) {
            throw new InputMismatchException("Given an invalid field number.");
        }

        int row = number / 10;
        int column = number % 10;
        return new Coordinate(row, column);
    }

    /**
     * returns the number of adjacent fields that have already been hit
     * @return number
     */
    private int numberOfOpenAdjacentFields(Coordinate coordinate) {
        int count = 0;
        // look over and under
        for (int i = -1; i < 2; i += 2) {
            if (coordinate.row + i < 10 && coordinate.row + i >= 0) {
                int value = field.fieldAt(new Coordinate(coordinate.row + i, coordinate.column));
                if (value == 2 || value == 3) {
                    count++;
                }
            }
        }
        // look left and right
        for (int i = -1; i < 2; i+= 2) {
            if (coordinate.column + i < 10 && coordinate.column + i >= 0) {
                int value = field.fieldAt(new Coordinate(coordinate.row, coordinate.column + i));
                if (value == 2 || value == 3) {
                    count++;
                }
            }
        }

        return count;
    }

    private LinkedList<Coordinate> fieldsFindShip() {
        LinkedList<Coordinate> possibleCoordinates = new LinkedList<>();
        int numberOfField = 0;
        while (possibleCoordinates.isEmpty()) {
            for (int i = 0; i < 100; i++) {
                boolean adjacentOpenField = false;
                Coordinate coordinate = convertNumberToCoordinate(i);
                int value = field.fieldAt(coordinate);
                if (value == 2 || value == 3) {
                    //field has already been hit
                    continue;
                } if (numberOfOpenAdjacentFields(coordinate) <= numberOfField) {
                    possibleCoordinates.add(coordinate);
                }
            }

            numberOfField++;
        }

        // sort out fields that have already been hit
        possibleCoordinates.removeIf(coordinate -> field.fieldAt(coordinate) == 2 || field.fieldAt(coordinate) == 3);

        if (possibleCoordinates.isEmpty()) {
            throw new RuntimeException("No possible coordinates found with a foundShip == true.");
        }
        return possibleCoordinates;
    }

    /**
     * should be called when we have the position of a ship -> foundShip == true
     */
    private LinkedList<Coordinate> fieldsDestroyShip() {
        LinkedList<Coordinate> possibleCoordinates = new LinkedList<>();
        if (haveSecondField) {
            // if we have two coordinates of the ship, then we know in which column or row the ship is in
            if (aShip.column == secondShipField.column) {
                // field below
                if (aShip.row + 1 != secondShipField.row && aShip.row + 1 < 10) {
                    possibleCoordinates.add(new Coordinate(aShip.row + 1, aShip.column));
                } else if (secondShipField.row + 1 != aShip.row && secondShipField.row + 1 < 10) {
                    possibleCoordinates.add(new Coordinate(secondShipField.row + 1, aShip.column));
                }
                // field over
                if (aShip.row - 1 != secondShipField.row && aShip.row - 1 >= 0) {
                    possibleCoordinates.add(new Coordinate(aShip.row - 1, aShip.column));
                } else if (secondShipField.row - 1 != aShip.row && secondShipField.row - 1 >= 0) {
                    possibleCoordinates.add(new Coordinate(secondShipField.row - 1, aShip.column));
                }
            } else if (aShip.row == secondShipField.row) {
                // field to the right
                if (aShip.column + 1 != secondShipField.column && aShip.column + 1 < 10) {
                    possibleCoordinates.add(new Coordinate(aShip.row, aShip.column + 1));
                } else if (secondShipField.column + 1 != aShip.column && secondShipField.column + 1 < 10) {
                    possibleCoordinates.add(new Coordinate(secondShipField.row, secondShipField.column + 1));
                }
                // field the left
                if (aShip.column - 1 != secondShipField.column && aShip.column - 1 >= 0) {
                    possibleCoordinates.add(new Coordinate(aShip.row, aShip.column - 1));
                } else if (secondShipField.column - 1 != aShip.column && secondShipField.column - 1 >= 0) {
                    possibleCoordinates.add(new Coordinate(aShip.row, secondShipField.column - 1));
                }
            } else {
                throw new RuntimeException("secondCoordinate and aShip are not in the same row or column.");
            }
        } else {
            // if only on coordinate of a ship is known than fields in all 4 directions will be added
            // over
            if (aShip.row - 1 >= 0) {
                possibleCoordinates.add(new Coordinate(aShip.row - 1, aShip.column));
            }
            // below
            if (aShip.row + 1 < 10) {
                possibleCoordinates.add(new Coordinate(aShip.row + 1, aShip.column));
            }
            // to the right
            if (aShip.column + 1 < 10) {
                possibleCoordinates.add(new Coordinate(aShip.row, aShip.column + 1));
            }
            // to the left
            if (aShip.column - 1 >= 0) {
                possibleCoordinates.add(new Coordinate(aShip.row, aShip.column - 1));
            }
        }

        // sort out fields that have already been hit
        possibleCoordinates.removeIf(coordinate -> field.fieldAt(coordinate) == 2 || field.fieldAt(coordinate) == 3);

        if (possibleCoordinates.isEmpty()) {
            throw new RuntimeException("No possible coordinates found with a foundShip == true.");
        }
        return possibleCoordinates;
    }

    private LinkedList<Coordinate> possibleFields() {
        if (foundShip) {
            return fieldsDestroyShip();
        } else {
            return fieldsFindShip();
        }
    }

    public Coordinate nextTarget() {
        LinkedList<Coordinate> possibleFields = possibleFields();
        Random random = new Random();
        int index = random.nextInt(possibleFields.size());
        return possibleFields.get(index);
    }
}
