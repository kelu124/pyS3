package com.lee.ultrascan.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import com.lee.ultrascan.C0796R;

public class ParamSettingsActivity extends Activity implements OnPreferenceChangeListener {
    public static final int REQUEST_SETTINGS = 1;
    public static final String RESULT_PARAM_CHANGED = "RESULT_PARAM_CHANGED";

    public static class ParamSettingsFragment extends PreferenceFragment {
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(C0796R.xml.preferences);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(16908290, new ParamSettingsFragment()).commit();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Intent data = new Intent();
        data.putExtra(RESULT_PARAM_CHANGED, true);
        setResult(-1, data);
        return true;
    }
}
