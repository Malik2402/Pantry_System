package za.co.smartpantry;

import android.content.Context;
import android.database.Cursor;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
}
