package za.co.smartpantry;

import android.content.Context;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;
import java.math.BigDecimal;
import org.junit.Test;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.ui.PantryActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

public class NavigationUpgradeTest {
    private final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test public void cancelAndBackFromHomeFormReturnToPantry() {
        context.deleteDatabase("smart_pantry.db");
        try (ActivityScenario<PantryActivity> scenario = ActivityScenario.launch(PantryActivity.class)) {
            onView(withId(R.id.home_add_ingredient)).perform(scrollTo(), click());
            onView(withId(R.id.ingredient_name)).perform(replaceText("Unsaved apple"), closeSoftKeyboard());
            onView(withId(R.id.cancel_ingredient)).perform(scrollTo(), click());
            onView(withId(R.id.bottom_pantry)).check(matches(isSelected()));
            onView(withId(R.id.pantry_empty)).check(matches(isDisplayed()));
            onView(withId(R.id.bottom_home)).perform(click());
            onView(withId(R.id.home_add_ingredient)).perform(scrollTo(), click());
            androidx.test.espresso.Espresso.pressBack();
            onView(withId(R.id.bottom_pantry)).check(matches(isSelected()));
            onView(withId(R.id.add_ingredient)).perform(click());
            onView(withContentDescription(R.string.back_to_pantry)).perform(click());
            onView(withId(R.id.bottom_pantry)).check(matches(isSelected()));
        }
    }

    @Test public void leavingEitherRecipeListReturnsHome() {
        try (ActivityScenario<PantryActivity> scenario = ActivityScenario.launch(PantryActivity.class)) {
            onView(withId(R.id.bottom_pantry)).perform(click());
            openActionBarOverflowOrOptionsMenu(context);
            onView(withText("Suggested Recipes")).perform(click());
            androidx.test.espresso.Espresso.pressBack();
            onView(withId(R.id.bottom_home)).check(matches(isSelected()));
            onView(withId(R.id.home_almost_there)).perform(scrollTo(), click());
            onView(withContentDescription(R.string.back_to_home)).perform(click());
            onView(withId(R.id.bottom_home)).check(matches(isSelected()));
        }
    }

    @Test public void leavingRecipeDetailReturnsHome() {
        context.deleteDatabase("smart_pantry.db");
        try (DatabaseHelper database = new DatabaseHelper(context)) {
            database.addPantryItem("Egg", new BigDecimal("2"), "item", null);
            database.addPantryItem("Olive oil", new BigDecimal("15"), "ml", null);
        }
        try (ActivityScenario<PantryActivity> scenario = ActivityScenario.launch(PantryActivity.class)) {
            onView(withId(R.id.home_view_all)).perform(scrollTo(), click());
            onView(withContentDescription("View Simple omelette")).perform(click());
            androidx.test.espresso.Espresso.pressBack();
            onView(withId(R.id.bottom_home)).check(matches(isSelected()));
            onView(withId(R.id.home_view_all)).perform(scrollTo(), click());
            onView(withContentDescription("View Simple omelette")).perform(click());
            onView(withContentDescription(R.string.back_to_home)).perform(click());
            onView(withId(R.id.bottom_home)).check(matches(isSelected()));
        }
    }
}
