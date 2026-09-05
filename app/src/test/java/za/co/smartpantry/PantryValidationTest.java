package za.co.smartpantry;

import org.junit.Test;
import za.co.smartpantry.data.PantryValidation;
import static org.junit.Assert.*;

public class PantryValidationTest {
    @Test public void namesMustHaveContent() {
        assertNotNull(PantryValidation.nameError("  "));
        assertNull(PantryValidation.nameError("  Tomato  "));
    }
    @Test public void quantitiesMustBePositiveFiniteDecimals() {
        for (String value : new String[]{"", "0", "-2", "NaN", "Infinity", "1e5", "1,5", "0.0000001", "1000000001"})
            assertNotNull(value, PantryValidation.quantityError(value));
        for (String value : new String[]{"0.1", "2", " 1.25 ", "0.000001", "1000000000"})
            assertNull(value, PantryValidation.quantityError(value));
    }
    @Test public void datesAreOptionalAndStrict() {
        assertNull(PantryValidation.expiryError(""));
        assertNull(PantryValidation.expiryError("2028-02-29"));
        assertNotNull(PantryValidation.expiryError("2026-02-29"));
        assertNotNull(PantryValidation.expiryError("2026-13-01"));
        assertNotNull(PantryValidation.expiryError("26-9-1"));
    }
}
