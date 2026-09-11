package za.co.smartpantry;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.Test;
import za.co.smartpantry.data.AppPreferences;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.ui.PantryActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class HomeScreenTest {
    private final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    private void screenshot(String name) throws Exception {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        File directory = new File(context.getExternalFilesDir(null), "home-review");
        assertTrue(directory.isDirectory() || directory.mkdirs());
        Bitmap bitmap = InstrumentationRegistry.getInstrumentation().getUiAutomation().takeScreenshot();
        assertNotNull(bitmap);
        try (FileOutputStream out = new FileOutputStream(new File(directory, name + ".png"))) {
            assertTrue(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out));
        }
        bitmap.recycle();
    }

    @Test public void homeTracksQuantityEditsSearchAndSettings() throws Exception {
        context.deleteDatabase("smart_pantry.db");
        new AppPreferences(context).setExpiryIndicatorsEnabled(true);
        try (DatabaseHelper database = new DatabaseHelper(context)) {
            database.addPantryItem("Tomato", new BigDecimal("2"), "item", null);
            database.addPantryItem("Onion", BigDecimal.ONE, "item", null);
            database.addPantryItem("Olive oil", new BigDecimal("10"), "ml", null);
            database.addPantryItem("Egg", new BigDecimal("2"), "item", LocalDate.now().plusDays(1).toString());
        }
        try (ActivityScenario<PantryActivity> scenario = ActivityScenario.launch(PantryActivity.class)) {
            onView(withId(R.id.home_item_count)).check(matches(withText("4")));
            onView(withId(R.id.home_expiry_count)).check(matches(withText("1")));
            onView(withText("Tomato and onion salad")).check(doesNotExist());
            onView(withId(R.id.home_search)).perform(replaceText("oil"), closeSoftKeyboard());
            onView(withText("Olive oil · 10 ml")).perform(scrollTo(), click());
            onView(withId(R.id.ingredient_quantity)).perform(scrollTo(), replaceText("15"), closeSoftKeyboard());
            onView(withId(R.id.save_ingredient)).perform(scrollTo(), click());
            onView(withId(R.id.home_search)).perform(scrollTo(), replaceText(""), closeSoftKeyboard());
            onView(withText("Tomato and onion salad")).check(matches(isDisplayed()));
            scenario.recreate();
            onView(withId(R.id.bottom_home)).check(matches(isSelected()));
            screenshot("01_home_populated");
            onView(withText("Tomato and onion salad")).perform(click());
            onView(withId(R.id.detail_ingredients)).check(matches(withText(containsString("olive oil — 15 ml"))));
            androidx.test.espresso.Espresso.pressBack();
            onView(withId(R.id.home_expiry_card)).perform(scrollTo(), click());
            onView(withId(R.id.ingredient_name)).check(matches(withText("Egg")));
            androidx.test.espresso.Espresso.pressBack();
            onView(withId(R.id.bottom_pantry)).perform(click());
            onView(withContentDescription("Delete Olive oil")).perform(click());
            onView(withId(android.R.id.button1)).perform(click());
            onView(withId(R.id.bottom_home)).perform(click());
            onView(withId(R.id.home_item_count)).check(matches(withText("3")));
            onView(withText("Tomato and onion salad")).check(doesNotExist());
            onView(withId(R.id.home_search)).perform(scrollTo(), replaceText("unknown"), closeSoftKeyboard());
            onView(withId(R.id.home_recipes_empty)).check(matches(withText(R.string.home_search_empty)));
            onView(withText(R.string.home_pantry_search_empty)).check(matches(isDisplayed()));
            scenario.recreate();
            onView(withId(R.id.home_search)).check(matches(withText("unknown")));
            onView(withId(R.id.bottom_settings)).perform(click());
            onView(withId(R.id.expiry_switch)).perform(click());
            androidx.test.espresso.Espresso.pressBack();
            onView(withId(R.id.home_expiry_stat)).check(matches(withEffectiveVisibility(Visibility.GONE)));
            onView(withId(R.id.home_expiry_card)).check(matches(withEffectiveVisibility(Visibility.GONE)));
        }
    }

    @Test public void emptyHomeCanAddAndOpenAllRecipes() throws Exception {
        context.deleteDatabase("smart_pantry.db");
        try (ActivityScenario<PantryActivity> scenario = ActivityScenario.launch(PantryActivity.class)) {
            onView(withId(R.id.home_item_count)).check(matches(withText("0")));
            onView(withId(R.id.home_recipes_empty)).check(matches(isDisplayed()));
            screenshot("02_home_empty");
            onView(withId(R.id.home_add_ingredient)).perform(scrollTo(), click());
            onView(withId(R.id.ingredient_name)).perform(replaceText("Apple"), closeSoftKeyboard());
            onView(withId(R.id.ingredient_quantity)).perform(scrollTo(), replaceText("2"), closeSoftKeyboard());
            onView(withId(R.id.ingredient_unit)).perform(scrollTo(), click());
            androidx.test.espresso.Espresso.onData(allOf(is(instanceOf(String.class)), is("item"))).perform(click());
            onView(withId(R.id.save_ingredient)).perform(scrollTo(), click());
            onView(withId(R.id.home_item_count)).check(matches(withText("1")));
            onView(withId(R.id.home_view_all)).perform(scrollTo(), click());
            onView(withId(R.id.recipes_empty)).check(matches(isDisplayed()));
            androidx.test.espresso.Espresso.pressBack();
            onView(withId(R.id.bottom_home)).check(matches(isSelected()));
        }
    }
}
