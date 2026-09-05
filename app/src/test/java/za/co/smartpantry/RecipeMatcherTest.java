package za.co.smartpantry;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import za.co.smartpantry.matching.RecipeMatcher;
import za.co.smartpantry.model.Ingredient;
import za.co.smartpantry.model.Recipe;
import static org.junit.Assert.*;

public class RecipeMatcherTest {
    private final RecipeMatcher matcher = new RecipeMatcher();
    private Ingredient ingredient(String name, String amount, String unit) {
        return new Ingredient(name, new BigDecimal(amount), unit);
    }
    private Recipe recipe(Ingredient... ingredients) {
        return new Recipe(1, "Test recipe", "Prepare.", Arrays.asList(ingredients));
    }
    @Test public void exactCompleteMatch() {
        assertTrue(matcher.matches(recipe(ingredient("tomato", "2", "item")),
                Arrays.asList(ingredient("tomato", "2", "item"))));
    }
    @Test public void missingIngredientFails() {
        assertFalse(matcher.matches(recipe(ingredient("tomato", "2", "item"), ingredient("onion", "1", "item")),
                Arrays.asList(ingredient("tomato", "2", "item"))));
    }
    @Test public void insufficientQuantityFails() {
        assertFalse(matcher.matches(recipe(ingredient("tomato", "2", "item")),
                Arrays.asList(ingredient("tomato", "1", "item"))));
    }
}
