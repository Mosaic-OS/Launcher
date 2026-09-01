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

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.launcher3.R
import com.android.launcher3.allapps.BaseAllAppsAdapter
import com.android.launcher3.allapps.BaseAllAppsAdapter.AdapterItem
import com.android.launcher3.allapps.search.DefaultSearchAdapterProvider
import com.android.launcher3.views.ActivityContext

class AssistantAdapterProvider(launcher: ActivityContext) :
    DefaultSearchAdapterProvider(launcher) {

    private var results: List<AdapterItem> = emptyList()

    fun onResultsChanged(results: List<AdapterItem>?) {
        this.results = results.orEmpty()
    }

    override fun isViewSupported(viewType: Int): Boolean =
        viewType == AssistantAdapterItem.VIEW_TYPE_ASSISTANT

    override fun getItemsPerRow(viewType: Int, appsPerRow: Int): Int =
        if (viewType == AssistantAdapterItem.VIEW_TYPE_ASSISTANT) 1
        else super.getItemsPerRow(viewType, appsPerRow)

    override fun onCreateViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int,
    ): BaseAllAppsAdapter.ViewHolder = BaseAllAppsAdapter.ViewHolder(
        layoutInflater.inflate(R.layout.search_result_assistant, parent, false)
    )

    override fun onBindView(holder: BaseAllAppsAdapter.ViewHolder, position: Int) {
        val item = results.getOrNull(position) as? AssistantAdapterItem ?: return
        (holder.itemView as AssistantRowView).bind(item.query)
    }
}
