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
    @Test public void greaterQuantityMatches() {
        assertTrue(matcher.matches(recipe(ingredient("tomato", "2", "item")),
                Arrays.asList(ingredient("tomato", "3", "item"))));
    }
    @Test public void caseDifferencesMatch() {
        assertTrue(matcher.matches(recipe(ingredient("TOMATO", "2", "item")),
                Arrays.asList(ingredient("Tomato", "2", "item"))));
    }
    @Test public void whitespaceMatches() {
        assertTrue(matcher.matches(recipe(ingredient("olive oil", "15", "ml")),
                Arrays.asList(ingredient("  OLIVE   OIL  ", "15", "ml"))));
    }
    @Test public void singularPluralMatches() {
        assertTrue(matcher.matches(recipe(ingredient("tomato", "2", "item")),
                Arrays.asList(ingredient("tomatoes", "2", "item"))));
    }
    @Test public void kilogramsConvertToGrams() {
        assertTrue(matcher.matches(recipe(ingredient("potato", "500", "g")),
                Arrays.asList(ingredient("potatoes", "0.5", "kg"))));
    }
    @Test public void litresConvertToMillilitres() {
        assertTrue(matcher.matches(recipe(ingredient("milk", "250", "ml")),
                Arrays.asList(ingredient("milk", "0.25", "l"))));
    }
    @Test public void spoonsConvertToMillilitres() {
        assertTrue(matcher.matches(recipe(ingredient("olive oil", "15", "ml")),
                Arrays.asList(ingredient("olive oil", "1", "tbsp"))));
    }
    @Test public void teaspoonsCombineWithTablespoons() {
        assertTrue(matcher.matches(recipe(ingredient("honey", "4", "tsp")),
                Arrays.asList(ingredient("honey", "1", "tbsp"), ingredient("honey", "5", "ml"))));
    }
    @Test public void incompatibleUnitsFail() {
        assertFalse(matcher.matches(recipe(ingredient("tomato", "2", "item")),
                Arrays.asList(ingredient("tomato", "2000", "g"))));
    }
    @Test public void duplicateQuantitiesCombine() {
        assertTrue(matcher.matches(recipe(ingredient("tomato", "3", "item")),
                Arrays.asList(ingredient("tomatoes", "1", "whole"), ingredient(" TOMATO ", "2", "piece"))));
    }
    @Test public void decimalQuantitiesAreExact() {
        assertTrue(matcher.matches(recipe(ingredient("milk", "0.3", "l")),
                Arrays.asList(ingredient("milk", "0.1", "l"), ingredient("milk", "0.2", "l"))));
    }
    @Test public void convertedShortageFails() {
        assertFalse(matcher.matches(recipe(ingredient("milk", "251", "ml")),
                Arrays.asList(ingredient("milk", "0.25", "l"))));
    }
    @Test public void compatibleStockIsNotMixedWithIncompatibleStock() {
        assertFalse(matcher.matches(recipe(ingredient("tomato", "3", "item")),
                Arrays.asList(ingredient("tomato", "2", "item"), ingredient("tomato", "1000", "g"))));
    }
    @Test public void aliasesMatch() {
        assertTrue(matcher.matches(recipe(ingredient("plain yoghurt", "150", "g")),
                Arrays.asList(ingredient("plain yogurt", "0.15", "kg"))));
    }
    @Test public void cookedAndDryRemainDistinct() {
        assertFalse(matcher.matches(recipe(ingredient("cooked rice", "200", "g")),
                Arrays.asList(ingredient("rice", "200", "g"))));
    }
    @Test public void unknownUnitFailsEvenWhenEqual() {
        assertFalse(matcher.matches(recipe(ingredient("flour", "1", "cup")),
                Arrays.asList(ingredient("flour", "1", "cup"))));
    }
    @Test public void tinyShortageIsNotRoundedUp() {
        assertFalse(matcher.matches(recipe(ingredient("milk", "1", "ml")),
                Arrays.asList(ingredient("milk", "0.999999", "ml"))));
    }
    @Test public void pluralPhraseMatches() {
        assertTrue(matcher.matches(recipe(ingredient("sweet potato", "300", "g")),
                Arrays.asList(ingredient("sweet potatoes", "300", "grams"))));
    }
    @Test public void emptyPantryFails() {
        assertFalse(matcher.matches(recipe(ingredient("egg", "1", "item")), Collections.emptyList()));
    }
    @Test public void everyIngredientMustQualify() {
        Recipe target = recipe(ingredient("egg", "2", "item"), ingredient("milk", "50", "ml"), ingredient("butter", "10", "g"));
        assertFalse(matcher.matches(target, Arrays.asList(ingredient("egg", "2", "item"), ingredient("milk", "50", "ml"), ingredient("butter", "9", "g"))));
        assertTrue(matcher.matches(target, Arrays.asList(ingredient("egg", "2", "item"), ingredient("milk", "50", "ml"), ingredient("butter", "10", "g"))));
    }
    @Test public void repeatedRecipeRequirementsCannotReuseStock() {
        assertFalse(matcher.matches(recipe(ingredient("egg", "2", "item"), ingredient("eggs", "1", "whole")),
                Arrays.asList(ingredient("egg", "2", "item"))));
    }
    @Test public void malformedRecipeDoesNotQualify() {
        assertFalse(matcher.matches(recipe(), Arrays.asList(ingredient("egg", "2", "item"))));
        assertFalse(matcher.matches(recipe(ingredient("egg", "0", "item")), Arrays.asList(ingredient("egg", "2", "item"))));
        assertFalse(matcher.matches(null, Collections.emptyList()));
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
