package za.co.smartpantry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.test.platform.app.InstrumentationRegistry;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import za.co.smartpantry.adapter.IngredientArtwork;
import za.co.smartpantry.adapter.RecipeArtwork;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.model.Ingredient;
import za.co.smartpantry.model.Recipe;
import static org.junit.Assert.*;

public class ArtworkTest {
    @Test public void everyBundledPhotoDecodesAndAliasesUseTheSamePicture() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Set<Integer> recipes = new HashSet<>();
        Set<Integer> ingredients = new HashSet<>();
        try (DatabaseHelper database = new DatabaseHelper(context)) {
            for (Recipe recipe : database.getRecipes()) {
                recipes.add(RecipeArtwork.forRecipe(recipe.id));
                for (Ingredient ingredient : recipe.ingredients)
                    ingredients.add(IngredientArtwork.forName(ingredient.name));
            }
        }
        assertEquals(40, recipes.size());
        assertEquals(45, ingredients.size());
        recipes.addAll(ingredients);
        for (int resource : recipes) {
            Bitmap image = BitmapFactory.decodeResource(context.getResources(), resource);
            assertNotNull(context.getResources().getResourceEntryName(resource), image);
            assertTrue(image.getWidth() > 0 && image.getHeight() > 0);
            image.recycle();
        }
        assertEquals(IngredientArtwork.forName("tomato"), IngredientArtwork.forName(" Tomatoes "));
        assertEquals(IngredientArtwork.forName("plain yoghurt"), IngredientArtwork.forName("plain yogurt"));
        assertNotEquals(IngredientArtwork.forName("rice"), IngredientArtwork.forName("cooked rice"));
        assertEquals(R.drawable.home_jar_icon, IngredientArtwork.forName("custom ingredient"));
    }
}
