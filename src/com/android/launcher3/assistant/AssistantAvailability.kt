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

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings


object AssistantAvailability {

    const val PACKAGE = "app.mosaicos.assistant"

    @Volatile
    private var available: Boolean? = null

    @JvmStatic
    fun isInstalled(context: Context): Boolean =
        available ?: runCatching {
            context.packageManager.getPackageInfo(PACKAGE, 0)
            context.packageManager.checkSignatures(context.packageName, PACKAGE) ==
                PackageManager.SIGNATURE_MATCH
        }.getOrDefault(false).also { available = it }

    @JvmStatic
    fun isSelected(context: Context): Boolean {
        if (!isInstalled(context)) return false
        val assistant = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ASSISTANT,
        ) ?: return false
        if (assistant.isEmpty()) return false
        if (assistant == PACKAGE) return true
        return ComponentName.unflattenFromString(assistant)?.packageName == PACKAGE
    }
}
