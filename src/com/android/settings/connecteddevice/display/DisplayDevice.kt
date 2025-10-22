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

package com.android.settings.connecteddevice.display

import android.view.Display.Mode

import androidx.annotation.Keep

enum class DisplayIsEnabled { YES, NO, UNKNOWN }

/**
 * Contains essential information from {@link android.view.Display} needed by the user to configure
 * a display.
 */
@Keep
data class DisplayDevice(val id: Int, val name: String, val mode: Mode?,
        val supportedModes: List<Mode>, val isEnabled: DisplayIsEnabled) {}
