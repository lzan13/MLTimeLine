package net.melove.app.ml.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.melove.app.ml.R;


/**
 * Created by Administrator on 2015/3/31.
 */
public class MLSettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.set_preferences);
    }
}
