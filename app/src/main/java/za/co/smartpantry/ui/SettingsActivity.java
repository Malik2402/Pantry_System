package za.co.smartpantry.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import za.co.smartpantry.R;
import za.co.smartpantry.data.AppPreferences;

public class SettingsActivity extends BaseActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        showScreen(R.layout.activity_settings, "Settings");
        AppPreferences preferences = new AppPreferences(this);
        SwitchCompat toggle = findViewById(R.id.expiry_switch);
        toggle.setChecked(preferences.expiryIndicatorsEnabled());
        TextView feedback = findViewById(R.id.settings_feedback);
        feedback.setText(toggle.isChecked() ? "Expiry indicators are on." : "Expiry indicators are off.");
        toggle.setOnCheckedChangeListener((button, checked) -> {
            if (preferences.setExpiryIndicatorsEnabled(checked)) {
                feedback.setText(checked ? "Preference saved. Expiry indicators are on." : "Preference saved. Expiry indicators are off.");
            } else {
                toggle.setOnCheckedChangeListener(null);
                toggle.setChecked(!checked);
                toggle.setEnabled(false);
                feedback.setText("Could not save your preference. Reopen Settings to try again.");
            }
        });
    }
}
