package za.co.smartpantry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;
import java.io.File;
import java.io.FileOutputStream;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import za.co.smartpantry.data.AppPreferences;
import za.co.smartpantry.ui.IngredientFormActivity;
import za.co.smartpantry.ui.PantryActivity;
import za.co.smartpantry.ui.RecipeDetailActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppFlowTest {
    private final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    private void navigate(String destination) {
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(destination)).perform(click());
    }

    private void amount(String value) {
        onView(withId(R.id.ingredient_quantity)).perform(scrollTo(), replaceText(value), closeSoftKeyboard());
    }

    private void add(String name, String quantity, String unit) {
        onView(withId(R.id.add_ingredient)).perform(click());
        onView(withId(R.id.ingredient_name)).perform(replaceText(name), closeSoftKeyboard());
        amount(quantity);
        onView(withId(R.id.ingredient_unit)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is(unit))).perform(click());
        onView(withId(R.id.save_ingredient)).perform(scrollTo(), click());
        onView(withId(R.id.pantry_feedback)).check(matches(withText("Ingredient added.")));
    }

    private void screenshot(String filename) throws Exception {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        File folder = new File(context.getExternalFilesDir(null), "evidence");
        assertTrue(folder.isDirectory() || folder.mkdirs());
        Bitmap bitmap = InstrumentationRegistry.getInstrumentation().getUiAutomation().takeScreenshot();
        assertNotNull(bitmap);
        try (FileOutputStream stream = new FileOutputStream(new File(folder, filename + ".png"))) {
            assertTrue(bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream));
        }
        bitmap.recycle();
    }

    @Test public void a_fullPantryAndMatchingFlow() throws Exception {
        context.deleteDatabase("smart_pantry.db");
        new AppPreferences(context).setExpiryIndicatorsEnabled(true);
        try (ActivityScenario<PantryActivity> scenario = ActivityScenario.launch(new Intent(context, PantryActivity.class).putExtra("show_pantry", true))) {
            onView(withId(R.id.pantry_empty)).check(matches(isDisplayed()));
            screenshot("01_empty_pantry");
            onView(withId(R.id.add_ingredient)).perform(click());
            screenshot("03_add_ingredient");
            onView(withId(R.id.save_ingredient)).perform(scrollTo(), click());
            androidx.test.espresso.Espresso.closeSoftKeyboard();
            onView(withId(R.id.ingredient_name)).check(matches(hasErrorText("Enter an ingredient name.")));
            screenshot("04_validation_error");
            onView(withId(R.id.ingredient_name)).perform(replaceText("Tomato"), closeSoftKeyboard());
            amount("1");
            onView(withId(R.id.ingredient_unit)).perform(scrollTo(), click());
            onData(allOf(is(instanceOf(String.class)), is("item"))).perform(click());
            onView(withId(R.id.save_ingredient)).perform(scrollTo(), click());
            onView(withId(R.id.pantry_feedback)).check(matches(withText("Ingredient added.")));
            screenshot("05_successful_creation");
            onView(withContentDescription("Edit Tomato")).perform(click());
            screenshot("06_edit_ingredient");
            amount("2");
            onView(withId(R.id.save_ingredient)).perform(scrollTo(), click());
            onView(withId(R.id.pantry_feedback)).check(matches(withText("Ingredient updated.")));
            screenshot("07_successful_update");
            add("Onion", "1", "item");
            navigate("Suggested Recipes");
            onView(withId(R.id.recipes_empty)).check(matches(isDisplayed()));
            screenshot("11_zero_matches");
            navigate("Pantry");
            add("Olive oil", "10", "ml");
            navigate("Suggested Recipes");
            onView(withId(R.id.recipe_summary)).check(matches(withText("0 of 20 recipes match")));
            screenshot("14_insufficient_quantity");
            navigate("Pantry");
            onView(withContentDescription("Edit Olive oil")).perform(click());
            amount("15");
            onView(withId(R.id.save_ingredient)).perform(scrollTo(), click());
            navigate("Suggested Recipes");
            onView(withText("Tomato and onion salad")).check(matches(isDisplayed()));
            screenshot("10_matching_recipes");
            screenshot("12_final_requirement_met");
            onView(withContentDescription("View Tomato and onion salad")).perform(click());
            onView(withId(R.id.detail_ingredients)).check(matches(withText(containsString("olive oil — 15 ml"))));
            screenshot("15_recipe_detail");
            navigate("Pantry");
            onView(withContentDescription("Delete Olive oil")).perform(click());
            screenshot("08_delete_confirmation");
            onView(withId(android.R.id.button1)).perform(click());
            onView(withId(R.id.pantry_feedback)).check(matches(withText("Ingredient deleted.")));
            screenshot("09_successful_deletion");
            navigate("Suggested Recipes");
            onView(withId(R.id.recipes_empty)).check(matches(isDisplayed()));
            onView(withText("Tomato and onion salad")).check(doesNotExist());
            screenshot("13_recipe_disappeared");
            navigate("Pantry");
            add("Olive oil", "15", "ml");
            navigate("Suggested Recipes");
            onView(withText("Tomato and onion salad")).check(matches(isDisplayed()));
            screenshot("12_final_requirement_met");
            navigate("Pantry");
            onView(withContentDescription("Delete Olive oil")).perform(click());
            onView(withId(android.R.id.button1)).perform(click());
            add("Apple", "3", "item");
            onView(withContentDescription("Edit Apple")).perform(click());
            onView(withId(R.id.ingredient_expiry)).perform(scrollTo(),
                    replaceText(java.time.LocalDate.now().plusDays(3).toString()), closeSoftKeyboard());
            onView(withId(R.id.save_ingredient)).perform(scrollTo(), click());
            onView(withText(startsWith("Expiring soon"))).check(matches(isDisplayed()));
            screenshot("02_populated_pantry");
            navigate("Settings");
            onView(withId(R.id.expiry_switch)).check(matches(isChecked())).perform(click());
            onView(withId(R.id.expiry_switch)).check(matches(isNotChecked()));
            screenshot("16_settings");
        }
    }

    @Test public void b_persistenceAfterReopen() throws Exception {
        try (ActivityScenario<PantryActivity> scenario = ActivityScenario.launch(new Intent(context, PantryActivity.class).putExtra("show_pantry", true))) {
            onView(withText("Apple")).check(matches(isDisplayed()));
            onView(withText("3 item")).check(matches(isDisplayed()));
            onView(withText(startsWith("Expiring soon"))).check(doesNotExist());
            screenshot("17_pantry_after_reopen");
            navigate("Settings");
            onView(withId(R.id.expiry_switch)).check(matches(isNotChecked()));
            screenshot("18_settings_after_reopen");
        }
    }

    @Test public void c_formSurvivesRecreation() {
        try (ActivityScenario<IngredientFormActivity> scenario = ActivityScenario.launch(IngredientFormActivity.class)) {
            onView(withId(R.id.ingredient_name)).perform(replaceText("Milk"), closeSoftKeyboard());
            amount("0.25");
            scenario.recreate();
            onView(withId(R.id.ingredient_name)).check(matches(withText("Milk")));
            onView(withId(R.id.ingredient_quantity)).check(matches(withText("0.25")));
        }
    }

    @Test public void d_missingRecordsAreHandled() {
        Intent intent = new Intent(context, RecipeDetailActivity.class).putExtra("recipe_id", 99999L);
        try (ActivityScenario<RecipeDetailActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withText("Recipe not found")).check(matches(isDisplayed()));
        }
        intent = new Intent(context, IngredientFormActivity.class).putExtra("pantry_id", 99999L);
        try (ActivityScenario<IngredientFormActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.save_ingredient)).check(matches(not(isEnabled())));
        }
    }
}
