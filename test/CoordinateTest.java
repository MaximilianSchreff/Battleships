import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {
    @Test
    void testTestCoordinateString() {
        //null string
        String str1 = null;
        System.out.println("Expected output: Error! Invalid coordinates. \nActual: ");
        boolean str1Test = Coordinate.validCoordinateString(str1);
        assertFalse(str1Test, "Null string is not handled correctly");

        //string too short
        String str2 = "a";
        System.out.println("Expected output: Error! Invalid coordinates. \nActual: ");
        boolean str2Test = Coordinate.validCoordinateString(str2);
        assertFalse(str2Test, "String which is too short is not handled correctly");

        //string too long
        String str3 = "AB10";
        System.out.println("Expected output: Error! Invalid coordinates. \nActual: ");
        boolean str3Test = Coordinate.validCoordinateString(str3);
        assertFalse(str3Test, "String which is too long is not handled correctly");

        //string does not contain actual coordinates with whitespace
        String str4 = "M ";
        System.out.println("Expected output: Error! Invalid coordinates. \nActual: ");
        boolean str4Test = Coordinate.validCoordinateString(str4);
        assertFalse(str4Test, "String with not actual coordinates is not handled correctly");

        //coordinate letter too big
        String str5 = "K2";
        System.out.println("Expected output: Error! Invalid coordinates. \nActual: ");
        boolean str5Test = Coordinate.validCoordinateString(str5);
        assertFalse(str5Test, "String with a too big letter coordinate is not handled correctly");

        //coordinate number to big
        String str6 = "J11";
        System.out.println("Expected output: Error! Invalid coordinates. \nActual: ");
        boolean str6Test = Coordinate.validCoordinateString(str6);
        assertFalse(str6Test, "String with a too big digit coordinate is not handled correctly");

        //string correct
        String str7 = "a1";
        boolean str7Test = Coordinate.validCoordinateString(str7);
        assertTrue(str7Test, "Correct string is not identified as correct.");

        //string correct with 10
        String str8 = "H10";
        assertTrue(Coordinate.validCoordinateString(str8),
                "Correct string with digit 10 is not identified as correct.");
    }

    @Test
    void testConvertString() {
        String str1 = "a2";
        Coordinate cord1 = new Coordinate(str1);

        assertEquals(str1, cord1.coordinateString, "String is not saved in object.");
        //test coordinates
        assertEquals(0, cord1.row, "Row is not correct");
        assertEquals(1, cord1.column, "Column is not correct");

        String str2 = "D10";
        Coordinate cord2 = new Coordinate(str2);

        assertEquals(str2, cord2.coordinateString, "String is not saved in object.");
        //test coordinates
        assertEquals(3, cord2.row, "Row is not correct");
        assertEquals(9, cord2.column, "Column is not correct");
    }
}
