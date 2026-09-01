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
import com.android.launcher3.allapps.BaseAllAppsAdapter
import com.android.launcher3.allapps.BaseAllAppsAdapter.AdapterItem
import com.android.launcher3.allapps.search.DefaultAppSearchAlgorithm
import com.android.launcher3.search.SearchAlgorithm
import com.android.launcher3.search.SearchCallback
import com.android.launcher3.util.LooperExecutor

// Search only appends a hand-off row, so typing never starts the model
class AssistantSearchAlgorithm(
    context: Context,
    uiExecutor: LooperExecutor,
) : SearchAlgorithm<AdapterItem> {

    private val appSearch = DefaultAppSearchAlgorithm(context, uiExecutor, true)
    private val appContext = context.applicationContext

    override fun doSearch(query: String, callback: SearchCallback<AdapterItem>) {
        appSearch.doSearch(query, object : SearchCallback<AdapterItem> {
            override fun onSearchResult(resultQuery: String, items: ArrayList<AdapterItem>) {
                if (shouldOffer(resultQuery, items)) {
                    items.add(AssistantAdapterItem(resultQuery.trim()))
                }
                callback.onSearchResult(resultQuery, items)
            }

            override fun clearSearchResult() = callback.clearSearchResult()
        })
    }

    override fun cancel(interruptActiveRequests: Boolean) = appSearch.cancel(interruptActiveRequests)

    override fun destroy() = appSearch.destroy()

    /**
     * Offered for anything that reads like a question rather than an app name: either nothing
     * matched, or the query has more than one word.
     */
    private fun shouldOffer(query: String, items: List<AdapterItem>): Boolean {
        if (
            !AssistantAvailability.isInstalled(appContext) ||
                !AssistantAvailability.isSelected(appContext)
        ) {
            return false
        }
        val trimmed = query.trim()
        if (trimmed.length < MIN_QUERY_LENGTH) return false

        // The "no apps found" row also carries an itemInfo, so match on the view type instead
        val appMatches = items.any { it.viewType == BaseAllAppsAdapter.VIEW_TYPE_ICON }
        return !appMatches || trimmed.contains(' ')
    }

    private companion object {
        const val MIN_QUERY_LENGTH = 4
    }
}
