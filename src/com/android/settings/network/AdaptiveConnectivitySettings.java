/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.network;

import static android.provider.Settings.Secure.ADAPTIVE_CONNECTIVITY_MOBILE_NETWORK_ENABLED;
import static android.provider.Settings.Secure.ADAPTIVE_CONNECTIVITY_WIFI_ENABLED;

import android.app.settings.SettingsEnums;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.SwitchPreferenceCompat;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.flags.Flags;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;

/** Adaptive connectivity is a feature which automatically manages network connections. */
@SearchIndexable
public class AdaptiveConnectivitySettings extends DashboardFragment {
  private static final String TAG = "AdaptiveConnectivitySettings";

  @Override
  public int getMetricsCategory() {
    return SettingsEnums.ADAPTIVE_CONNECTIVITY_CATEGORY;
  }

  @Override
  protected String getLogTag() {
    return TAG;
  }

  @Override
  protected int getPreferenceScreenResId() {
    return R.xml.adaptive_connectivity_settings;
  }

  public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
      new BaseSearchIndexProvider(R.xml.adaptive_connectivity_settings);

  @Override
  public @Nullable String getPreferenceScreenBindingKey(@NonNull Context context) {
    return AdaptiveConnectivityScreen.KEY;
  }

  @Override
  public void onCreatePreferences(@NonNull Bundle savedInstanceState, @NonNull String rootKey) {
    Log.i("Settings", "onCreatePreferences");
    super.onCreatePreferences(savedInstanceState, rootKey);
    if (Flags.enableNestedToggleSwitches() && !isCatalystEnabled()) {
      setupSwitchPreferenceCompat(ADAPTIVE_CONNECTIVITY_WIFI_ENABLED);
      setupSwitchPreferenceCompat(ADAPTIVE_CONNECTIVITY_MOBILE_NETWORK_ENABLED);
    }
  }

  private void setupSwitchPreferenceCompat(String key) {
    SwitchPreferenceCompat switchPreference = findPreference(key);
    if (switchPreference != null) {
      switchPreference.setOnPreferenceChangeListener(
          (preference, newValue) -> {
            boolean isChecked = (Boolean) newValue;
            Settings.Secure.putInt(getContentResolver(), key, isChecked ? 1 : 0);
            if (preference.getKey().equals(ADAPTIVE_CONNECTIVITY_WIFI_ENABLED)) {
              getSystemService(WifiManager.class).setWifiScoringEnabled(isChecked);
            }
            return true;
          });
      switchPreference.setVisible(true);
    }
  }
}
