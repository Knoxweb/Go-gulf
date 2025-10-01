package com.gogulf.passenger.app.utils

import android.content.Context
import android.graphics.Canvas
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlin.math.roundToInt

class MaterialLineInItemDecoration(
    private val context: Context,
    private val startGapInDP: Int,
    private val endGapInDp: Int = 0) : MaterialDividerItemDecoration(context, LinearLayoutManager(context).orientation) {

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        val density: Float = context.resources.displayMetrics.density
        val startOffset = (startGapInDP * density).roundToInt()
        val endOffset = (endGapInDp * density).roundToInt()

        dividerInsetStart = startOffset
        dividerInsetEnd = endOffset
        isLastItemDecorated = false

        dividerColor =  ColorUtils.setAlphaComponent(dividerColor, 100)

    }
}