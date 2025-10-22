/*
 * Copyright (C) 2025 The Android Open Source Project
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

package com.android.settings.notification;

import static android.service.notification.Adjustment.KEY_SUMMARIZATION;

import static com.google.common.truth.Truth.assertThat;

import static org.mockito.Mockito.when;

import android.app.Flags;
import android.app.INotificationManager;
import android.content.Context;
import android.platform.test.flag.junit.SetFlagsRule;
import android.service.notification.Adjustment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class BundleManageAppsPreferenceControllerTest {
    @Rule
    public final SetFlagsRule mSetFlagsRule = new SetFlagsRule();

    private static final String PREFERENCE_KEY = "preference_key";

    private Context mContext;
    BundleManageAppsPreferenceController mController;
    @Mock
    INotificationManager mInm;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mContext = RuntimeEnvironment.application;
        mSetFlagsRule.enableFlags(Flags.FLAG_NOTIFICATION_CLASSIFICATION_UI);
        mController = new BundleManageAppsPreferenceController(mContext, PREFERENCE_KEY);
        mController.mBackend.setNm(mInm);
    }

    @Test
    public void isAvailable_flagEnabledNasSupports_shouldReturnTrue() {
        assertThat(mController.isAvailable()).isTrue();
    }

    @Test
    public void isAvailable_flagEnabledNasDoesNotSupport_shouldReturnFalse() throws Exception {
        when(mInm.getUnsupportedAdjustmentTypes()).thenReturn(List.of(Adjustment.KEY_TYPE));
        assertThat(mController.isAvailable()).isFalse();
    }

    @Test
    public void isAvailable_flagDisabledNasSupports_shouldReturnFalse() {
        mSetFlagsRule.disableFlags(Flags.FLAG_NOTIFICATION_CLASSIFICATION_UI);
        assertThat(mController.isAvailable()).isFalse();
    }
}
