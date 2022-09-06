import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattlefieldTest {

    @Test
    void testNotDiagonalCoordinates() {
        Battlefield field = new Battlefield(1, 1);

        //coordinates diagonal
        Coordinate start = new Coordinate("B10");
        Coordinate end = new Coordinate("D7");
        assertFalse(Battlefield.notDiagonalCoordinates(start, end),
                "Invalid coordinates were checked as true.");

        //coordinates not diagonal
        start = new Coordinate("A6");
        end = new Coordinate("A10");
        assertTrue(Battlefield.notDiagonalCoordinates(start, end),
                "Valid coordinates were checked as false.");
    }

    @Test
    void testValidShip() {
        int length = 4;
        Battlefield field = new Battlefield(1, 1);

        //correct coordinates
        Coordinate start = new Coordinate("A1");
        Coordinate end = new Coordinate("D1");
        assertTrue(Battlefield.validShip(start, end, length),
                "Valid coordinates were checked as false.");

        //coordinates too short
        start = new Coordinate("A1");
        end = new Coordinate("C1");
        assertFalse(Battlefield.validShip(start, end, length),
                "Invalid coordinates were checked as true.");

        //coordinates too long
        start = new Coordinate("B10");
        end = new Coordinate("B4");
        assertFalse(Battlefield.validShip(start, end, length),
                "Invalid coordinates were checked as true.");
    }

    @Test
    void testDoesNotTouchShip() {
        Battlefield field = new Battlefield(1, 1);

        field.array[1][1] = 1;
        field.array[2][1] = 1;
        field.array[3][1] = 1;
        field.array[4][1] = 1;
        field.array[6][3] = 1;
        field.array[6][4] = 1;

        //Ship crosses another ship & start < end
        assertFalse(field.doesNotTouchShip(new Coordinate("B1"), new Coordinate("B4")),
                "Evaluated to true even though it crosses another ship.");
        //Ship is on another ship & end < start
        assertFalse(field.doesNotTouchShip(new Coordinate("G5"), new Coordinate("G4")),
                "Evaluated to true even though it is on another ship.");
        //Ship is placed correctly
        assertTrue(field.doesNotTouchShip(new Coordinate("H8"), new Coordinate("D8")),
                "Evaluated to false even though the ship was placed correctly.");
        //Ship touches another ship & start < end
        assertFalse(field.doesNotTouchShip(new Coordinate("E3"), new Coordinate("I3")),
                "Evaluated to true even though it touches another ship.");
        //Ship touches another ship & end < start
        assertFalse(field.doesNotTouchShip(new Coordinate("G7"), new Coordinate("G6")),
                "Evaluated to true even though it touches another ship.");
        //Ship touches another ship & next to edge
        assertFalse(field.doesNotTouchShip(new Coordinate("E1"), new Coordinate("H1")),
                "Evaluated to true even though it touches another ship.");
        //Ship is placed correctly & next to the edge
        assertTrue(field.doesNotTouchShip(new Coordinate("C10"), new Coordinate("A10")),
                "Evaluated to false even though the ship was placed correctly.");
    }

    @Test
    void testFillField() {
        Battlefield field = new Battlefield(1, 1);

        //horizontal ship & start < end
        field.fillField(new Coordinate("A1"), new Coordinate("A3"));
        assertEquals(1, field.array[0][0], "Ship was not placed");
        assertEquals(1, field.array[0][1], "Ship was not placed");
        assertEquals(1, field.array[0][2], "Ship was not placed");

        //horizontal ship & end < start
        field.fillField(new Coordinate("B10"), new Coordinate("B9"));
        assertEquals(1, field.array[1][9], "Ship was not placed");
        assertEquals(1, field.array[1][8], "Ship was not placed");

        //vertical ship & start < end
        field.fillField(new Coordinate("C5"), new Coordinate("F5"));
        assertEquals(1, field.array[2][4], "Ship was not placed");
        assertEquals(1, field.array[3][4], "Ship was not placed");
        assertEquals(1, field.array[4][4], "Ship was not placed");
        assertEquals(1, field.array[5][4], "Ship was not placed");

        //vertical ship & end < start
        field.fillField(new Coordinate("H8"), new Coordinate("G8"));
        assertEquals(1, field.array[6][7], "Ship was not placed");
        assertEquals(1, field.array[7][7], "Ship was not placed");
    }

    @Test
    void testPlaceShip() {
        Battlefield field = new Battlefield(1, 1);

        //valid placement & vertical
        assertTrue(field.placeShipPlayer1(new Coordinate("A3"),
                        new Coordinate("D3"), Ships_1.BATTLESHIP),
                "Valid placement was handled as invalid.");
        assertEquals(1, field.array[0][2], "Ship was not placed correctly.");
        assertEquals(1, field.array[1][2], "Ship was not placed correctly.");
        assertEquals(1, field.array[2][2], "Ship was not placed correctly.");
        assertEquals(1, field.array[3][2], "Ship was not placed correctly.");

        //valid placement & horizontal
        assertTrue(field.placeShipPlayer1(new Coordinate("A10"), new Coordinate("A9"),
                        Ships_1.DESTROYER), "Valid placement was handled as invalid.");
        assertEquals(1, field.array[0][9], "Ship was not placed correctly.");
        assertEquals(1, field.array[0][8], "Ship was not placed correctly.");

        //wrong length
        assertFalse(field.placeShipPlayer1(new Coordinate("I1"), new Coordinate("I7"),
                        Ships_1.AIRCRAFT_CARRIER), "Wrong length has not been detected.");
        assertEquals(0, field.array[8][0], "Invalid ship has still been placed.");
        assertEquals(0, field.array[8][1], "Invalid ship has still been placed.");
        assertEquals(0, field.array[8][2], "Invalid ship has still been placed.");
        assertEquals(0, field.array[8][3], "Invalid ship has still been placed.");
        assertEquals(0, field.array[8][4], "Invalid ship has still been placed.");
        assertEquals(0, field.array[8][5], "Invalid ship has still been placed.");
        assertEquals(0, field.array[8][6], "Invalid ship has still been placed.");

        //invalid placement - diagonal
        assertFalse(field.placeShipPlayer1(new Coordinate("J10"), new Coordinate("I8"),
                        Ships_1.CRUISER), "Diagonal ship has not been detected as invalid.");
        assertEquals(0, field.array[9][9], "Invalid ship has still been placed.");

        //invalid placement - touches another ship
        assertFalse(field.placeShipPlayer1(new Coordinate("A2"), new Coordinate("A1"),
                        Ships_1.DESTROYER), "Invalid placement has not been detected.");
        assertEquals(0, field.array[0][0], "Invalid ship has still been placed.");
    }

    @Test
    void testInitiateField() {

    }
}