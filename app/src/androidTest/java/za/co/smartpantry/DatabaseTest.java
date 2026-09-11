package za.co.smartpantry;

import android.content.Context;
import android.database.Cursor;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import za.co.smartpantry.data.DatabaseHelper;
import static org.junit.Assert.*;

public class DatabaseTest {
    private Context context;
    private DatabaseHelper helper;

    @Before public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.deleteDatabase("pantry_test.db");
        helper = new DatabaseHelper(context, "pantry_test.db");
    }

    @After public void tearDown() {
        helper.close();
        context.deleteDatabase("pantry_test.db");
    }

    @Test public void schemaAndForeignKeysExist() {
        try (Cursor tables = helper.getReadableDatabase().rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name IN "
                + "('pantry_items','recipes','recipe_ingredients')", null)) {
            assertEquals(3, tables.getCount());
        }
        try (Cursor keys = helper.getReadableDatabase().rawQuery("PRAGMA foreign_keys", null)) {
            assertTrue(keys.moveToFirst());
            assertEquals(1, keys.getInt(0));
        }
    }

    @Test public void crudSurvivesDatabaseReopen() {
        long id = helper.addPantryItem(" Tomato ", new BigDecimal("1.25"), "kg", "2026-09-10");
        assertEquals("Tomato", helper.getPantryItem(id).name);
        helper.close();
        helper = new DatabaseHelper(context, "pantry_test.db");
        assertEquals(0, new BigDecimal("1.25").compareTo(helper.getPantryItem(id).quantity));
        assertTrue(helper.updatePantryItem(id, "Tomatoes", new BigDecimal("2"), "kg", null));
        assertNull(helper.getPantryItem(id).expiry);
        assertEquals(1, helper.getPantryItems().size());
        assertTrue(helper.deletePantryItem(id));
        assertNull(helper.getPantryItem(id));
        assertFalse(helper.deletePantryItem(id));
        assertFalse(helper.updatePantryItem(id, "Tomato", BigDecimal.ONE, "item", null));
        helper.close();
        helper = new DatabaseHelper(context, "pantry_test.db");
        assertTrue(helper.getPantryItems().isEmpty());
    }

    @Test public void invalidDataIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> helper.addPantryItem(" ", BigDecimal.ONE, "g", null));
        assertThrows(IllegalArgumentException.class,
                () -> helper.addPantryItem("Rice", BigDecimal.ZERO, "g", null));
        assertThrows(IllegalArgumentException.class,
                () -> helper.addPantryItem("Rice", BigDecimal.ONE, "cup", null));
        assertThrows(java.time.format.DateTimeParseException.class,
                () -> helper.addPantryItem("Rice", BigDecimal.ONE, "g", "2026-02-30"));
        assertTrue(helper.getPantryItems().isEmpty());
    }

    @Test public void fortyRecipesAreSeededOnceWithCompleteIngredients() {
        assertEquals(40, helper.getRecipes().size());
        for (za.co.smartpantry.model.Recipe recipe : helper.getRecipes()) {
            assertFalse(recipe.ingredients.isEmpty());
            assertFalse(recipe.steps.trim().isEmpty());
            for (za.co.smartpantry.model.Ingredient ingredient : recipe.ingredients) {
                assertTrue(ingredient.quantity.signum() > 0);
                assertFalse(ingredient.unit.isEmpty());
            }
        }
        helper.close();
        helper = new DatabaseHelper(context, "pantry_test.db");
        assertEquals(40, helper.getRecipes().size());
        assertEquals("Tomato and onion salad", helper.getRecipe(1).name);
        assertNull(helper.getRecipe(9999));
    }
}
