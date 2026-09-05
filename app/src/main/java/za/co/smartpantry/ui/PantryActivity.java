package za.co.smartpantry.ui;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import za.co.smartpantry.R;
import za.co.smartpantry.adapter.PantryAdapter;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.model.PantryItem;

public class PantryActivity extends BaseActivity implements PantryAdapter.Actions {
    private DatabaseHelper database;
    private PantryAdapter adapter;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        showScreen(R.layout.activity_pantry, "My pantry");
        database = new DatabaseHelper(this);
        adapter = new PantryAdapter(this);
        androidx.recyclerview.widget.RecyclerView list = findViewById(R.id.pantry_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        findViewById(R.id.add_ingredient).setOnClickListener(view ->
                startActivityForResult(new Intent(this, IngredientFormActivity.class), 1));
        if (state != null) feedback(state.getString("feedback", ""));
    }

    @Override protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        try {
            List<PantryItem> items = database.getPantryItems();
            adapter.setExpiryIndicators(new za.co.smartpantry.data.AppPreferences(this).expiryIndicatorsEnabled());
            adapter.submit(items);
            ((TextView) findViewById(R.id.pantry_summary)).setText(items.size() + " pantry entries · stored on this device");
            findViewById(R.id.pantry_empty).setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
        } catch (SQLException exception) {
            adapter.submit(java.util.Collections.emptyList());
            findViewById(R.id.pantry_empty).setVisibility(View.GONE);
            feedback("Could not load your pantry. Please reopen this screen to try again.");
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
    }

    @Override protected void onSaveInstanceState(Bundle state) {
        state.putString("feedback", ((TextView) findViewById(R.id.pantry_feedback)).getText().toString());
        super.onSaveInstanceState(state);
    }

    @Override protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
