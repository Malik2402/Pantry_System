package za.co.smartpantry.ui;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.TextView;
import za.co.smartpantry.R;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.model.Ingredient;
import za.co.smartpantry.model.Recipe;

public class RecipeDetailActivity extends BaseActivity {
    private DatabaseHelper database;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        showScreen(R.layout.activity_recipe_detail, "Recipe detail");
        database = new DatabaseHelper(this);
        findViewById(R.id.detail_back).setOnClickListener(view ->
                startActivity(new Intent(this, SuggestionsActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));
    }

    @Override protected void onResume() {
        super.onResume();
        try {
            Recipe recipe = database.getRecipe(getIntent().getLongExtra("recipe_id", -1));
            if (recipe == null) {
                ((TextView) findViewById(R.id.detail_name)).setText("Recipe not found");
                ((TextView) findViewById(R.id.detail_steps)).setText("Return to suggestions and choose another recipe.");
                return;
            }
            ((TextView) findViewById(R.id.detail_name)).setText(recipe.name);
            StringBuilder ingredients = new StringBuilder();
            for (Ingredient ingredient : recipe.ingredients)
                ingredients.append("• ").append(ingredient.name).append(" — ").append(ingredient.amountLabel()).append("\n");
            ((TextView) findViewById(R.id.detail_ingredients)).setText(ingredients.toString().trim());
            ((TextView) findViewById(R.id.detail_steps)).setText(recipe.steps);
        } catch (SQLException exception) {
            ((TextView) findViewById(R.id.detail_name)).setText("Could not load this recipe");
            ((TextView) findViewById(R.id.detail_steps)).setText("Please return to suggestions and try again.");
        }
    }

    @Override protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
