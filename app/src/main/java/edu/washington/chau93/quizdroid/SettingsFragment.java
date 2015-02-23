package edu.washington.chau93.quizdroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Created by Aaron on 2/22/2015.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);

        if(pref instanceof CheckBoxPreference){
            CheckBoxPreference checkBoxPref = (CheckBoxPreference) pref;
            if(checkBoxPref.getTitle().equals("Check For Updates")){
                
            }
        }
    }
}
