package com.brochill.minismodule.util

import com.brochill.minismodule.data.model.Episode
import com.brochill.minismodule.data.model.Series

interface RecyclerviewClickListener {
    fun onItemClickListener(item: Any, position: Int, data:MutableList<Episode>)
    fun onSeriesItemClick(item: Any, position: Int, data: MutableList<Series>)
}