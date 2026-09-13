package za.co.smartpantry;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import za.co.smartpantry.matching.RecipeMatcher;
import za.co.smartpantry.model.Ingredient;
import za.co.smartpantry.model.Recipe;
import static org.junit.Assert.*;

public class AlmostThereTest {
    private final RecipeMatcher matcher = new RecipeMatcher();
    private Ingredient i(String name, String amount, String unit) {
        return new Ingredient(name, new BigDecimal(amount), unit);
    }
    private Recipe r(Ingredient... parts) { return new Recipe(1, "Test", "Prepare.", Arrays.asList(parts)); }
    private void shortage(Ingredient missing, String name, String amount, String unit) {
        assertNotNull(missing);
        assertEquals(name, missing.name);
        assertEquals(0, new BigDecimal(amount).compareTo(missing.quantity));
        assertEquals(unit, missing.unit);
    }
    @Test public void missingIngredientIsReported() {
        Recipe recipe = r(i("tomato", "2", "item"), i("olive oil", "15", "ml"));
        shortage(matcher.missingOne(recipe, Arrays.asList(i("tomato", "2", "item"))), "olive oil", "15", "ml");
        shortage(matcher.missingOne(r(i("water", "100", "ml")), Collections.emptyList()), "water", "100", "ml");
    }
    @Test public void shortQuantityIsReportedAndNeverStrict() {
        Recipe recipe = r(i("tomato", "2", "item"), i("olive oil", "15", "ml"));
        java.util.List<Ingredient> pantry = Arrays.asList(i("tomato", "2", "item"), i("olive oil", "10", "ml"));
        shortage(matcher.missingOne(recipe, pantry), "olive oil", "5", "ml");
        assertFalse(matcher.matches(recipe, pantry));
    }
    @Test public void completeRecipeLeavesAlmostThere() {
        Recipe recipe = r(i("tomato", "2", "item"), i("olive oil", "15", "ml"));
        assertNull(matcher.missingOne(recipe, recipe.ingredients));
        assertTrue(matcher.matches(recipe, recipe.ingredients));
    }
    @Test public void twoMissingOrInsufficientRequirementsAreExcluded() {
        Recipe recipe = r(i("tomato", "2", "item"), i("olive oil", "15", "ml"));
        assertNull(matcher.missingOne(recipe, Collections.emptyList()));
        assertNull(matcher.missingOne(recipe, Arrays.asList(i("tomato", "1", "item"), i("olive oil", "10", "ml"))));
    }
    @Test public void duplicateRequirementsAndPantryQuantitiesAreCombined() {
        Recipe recipe = r(i("tomato", "2", "item"), i("olive oil", "1", "tbsp"), i("olive oil", "10", "ml"));
        shortage(matcher.missingOne(recipe, Arrays.asList(i(" TOMATOES ", "2", "piece"),
                i("olive oil", "1", "tsp"), i("olive oil", "10", "ml"))), "olive oil", "10", "ml");
    }
    @Test public void compatibleUnitsProduceAnExactShortage() {
        Recipe recipe = r(i("flour", "0.5", "kg"), i("egg", "1", "item"));
        shortage(matcher.missingOne(recipe, Arrays.asList(i("flour", "250", "g"), i("egg", "1", "item"))), "flour", "250", "g");
    }
    @Test public void incompatibleUnitsNeverSatisfyARequirement() {
        Recipe recipe = r(i("flour", "50", "g"), i("egg", "1", "item"));
        shortage(matcher.missingOne(recipe, Arrays.asList(i("flour", "100", "ml"), i("egg", "1", "item"))), "flour", "50", "g");
    }
    @Test public void invalidRecipesAndNullPantryAreExcluded() {
        assertNull(matcher.missingOne(null, Collections.emptyList()));
        assertNull(matcher.missingOne(r(), Collections.emptyList()));
        assertNull(matcher.missingOne(r(i("egg", "1", "item")), null));
        assertNull(matcher.missingOne(r(i("egg", "1", "item"), i("flour", "1", "cup")), Arrays.asList(i("egg", "1", "item"))));
    }
}
