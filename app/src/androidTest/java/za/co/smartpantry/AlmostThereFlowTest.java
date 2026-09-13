package za.co.smartpantry;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;
import java.math.BigDecimal;
import java.io.File;
import java.io.FileOutputStream;
import org.junit.Test;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.ui.PantryActivity;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.*;

public class AlmostThereFlowTest {
    @Test public void separateListsRefreshAfterQuantityChanges() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.deleteDatabase("smart_pantry.db");
        long oil;
        try (DatabaseHelper helper = new DatabaseHelper(context)) {
            helper.addPantryItem("Tomato", new BigDecimal("2"), "item", null);
            helper.addPantryItem("Onion", BigDecimal.ONE, "item", null);
            oil = helper.addPantryItem("Olive oil", new BigDecimal("10"), "ml", null);
        }
        try (ActivityScenario<PantryActivity> scenario = ActivityScenario.launch(PantryActivity.class)) {
            onView(withText("Tomato and onion salad")).check(doesNotExist());
            onView(withId(R.id.home_almost_there)).perform(scrollTo(), click());
            onView(withId(R.id.suggestions_heading)).check(matches(withText("Almost There")));
            onView(withText("Tomato and onion salad")).check(matches(isDisplayed()));
            onView(withText("Still needed: olive oil — 5 ml")).check(matches(isDisplayed()));
            File directory = new File(context.getExternalFilesDir(null), "almost-review");
            assertTrue(directory.isDirectory() || directory.mkdirs());
            Bitmap image = InstrumentationRegistry.getInstrumentation().getUiAutomation().takeScreenshot();
            assertNotNull(image);
            try (FileOutputStream out = new FileOutputStream(new File(directory, "almost-list.png"))) {
                assertTrue(image.compress(Bitmap.CompressFormat.PNG, 100, out));
            }
            image.recycle();
            onView(withContentDescription("View Tomato and onion salad")).perform(click());
            onView(withId(R.id.detail_availability)).check(matches(withText("Still needed: olive oil — 5 ml")));
            onView(withId(R.id.detail_back)).perform(scrollTo(), click());
            onView(withId(R.id.suggestions_heading)).check(matches(withText("Almost There")));
            openActionBarOverflowOrOptionsMenu(context);
            onView(withText("Suggested Recipes")).perform(click());
            onView(withId(R.id.recipes_empty)).check(matches(isDisplayed()));
            onView(withText("Tomato and onion salad")).check(doesNotExist());
            androidx.test.espresso.Espresso.pressBack();
            try (DatabaseHelper helper = new DatabaseHelper(context)) {
                helper.updatePantryItem(oil, "Olive oil", new BigDecimal("15"), "ml", null);
            }
            onView(withId(R.id.home_almost_there)).perform(scrollTo(), click());
            onView(withText("Tomato and onion salad")).check(doesNotExist());
            openActionBarOverflowOrOptionsMenu(context);
            onView(withText("Suggested Recipes")).perform(click());
            onView(withText("Tomato and onion salad")).check(matches(isDisplayed()));
            androidx.test.espresso.Espresso.pressBack();
        }
    }
}
