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
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.launcher3.R

private const val TAG = "AssistantRowView"

class AssistantRowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private lateinit var subtitle: TextView
    private var query = ""

    override fun onFinishInflate() {
        super.onFinishInflate()
        subtitle = findViewById(R.id.assistant_subtitle)
        setOnClickListener { open() }
    }

    fun bind(query: String) {
        this.query = query
        subtitle.text = query
    }

    private fun open() {
        if (query.isEmpty()) return
        if (!AssistantAvailability.isSelected(context)) {
            Toast.makeText(context, R.string.assistant_row_unavailable, Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(Intent.ACTION_ASSIST)
            .setComponent(
                ComponentName(
                    AssistantAvailability.PACKAGE, AssistantLauncher.ASSISTANT_ACTIVITY
                )
            )
            .putExtra(EXTRA_QUERY, query)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching { context.startActivity(intent) }
            .onFailure {
                Log.w(TAG, "Could not open the assistant", it)
                Toast.makeText(context, R.string.assistant_row_unavailable, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private companion object {
        const val EXTRA_QUERY = "app.mosaicos.assistant.extra.QUERY"
    }
}
