package za.co.smartpantry.ui;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import za.co.smartpantry.R;
import za.co.smartpantry.data.DatabaseHelper;
import za.co.smartpantry.data.PantryValidation;
import za.co.smartpantry.model.PantryItem;

public class IngredientFormActivity extends BaseActivity {
    private DatabaseHelper database;
    private EditText name;
    private EditText quantity;
    private EditText expiry;
    private Spinner unit;
    private long recordId;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        recordId = getIntent().getLongExtra("pantry_id", -1);
        showScreen(R.layout.activity_ingredient_form, recordId == -1 ? "Add ingredient" : "Edit ingredient");
        database = new DatabaseHelper(this);
        name = findViewById(R.id.ingredient_name);
        quantity = findViewById(R.id.ingredient_quantity);
        expiry = findViewById(R.id.ingredient_expiry);
        unit = findViewById(R.id.ingredient_unit);
        List<String> choices = new ArrayList<>();
        choices.add("Select a unit");
        choices.addAll(PantryValidation.UNITS);
        ArrayAdapter<String> units = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);
        units.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(units);
        findViewById(R.id.cancel_ingredient).setOnClickListener(view -> finish());
        findViewById(R.id.save_ingredient).setOnClickListener(view -> save());
        if (state == null && recordId != -1) loadRecord();
    }

    private void loadRecord() {
        try {
            PantryItem item = database.getPantryItem(recordId);
            if (item == null) {
                feedback("This ingredient no longer exists. Return to your pantry.");
                findViewById(R.id.save_ingredient).setEnabled(false);
                return;
            }
            name.setText(item.name);
            quantity.setText(item.quantity.stripTrailingZeros().toPlainString());
            expiry.setText(item.expiry);
            unit.setSelection(PantryValidation.UNITS.indexOf(item.unit) + 1);
        } catch (SQLException exception) {
            feedback("Could not load this ingredient. Return to your pantry and try again.");
            findViewById(R.id.save_ingredient).setEnabled(false);
        }
    }

    private void save() {
        String ingredientName = name.getText().toString().trim();
        String amount = quantity.getText().toString().trim();
        String date = expiry.getText().toString().trim();
        String nameError = PantryValidation.nameError(ingredientName);
        String amountError = PantryValidation.quantityError(amount);
        String dateError = PantryValidation.expiryError(date);
        name.setError(nameError);
        quantity.setError(amountError);
        expiry.setError(dateError);
        boolean unitMissing = unit.getSelectedItemPosition() == 0;
        TextView unitError = findViewById(R.id.unit_error);
        unitError.setText("Select a unit.");
        unitError.setVisibility(unitMissing ? View.VISIBLE : View.GONE);
        if (nameError != null || amountError != null || dateError != null || unitMissing) {
            feedback("Please correct the highlighted fields.");
            if (nameError != null) name.requestFocus();
            else if (amountError != null) quantity.requestFocus();
            else if (unitMissing) unit.requestFocus();
            else expiry.requestFocus();
            return;
        }
        try {
            String selectedUnit = unit.getSelectedItem().toString();
            if (recordId == -1) database.addPantryItem(ingredientName, new BigDecimal(amount), selectedUnit, date);
            else if (!database.updatePantryItem(recordId, ingredientName, new BigDecimal(amount), selectedUnit, date)) {
                feedback("This ingredient was removed. Return to your pantry.");
                return;
            }
            setResult(RESULT_OK, new Intent().putExtra("feedback",
                    recordId == -1 ? "Ingredient added." : "Ingredient updated."));
            finish();
        } catch (SQLException | IllegalArgumentException exception) {
            feedback("Could not save this ingredient. Please check the values and try again.");
        }
    }

    private void feedback(String message) {
        TextView view = findViewById(R.id.form_feedback);
        view.setText(message);
        view.setVisibility(View.VISIBLE);
    }

    @Override protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
