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

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        showScreen(R.layout.activity_suggestions, "Suggested recipes");
        database = new DatabaseHelper(this);
        adapter = new RecipeAdapter(id -> startActivity(new Intent(this, RecipeDetailActivity.class)
                .putExtra("recipe_id", id)));
        RecyclerView list = findViewById(R.id.recipe_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    @Override protected void onResume() {
        super.onResume();
        try {
            List<PantryItem> pantry = database.getPantryItems();
            List<Recipe> matches = new ArrayList<>();
            RecipeMatcher matcher = new RecipeMatcher();
            for (Recipe recipe : database.getRecipes())
                if (matcher.matches(recipe, pantry)) matches.add(recipe);
            adapter.submit(matches);
            ((TextView) findViewById(R.id.recipe_summary)).setText(matches.size() + " of 20 recipes match");
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
