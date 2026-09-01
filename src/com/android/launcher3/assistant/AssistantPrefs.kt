/*
 * Copyright (C) 2026 The MosaicOS Project
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
package com.android.launcher3.assistant

import android.content.Context
import com.android.launcher3.LauncherPrefs

object AssistantPrefs {

    private val SHOW_IN_HOME_MENU = LauncherPrefs.backedUpItem("pref_assistant_enabled", false)

    fun shouldShowInHomeMenu(context: Context): Boolean =
        SHOW_IN_HOME_MENU.get(context) &&
            AssistantAvailability.isInstalled(context) &&
            AssistantAvailability.isSelected(context)
}
