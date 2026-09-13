package za.co.smartpantry.ui;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import za.co.smartpantry.R;
import za.co.smartpantry.adapter.RecipeAdapter;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.matching.RecipeMatcher;
import za.co.smartpantry.model.PantryItem;
import za.co.smartpantry.model.Recipe;

public class SuggestionsActivity extends BaseActivity {
    private DatabaseHelper database;
    private RecipeAdapter adapter;
    private boolean almost;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        showScreen(R.layout.activity_suggestions, "Suggested recipes");
        database = new DatabaseHelper(this);
        adapter = new RecipeAdapter(id -> startActivity(new Intent(this, RecipeDetailActivity.class)
                .putExtra("recipe_id", id).putExtra("almost_there", almost)));
        RecyclerView list = findViewById(R.id.recipe_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    @Override protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        refresh();
    }

    @Override public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.nav_recipes) {
            getIntent().putExtra("almost_there", false);
            refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        almost = getIntent().getBooleanExtra("almost_there", false);
        setTitle(almost ? R.string.almost_title : R.string.ui_suggested_recipes);
        ((TextView) findViewById(R.id.suggestions_heading)).setText(almost ? R.string.almost_title : R.string.ui_ready_to_cook);
        ((TextView) findViewById(R.id.suggestions_description)).setText(almost ? R.string.almost_description
                : R.string.ui_every_ingredient_enough_of_each_these_recipes_fit_your_pantry_rig);
        ((TextView) findViewById(R.id.recipes_empty)).setText(almost ? R.string.almost_empty
                : R.string.ui_no_recipes_match_your_pantry_yet_add_more_ingredients);
        try {
            List<PantryItem> pantry = database.getPantryItems();
            List<Recipe> matches = new ArrayList<>();
            RecipeMatcher matcher = new RecipeMatcher();
            List<Recipe> recipes = database.getRecipes();
            java.util.Map<Long, String> shortages = new java.util.HashMap<>();
            for (Recipe recipe : recipes) {
                if (almost) {
                    za.co.smartpantry.model.Ingredient missing = matcher.missingOne(recipe, pantry);
                    if (missing != null) {
                        matches.add(recipe);
                        shortages.put(recipe.id, getString(R.string.almost_shortage, missing.name, missing.amountLabel()));
                    }
                } else if (matcher.matches(recipe, pantry)) matches.add(recipe);
            }
            adapter.submit(matches, shortages);
            ((TextView) findViewById(R.id.recipe_summary)).setText(getString(almost
                    ? R.string.almost_summary : R.string.recipe_match_summary, matches.size(), recipes.size()));
            findViewById(R.id.recipes_empty).setVisibility(matches.isEmpty() ? View.VISIBLE : View.GONE);
        } catch (SQLException exception) {
            adapter.submit(Collections.emptyList());
            findViewById(R.id.recipes_empty).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.recipe_summary)).setText("Could not load recipes. Please reopen this screen to try again.");
        }
    }

    @Override protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
