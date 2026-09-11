package za.co.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import za.co.smartpantry.R;

public abstract class BaseActivity extends AppCompatActivity {
    protected void showScreen(int layout, String title) {
        super.setContentView(R.layout.activity_shell);
        setSupportActionBar(findViewById(R.id.toolbar));
        setTitle(title);
        FrameLayout container = findViewById(R.id.screen_container);
        getLayoutInflater().inflate(layout, container, true);
        boolean topLevel = this instanceof PantryActivity || this instanceof SettingsActivity
                || this instanceof SuggestionsActivity;
        findViewById(R.id.bottom_navigation).setVisibility(topLevel ? View.VISIBLE : View.GONE);
        findViewById(R.id.bottom_home).setOnClickListener(view -> openPantry(false));
        findViewById(R.id.bottom_pantry).setOnClickListener(view -> openPantry(true));
        findViewById(R.id.bottom_settings).setOnClickListener(view -> openDestination(SettingsActivity.class));
        if (this instanceof SettingsActivity) selectBottom(R.id.bottom_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.shell), (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.displayCutout() | WindowInsetsCompat.Type.ime());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        Class<?> target;
        if (item.getItemId() == R.id.nav_home) { openPantry(false); return true; }
        else if (item.getItemId() == R.id.nav_pantry) { openPantry(true); return true; }
        else if (item.getItemId() == R.id.nav_recipes) target = SuggestionsActivity.class;
        else if (item.getItemId() == R.id.nav_settings) target = SettingsActivity.class;
        else return super.onOptionsItemSelected(item);
        openDestination(target);
        return true;
    }

    private void openDestination(Class<?> target) {
        if (!getClass().equals(target)) startActivity(new Intent(this, target)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    private void openPantry(boolean list) {
        if (this instanceof PantryActivity) ((PantryActivity) this).showPantryPanel(list);
        else startActivity(new Intent(this, PantryActivity.class).putExtra("show_pantry", list)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    protected void selectBottom(int selected) {
        for (int id : new int[]{R.id.bottom_home, R.id.bottom_pantry, R.id.bottom_settings}) {
            TextView tab = findViewById(id);
            tab.setSelected(id == selected);
            tab.setTypeface(null, id == selected ? android.graphics.Typeface.BOLD
                    : android.graphics.Typeface.NORMAL);
            tab.setBackgroundResource(id == selected ? R.drawable.home_sage
                    : android.R.color.transparent);
            tab.setAlpha(id == selected ? 1f : 0.7f);
        }
    }
}
