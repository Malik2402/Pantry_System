package za.co.smartpantry.ui;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import za.co.smartpantry.R;
import za.co.smartpantry.adapter.PantryAdapter;
import za.co.smartpantry.adapter.HomeRecipeAdapter;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.model.PantryItem;
import za.co.smartpantry.model.Recipe;
import za.co.smartpantry.matching.RecipeMatcher;

public class PantryActivity extends BaseActivity implements PantryAdapter.Actions {
    private DatabaseHelper database;
    private PantryAdapter adapter;
    private HomeRecipeAdapter homeAdapter;
    private List<PantryItem> pantry = new ArrayList<>();
    private List<Recipe> readyRecipes = new ArrayList<>();
    private boolean showingPantry;
    private boolean loadFailed;
    private final androidx.activity.OnBackPressedCallback pantryBack =
            new androidx.activity.OnBackPressedCallback(false) {
                @Override public void handleOnBackPressed() { showPantryPanel(false); }
            };

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        showScreen(R.layout.activity_pantry, "My pantry");
        getOnBackPressedDispatcher().addCallback(this, pantryBack);
        database = new DatabaseHelper(this);
        adapter = new PantryAdapter(this);
        androidx.recyclerview.widget.RecyclerView list = findViewById(R.id.pantry_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        findViewById(R.id.add_ingredient).setOnClickListener(view ->
                startActivityForResult(new Intent(this, IngredientFormActivity.class), 1));
        findViewById(R.id.home_add_ingredient).setOnClickListener(view ->
                startActivityForResult(new Intent(this, IngredientFormActivity.class), 1));
        findViewById(R.id.home_view_all).setOnClickListener(view ->
                startActivity(new Intent(this, SuggestionsActivity.class)));
        findViewById(R.id.home_pantry_stat).setOnClickListener(view -> showPantryPanel(true));
        findViewById(R.id.home_expiry_stat).setOnClickListener(view -> showPantryPanel(true));
        homeAdapter = new HomeRecipeAdapter(id -> startActivity(
                new Intent(this, RecipeDetailActivity.class).putExtra("recipe_id", id)));
        androidx.recyclerview.widget.RecyclerView recipes = findViewById(R.id.home_recipe_list);
        recipes.setLayoutManager(new LinearLayoutManager(this));
        recipes.setAdapter(homeAdapter);
        ((EditText) findViewById(R.id.home_search)).addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { renderSearch(); }
            @Override public void afterTextChanged(Editable text) {}
        });
        showPantryPanel(state == null ? getIntent().getBooleanExtra("show_pantry", false)
                : state.getBoolean("show_pantry"));
        if (state != null) feedback(state.getString("feedback", ""));
    }

    @Override protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        try {
            List<PantryItem> items = database.getPantryItems();
            pantry = items;
            readyRecipes.clear();
            RecipeMatcher matcher = new RecipeMatcher();
            for (Recipe recipe : database.getRecipes())
                if (matcher.matches(recipe, pantry)) readyRecipes.add(recipe);
            loadFailed = false;
            adapter.setExpiryIndicators(new za.co.smartpantry.data.AppPreferences(this).expiryIndicatorsEnabled());
            adapter.submit(items);
            ((TextView) findViewById(R.id.pantry_summary)).setText(items.size() + " pantry entries · stored on this device");
            findViewById(R.id.pantry_empty).setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
            renderHome();
        } catch (SQLException exception) {
            loadFailed = true;
            pantry.clear();
            readyRecipes.clear();
            adapter.submit(java.util.Collections.emptyList());
            findViewById(R.id.pantry_empty).setVisibility(View.GONE);
            feedback("Could not load your pantry. Please reopen this screen to try again.");
            renderHome();
        }
    }

    void showPantryPanel(boolean list) {
        showingPantry = list;
        pantryBack.setEnabled(list);
        findViewById(R.id.pantry_panel).setVisibility(list ? View.VISIBLE : View.GONE);
        findViewById(R.id.home_scroll).setVisibility(list ? View.GONE : View.VISIBLE);
        findViewById(R.id.toolbar).setVisibility(list ? View.VISIBLE : View.GONE);
        selectBottom(list ? R.id.bottom_pantry : R.id.bottom_home);
        android.view.inputmethod.InputMethodManager keyboard = (android.view.inputmethod.InputMethodManager)
                getSystemService(INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(findViewById(R.id.home_search).getWindowToken(), 0);
        findViewById(R.id.home_search).clearFocus();
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        showPantryPanel(intent.getBooleanExtra("show_pantry", false));
    }

    private void renderHome() {
        ((TextView) findViewById(R.id.home_item_count)).setText(loadFailed ? "—" : String.valueOf(pantry.size()));
        boolean expiryEnabled = new za.co.smartpantry.data.AppPreferences(this).expiryIndicatorsEnabled();
        LocalDate today = LocalDate.now();
        int soon = 0;
        PantryItem earliest = null;
        LocalDate earliestDate = null;
        for (PantryItem item : pantry) {
            if (item.expiry == null || item.expiry.isEmpty()) continue;
            LocalDate date;
            try { date = LocalDate.parse(item.expiry); }
            catch (java.time.format.DateTimeParseException ignored) { continue; }
            if (!date.isBefore(today) && !date.isAfter(today.plusDays(7))) soon++;
            if (!date.isAfter(today.plusDays(7)) && (earliestDate == null || date.isBefore(earliestDate))) {
                earliest = item;
                earliestDate = date;
            }
        }
        findViewById(R.id.home_expiry_stat).setVisibility(expiryEnabled ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.home_expiry_count)).setText(loadFailed ? "—" : String.valueOf(soon));
        View card = findViewById(R.id.home_expiry_card);
        card.setVisibility(expiryEnabled && earliest != null ? View.VISIBLE : View.GONE);
        if (earliest != null) {
            long days = ChronoUnit.DAYS.between(today, earliestDate);
            int message = days < 0 ? R.string.home_expiry_past : days == 0 ? R.string.home_expiry_today
                    : days == 1 ? R.string.home_expiry_tomorrow : R.string.home_expiry_days;
            ((TextView) findViewById(R.id.home_expiry_title)).setText(
                    days < 0 ? R.string.home_check_expiry : R.string.home_use_soon);
            ((TextView) findViewById(R.id.home_expiry_message)).setText(getString(message, earliest.name, days));
            PantryItem item = earliest;
            card.setOnClickListener(view -> edit(item));
        }
        renderSearch();
    }

    private void renderSearch() {
        String query = ((EditText) findViewById(R.id.home_search)).getText().toString().trim().toLowerCase(Locale.ROOT);
        List<Recipe> results = new ArrayList<>();
        for (Recipe recipe : readyRecipes) {
            boolean found = recipe.name.toLowerCase(Locale.ROOT).contains(query);
            for (za.co.smartpantry.model.Ingredient ingredient : recipe.ingredients)
                found |= ingredient.name.toLowerCase(Locale.ROOT).contains(query);
            if (found && (!query.isEmpty() || results.size() < 2)) results.add(recipe);
        }
        homeAdapter.submit(results);
        TextView empty = findViewById(R.id.home_recipes_empty);
        empty.setVisibility(results.isEmpty() ? View.VISIBLE : View.GONE);
        empty.setText(loadFailed ? R.string.home_load_error : query.isEmpty()
                ? R.string.ui_no_recipes_match_your_pantry_yet_add_more_ingredients : R.string.home_search_empty);
        LinearLayout pantryResults = findViewById(R.id.home_pantry_results);
        pantryResults.removeAllViews();
        pantryResults.setVisibility(query.isEmpty() || loadFailed ? View.GONE : View.VISIBLE);
        if (!query.isEmpty() && !loadFailed) {
            TextView heading = new TextView(this);
            heading.setText(R.string.home_pantry_results_title);
            heading.setTextSize(18);
            heading.setTypeface(null, android.graphics.Typeface.BOLD);
            pantryResults.addView(heading);
            int count = 0;
            for (PantryItem item : pantry) {
                if (!item.name.toLowerCase(Locale.ROOT).contains(query)) continue;
                TextView row = new TextView(this);
                row.setText(item.name + " · " + item.quantity.stripTrailingZeros().toPlainString() + " " + item.unit);
                row.setTextColor(getColor(R.color.forest));
                row.setTextSize(16);
                row.setMinHeight((int) (48 * getResources().getDisplayMetrics().density));
                row.setGravity(android.view.Gravity.CENTER_VERTICAL);
                row.setFocusable(true);
                row.setOnClickListener(view -> edit(item));
                pantryResults.addView(row);
                count++;
            }
            if (count == 0) {
                TextView none = new TextView(this);
                none.setText(R.string.home_pantry_search_empty);
                pantryResults.addView(none);
            }
        }
    }

    @Override public void edit(PantryItem item) {
        startActivityForResult(new Intent(this, IngredientFormActivity.class).putExtra("pantry_id", item.id), 1);
    }

    @Override public void delete(PantryItem item) {
        new AlertDialog.Builder(this).setTitle("Delete ingredient?")
                .setMessage("Remove " + item.name + " from your pantry?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    try {
                        boolean deleted = database.deletePantryItem(item.id);
                        refresh();
                        feedback(deleted ? "Ingredient deleted." : "This ingredient was already removed.");
                    } catch (SQLException exception) {
                        feedback("Could not delete this ingredient. Please try again.");
                    }
                }).show();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null)
            feedback(data.getStringExtra("feedback"));
    }

    private void feedback(String message) {
        TextView view = findViewById(R.id.pantry_feedback);
        view.setText(message);
        view.setVisibility(message == null || message.isEmpty() ? View.GONE : View.VISIBLE);
        TextView homeFeedback = findViewById(R.id.home_feedback);
        homeFeedback.setText(message);
        homeFeedback.setVisibility(view.getVisibility());
    }

    @Override protected void onSaveInstanceState(Bundle state) {
        state.putString("feedback", ((TextView) findViewById(R.id.pantry_feedback)).getText().toString());
        state.putBoolean("show_pantry", showingPantry);
        super.onSaveInstanceState(state);
    }

    @Override protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
