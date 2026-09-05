package za.co.smartpantry.data;

import android.content.Context;
import android.content.SharedPreferences;

public final class AppPreferences {
    private final SharedPreferences preferences;

    public AppPreferences(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences("pantry_settings", Context.MODE_PRIVATE);
    }

    public boolean expiryIndicatorsEnabled() { return preferences.getBoolean("expiry_indicators", true); }

    public boolean setExpiryIndicatorsEnabled(boolean enabled) {
        return preferences.edit().putBoolean("expiry_indicators", enabled).commit();
    }
}
