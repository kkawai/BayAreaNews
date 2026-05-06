package com.kk.android.bayareanews.common

import android.content.Context
import com.kk.android.bayareanews.R

object DeviceUtils {
    fun isLargeScreenDevice(context: Context): Boolean {
        return try {
            context.resources.getBoolean(R.bool.isLargeScreenDevice)
        }catch (_: Throwable){false}
    }
}