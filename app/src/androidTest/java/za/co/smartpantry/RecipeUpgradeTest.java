package za.co.smartpantry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.matching.RecipeMatcher;
import za.co.smartpantry.matching.UnitConverter;
import za.co.smartpantry.model.Ingredient;
import za.co.smartpantry.model.Recipe;
import static org.junit.Assert.*;

public class RecipeUpgradeTest {
    private final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    private static final String DATABASE = "recipe_upgrade_test.db";

    @After public void cleanUp() { context.deleteDatabase(DATABASE); }

    private String prepareLegacy(int version) throws Exception {
        context.deleteDatabase(DATABASE);
        String fixture;
        try (InputStream stream = InstrumentationRegistry.getInstrumentation().getContext()
                .getAssets().open("legacy_v2.sql")) {
            java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = stream.read(buffer)) != -1) bytes.write(buffer, 0, read);
            fixture = new String(bytes.toByteArray(), StandardCharsets.UTF_8);
        }
        try (SQLiteDatabase database = context.openOrCreateDatabase(DATABASE, 0, null)) {
            for (String statement : fixture.split(";")) {
                statement = statement.trim();
                if (statement.isEmpty()) continue;
                if (version == 1 && (statement.startsWith("INSERT INTO recipes")
                        || statement.startsWith("INSERT INTO recipe_ingredients"))) continue;
                database.execSQL(statement);
            }
            database.setVersion(version);
            return originalRecipes(database);
        }
    }

    private String originalRecipes(SQLiteDatabase database) {
        StringBuilder snapshot = new StringBuilder();
        try (Cursor rows = database.rawQuery("SELECT r.id,r.name,r.steps,i.id,i.name,i.quantity,i.unit "
                + "FROM recipes r JOIN recipe_ingredients i ON r.id=i.recipe_id "
                + "WHERE r.id<=20 ORDER BY r.id,i.id", null)) {
            while (rows.moveToNext()) {
                for (int column = 0; column < rows.getColumnCount(); column++)
                    snapshot.append(rows.getString(column)).append('|');
                snapshot.append('\n');
            }
        }
        return snapshot.toString();
    }

    private void verifyUpgrade(int version) throws Exception {
        String previous = prepareLegacy(version);
        try (DatabaseHelper helper = new DatabaseHelper(context, DATABASE)) {
            assertEquals(40, helper.getRecipes().size());
            if (version == 2) assertEquals(previous, originalRecipes(helper.getReadableDatabase()));
            assertEquals("Tomato", helper.getPantryItem(77).name);
            assertEquals("kg", helper.getPantryItem(77).unit);
            assertEquals("2026-09-30", helper.getPantryItem(77).expiry);
            assertEquals(0, new BigDecimal("1.25").compareTo(helper.getPantryItem(77).quantity));
            assertEquals(1, helper.getPantryItems().size());
            assertEquals(3, helper.getReadableDatabase().getVersion());
            try (Cursor errors = helper.getReadableDatabase().rawQuery("PRAGMA foreign_key_check", null)) {
                assertEquals(0, errors.getCount());
            }
        }
        try (DatabaseHelper reopened = new DatabaseHelper(context, DATABASE)) {
            assertEquals(40, reopened.getRecipes().size());
            assertEquals(1, reopened.getPantryItems().size());
            assertEquals("Berry yoghurt bowl", reopened.getRecipe(40).name);
        }
    }

    @Test public void versionTwoPreservesPantryAndOriginalRecipes() throws Exception { verifyUpgrade(2); }
    @Test public void versionOneCanUpgradeDirectly() throws Exception { verifyUpgrade(1); }

    @Test public void everyRecipeRequiresAllItsQuantities() {
        context.deleteDatabase(DATABASE);
        try (DatabaseHelper helper = new DatabaseHelper(context, DATABASE)) {
            RecipeMatcher matcher = new RecipeMatcher();
            List<Recipe> recipes = helper.getRecipes();
            assertEquals(40, recipes.size());
            for (Recipe recipe : recipes) {
                assertTrue(recipe.name, matcher.matches(recipe, recipe.ingredients));
                for (int index = 0; index < recipe.ingredients.size(); index++) {
                    Ingredient ingredient = recipe.ingredients.get(index);
                    assertNotNull(ingredient.unit, UnitConverter.find(ingredient.unit));
                    List<Ingredient> insufficient = new ArrayList<>(recipe.ingredients);
                    insufficient.set(index, new Ingredient(ingredient.name,
                            ingredient.quantity.divide(new BigDecimal("2")), ingredient.unit));
                    assertFalse(recipe.name + ": insufficient " + ingredient.name,
                            matcher.matches(recipe, insufficient));
                    insufficient.remove(index);
                    assertFalse(recipe.name + ": missing " + ingredient.name,
                            matcher.matches(recipe, insufficient));
                }
            }
        }
    }

    @Test public void newRecipeAppearsAndDisappearsWithPantryChanges() {
        context.deleteDatabase("smart_pantry.db");
        try (DatabaseHelper helper = new DatabaseHelper(context)) {
            helper.addPantryItem("Bread", new BigDecimal("2"), "piece", null);
            helper.addPantryItem("Butter", new BigDecimal("15"), "g", null);
            helper.addPantryItem("Garlic", new BigDecimal("5"), "g", null);
        }
        try (androidx.test.core.app.ActivityScenario<za.co.smartpantry.ui.PantryActivity> scenario =
                androidx.test.core.app.ActivityScenario.launch(za.co.smartpantry.ui.PantryActivity.class)) {
            androidx.test.espresso.Espresso.onView(androidx.test.espresso.matcher.ViewMatchers.withText("Garlic bread"))
                    .perform(androidx.test.espresso.action.ViewActions.scrollTo(), androidx.test.espresso.action.ViewActions.click());
            androidx.test.espresso.Espresso.onView(androidx.test.espresso.matcher.ViewMatchers.withId(R.id.detail_ingredients))
                    .check(androidx.test.espresso.assertion.ViewAssertions.matches(
                            androidx.test.espresso.matcher.ViewMatchers.withText(org.hamcrest.Matchers.containsString("butter — 15 g"))));
            androidx.test.espresso.Espresso.pressBack();
            androidx.test.espresso.Espresso.onView(androidx.test.espresso.matcher.ViewMatchers.withId(R.id.home_view_all))
                    .perform(androidx.test.espresso.action.ViewActions.scrollTo(), androidx.test.espresso.action.ViewActions.click());
            androidx.test.espresso.Espresso.onView(androidx.test.espresso.matcher.ViewMatchers.withId(R.id.recipe_summary))
                    .check(androidx.test.espresso.assertion.ViewAssertions.matches(
                            androidx.test.espresso.matcher.ViewMatchers.withText("1 of 40 recipes match")));
            androidx.test.espresso.Espresso.pressBack();
            androidx.test.espresso.Espresso.onView(androidx.test.espresso.matcher.ViewMatchers.withId(R.id.bottom_pantry))
                    .perform(androidx.test.espresso.action.ViewActions.click());
            androidx.test.espresso.Espresso.onView(androidx.test.espresso.matcher.ViewMatchers.withContentDescription("Delete Butter"))
                    .perform(androidx.test.espresso.action.ViewActions.click());
            androidx.test.espresso.Espresso.onView(androidx.test.espresso.matcher.ViewMatchers.withId(android.R.id.button1))
                    .perform(androidx.test.espresso.action.ViewActions.click());
            androidx.test.espresso.Espresso.onView(androidx.test.espresso.matcher.ViewMatchers.withId(R.id.bottom_home))
                    .perform(androidx.test.espresso.action.ViewActions.click());
            androidx.test.espresso.Espresso.onView(androidx.test.espresso.matcher.ViewMatchers.withText("Garlic bread"))
                    .check(androidx.test.espresso.assertion.ViewAssertions.doesNotExist());
        }
    }
}
