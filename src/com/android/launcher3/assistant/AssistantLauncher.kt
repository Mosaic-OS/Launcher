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
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.launcher3.R

private const val TAG = "AssistantLauncher"

object AssistantLauncher {

    fun launch(context: Context, source: View): Boolean {
        if (!AssistantAvailability.isSelected(context)) {
            Toast.makeText(context, R.string.assistant_row_unavailable, Toast.LENGTH_SHORT).show()
            return false
        }
        val location = IntArray(2)
        source.getLocationOnScreen(location)
        val intent = Intent(Intent.ACTION_ASSIST)
            .setComponent(ComponentName(AssistantAvailability.PACKAGE, ASSISTANT_ACTIVITY))
            .putExtra(EXTRA_SOURCE_X, location[0] + source.width / 2)
            .putExtra(EXTRA_SOURCE_Y, location[1] + source.height / 2)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return runCatching { context.startActivity(intent) }
            .onFailure {
                Log.w(TAG, "Could not open the assistant", it)
                Toast.makeText(context, R.string.assistant_row_unavailable, Toast.LENGTH_SHORT)
                    .show()
            }
            .isSuccess
    }

    const val ASSISTANT_ACTIVITY = "app.mosaicos.assistant.ui.AssistantActivity"

    private const val EXTRA_SOURCE_X = "app.mosaicos.assistant.extra.SOURCE_X"
    private const val EXTRA_SOURCE_Y = "app.mosaicos.assistant.extra.SOURCE_Y"
}
